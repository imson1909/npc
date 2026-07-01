package net.nekoyuni.SimpleEnemyMod.entity.client.pmc_unit;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.world.entity.Entity;
import net.nekoyuni.SimpleEnemyMod.entity.client.animation.config.UnitAnimationConfig;
import net.nekoyuni.SimpleEnemyMod.entity.client.util.IArmorBoneProvider;
import net.nekoyuni.SimpleEnemyMod.entity.client.util.UnitModelDefinitions;
import net.nekoyuni.SimpleEnemyMod.entity.unit.AbstractUnit;

public class PmcUnitModel<T extends Entity> extends HierarchicalModel<T> implements IArmorBoneProvider {
   private final ModelPart fakeRoot;
   private final ModelPart unit;
   private final ModelPart head;
   private final ModelPart body;
   private final ModelPart rightArm;
   private final ModelPart rightHandAnchor;
   private final ModelPart leftArm;
   private final ModelPart rightLeg;
   private final ModelPart leftLeg;

   public PmcUnitModel(ModelPart root) {
      this.fakeRoot = root.getChild("fakeRoot");
      this.unit = this.fakeRoot.getChild("unit");
      this.head = this.unit.getChild("head");
      this.body = this.unit.getChild("body");
      this.rightArm = this.unit.getChild("rightArm");
      this.rightHandAnchor = this.rightArm.getChild("rightHandAnchor");
      this.leftArm = this.unit.getChild("leftArm");
      this.rightLeg = this.unit.getChild("rightLeg");
      this.leftLeg = this.unit.getChild("leftLeg");
   }

   @Override
   public void translateToBody(PoseStack poseStack) {
   }

   public static LayerDefinition createBodyLayer() {
      return UnitModelDefinitions.createBaseUnitBodyLayer();
   }

   @Override
   public void prepareMobModel(T entity, float limbSwing, float limbSwingAmount, float ageInTicks) {
      this.root().getAllParts().forEach(ModelPart::resetPose);
      if (entity instanceof AbstractUnit unitEntity) {
         if (unitEntity.getAnimationManager() == null) {
            unitEntity.setAnimationManager(UnitAnimationConfig.create(unitEntity, this.head, this.rightArm, this.leftArm));
         }
         unitEntity.getAnimationManager().update(unitEntity, unitEntity.tickCount);
      }
   }

   @Override
   public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
      if (entity instanceof AbstractUnit unitEntity) {
         if (unitEntity.getAnimationManager() != null) {
            unitEntity.getAnimationManager().applyProceduralLayers(this.root(), unitEntity, ageInTicks);
         }
      }
   }

   @Override
   public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
      this.fakeRoot.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
   }

   @Override
   public ModelPart root() {
      return this.fakeRoot;
   }

   @Override
   public ModelPart getUnit() {
      return this.unit;
   }

   @Override
   public ModelPart getHead() {
      return this.head;
   }

   @Override
   public ModelPart getBody() {
      return this.body;
   }

   @Override
   public ModelPart getRightArm() {
      return this.rightArm;
   }

   @Override
   public ModelPart getLeftArm() {
      return this.leftArm;
   }

   @Override
   public ModelPart getRightLeg() {
      return this.rightLeg;
   }

   @Override
   public ModelPart getLeftLeg() {
      return this.leftLeg;
   }
}