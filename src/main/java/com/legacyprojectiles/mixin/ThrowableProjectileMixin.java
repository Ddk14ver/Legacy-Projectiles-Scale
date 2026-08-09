package com.legacyprojectiles.mixin;

import com.legacyprojectiles.config.LegacyProjectilesConfig;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

/**
 * Removes the "hidden during the first 2 ticks while within 3.5 blocks of the camera" rule
 * for thrown items, restoring the 1.8 behaviour where the projectile is always visible.
 *
 * <p>{@code ThrowableProjectile#shouldRenderAtSqrDistance} (MC 26.2 name; formerly
 * {@code ThrownEntity#shouldRender}) contains:
 *
 * <pre>{@code
 * if (this.tickCount < 2 && distance < 12.25) {
 *     return false;
 * }
 * }</pre>
 *
 * <p>When the config's {@code removeEarlyHide} is enabled, the 12.25 constant is turned into
 * 0.0 so {@code distance < 0.0} is always false (distances are never negative) and the
 * early-return can never trigger. The far-distance culling at the end of the method is
 * untouched. The constant is compiled both as a float and as a double literal, so two
 * {@code @ModifyConstant} handlers (each {@code require = 0}, matching whichever form the
 * compiler emitted) are used.
 *
 * <p>This mixin targets the abstract {@code ThrowableProjectile}, covering every thrown
 * item: snowball, ender pearl, splash/lingering potion, egg and XP bottle - all of which
 * were rendered by {@code RenderSnowball} in 1.8 and had no such hiding rule.
 */
@Mixin(ThrowableProjectile.class)
public abstract class ThrowableProjectileMixin {
	@ModifyConstant(method = "shouldRenderAtSqrDistance", constant = @Constant(floatValue = 12.25F), require = 0)
	private float legacyProjectilesScale$alwaysRenderEarlyFloat(float constant) {
		return LegacyProjectilesConfig.isEarlyHideRemoved() ? 0.0F : constant;
	}

	@ModifyConstant(method = "shouldRenderAtSqrDistance", constant = @Constant(doubleValue = 12.25), require = 0)
	private double legacyProjectilesScale$alwaysRenderEarlyDouble(double constant) {
		return LegacyProjectilesConfig.isEarlyHideRemoved() ? 0.0 : constant;
	}
}
