package net.nekoyuni.SimpleEnemyMod.event.server;

import com.tacz.guns.api.event.server.AmmoHitBlockEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.nekoyuni.SimpleEnemyMod.config.common.EffectConfig;
import net.nekoyuni.SimpleEnemyMod.entity.unit.RUunitEntity;
import net.nekoyuni.SimpleEnemyMod.entity.unit.USunitEntity;
import net.nekoyuni.SimpleEnemyMod.network.ModNetworking;
import net.nekoyuni.SimpleEnemyMod.network.packets.PacketSuppression;

@EventBusSubscriber
public class PlayerSuppressionEventHandler {
   @SubscribeEvent
   public static void onAmmoHitBlock(AmmoHitBlockEvent event) {
      if ((Boolean)EffectConfig.ENABLE_SUPPRESSION.get()) {
         if (!event.getLevel().isClientSide) {
            Entity shooter = event.getAmmo().getOwner();
            if (shooter != null) {
               boolean isEnemy = shooter instanceof RUunitEntity || shooter instanceof USunitEntity;
               if (isEnemy) {
                  Vec3 hitPos = event.getHitResult().getLocation();
                  double radius = 7.0;

                  for (ServerPlayer player : event.getLevel().getEntitiesOfClass(ServerPlayer.class, new AABB(hitPos, hitPos).inflate(radius))) {
                     double distanceSq = player.distanceToSqr(hitPos);
                     double radiusSq = radius * radius;
                     if (distanceSq < radiusSq) {
                        float intensity = (float)(1.0 - distanceSq / radiusSq);
                        float finalAmount = intensity * 0.3F;
                        ModNetworking.sendToPlayer(new PacketSuppression(finalAmount), player);
                     }
                  }
               }
            }
         }
      }
   }
}
