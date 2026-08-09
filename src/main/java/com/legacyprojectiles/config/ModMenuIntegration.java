package com.legacyprojectiles.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/**
 * Mod Menu entrypoint: opens the Cloth Config screen from the Mod Menu's mod list.
 */
@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		// AutoConfig.getConfigScreen is deprecated for removal but still the standard way
		// to bridge AutoConfig into Mod Menu in Cloth Config 21.x.
		@SuppressWarnings("removal")
		ConfigScreenFactory<?> factory = parent -> AutoConfig.getConfigScreen(LegacyProjectilesConfig.class, parent).get();
		return factory;
	}
}
