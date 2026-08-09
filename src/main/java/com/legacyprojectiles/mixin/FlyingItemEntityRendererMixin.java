package com.legacyprojectiles.mixin;

import com.legacyprojectiles.config.LegacyProjectilesConfig;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.render.entity.state.FlyingItemEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Applies the configured scale multiplier to flying items.
 *
 * <p>In 1.8 every thrown item (snowball, ender pearl, potion, egg, XP bottle, eye of ender)
 * was rendered by {@code RenderSnowball} with a hard-coded 0.5 scale. In 1.21.11 they are
 * registered with the default {@code FlyingItemEntityRenderer} (scale 1.0F), so they appear
 * twice as large. The per-entity multiplier comes from {@link LegacyProjectilesConfig}
 * (default 0.5 = the 1.8 size).
 *
 * <p>Since the render-state refactor, {@code render} no longer sees the entity - only the
 * render state. The entity type is therefore stashed into the state during
 * {@code updateRenderState} (see {@link FlyingItemEntityRenderStateMixin}) and read back in
 * the scale redirect below. Fireballs / small fireballs / arrows are not in the config and
 * keep their vanilla scales.
 */
@Mixin(FlyingItemEntityRenderer.class)
public abstract class FlyingItemEntityRendererMixin {
	@Inject(method = "updateRenderState(Lnet/minecraft/entity/Entity;Lnet/minecraft/client/render/entity/state/FlyingItemEntityRenderState;F)V", at = @At("HEAD"))
	private void legacyProjectilesScale$storeEntityType(Entity entity, FlyingItemEntityRenderState state, float tickDelta, CallbackInfo ci) {
		((FlyingItemEntityRenderStateMixin) (Object) state).legacyProjectilesScale$setEntityType(entity.getType());
	}

	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;scale(FFF)V"))
	private void legacyProjectilesScale$applyConfiguredScale(MatrixStack matrices, float x, float y, float z, FlyingItemEntityRenderState state) {
		EntityType<?> type = ((FlyingItemEntityRenderStateMixin) (Object) state).legacyProjectilesScale$getEntityType();
		float multiplier = LegacyProjectilesConfig.getScaleMultiplier(type);
		matrices.scale(x * multiplier, y * multiplier, z * multiplier);
	}
}
