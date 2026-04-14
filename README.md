# 🟢 Booger Client — Fabric Mod for Minecraft 25w41a (1.21.11 Snapshot)

A feature-rich Fabric client mod providing a drag-and-drop HUD editor, PvP tools,
custom crosshairs, cosmetics, and performance tweaks. Targets the **25w41a** snapshot.

---

## Features

### 🖥️ HUD System (drag-and-drop, F6 to edit)
| Element | Description |
|---------|-------------|
| FPS Counter | Color-coded: green ≥120, yellow ≥60, red <60 |
| Ping Display | Live server ping in ms |
| CPS Counter | Left and right clicks per second (1-second window) |
| Armor Durability | Bar graph for each armor piece with % remaining |
| Combo Counter | Hit combo with pop animation, 3s timeout |
| Speedometer | Movement speed in blocks/second |
| Coordinates | X/Y/Z with live updates |
| Potion Status | All active effects with countdown timers |
| Compass Bar | Directional compass with N/S/E/W markers |

### ⚔️ PvP Enhancements
- **Auto-Sprint** — automatically keeps you sprinting when moving forward
- **Combo Tracker** — counts consecutive hits, resets on damage or timeout
- **CPS Tracker** — accurate per-second click tracking via Mixin
- **Reach Display** — show attack reach in HUD
- **Hitbox Toggle** — toggle entity hitbox rendering

### ✛ Custom Crosshair
- **6 styles**: Cross, Dot, Circle, Square, Arrow, Plus
- Configurable **size**, **gap**, **thickness**
- **Hit color flash** — crosshair turns red on entity hit
- Optional **center dot** and **outline**
- Fully replaces vanilla crosshair via Mixin

### 🎨 Cosmetics (client-side)
- **6 cape styles**: Gradient Red/Blue/Green, Flame, Galaxy, Rainbow
- **Hat styles**: Crown, Party, Santa, Witch *(requires custom textures)*
- **Wings** toggle *(requires custom model — extend CosmeticsManager)*

### ⚙️ Performance
- **Reduced Particles** — reduce particle density
- **Entity Culling** — skip rendering entities behind solid blocks
- **Smooth FPS** — frame pacing improvements

---

## Controls

| Key | Action |
|-----|--------|
| `Right Shift` | Open Booger Client menu |
| `F6` | Toggle HUD edit mode (drag elements) |

---

## Installation

### Requirements
- Minecraft **25w41a** (1.21.11 snapshot)
- [Fabric Loader](https://fabricmc.net/use/installer/) **0.16.10+**
- [Fabric API](https://modrinth.com/mod/fabric-api/version/0.135.1+1.21.11) **0.135.1+1.21.11**
- Java **21**

### Build from source
```bash
./gradlew build
# Output: build/libs/boogerclient-1.0.0.jar
```

### Install
1. Copy `boogerclient-1.0.0.jar` into your `.minecraft/mods/` folder
2. Ensure Fabric API jar is also in `mods/`
3. Launch Minecraft with the Fabric profile

---

## Project Structure

```
src/main/java/com/boogerclient/
├── BoogerClient.java              # Mod entrypoint
├── config/
│   └── ApexConfig.java          # JSON config (saved to .minecraft/config/boogerclient.json)
├── hud/
│   ├── HudElement.java          # Abstract draggable element base
│   ├── HudManager.java          # Manages all elements, edit mode, drag logic
│   └── elements/
│       ├── TextHudElement.java  # Base for text-rendered elements
│       ├── FpsElement.java
│       ├── PingElement.java
│       ├── CpsElement.java
│       ├── ArmorElement.java
│       ├── ComboElement.java
│       ├── SpeedometerElement.java
│       ├── CoordinatesElement.java
│       ├── PotionStatusElement.java
│       └── DirectionElement.java
├── pvp/
│   └── PvpManager.java          # Auto-sprint, combo/CPS event routing
├── render/
│   └── CrosshairRenderer.java   # 6-style crosshair with hit flash
├── cosmetics/
│   ├── CosmeticsManager.java    # Cape/hat management
│   └── CapeRenderer.java        # Cape geometry + texture rendering
├── mixin/
│   ├── GameRendererMixin.java
│   ├── InGameHudMixin.java      # Replaces vanilla crosshair
│   ├── MouseMixin.java          # CPS click tracking
│   ├── PlayerEntityMixin.java   # Attack/damage events for combo
│   └── LivingEntityRendererMixin.java  # Cape injection
└── gui/
    └── ApexMenuScreen.java      # 5-tab settings screen
```

---

## Adding Custom Cosmetics

To add your own cape texture:
1. Create a 64×32 RGBA PNG following Minecraft's cape UV layout
2. Place it at `assets/boogerclient/textures/cosmetics/cape_custom.png`
3. Add an entry to `CosmeticsManager.CAPE_TEXTURES`
4. Add the name to `CapeStyle` enum in `ApexConfig`

---

## Config File

Saved to `.minecraft/config/boogerclient.json` — edit manually or via the in-game menu.
All HUD element positions are stored as 0.0–1.0 fractions of screen size, so they
scale correctly across resolutions.

---

## License
MIT — free to use, modify, and redistribute.
