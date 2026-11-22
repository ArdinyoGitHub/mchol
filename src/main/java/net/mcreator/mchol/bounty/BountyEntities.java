package net.mcreator.mchol.bounty;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.BuiltInRegistries;

import net.mcreator.mchol.McholMod;

import javax.annotation.Nullable;

/**
 * The bounty bosses (Krubis, Douglas) and shopkeepers (Gene, Mr. Keeps) are MCreator livingentity elements.
 * The hand-written code refers to them by registry id instead of the generated classes, so it keeps compiling
 * whatever MCreator regenerates.
 */
public enum BountyEntities {
	KRUBIS("krubis"), DOUGLAS("douglas"), GENE("gene"), MR_KEEPS("mr_keeps");

	public final Identifier id;

	BountyEntities(String path) {
		this.id = Identifier.fromNamespaceAndPath(McholMod.MODID, path);
	}

	@Nullable
	public EntityType<?> type() {
		return BuiltInRegistries.ENTITY_TYPE.getOptional(id).orElse(null);
	}

	public boolean is(Entity entity) {
		return entity.getType() == type();
	}

	@Nullable
	public static BountyEntities of(Entity entity) {
		for (BountyEntities value : values())
			if (value.is(entity))
				return value;
		return null;
	}

	public boolean isBoss() {
		return this == KRUBIS || this == DOUGLAS;
	}

	public boolean isShopkeeper() {
		return this == GENE || this == MR_KEEPS;
	}
}
