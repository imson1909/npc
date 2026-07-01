package net.nekoyuni.SimpleEnemyMod.event.common;

import net.minecraft.world.entity.EntityType;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.nekoyuni.SimpleEnemyMod.entity.unit.PmcUnitEntity;
import net.nekoyuni.SimpleEnemyMod.entity.unit.RUunitEntity;
import net.nekoyuni.SimpleEnemyMod.entity.unit.USunitEntity;
import net.nekoyuni.SimpleEnemyMod.registry.ModEntities;

@EventBusSubscriber(modid = "simpleenemymod", bus = Bus.MOD)
public class EntityRegistrationHandler {
   @SubscribeEvent
   public static void registerAttributes(EntityAttributeCreationEvent event) {
      event.put((EntityType)ModEntities.USUNIT.get(), USunitEntity.createAttributes().build());
      event.put((EntityType)ModEntities.RUUNIT.get(), RUunitEntity.createAttributes().build());
      event.put((EntityType)ModEntities.PMCUNIT.get(), PmcUnitEntity.createAttributes().build());
   }
}
