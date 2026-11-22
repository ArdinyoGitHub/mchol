package net.mcreator.mchol.sky;

import org.joml.Matrix4f;

import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.RegisterRenderPipelinesEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.Registries;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.BindGroupLayouts;
import net.minecraft.client.Minecraft;

import net.mcreator.mchol.McholMod;

import java.util.OptionalDouble;
import java.util.Optional;
import java.util.Map;
import java.lang.reflect.Method;

import com.mojang.renderpearl.api.vertex.VertexFormat;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.pipeline.DepthStencilState;
import com.mojang.renderpearl.api.pipeline.CompareOp;
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.BlendFunction;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.systems.RenderSystem;

/**
 * Keeps the custom dimension skyboxes visible with Iris shader packs (Complementary and the like).
 * The generated McholModSkyboxRenderer draws the skybox right after the vanilla sky, but a shader pack repaints every sky pixel
 * with its own sky (and fog) in its later passes. So while a shader pack is in use the skybox is drawn again after the pack has
 * finished the frame, pushed onto the far plane so the depth test keeps it behind the world and the hand.
 * AfterLevel still runs before Iris' composite/final passes, so it only records the camera; the drawing happens in
 * {@link #renderAfterShaders()}, called by LevelRendererMixin right after Iris finalizes the level.
 * Iris is optional: it is only looked up through reflection.
 */
