package com.legacyprojectiles.mixin;

import net.minecraft.client.render.entity.state.FlyingItemEntityRenderState;
import net.minecraft.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

/**
 * Carries the entity type from {@code updateRenderState} (where the entity is available)
 * into {@code render} (where it is not) through the render state.
 *
 * <p>Since the 1.21.6 render refactor, {@code FlyingItemEntityRenderer#render} only receives
 * the render state, not the entity. Each entity owns its render state instance
 * (created by {@code createRenderState()}), so storing the type here is race-free: the
 * {@code updateRenderState -> render} pair runs per entity on the render thread.
 */
@Mixin(FlyingItemEntityRenderState.class)
public abstract class FlyingItemEntityRenderStateMixin {
	@Unique
	private EntityType<?> legacyProjectilesScale$entityType;

	@Unique
	public EntityType<?> legacyProjectilesScale$getEntityType() {
		return this.legacyProjectilesScale$entityType;
	}

	@Unique
	public void legacyProjectilesScale$setEntityType(EntityType<?> type) {
		this.legacyProjectilesScale$entityType = type;
	}
}
