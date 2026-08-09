package com.legacyprojectiles.mixin;

import com.legacyprojectiles.config.LegacyProjectilesConfig;
import net.minecraft.world.entity.projectile.EyeOfEnder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

/**
 * Same early-hiding removal as {@link ThrowableProjectileMixin}, but for the eye of ender,
 * which duplicates the {@code tickCount < 2 && distance < 12.25} rule in its own
 * {@code shouldRenderAtSqrDistance}. The eye was rendered by {@code RenderSnowball} in 1.8
 * and had no such rule either.
 */
@Mixin(EyeOfEnder.class)
public abstract class EyeOfEnderMixin {
	@ModifyConstant(method = "shouldRenderAtSqrDistance", constant = @Constant(floatValue = 12.25F), require = 0)
	private float legacyProjectilesScale$alwaysRenderEarlyFloat(float constant) {
		return LegacyProjectilesConfig.isEarlyHideRemoved() ? 0.0F : constant;
	}

	@ModifyConstant(method = "shouldRenderAtSqrDistance", constant = @Constant(doubleValue = 12.25), require = 0)
	private double legacyProjectilesScale$alwaysRenderEarlyDouble(double constant) {
		return LegacyProjectilesConfig.isEarlyHideRemoved() ? 0.0 : constant;
	}
}
