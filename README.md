# Legacy Projectiles Scale

A client-side Fabric mod for **Minecraft 1.21.11** that restores the **1.8** visuals for
thrown projectiles.`This mod was completely coded by Deepseek V4 Flash0731.`

## What it does

Two differences exist between 1.8 and modern Minecraft for thrown items:

1. **Scale.** In 1.8 (`RenderSnowball`) snowballs, ender pearls, splash/lingering potions,
   eggs, XP bottles and eyes of ender were rendered with a hard-coded `scale(0.5F)`. In
   1.21.11 they are registered with the default `FlyingItemEntityRenderer` constructor
   (scale `1.0F`), so projectiles appear twice as large. This mod scales them down
   (default `0.75`, configurable per projectile).
2. **Early hiding.** Since ~1.21.6 the render pipeline hides the projectile during its
   first 2 ticks while it is still within 3.5 blocks of the camera:
   `if (this.age < 2 && distance < 12.25) return false;` in `ThrownEntity#shouldRender`
   (and the same rule in `EyeOfEnderEntity#shouldRender`). 1.8 had no such rule. This mod
   disables it (configurable).

Arrows are **not** touched: they use `TippableArrowRenderer` with the same `0.05625`
geometry scale in both versions.

## Configuration

Requires **Mod Menu** and **Cloth Config** at runtime (both optional for playing - without
them the mod just uses the defaults). Open the config screen from Mod Menu, or edit
`config/legacy-projectiles-scale.json` directly:

- Per-projectile **scale multiplier**: `snowball`, `enderPearl`, `splashPotion`,
  `lingeringPotion`, `egg`, `experienceBottle`, `enderEye` — default `0.75`
  (`1.0` = modern size; 1.8 vanilla was ~`0.5`). Values apply instantly.
- `removeEarlyHide` — whether the first-2-ticks hiding is removed (default `true`).

## Implementation notes (1.21.11 render-state architecture)

Since the 1.21.6 render refactor the projectile is no longer hidden inside
`FlyingItemEntityRenderer#render`; the early-hide rule lives in the entity itself
(`ThrownEntity#shouldRender` / `EyeOfEnderEntity#shouldRender`). The mod therefore patches:

- `ThrownEntityMixin` + `EyeOfEnderEntityMixin` — turn the `12.25F` distance constant into
  `0.0F` inside `shouldRender`, so `distance < 0.0` is always false and the early-return can
  never trigger (the far-distance culling at the end of the method is untouched). The
  constant is compiled both as float and as double, so two `@ModifyConstant` handlers with
  `require = 0` cover whichever form the compiler emitted.
- `FlyingItemEntityRendererMixin` — `updateRenderState` stashes the entity type into the
  render state (via `FlyingItemEntityRenderStateMixin`), and the `render` method's
  `MatrixStack.scale` call is redirected to multiply by the configured per-type multiplier.
  Fireballs / small fireballs / arrows are not in the config and keep vanilla scales.

`required: true` is set in the mixin config so a mismatched Minecraft version fails loudly
instead of silently not applying.

## Build

Requires JDK 21+ (the mod targets Java 21).

```
gradlew build
```

The jar lands in `build/libs/`. The mod itself does not depend on the Fabric API; Mod Menu
and Cloth Config are runtime mods (Mod Menu also needs its own deps: Fabric API and
placeholder-api).