@EventBusSubscriber(Dist.CLIENT)
public class ShaderSafeSkybox {
	private static final Map<ResourceKey<Level>, Identifier> SKYBOXES = Map.of(dimension("zephyr_paradise"), texture("zephyr_paradise"), dimension("port_terenne"), texture("port_terenne"), dimension("gatlus"), texture("gatlus"));
	/** Same as the vanilla END_SKY pipeline (used by the generated skybox), but with the far-plane vertex shader and a depth test. */
	private static final RenderPipeline FAR_SKYBOX = RenderPipeline.builder(RenderPipelines.GLOBALS_SNIPPET).withBindGroupLayout(BindGroupLayouts.PROJECTION).withBindGroupLayout(BindGroupLayouts.DYNAMIC_TRANSFORMS)
			.withLocation(Identifier.fromNamespaceAndPath(McholMod.MODID, "pipeline/skybox_far")).withVertexShader(Identifier.fromNamespaceAndPath(McholMod.MODID, "core/skybox_far")).withFragmentShader("core/position_tex_color")
			.withBindGroupLayout(BindGroupLayouts.SAMPLER0).withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT)).withDepthStencilState(new DepthStencilState(CompareOp.LESS_THAN_OR_EQUAL, false))
			.withVertexBinding(0, DefaultVertexFormat.POSITION_TEX_COLOR).withPrimitiveTopology(PrimitiveTopology.QUADS).build();

	private static GpuBuffer skyboxBuffer;
	private static Identifier pendingSkybox;
	private static final Matrix4f pendingModelView = new Matrix4f();
	private static Object irisApi;
	private static Method shaderPackInUse;
	private static boolean irisLookedUp;

	private static ResourceKey<Level> dimension(String name) {
		return ResourceKey.create(Registries.DIMENSION, Identifier.fromNamespaceAndPath(McholMod.MODID, name));
	}

	private static Identifier texture(String name) {
		return Identifier.fromNamespaceAndPath(McholMod.MODID, "textures/skybox/" + name + ".png");
	}

	@SubscribeEvent
	public static void registerPipelines(RegisterRenderPipelinesEvent event) {
		event.registerPipeline(FAR_SKYBOX);
	}

	@SubscribeEvent
	public static void afterLevel(RenderLevelStageEvent.AfterLevel event) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.level == null)
			return;
		Identifier skybox = SKYBOXES.get(mc.level.dimension());
		if (skybox != null && isShaderPackInUse()) {
			// the level's own model view stack is already popped here, only the camera rotation is needed for a skybox
			pendingModelView.set(RenderSystem.getModelViewStack()).mul(event.getModelViewMatrix());
			pendingSkybox = skybox;
		}
	}

	/** Called from LevelRendererMixin after the level (and Iris' final pass) is done. */
	public static void renderAfterShaders() {
		Identifier skybox = pendingSkybox;
		pendingSkybox = null;
		if (skybox != null)
			render(pendingModelView, skybox);
	}

	private static boolean isShaderPackInUse() {
		if (!irisLookedUp) {
			irisLookedUp = true;
			try {
				Class<?> api = Class.forName("net.irisshaders.iris.api.v0.IrisApi");
				irisApi = api.getMethod("getInstance").invoke(null);
				shaderPackInUse = api.getMethod("isShaderPackInUse");
			} catch (ReflectiveOperationException | LinkageError e) {
				irisApi = null; // Iris is not installed
			}
		}
		if (irisApi == null)
			return false;
		try {
			return (boolean) shaderPackInUse.invoke(irisApi);
		} catch (ReflectiveOperationException e) {
			irisApi = null;
			return false;
		}
	}

	private static void render(Matrix4f modelView, Identifier textureId) {
		if (skyboxBuffer == null)
			skyboxBuffer = buildSkyboxBuffer();
		Minecraft mc = Minecraft.getInstance();
		GpuBufferSlice dynamicTransforms = RenderSystem.getDynamicUniforms().writeTransform(modelView);
		GpuTextureView color = mc.gameRenderer.mainRenderTarget().getColorTextureView();
		GpuTextureView depth = mc.gameRenderer.mainRenderTarget().getDepthTextureView();
		RenderSystem.AutoStorageIndexBuffer indices = RenderSystem.getSequentialBuffer(PrimitiveTopology.QUADS);
		GpuBuffer indexBuffer = indices.getBuffer(36);
		AbstractTexture texture = mc.getTextureManager().getTexture(textureId);
		try (RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "mchol Shader Safe Skybox", color, Optional.empty(), depth, OptionalDouble.empty())) {
			renderPass.setPipeline(RenderSystem.getCompiledPipeline(FAR_SKYBOX));
			RenderSystem.bindDefaultUniforms(renderPass);
			renderPass.setUniform("DynamicTransforms", dynamicTransforms);
			renderPass.setUniform("Sampler0", texture.getTextureView(), texture.getSampler());
			renderPass.setVertexBuffer(0, skyboxBuffer.slice());
			renderPass.setIndexBuffer(indexBuffer, indices.type());
			renderPass.drawIndexed(36, 1, 0, 0, 0);
		}
	}

	/** Same cube and texture layout (4x3 cross) as the generated skybox renderer. */
	private static GpuBuffer buildSkyboxBuffer() {
		VertexFormat format = DefaultVertexFormat.POSITION_TEX_COLOR;
		try (ByteBufferBuilder byteBufferBuilder = ByteBufferBuilder.exactlySized(24 * format.getVertexSize())) {
			BufferBuilder bufferBuilder = new BufferBuilder(byteBufferBuilder, PrimitiveTopology.QUADS, format);
			float d = 100.0F;
			float s = 100.0F;
			addFace(bufferBuilder, -s, d, -s, 1 / 4F, 1 / 3F, s, d, -s, 2 / 4F, 1 / 3F, s, d, s, 2 / 4F, 0F, -s, d, s, 1 / 4F, 0F);
			addFace(bufferBuilder, -s, -d, -s, 1 / 4F, 2 / 3F, -s, -d, s, 1 / 4F, 1F, s, -d, s, 2 / 4F, 1F, s, -d, -s, 2 / 4F, 2 / 3F);
			addFace(bufferBuilder, -d, -s, s, 0F, 2 / 3F, -d, -s, -s, 1 / 4F, 2 / 3F, -d, s, -s, 1 / 4F, 1 / 3F, -d, s, s, 0F, 1 / 3F);
			addFace(bufferBuilder, -s, -s, -d, 1 / 4F, 2 / 3F, s, -s, -d, 2 / 4F, 2 / 3F, s, s, -d, 2 / 4F, 1 / 3F, -s, s, -d, 1 / 4F, 1 / 3F);
			addFace(bufferBuilder, d, -s, -s, 2 / 4F, 2 / 3F, d, -s, s, 3 / 4F, 2 / 3F, d, s, s, 3 / 4F, 1 / 3F, d, s, -s, 2 / 4F, 1 / 3F);
			addFace(bufferBuilder, s, -s, d, 3 / 4F, 2 / 3F, -s, -s, d, 1F, 2 / 3F, -s, s, d, 1F, 1 / 3F, s, s, d, 3 / 4F, 1 / 3F);
			try (MeshData meshData = bufferBuilder.buildOrThrow()) {
				return RenderSystem.getDevice().createBuffer(() -> "mchol Shader Safe Skybox", 40, meshData.vertexBuffer());
			}
		}
	}

	private static void addFace(BufferBuilder b, float x1, float y1, float z1, float u1, float v1, float x2, float y2, float z2, float u2, float v2, float x3, float y3, float z3, float u3, float v3, float x4, float y4, float z4, float u4, float v4) {
		b.addVertex(x1, y1, z1).setUv(u1, v1).setColor(0xFFFFFFFF);
		b.addVertex(x2, y2, z2).setUv(u2, v2).setColor(0xFFFFFFFF);
		b.addVertex(x3, y3, z3).setUv(u3, v3).setColor(0xFFFFFFFF);
		b.addVertex(x4, y4, z4).setUv(u4, v4).setColor(0xFFFFFFFF);
	}
}
