package com.legacyprojectiles;

import com.legacyprojectiles.config.LegacyProjectilesConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/**
 * Client-side entrypoint: registers the config (Cloth Config), which is then read by the
 * mixins on every frame. Mixin-based behaviour lives in the {@code mixin} package.
 */
@Environment(EnvType.CLIENT)
public class LegacyProjectilesScaleClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		AutoConfig.register(LegacyProjectilesConfig.class, GsonConfigSerializer::new);
	}
}
