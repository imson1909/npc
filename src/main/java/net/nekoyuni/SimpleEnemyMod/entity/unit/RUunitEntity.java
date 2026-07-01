package net.nekoyuni.SimpleEnemyMod.entity.unit;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.nekoyuni.SimpleEnemyMod.config.common.FactionsConfig;
import net.nekoyuni.SimpleEnemyMod.entity.ai.goals.VerticalAwareTargetGoal;
import net.nekoyuni.SimpleEnemyMod.entity.ai.roles.utils.UnitRole;
import net.nekoyuni.SimpleEnemyMod.entity.equipment.RuWeaponEquipper;
import net.nekoyuni.SimpleEnemyMod.registry.ModSounds;

public class RUunitEntity extends AbstractUnit {
   private static final int VARIANT_COUNT = 5;
   private int variant = this.random.nextInt(5);

   public RUunitEntity(EntityType<? extends Monster> entityType, Level level) {
      super(entityType, level);
   }

   public int getVariant() {
      return this.variant;
   }

   public void setVariant(int variant) {
      this.variant = variant;
      this.entityData.set(DATA_VARIANT_ID, variant);
   }

   @Override
   public void readAdditionalSaveData(CompoundTag tag) {
      super.readAdditionalSaveData(tag);
      if (tag.contains("Variant")) {
         this.setVariant(tag.getInt("Variant"));
      }
   }

   @Override
   public void addAdditionalSaveData(CompoundTag tag) {
      super.addAdditionalSaveData(tag);
      tag.putInt("Variant", this.getVariant());
   }

   @Override
   public void equipRandomGun() {
      RuWeaponEquipper.equipRandomGun(this, this.getRandom());
   }

   @Override
   public void setupRoleGoals() {
      double vRange = 32.0;
      this.goalSelector.removeAllGoals(pGoal -> true);
      this.targetSelector.removeAllGoals(pGoal -> true);
      if (this.role == null) {
         this.setRole(UnitRole.DEFAULT);
      }

      this.targetSelector.addGoal(1, new HurtByTargetGoal(this, new Class[0]).setAlertOthers(new Class[0]));
      this.targetSelector.addGoal(1, new VerticalAwareTargetGoal(this, PmcUnitEntity.class, true, vRange));
      if (!(Boolean)FactionsConfig.RU_UNITS_FRIENDLY.get()) {
         this.targetSelector.addGoal(2, new VerticalAwareTargetGoal(this, PmcUnitEntity.class, true, vRange));
         this.targetSelector.addGoal(2, new VerticalAwareTargetGoal(this, Player.class, true, vRange));
      }

      this.targetSelector.addGoal(2, new VerticalAwareTargetGoal(this, USunitEntity.class, true, vRange));
      this.targetSelector.addGoal(3, new VerticalAwareTargetGoal(this, Zombie.class, true, vRange));
      this.targetSelector.addGoal(3, new VerticalAwareTargetGoal(this, Skeleton.class, true, vRange));
      this.targetSelector.addGoal(3, new VerticalAwareTargetGoal(this, IronGolem.class, true, vRange));
      this.targetSelector.addGoal(4, new VerticalAwareTargetGoal(this, AbstractIllager.class, true, vRange));
      this.targetSelector.addGoal(2, new VerticalAwareTargetGoal(this, Monster.class, true, vRange, target -> {
         if (target.getClass() == this.getClass()) {
            return false;
         } else if ((Boolean)FactionsConfig.RU_UNITS_FRIENDLY.get() && target instanceof PmcUnitEntity) {
            return false;
         } else {
            return target instanceof AbstractUnit ? true : target instanceof Enemy;
         }
      }));
      switch (this.getRole()) {
         case DEFAULT:
            UnitRole.DEFAULT.getGoals().addGoals(this);
            break;
         case SQUAD_LEADER:
            UnitRole.SQUAD_LEADER.getGoals().addGoals(this);
            break;
         case SQUAD_UNIT:
            UnitRole.SQUAD_UNIT.getGoals().addGoals(this);
            break;
         default:
            UnitRole.DEFAULT.getGoals().addGoals(this);
      }
   }

   @Override
   protected SoundEvent getCustomHurtSound() {
      return (SoundEvent)ModSounds.SOUND_RU_UNIT_HURT.get();
   }

   @Override
   protected SoundEvent getCustomDeathSound() {
      return (SoundEvent)ModSounds.SOUND_RU_UNIT_DEATH.get();
   }

   @Override
   protected SoundEvent getCustomAlertSound() {
      return (SoundEvent)ModSounds.SOUND_RU_UNIT_ALERT.get();
   }

   @Override
   public float getSoundVolume() {
      return 1.5F;
   }
}
