package me.theabab2333.headtap.entity.model;// Made with Blockbench 4.9.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class ThrownBambooJavelinModel<T extends Entity> extends EntityModel<T> {
	private final ModelPart bb_main;

	public ThrownBambooJavelinModel(ModelPart root) {
		this.bb_main = root.getChild("bb_main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition cube_r1 = bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 48).addBox(0.0F, -2.0F, -23.0F, 1.0F, 1.0F, 20.0F, new CubeDeformation(0.0F))
			.texOffs(24, 24).addBox(-2.0F, -1.0F, -24.0F, 1.0F, 1.0F, 21.0F, new CubeDeformation(0.0F))
			.texOffs(25, 46).addBox(0.0F, -3.0F, -24.0F, 1.0F, 1.0F, 21.0F, new CubeDeformation(0.0F))
			.texOffs(0, 24).addBox(-2.0F, -2.0F, -25.0F, 1.0F, 1.0F, 22.0F, new CubeDeformation(0.0F))
			.texOffs(25, 0).addBox(-1.0F, -3.0F, -25.0F, 1.0F, 1.0F, 22.0F, new CubeDeformation(0.0F))
			.texOffs(0, 0).addBox(-2.0F, -3.0F, -26.0F, 1.0F, 1.0F, 23.0F, new CubeDeformation(0.0F))
			.texOffs(47, 23).addBox(-1.0F, -1.0F, -23.0F, 2.0F, 1.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.5708F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
		bb_main.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
	}
}