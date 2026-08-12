# Drive by DashPanels

An addon that wires [Dashpanels](https://modrinth.com/mod/dashpanels) control panels into the
[Drive-By-Wire](https://modrinth.com/mod/drive-by-wire-sable) cable network.

* Minecraft **1.21.1**
* NeoForge **21.1.235**
* Requires **Dashpanels 2.x** and **Drive-By-Wire with Sable 0.2.9+** (plus their own dependencies: Create, Sable, ...)

## What it does

Dashpanels modules are addressed by name inside a panel network. Drive-By-Wire moves named channels
through cables instead of redstone links. This addon maps one onto the other with two blocks.

### Panel Wire Transmitter

Attach it to a panel or a panel cable (any face). It joins the panel network and publishes **every
input module as its own wire channel**:

| Module | Channel |
| --- | --- |
| switch named `throttle` | `throttle` |
| joystick named `stick` | `stick/x`, `stick/y`, ... (one channel per entry) |

Hold the Drive-By-Wire **Wire**, right-click the transmitter, **scroll** to pick the module channel,
then right-click the face of the block that should receive it. Only changed values are pushed, so a
panel full of switches costs almost nothing per tick.

Right-click the transmitter with an empty hand to list the channels it currently offers.

### Panel Wire Receiver

The other direction: it takes the signal arriving from a wire connection on **any of its faces** and
feeds it into one output module (indicator light, display, ...) of the panel network it is attached to.

Pick the target module by name:

* rename the item in an anvil before placing it, or
* right-click the placed block with a renamed **name tag**.

Use `module/entry` to address a single entry of a multi-output module. Right-click with an empty hand
to see the current target and signal.

## Building

```bash
gradle build
```

The jar lands in `build/libs/`. Dashpanels and Drive-By-Wire are pulled from the Modrinth maven at
build time, so no manual jar juggling is needed. Every push to `main` is built by GitHub Actions and
published as a release with the jar attached.

For a dev run (`gradle runClient`) drop Dashpanels, Drive-By-Wire and their dependencies into
`run/mods` first.

## Notes on compatibility

Drive-By-Wire's network manager is bound at runtime through a small reflection bridge
(`WireBridge`). If a future Drive-By-Wire version renames it, the bridge blocks go idle and log a
warning instead of crashing the game.

## License

MIT. Drive-By-Wire and Dashpanels belong to their respective authors.
