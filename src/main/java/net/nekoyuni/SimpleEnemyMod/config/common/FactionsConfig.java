package net.nekoyuni.SimpleEnemyMod.config.common;

import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public class FactionsConfig {
   public static ConfigValue<Boolean> RU_UNITS_FRIENDLY;
   public static ConfigValue<Boolean> US_UNITS_FRIENDLY;

   public static void init(Builder builder) {
      builder.push("Factions");
      RU_UNITS_FRIENDLY = builder.comment("If true, Ru Units will be friendly with Players and PMC Units").define("ruUnitsFriendly", false);
      US_UNITS_FRIENDLY = builder.comment("If true, Us Units will be friendly with Players and PMC Units").define("usUnitsFriendly", false);
      builder.pop();
   }
}