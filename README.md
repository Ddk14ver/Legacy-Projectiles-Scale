# Legacy Projectiles Scale (MC 26.2)

A client-side Fabric mod for **Minecraft 26.2** that restores the **1.8** visuals for
thrown projectiles.

> This is the **fabric-26.2** branch. The 1.21.11 version lives on **master**.
> `This mod was completely coded by Deepseek V4 Flash0731.`

## What it does

Two differences exist between 1.8 and modern Minecraft for thrown items:

1. **Scale.** In 1.8 (`RenderSnowball`) snowballs, ender pearls, splash/lingering potions,
   eggs, XP bottles and eyes of ender were rendered with a hard-coded `scale(0.5F)`. In
   26.2 they are registered with the default `ThrownItemRenderer` constructor (scale
   `1.0F`), so projectiles appear twice as large. This mod halves the scale back to `0.5F`
   (configurable per projectile).
2. **Early hiding.** Since ~1.21.6 the render pipeline hides the projectile during its
   first 2 ticks while it is still within 3.5 blocks of the camera:
   `if (tickCount < 2 && distance < 12.25) return false;` in
   `ThrowableProjectile#shouldRenderAtSqrDistance` (and the same rule in
   `EyeOfEnder#shouldRenderAtSqrDistance`). 1.8 had no such rule. This mod disables it
   (configurable).

Arrows are **not** touched: they use `TippableArrowRenderer` with the same geometry scale
in both versions.

## Configuration

Requires **Mod Menu** and **Cloth Config** at runtime (both optional for playing - without
them the mod just uses the defaults). Open the config screen from Mod Menu, or edit
`config/legacy-projectiles-scale.json` directly:

- Per-projectile **scale multiplier**: `snowball`, `enderPearl`, `splashPotion`,
  `lingeringPotion`, `egg`, `experienceBottle`, `enderEye` — default `0.5` (= 1.8 size,
  `1.0` = modern size). Values apply instantly.
- `removeEarlyHide` — whether the first-2-ticks hiding is removed (default `true`).

## Build

- **JDK 25+** (the MC 26.2 ecosystem - cloth-config/modmenu - is built for JVM 25).

```
gradlew build
```

## Notes for this branch (MC 26.2)

- Minecraft 26.1+ ships **unobfuscated** code (official Mojang names with parameter names),
  so **no mappings declaration** is needed — Yarn was discontinued after 1.21.11. Loom's
  obfuscation pipeline is switched off with `fabric.loom.disableObfuscation=true` in
  `gradle.properties`.
- Loom version `1.17-20260807.132355-18` (the pinned snapshot the official
  fabric-example-mod template points at).
- Mixins target the unobfuscated names: `ThrownItemRenderer`, `ThrownItemRenderState`,
  `ThrowableProjectile`, `EyeOfEnder`, `EntityTypes`, `PoseStack`.

## Implementation notes (26.2 render pipeline)

- `ThrowableProjectileMixin` + `EyeOfEnderMixin` — turn the `12.25F` distance constant into
  `0.0F` inside `shouldRenderAtSqrDistance`, so `distance < 0.0` is always false and the
  early-return can never trigger (far-distance culling untouched). The constant compiles
  both as float and as double, so two `@ModifyConstant` handlers with `require = 0` cover
  either form.
- `ThrownItemRendererMixin` — `extractRenderState` stashes the entity type into the render
  state (via `ThrownItemRenderStateMixin`), and the `submit` method's `PoseStack.scale`
  call is redirected to multiply by the configured per-type multiplier. Fireballs / small
  fireballs / arrows are not in the config and keep vanilla scales.

`required: true` is set in the mixin config so a mismatched Minecraft version fails loudly
instead of silently not applying.
