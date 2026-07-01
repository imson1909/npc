package net.nekoyuni.SimpleEnemyMod.registry;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.nekoyuni.SimpleEnemyMod.block.RecruitTableBlock;

public class ModBlocks {
   public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "simpleenemymod");
   public static final RegistryObject<Block> RECRUIT_TABLE = BLOCKS.register(
      "recruit_table", () -> new RecruitTableBlock(Block.Properties.of().mapColor(MapColor.STONE).strength(1.0F).sound(SoundType.STONE))
   );

   public static void register(IEventBus eventBus) {
      BLOCKS.register(eventBus);
   }
}
