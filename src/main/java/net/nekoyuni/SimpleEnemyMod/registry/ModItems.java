package net.nekoyuni.SimpleEnemyMod.registry;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.nekoyuni.SimpleEnemyMod.entity.ai.roles.utils.UnitRole;
import net.nekoyuni.SimpleEnemyMod.item.RoleSpawnEggItem;

public class ModItems {
   public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "simpleenemymod");
   private static final int COLOR_US_UNIT = 7824192;
   private static final int COLOR_US_SQUAD_LEADER = 6313266;
   private static final int COLOR_US_SQUAD_UNIT = 7035450;
   private static final int COLOR_RU_UNIT = 5789253;
   private static final int COLOR_RU_SQUAD_LEADER = 4209962;
   private static final int COLOR_RU_SQUAD_UNIT = 2170909;
   private static final int COLOR_PMC_UNIT = 5985087;
   public static final RegistryObject<Item> US_UNIT_SPAWN_EGG = ITEMS.register(
      "us_unit_spawn_egg",
      () -> new RoleSpawnEggItem(() -> (EntityType<? extends Mob>)ModEntities.USUNIT.get(), 7824192, 6049334, new Properties().stacksTo(64), UnitRole.DEFAULT)
   );
   public static final RegistryObject<Item> US_SQUAD_LEADER_SPAWN_EGG = ITEMS.register(
      "us_squad_leader_spawn_egg",
      () -> new RoleSpawnEggItem(
         () -> (EntityType<? extends Mob>)ModEntities.USUNIT.get(), 6313266, 9075279, new Properties().stacksTo(64), UnitRole.SQUAD_LEADER
      )
   );
   public static final RegistryObject<Item> US_SQUAD_UNIT_SPAWN_EGG = ITEMS.register(
      "us_squad_unit_spawn_egg",
      () -> new RoleSpawnEggItem(
         () -> (EntityType<? extends Mob>)ModEntities.USUNIT.get(), 7035450, 4865833, new Properties().stacksTo(64), UnitRole.SQUAD_UNIT
      )
   );
   public static final RegistryObject<Item> RU_UNIT_SPAWN_EGG = ITEMS.register(
      "ru_unit_spawn_egg",
      () -> new RoleSpawnEggItem(() -> (EntityType<? extends Mob>)ModEntities.RUUNIT.get(), 5789253, 3157796, new Properties().stacksTo(64), UnitRole.DEFAULT)
   );
   public static final RegistryObject<Item> RU_SQUAD_LEADER_SPAWN_EGG = ITEMS.register(
      "ru_squad_leader_spawn_egg",
      () -> new RoleSpawnEggItem(
         () -> (EntityType<? extends Mob>)ModEntities.RUUNIT.get(), 4209962, 9144426, new Properties().stacksTo(64), UnitRole.SQUAD_LEADER
      )
   );
   public static final RegistryObject<Item> RU_SQUAD_UNIT_SPAWN_EGG = ITEMS.register(
      "ru_squad_unit_spawn_egg",
      () -> new RoleSpawnEggItem(
         () -> (EntityType<? extends Mob>)ModEntities.RUUNIT.get(), 2170909, 7894362, new Properties().stacksTo(64), UnitRole.SQUAD_UNIT
      )
   );
   public static final RegistryObject<Item> PMC_UNIT_SPAWN_EGG = ITEMS.register(
      "pmc_unit_spawn_egg",
      () -> new RoleSpawnEggItem(
         () -> (EntityType<? extends Mob>)ModEntities.PMCUNIT.get(), 5985087, 2435871, new Properties().stacksTo(64), UnitRole.FRIENDLY_DEFAULT
      )
   );
   public static final RegistryObject<Item> PMC_SQUAD_LEADER_SPAWN_EGG = ITEMS.register(
      "pmc_squad_leader_spawn_egg",
      () -> new RoleSpawnEggItem(
         () -> (EntityType<? extends Mob>)ModEntities.PMCUNIT.get(), 5985087, 7764064, new Properties().stacksTo(64), UnitRole.FRIENDLY_SQUAD_LEADER
      )
   );
   public static final RegistryObject<Item> PMC_SQUAD_UNIT_SPAWN_EGG = ITEMS.register(
      "pmc_squad_unit_spawn_egg",
      () -> new RoleSpawnEggItem(
         () -> (EntityType<? extends Mob>)ModEntities.PMCUNIT.get(), 5985087, 7764064, new Properties().stacksTo(64), UnitRole.FRIENDLY_SQUAD_UNIT
      )
   );
   public static final RegistryObject<Item> RECRUIT_TABLE_ITEM = ITEMS.register(
      "recruit_table", () -> new BlockItem((Block)ModBlocks.RECRUIT_TABLE.get(), new Properties())
   );

   public static void register(IEventBus eventBus) {
      ITEMS.register(eventBus);
   }
}
