package com.legacyprojectiles.mixin;

import net.minecraft.entity.EntityType;

/**
 * Plain (non-mixin) interface implemented by {@link FlyingItemEntityRenderStateMixin} so
 * that other mixins can read the stashed entity type without referencing a mixin class.
 *
 * <p>Mixin cannot resolve references to other mixin classes inside injected handler
 * bytecode (it tries to find the type within the target class hierarchy, which fails at
 * runtime with "Resolution error: unable to find corresponding type"), so the hand-off
 * goes through this interface instead.
 */
public interface FlyingItemEntityRenderStateEntityType {
	EntityType<?> legacyProjectilesScale$getEntityType();

	void legacyProjectilesScale$setEntityType(EntityType<?> type);
}
