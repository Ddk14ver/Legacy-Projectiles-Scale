package com.legacyprojectiles.mixin;

import net.minecraft.world.entity.EntityType;

/**
 * Plain (non-mixin) interface implemented by {@link ThrownItemRenderStateMixin} so that
 * other mixins can read the stashed entity type without referencing a mixin class.
 *
 * <p>Mixin cannot resolve references to other mixin classes inside injected handler
 * bytecode (it tries to find the type within the target class hierarchy, which fails),
 * so the hand-off goes through this interface instead.
 */
public interface ThrownItemRenderStateEntityType {
	EntityType<?> legacyProjectilesScale$getEntityType();

	void legacyProjectilesScale$setEntityType(EntityType<?> type);
}
