package com.legacyprojectiles.mixin;

import com.legacyprojectiles.config.LegacyProjectilesConfig;
import net.minecraft.entity.EyeOfEnderEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

/**
 * Same early-hiding removal as {@link ThrownEntityMixin}, but for the eye of ender, which
 * duplicates the {@code age < 2 && distance < 12.25} rule in its own {@code shouldRender}.
 * The eye was rendered by {@code RenderSnowball} in 1.8 and had no such rule either.
 */
@Mixin(EyeOfEnderEntity.class)
public abstract class EyeOfEnderEntityMixin {
	@ModifyConstant(method = "shouldRender", constant = @Constant(floatValue = 12.25F), require = 0)
	private float legacyProjectilesScale$alwaysRenderEarlyFloat(float constant) {
		return LegacyProjectilesConfig.isEarlyHideRemoved() ? 0.0F : constant;
	}

	@ModifyConstant(method = "shouldRender", constant = @Constant(doubleValue = 12.25), require = 0)
	private double legacyProjectilesScale$alwaysRenderEarlyDouble(double constant) {
		return LegacyProjectilesConfig.isEarlyHideRemoved() ? 0.0 : constant;
	}
}
