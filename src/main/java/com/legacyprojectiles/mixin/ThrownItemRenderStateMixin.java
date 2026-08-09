package com.legacyprojectiles.mixin;

import net.minecraft.client.renderer.entity.state.ThrownItemRenderState;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

/**
 * Carries the entity type from {@code extractRenderState} (where the entity is available)
 * into {@code submit} (where it is not) through the render state.
 *
 * <p>Each entity owns its render state instance (created by {@code createRenderState()}), so
 * storing the type here is race-free: the {@code extractRenderState -> submit} pair runs per
 * entity on the render thread.
 *
 * <p>Access is exposed through the plain interface {@link ThrownItemRenderStateEntityType}
 * - never cast to this mixin class directly, Mixin cannot remap references to other mixin
 * classes inside injected handlers.
 */
@Mixin(ThrownItemRenderState.class)
public abstract class ThrownItemRenderStateMixin implements ThrownItemRenderStateEntityType {
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
