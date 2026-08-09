package com.legacyprojectiles.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;

/**
 * Configuration for the mod, exposed in the Mod Menu through Cloth Config.
 *
 * <p>Every thrown projectile that was rendered by {@code RenderSnowball} in 1.8 gets its own
 * scale multiplier (default 0.5, which restores the 1.8 size). {@code removeEarlyHide}
 * disables the "don't draw for the first 2 ticks within 3.5 blocks" rule that modern
 * versions added.
 *
 * <p>Saved to {@code config/legacy-projectiles-scale.json} by {@link AutoConfig}; changes in
 * the GUI apply instantly (values are read from the holder on every frame).
 *
 * <p>Note: Cloth Config 21.x removed the {@code @ConfigEntry.Gui.DoubleSlider} annotation,
 * so the scale fields render as plain numeric input fields in the GUI.
 */
@Config(name = "legacy-projectiles-scale")
public class LegacyProjectilesConfig implements ConfigData {
	// --- Per-projectile scale multipliers (1.0 = modern size, 0.5 = 1.8 size) ---

	@ConfigEntry.Gui.Tooltip
	public double snowballScale = 0.75;

	@ConfigEntry.Gui.Tooltip
	public double enderPearlScale = 0.75;

	@ConfigEntry.Gui.Tooltip
	public double splashPotionScale = 0.75;

	@ConfigEntry.Gui.Tooltip
	public double lingeringPotionScale = 0.75;

	@ConfigEntry.Gui.Tooltip
	public double eggScale = 0.75;

	@ConfigEntry.Gui.Tooltip
	public double experienceBottleScale = 0.75;

	@ConfigEntry.Gui.Tooltip
	public double enderEyeScale = 0.75;

	// --- Behaviour ---

	/**
	 * Removes the {@code age < 2 && distance < 12.25} early-return in
	 * {@code ThrownEntity#shouldRender} / {@code EyeOfEnderEntity#shouldRender}, so the
	 * projectile is visible from the moment it is thrown. 1.8 had no such rule.
	 */
	@ConfigEntry.Gui.Tooltip
	public boolean removeEarlyHide = true;

	public static LegacyProjectilesConfig get() {
		return AutoConfig.getConfigHolder(LegacyProjectilesConfig.class).getConfig();
	}

	/**
	 * Scale multiplier for the given entity type, or 1.0F for anything not in the list
	 * (e.g. fireballs, arrows - which never had the 1.8 0.5 scale).
	 */
	public static float getScaleMultiplier(EntityType<?> type) {
		LegacyProjectilesConfig config = get();
		if (type == EntityTypes.SNOWBALL) {
			return (float) config.snowballScale;
		}
		if (type == EntityTypes.ENDER_PEARL) {
			return (float) config.enderPearlScale;
		}
		if (type == EntityTypes.SPLASH_POTION) {
			return (float) config.splashPotionScale;
		}
		if (type == EntityTypes.LINGERING_POTION) {
			return (float) config.lingeringPotionScale;
		}
		if (type == EntityTypes.EGG) {
			return (float) config.eggScale;
		}
		if (type == EntityTypes.EXPERIENCE_BOTTLE) {
			return (float) config.experienceBottleScale;
		}
		if (type == EntityTypes.EYE_OF_ENDER) {
			return (float) config.enderEyeScale;
		}
		return 1.0F;
	}

	public static boolean isEarlyHideRemoved() {
		return get().removeEarlyHide;
	}
}
