package net.mcreator.mchol.client.gui;

import net.neoforged.neoforge.client.network.ClientPacketDistributor;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.Identifier;
import net.minecraft.network.chat.Component;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.GuiGraphicsExtractor;

import net.mcreator.mchol.world.inventory.BountyHunter9000GUIMenu;
import net.mcreator.mchol.network.BountyHunter9000GUIButtonMessage;
import net.mcreator.mchol.init.McholModScreens;

import com.mojang.blaze3d.platform.InputConstants;

public class BountyHunter9000GUIScreen extends AbstractContainerScreen<BountyHunter9000GUIMenu> implements McholModScreens.ScreenAccessor {
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	private boolean menuStateUpdateActive = false;
	private Button button_zephyr_paradise;
	private Button button_gatlus;
	private Button button_nether;
	private Button button_end;
	private Button button_overworld;
	private Button button_port_terenne;
	private static final Identifier BACKGROUND = Identifier.parse("mchol:textures/screens/bounty_hunter_9000_gui.png");

	public BountyHunter9000GUIScreen(BountyHunter9000GUIMenu container, Inventory inventory, Component text) {
		super(container, inventory, text, 176, 166);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
	}

	@Override
	public void updateMenuState(int elementType, String name, Object elementState) {
		menuStateUpdateActive = true;
		menuStateUpdateActive = false;
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
		super.extractRenderState(guiGraphics, mouseX, mouseY, partialTicks);
	}

	@Override
	public void extractBackground(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
		super.extractBackground(guiGraphics, mouseX, mouseY, partialTicks);
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
	}

	@Override
	public boolean keyPressed(KeyEvent event) {
		int key = InputConstants.getKey(event).getValue();
		if (key == 256) {
			this.minecraft.player.closeContainer();
			return true;
		}
		return super.keyPressed(event);
	}

	@Override
	protected void extractLabels(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY) {
		guiGraphics.text(this.font, Component.translatable("gui.mchol.bounty_hunter_9000_gui.label_bring_another_bh9k_so_you_dont_g"), -14, -22, -26368, false);
	}

	@Override
	public void init() {
		super.init();
		button_zephyr_paradise = Button.builder(Component.translatable("gui.mchol.bounty_hunter_9000_gui.button_zephyr_paradise"), e -> {
			int x = BountyHunter9000GUIScreen.this.x;
			int y = BountyHunter9000GUIScreen.this.y;
			if (true) {
				ClientPacketDistributor.sendToServer(new BountyHunter9000GUIButtonMessage(0, x, y, z));
				BountyHunter9000GUIButtonMessage.handleButtonAction(entity, 0, x, y, z);
			}
		}).bounds(this.leftPos + 12, this.topPos + 72, 103, 20).build();
		this.addRenderableWidget(button_zephyr_paradise);
		button_gatlus = Button.builder(Component.translatable("gui.mchol.bounty_hunter_9000_gui.button_gatlus"), e -> {
			int x = BountyHunter9000GUIScreen.this.x;
			int y = BountyHunter9000GUIScreen.this.y;
			if (true) {
				ClientPacketDistributor.sendToServer(new BountyHunter9000GUIButtonMessage(1, x, y, z));
				BountyHunter9000GUIButtonMessage.handleButtonAction(entity, 1, x, y, z);
			}
		}).bounds(this.leftPos + 11, this.topPos + 93, 56, 20).build();
		this.addRenderableWidget(button_gatlus);
		button_nether = Button.builder(Component.translatable("gui.mchol.bounty_hunter_9000_gui.button_nether"), e -> {
			int x = BountyHunter9000GUIScreen.this.x;
			int y = BountyHunter9000GUIScreen.this.y;
			if (true) {
				ClientPacketDistributor.sendToServer(new BountyHunter9000GUIButtonMessage(2, x, y, z));
				BountyHunter9000GUIButtonMessage.handleButtonAction(entity, 2, x, y, z);
			}
		}).bounds(this.leftPos + 13, this.topPos + 27, 56, 20).build();
		this.addRenderableWidget(button_nether);
		button_end = Button.builder(Component.translatable("gui.mchol.bounty_hunter_9000_gui.button_end"), e -> {
			int x = BountyHunter9000GUIScreen.this.x;
			int y = BountyHunter9000GUIScreen.this.y;
			if (true) {
				ClientPacketDistributor.sendToServer(new BountyHunter9000GUIButtonMessage(3, x, y, z));
				BountyHunter9000GUIButtonMessage.handleButtonAction(entity, 3, x, y, z);
			}
		}).bounds(this.leftPos + 13, this.topPos + 51, 40, 20).build();
		this.addRenderableWidget(button_end);
		button_overworld = Button.builder(Component.translatable("gui.mchol.bounty_hunter_9000_gui.button_overworld"), e -> {
			int x = BountyHunter9000GUIScreen.this.x;
			int y = BountyHunter9000GUIScreen.this.y;
			if (true) {
				ClientPacketDistributor.sendToServer(new BountyHunter9000GUIButtonMessage(4, x, y, z));
				BountyHunter9000GUIButtonMessage.handleButtonAction(entity, 4, x, y, z);
			}
		}).bounds(this.leftPos + 10, this.topPos + 4, 72, 20).build();
		this.addRenderableWidget(button_overworld);
		button_port_terenne = Button.builder(Component.translatable("gui.mchol.bounty_hunter_9000_gui.button_port_terenne"), e -> {
			int x = BountyHunter9000GUIScreen.this.x;
			int y = BountyHunter9000GUIScreen.this.y;
			if (true) {
				ClientPacketDistributor.sendToServer(new BountyHunter9000GUIButtonMessage(5, x, y, z));
				BountyHunter9000GUIButtonMessage.handleButtonAction(entity, 5, x, y, z);
			}
		}).bounds(this.leftPos + 11, this.topPos + 116, 87, 20).build();
		this.addRenderableWidget(button_port_terenne);
	}
}