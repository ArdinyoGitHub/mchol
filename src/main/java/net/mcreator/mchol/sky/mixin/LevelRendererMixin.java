package net.mcreator.mchol.sky.mixin;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.renderer.LevelRenderer;

import net.mcreator.mchol.sky.ShaderSafeSkybox;

/**
 * Iris runs the shader pack's composite and final passes when LevelRenderer.render pops its model view matrix; NeoForge's
 * AfterLevel event comes before that, so anything drawn there is painted over. This hooks the next call after the pop
 * (closing the prepared feature frame), which is after Iris, and draws the custom dimension skybox there.
 */
@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {
	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/feature/FeatureRenderDispatcher$PreparedFrame;close()V"), require = 0)
	private void mchol$skyboxAfterShaders(CallbackInfo ci) {
		ShaderSafeSkybox.renderAfterShaders();
	}
}
