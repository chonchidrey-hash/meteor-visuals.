# Meteor Visuals (Fabric 1.21.5)

A standalone client-side Fabric mod with a Meteor-inspired compact Click GUI and two visual modules: Hitboxes and Tracers. It is an independent implementation; it does **not** bundle or copy Meteor Client source code and is not affiliated with Meteor Development.

## Features in this first build

- Right Shift opens the GUI.
- Independent Hitboxes and Tracers toggles.
- Player and mob/animal filters.
- Separate hitbox and tracer colors (preset color cycling).
- Tracer width control.
- Maximum render distance control.
- Settings persist in `config/meteor-visuals.properties`.
- Client-side only; no server mod is required.

## Compatibility

- Minecraft Java Edition 1.21.5
- Java 21
- Fabric Loader 0.16.10 or newer
- Fabric API 0.119.5+1.21.5

Lunar's launcher screenshot showed a Fabric 1.21.5 profile with Fabric API and Mod Menu. This mod is built for that profile, but Lunar compatibility still needs an in-game test.

## Build

Open this folder as a Gradle project in IntelliJ IDEA (Java 21), or use Gradle 8.12+:

```bash
gradle build
```

The installable remapped mod jar will be created in `build/libs/meteor-visuals-0.1.0.jar`.

## Install on Lunar

1. Build the project.
2. In Lunar's Minecraft 1.21.5 Fabric profile, use **Add Content** / the mods folder to add `meteor-visuals-0.1.0.jar`.
3. Keep Fabric API installed.
4. Launch and press Right Shift.

## Notes / current limitations

- Hitboxes currently render as outlines; filled hitbox faces and through-wall rendering are not included yet.
- Tracers originate at the camera/crosshair center.
- This project has not been compiled or launched in this environment because outbound network access is unavailable. Run the Gradle build locally before installing; if Gradle reports an API/mapping error, send the full error text and I can correct it.

## License

GPL-3.0-or-later. This is a standalone implementation, not a redistribution of Meteor Client code. Meteor Client is separately licensed under GPL-3.0: https://github.com/MeteorDevelopment/meteor-client
