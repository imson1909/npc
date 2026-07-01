package net.nekoyuni.SimpleEnemyMod.procedural.events.type;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.nekoyuni.SimpleEnemyMod.config.common.EventSpawnConfig;
import net.nekoyuni.SimpleEnemyMod.entity.ai.roles.utils.UnitRole;
import net.nekoyuni.SimpleEnemyMod.entity.unit.AbstractUnit;
import net.nekoyuni.SimpleEnemyMod.entity.unit.RUunitEntity;
import net.nekoyuni.SimpleEnemyMod.entity.unit.USunitEntity;
import net.nekoyuni.SimpleEnemyMod.procedural.events.system.DynamicEvent;
import net.nekoyuni.SimpleEnemyMod.registry.ModEntities;
import net.nekoyuni.SimpleEnemyMod.spawn.utils.SpawnHelper;
import net.minecraft.world.level.levelgen.Heightmap;

public class CombatEvent extends DynamicEvent {
   public CombatEvent() {
      super("far_combat");
   }

   @Override
   public double getBaseChance() {
      return (Double)EventSpawnConfig.COMBAT_BASE_CHANCE.get();
   }

   @Override
   public double getFailureMultiplier() {
      return (Double)EventSpawnConfig.COMBAT_FAILURE_MULTIPLIER.get();
   }

   @Override
   public int getMinDistance() {
      return 70;
   }

   @Override
   public int getMaxDistance() {
      return 120;
   }

   @Override
   public double getBiomeModifier(ServerLevel level, BlockPos pos) {
      Holder<Biome> biome = level.getBiome(pos);
      if (biome.is(BiomeTags.IS_DEEP_OCEAN) || biome.is(BiomeTags.IS_OCEAN)) {
         return 1.5;
      } else {
         return biome.is(BiomeTags.IS_BEACH) ? 0.8 : 1.0;
      }
   }

   @Override
   public boolean execute(ServerLevel level, ServerPlayer player, BlockPos centerPos) {
      if (!SpawnHelper.isValidSpawn(level, centerPos)) {
         return false;
      }

      int min = (Integer)EventSpawnConfig.COMBAT_MIN_SIZE.get();
      int max = (Integer)EventSpawnConfig.COMBAT_MAX_SIZE.get();
      int squadSize = min + level.random.nextInt(Math.max(1, max - min + 1));
      int separation = 24;
      BlockPos posRu = centerPos.offset(separation, 0, 0);
      BlockPos posUs = centerPos.offset(-separation, 0, 0);
      posRu = this.adjustHeight(level, posRu);
      posUs = this.adjustHeight(level, posUs);

      for (int i = 0; i < squadSize; i++) {
         this.spawnUnit(level, posRu, true);
      }

      for (int i = 0; i < squadSize; i++) {
         this.spawnUnit(level, posUs, false);
      }

      level.playSound(null, centerPos, SoundEvents.AMBIENT_CAVE.value(), SoundSource.AMBIENT, 2.0F, 0.8F);
      return true;
   }

   private void spawnUnit(ServerLevel level, BlockPos basePos, boolean isRu) {
      double dx = (level.random.nextDouble() - 0.5) * 6.0;
      double dz = (level.random.nextDouble() - 0.5) * 6.0;
      BlockPos spawnPos = basePos.offset((int)dx, 0, (int)dz);
      spawnPos = new BlockPos(spawnPos.getX(), basePos.getY(), spawnPos.getZ());
      AbstractUnit unit;
      if (isRu) {
         unit = new RUunitEntity((EntityType<? extends Monster>)ModEntities.RUUNIT.get(), level);
      } else {
         unit = new USunitEntity((EntityType<? extends Monster>)ModEntities.USUNIT.get(), level);
      }

      unit.setRole(UnitRole.DEFAULT);
      unit.moveTo(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
      unit.finalizeSpawn(level, level.getCurrentDifficultyAt(spawnPos), MobSpawnType.EVENT, null, null);
      level.addFreshEntity(unit);
   }

   private BlockPos adjustHeight(ServerLevel level, BlockPos pos) {
      int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos.getX(), pos.getZ());
      return new BlockPos(pos.getX(), y, pos.getZ());
   }
}
