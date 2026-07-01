package net.nekoyuni.SimpleEnemyMod.entity.client.us_unit;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Mth;
import net.nekoyuni.SimpleEnemyMod.config.ClientConfig;
import net.nekoyuni.SimpleEnemyMod.entity.client.GunLayerRenderer;
import net.nekoyuni.SimpleEnemyMod.entity.unit.USunitEntity;
import org.joml.Quaternionf;

public class USunitRenderer extends MobRenderer<USunitEntity, USunitModel<USunitEntity>> {
   private static ResourceLocation[] USUNIT_TEXTURES = new ResourceLocation[]{
      new ResourceLocation("simpleenemymod", "textures/entity/us_unit/us_unit_default.png")
   };

   public USunitRenderer(Context context) {
      super(context, new USunitModel(context.bakeLayer(USunitModelLayers.USUNIT_LAYER)), 0.5F);
      this.addLayer(new GunLayerRenderer(this, context.getItemInHandRenderer()));
   }

   public ResourceLocation getTextureLocation(USunitEntity entity) {
      int variant = entity.getVariant();
      return variant >= 0 && variant < USUNIT_TEXTURES.length ? USUNIT_TEXTURES[variant] : USUNIT_TEXTURES[0];
   }

   public boolean shouldRender(USunitEntity entity, Frustum frustum, double camX, double camY, double camZ) {
      int configDist = (Integer)ClientConfig.RENDER_DISTANCE.get();
      double maxDistance = configDist * configDist;
      double dx = entity.getX() - camX;
      double dy = entity.getY() - camY;
      double dz = entity.getZ() - camZ;
      double distanceSq = dx * dx + dy * dy + dz * dz;
      return distanceSq <= maxDistance ? true : super.shouldRender(entity, frustum, camX, camY, camZ);
   }

   protected void setupRotations(USunitEntity pEntity, PoseStack pPoseStack, float pAgeInTicks, float pBodyYRot, float pPartialTicks) {
      if (pEntity.deathAnimationState.isStarted()) {
         ((USunitModel)this.model).prepareMobModel(pEntity, 0.0F, 0.0F, pEntity.tickCount + pPartialTicks);
         float rootMotionX = ((USunitModel)this.model).root().x / 16.0F;
         float rootMotionY = ((USunitModel)this.model).root().y / 16.0F;
         float rootMotionZ = ((USunitModel)this.model).root().z / 16.0F;
         float rootRotX = ((USunitModel)this.model).root().xRot;
         float rootRotY = ((USunitModel)this.model).root().yRot;
         float rootRotZ = ((USunitModel)this.model).root().zRot;
         pPoseStack.translate(rootMotionX, 0.0F, 0.0F);
         pPoseStack.mulPose(new Quaternionf().rotationXYZ(rootRotX, rootRotY, rootRotZ));
         ((USunitModel)this.model).root().setPos(0.0F, 0.0F, 0.0F);
         ((USunitModel)this.model).root().setRotation(0.0F, 0.0F, 0.0F);
         float bodyRotation = Mth.lerp(pPartialTicks, pEntity.yBodyRotO, pEntity.yBodyRot);
         pPoseStack.mulPose(Axis.YP.rotationDegrees(180.0F - bodyRotation));
      } else {
         super.setupRotations(pEntity, pPoseStack, pAgeInTicks, pBodyYRot, pPartialTicks);
      }
   }

   public void render(USunitEntity p_115455_, float p_115456_, float p_115457_, PoseStack p_115458_, MultiBufferSource p_115459_, int p_115460_) {
      super.render(p_115455_, p_115456_, p_115457_, p_115458_, p_115459_, p_115460_);
   }

   private static void reloadTextures(ResourceManager rm) {
      List<ResourceLocation> found = new ArrayList<>();
      rm.listResources("textures/entity/us_unit", path -> path.getPath().endsWith(".png")).keySet().forEach(found::add);
      found.sort(Comparator.comparing(ResourceLocation::getPath));
      if (!found.isEmpty()) {
         USUNIT_TEXTURES = found.toArray(new ResourceLocation[0]);
      }
   }
}