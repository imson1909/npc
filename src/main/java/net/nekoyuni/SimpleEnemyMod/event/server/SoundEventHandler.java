package net.nekoyuni.SimpleEnemyMod.event.server;

import com.mojang.logging.LogUtils;
import com.tacz.guns.api.event.server.AmmoHitBlockEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.nekoyuni.SimpleEnemyMod.entity.unit.RUunitEntity;
import net.nekoyuni.SimpleEnemyMod.entity.unit.USunitEntity;
import net.nekoyuni.SimpleEnemyMod.network.ModNetworking;
import net.nekoyuni.SimpleEnemyMod.network.packets.PacketPlayImpactSound;
import org.slf4j.Logger;

@EventBusSubscriber(modid = "simpleenemymod")
public class SoundEventHandler {
   private static final Logger LOGGER = LogUtils.getLogger();
   private static final Map<Long, Long> lastSoundTime = new HashMap<>();
   private static final long MIN_SOUND_INTERVAL_MS = 100L;
   private static final long CLEANUP_INTERVAL_MS = 3000L;
   private static long lastCleanupTime = 0L;
   private static final double RANGE_SQ = 4096.0;

   @SubscribeEvent
   public static void onAmmoHitBlock(AmmoHitBlockEvent event) {
      Entity shooter = event.getAmmo().getOwner();
      if (shooter instanceof USunitEntity || shooter instanceof RUunitEntity) {
         ServerLevel level = (ServerLevel)event.getLevel();
         Vec3 impactPos = event.getHitResult().getLocation();
         List<ServerPlayer> nearbyPlayers = level.players().stream().filter(p -> p.distanceToSqr(impactPos) < 4096.0).collect(Collectors.toList());
         if (!nearbyPlayers.isEmpty()) {
            long locationKey = getLocationKeyLong(impactPos, 2.0);
            long currentTime = System.currentTimeMillis();
            Long lastTime = lastSoundTime.get(locationKey);
            if (lastTime == null || currentTime - lastTime >= 100L) {
               lastSoundTime.put(locationKey, currentTime);

               for (ServerPlayer player : nearbyPlayers) {
                  double distanceSqr = player.distanceToSqr(impactPos);
                  float distance = (float)Math.sqrt(distanceSqr);
                  float normalized = distance / 64.0F;
                  float volume = Math.max(0.1F, 2.5F * (1.0F - normalized * normalized));
                  float pitch = 0.8F + level.random.nextFloat() * 0.4F;
                  PacketPlayImpactSound packet = new PacketPlayImpactSound(
                     impactPos.x, impactPos.y, impactPos.z, volume, pitch, SoundSource.NEUTRAL, currentTime
                  );

                  try {
                     ModNetworking.sendToPlayer(packet, player);
                  } catch (Exception e) {
                     LOGGER.error("Failed to send sound packet to player {}: {}", player.getName().getString(), e.getMessage());
                  }
               }

               if (currentTime - lastCleanupTime > 3000L) {
                  cleanup(currentTime);
                  lastCleanupTime = currentTime;
               }
            }
         }
      }
   }

   private static long getLocationKeyLong(Vec3 pos, double precision) {
      int x = (int)(pos.x / precision) & 1048575;
      int y = (int)(pos.y / precision) & 4095;
      int z = (int)(pos.z / precision) & 1048575;
      return (long)x << 32 | (long)y << 20 | z;
   }

   public static void cleanup(long now) {
      lastSoundTime.entrySet().removeIf(entry -> now - entry.getValue() > 5000L);
   }
}
