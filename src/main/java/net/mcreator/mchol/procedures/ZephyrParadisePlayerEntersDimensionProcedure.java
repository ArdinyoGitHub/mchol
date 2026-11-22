package net.mcreator.mchol.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;

import net.mcreator.mchol.McholMod;

public class ZephyrParadisePlayerEntersDimensionProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if (world instanceof Level _level) {
			if (!_level.isClientSide()) {
				_level.playSound(null,
						BlockPos.containing(
								(entity instanceof ServerPlayer _player)
										? (_player.getRespawnConfig() != null && (ServerPlayer.RespawnConfig.getDimensionOrDefault(_player.getRespawnConfig()).equals(_player.level().dimension()))
												? _player.getRespawnConfig().respawnData().pos().getX()
												: _player.level().getLevelData().getRespawnData().pos().getX())
										: 0,
								(entity instanceof ServerPlayer _player)
										? (_player.getRespawnConfig() != null && (ServerPlayer.RespawnConfig.getDimensionOrDefault(_player.getRespawnConfig()).equals(_player.level().dimension()))
												? _player.getRespawnConfig().respawnData().pos().getY()
												: _player.level().getLevelData().getRespawnData().pos().getY())
										: 0,
								(entity instanceof ServerPlayer _player)
										? (_player.getRespawnConfig() != null && (ServerPlayer.RespawnConfig.getDimensionOrDefault(_player.getRespawnConfig()).equals(_player.level().dimension()))
												? _player.getRespawnConfig().respawnData().pos().getZ()
												: _player.level().getLevelData().getRespawnData().pos().getZ())
										: 0),
						BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("mchol:zephyrtobacco")), SoundSource.MUSIC, 1, 1);
			} else {
				_level.playLocalSound(
						((entity instanceof ServerPlayer _player)
								? (_player.getRespawnConfig() != null && (ServerPlayer.RespawnConfig.getDimensionOrDefault(_player.getRespawnConfig()).equals(_player.level().dimension()))
										? _player.getRespawnConfig().respawnData().pos().getX()
										: _player.level().getLevelData().getRespawnData().pos().getX())
								: 0),
						((entity instanceof ServerPlayer _player)
								? (_player.getRespawnConfig() != null && (ServerPlayer.RespawnConfig.getDimensionOrDefault(_player.getRespawnConfig()).equals(_player.level().dimension()))
										? _player.getRespawnConfig().respawnData().pos().getY()
										: _player.level().getLevelData().getRespawnData().pos().getY())
								: 0),
						((entity instanceof ServerPlayer _player)
								? (_player.getRespawnConfig() != null && (ServerPlayer.RespawnConfig.getDimensionOrDefault(_player.getRespawnConfig()).equals(_player.level().dimension()))
										? _player.getRespawnConfig().respawnData().pos().getZ()
										: _player.level().getLevelData().getRespawnData().pos().getZ())
								: 0),
						BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("mchol:zephyrtobacco")), SoundSource.MUSIC, 1, 1, false);
			}
		}
		McholMod.queueServerWork(200, () -> {
			if (world instanceof Level _level) {
				if (!_level.isClientSide()) {
					_level.playSound(null,
							BlockPos.containing(
									(entity instanceof ServerPlayer _player)
											? (_player.getRespawnConfig() != null && (ServerPlayer.RespawnConfig.getDimensionOrDefault(_player.getRespawnConfig()).equals(_player.level().dimension()))
													? _player.getRespawnConfig().respawnData().pos().getX()
													: _player.level().getLevelData().getRespawnData().pos().getX())
											: 0,
									(entity instanceof ServerPlayer _player)
											? (_player.getRespawnConfig() != null && (ServerPlayer.RespawnConfig.getDimensionOrDefault(_player.getRespawnConfig()).equals(_player.level().dimension()))
													? _player.getRespawnConfig().respawnData().pos().getY()
													: _player.level().getLevelData().getRespawnData().pos().getY())
											: 0,
									(entity instanceof ServerPlayer _player)
											? (_player.getRespawnConfig() != null && (ServerPlayer.RespawnConfig.getDimensionOrDefault(_player.getRespawnConfig()).equals(_player.level().dimension()))
													? _player.getRespawnConfig().respawnData().pos().getZ()
													: _player.level().getLevelData().getRespawnData().pos().getZ())
											: 0),
							BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("mchol:zephyrtobacco")), SoundSource.MUSIC, 1, 1);
				} else {
					_level.playLocalSound(
							((entity instanceof ServerPlayer _player)
									? (_player.getRespawnConfig() != null && (ServerPlayer.RespawnConfig.getDimensionOrDefault(_player.getRespawnConfig()).equals(_player.level().dimension()))
											? _player.getRespawnConfig().respawnData().pos().getX()
											: _player.level().getLevelData().getRespawnData().pos().getX())
									: 0),
							((entity instanceof ServerPlayer _player)
									? (_player.getRespawnConfig() != null && (ServerPlayer.RespawnConfig.getDimensionOrDefault(_player.getRespawnConfig()).equals(_player.level().dimension()))
											? _player.getRespawnConfig().respawnData().pos().getY()
											: _player.level().getLevelData().getRespawnData().pos().getY())
									: 0),
							((entity instanceof ServerPlayer _player)
									? (_player.getRespawnConfig() != null && (ServerPlayer.RespawnConfig.getDimensionOrDefault(_player.getRespawnConfig()).equals(_player.level().dimension()))
											? _player.getRespawnConfig().respawnData().pos().getZ()
											: _player.level().getLevelData().getRespawnData().pos().getZ())
									: 0),
							BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("mchol:zephyrtobacco")), SoundSource.MUSIC, 1, 1, false);
				}
			}
		});
	}
}