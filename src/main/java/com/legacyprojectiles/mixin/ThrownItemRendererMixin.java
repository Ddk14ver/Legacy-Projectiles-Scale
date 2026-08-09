package com.legacyprojectiles.mixin;
import com.legacyprojectiles.api.ThrownItemRenderStateEntityType;

import com.legacyprojectiles.config.LegacyProjectilesConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.entity.state.ThrownItemRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Applies the configured scale multiplier to thrown items (MC 26.2, unobfuscated names).
 *
 * <p>In 1.8 every thrown item (snowball, ender pearl, potion, egg, XP bottle, eye of ender)
 * was rendered by {@code RenderSnowball} with a hard-coded 0.5 scale. In 26.2 they are
 * registered with the default {@code ThrownItemRenderer} (scale 1.0F), so they appear twice
 * as large. The per-entity multiplier comes from {@link LegacyProjectilesConfig}
 * (default 0.5 = the 1.8 size).
 *
 * <p>In the render-state pipeline {@code submit} no longer sees the entity, so the entity
 * type is stashed into the render state during {@code extractRenderState} (see
 * {@link ThrownItemRenderStateEntityType}) and read back in the scale redirect below.
 * Fireballs / small fireballs / arrows are not in the config and keep their vanilla scales.
 */
@Mixin(ThrownItemRenderer.class)
public abstract class ThrownItemRendererMixin {
	@Inject(method = "extractRenderState(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/client/renderer/entity/state/ThrownItemRenderState;F)V", at = @At("HEAD"))
	private void legacyProjectilesScale$storeEntityType(Entity entity, ThrownItemRenderState state, float partialTicks, CallbackInfo ci) {
		((ThrownItemRenderStateEntityType) (Object) state).legacyProjectilesScale$setEntityType(entity.getType());
	}

	@Redirect(method = "submit", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;scale(FFF)V"))
	private void legacyProjectilesScale$applyConfiguredScale(PoseStack poseStack, float x, float y, float z, ThrownItemRenderState state) {
		EntityType<?> type = ((ThrownItemRenderStateEntityType) (Object) state).legacyProjectilesScale$getEntityType();
		float multiplier = LegacyProjectilesConfig.getScaleMultiplier(type);
		poseStack.scale(x * multiplier, y * multiplier, z * multiplier);
	}
}
