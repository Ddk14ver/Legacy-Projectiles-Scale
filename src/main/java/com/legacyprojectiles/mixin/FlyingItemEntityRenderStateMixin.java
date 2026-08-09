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
 *
 * <p>Access is exposed through the plain interface {@link FlyingItemEntityRenderStateEntityType}
 * - never cast to this mixin class directly, Mixin cannot remap references to other mixin
 * classes inside injected handlers.
 */
@Mixin(FlyingItemEntityRenderState.class)
public abstract class FlyingItemEntityRenderStateMixin implements FlyingItemEntityRenderStateEntityType {
	@Unique
	private EntityType<?> legacyProjectilesScale$entityType;

	@Override
	public EntityType<?> legacyProjectilesScale$getEntityType() {
		return this.legacyProjectilesScale$entityType;
	}

	@Override
	public void legacyProjectilesScale$setEntityType(EntityType<?> type) {
		this.legacyProjectilesScale$entityType = type;
	}
}
