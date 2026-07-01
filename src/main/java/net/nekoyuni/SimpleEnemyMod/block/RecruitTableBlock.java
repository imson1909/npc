package net.nekoyuni.SimpleEnemyMod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.nekoyuni.SimpleEnemyMod.config.common.MiscConfig;
import net.nekoyuni.SimpleEnemyMod.entity.unit.PmcUnitEntity;
import net.nekoyuni.SimpleEnemyMod.registry.ModEntities;

public class RecruitTableBlock extends HorizontalDirectionalBlock {
   public RecruitTableBlock(Properties pProperties) {
      super(pProperties);
      this.registerDefaultState((BlockState)((BlockState)this.getStateDefinition()	.any()).setValue(FACING, Direction.NORTH));
   }

   public void createBlockStateDefinition(Builder<Block, BlockState> builder) {
      builder.add(new Property[]{FACING});
   }

   public BlockState getStateForPlacement(BlockPlaceContext context) {
      return (BlockState)this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
   }

   public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
      if (level.isClientSide) {
         return InteractionResult.SUCCESS;
      }

      Item requiredItem = MiscConfig.getRecruitItem();
      int requiredAmount = (Integer)MiscConfig.RECRUIT_PRICE.get();
      ItemStack itemInHand = player.getItemInHand(hand);
      if (itemInHand.getItem() == requiredItem && itemInHand.getCount() >= requiredAmount) {
         PmcUnitEntity unit = (PmcUnitEntity)((EntityType)ModEntities.PMCUNIT.get()).create(level);
         if (unit == null) {
            return InteractionResult.FAIL;
         }

         itemInHand.shrink(requiredAmount);
         unit.moveTo(pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, 0.0F, 0.0F);
         unit.finalizeSpawn((ServerLevelAccessor)level, level.getCurrentDifficultyAt(pos), MobSpawnType.TRIGGERED, null, null);
         unit.setOwner(player.getUUID());
         level.addFreshEntity(unit);
         this.playContractSuccessSound(level, pos);
         return InteractionResult.SUCCESS;
      } else {
         player.displayClientMessage(Component.literal("You need " + requiredAmount + " " + requiredItem.getDescription().getString() + " to recruit a PMC!!"), true);
         return InteractionResult.FAIL;
      }
   }

   private void playContractSuccessSound(Level level, BlockPos pos) {
      if (level instanceof ServerLevel serverLevel) {
         if (level.getServer() != null) {
            serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER, pos.getX() + 0.5, pos.getY() + 1.1, pos.getZ() + 0.5, 10, 0.2, 0.2, 0.2, 0.05);
            level.playSound(null, pos, SoundEvents.ANVIL_LAND, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.getServer().tell(new TickTask(level.getServer().getTickCount() + 10, () -> {
               level.playSound(null, pos, SoundEvents.IRON_DOOR_OPEN, SoundSource.BLOCKS, 1.0F, 1.0F);
               serverLevel.sendParticles(ParticleTypes.POOF, pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5, 15, 0.4, 0.4, 0.4, 0.1);
            }));
         }
      }
   }
}