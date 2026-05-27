<div align="center">

# Physis: Living Ecosystems for Minecraft

![PHYSIS](https://cdn.modrinth.com/data/cached_images/db5faeade782f5804349a61311c6a6194cde14df.png)

[![Source Code](https://img.shields.io/badge/Source-Code-E0E0E0?style=for-the-badge&logo=github&logoColor=white&labelColor=1c1c1c)](https://github.com/DevGBX9/Physis)
[![Issues](https://img.shields.io/badge/Report-Issues-E0E0E0?style=for-the-badge&logo=github&logoColor=white&labelColor=1c1c1c)](https://github.com/DevGBX9/Physis/issues)

</div>

Physis is a lightweight mod for both clients and servers designed to bring a sense of natural evolution and growth to Minecraft. It introduces autonomous, seed-based propagation systems where grass, flowers, ferns, and bushes spread organically and react to their environment in a way that feels natural, alive, and fully faithful to vanilla Minecraft. Everything happens automatically, requiring no player intervention.

The name Physis (φύσις) is the ancient Greek word for nature, representing the innate force that drives growth and change in all living things.

## Core Simulation

Rather than relying on simple random events tied to the player's presence, Physis operates as a global simulation. The growth logic is hooked directly into chunk ticking, meaning that vegetation continues to grow, expand, and compete across all loaded chunks independently of player distance.

## Seed-based Value Noise Distribution

Vegetation propagation in Physis does not spread in simple, uniform random patterns. Instead, the simulation utilizes a multi-octave Value Noise system seeded by the world's seed:
* **Organic Clusters:** Plants automatically group together in realistic, smooth clusters rather than scattering uniformly.
* **Vanilla-like Texture:** Combining biome-scale, patch-scale, and detail-scale noise, the mod perfectly mirrors the textured, organic distribution seen in vanilla Minecraft world generation.

## Vegetation and Ground Cover Spreading

The ground-level flora also spreads organically from existing plants. The system looks at nearby vegetation and attempts to spread it based on its specific ecological role.

### Spread Dynamics and Auto-Pruning

Every plant has its own growth rate, density limit, and required spacing. When a cluster of grass or plants exceeds its maximum density, the system does not simply halt growth. Instead, it utilizes an **auto-pruning** mechanic, randomly removing excess vegetation. This prevents the landscape from looking like an artificial, solid carpet and maintains the natural, scattered aesthetic of vanilla Minecraft.

| Plant Type | Spread Rate | Max Local Density | Spacing Rules | Ecological Role |
| :--- | :--- | :--- | :--- | :--- |
| **Grass** | 30% | 10 to 18 per 5x5 area | Tolerates clustering | The primary ground cover, spreading quickly across open terrain. |
| **Bushes** | 8% | 3 to 7 per 5x5 area | Can cluster tightly | Forms dense, cozy thickets in the landscape. |
| **Ferns** | 1.5% | 2 per 5x5 area | Requires 20-30 block separation | Spreads slowly and jumps to distant spots to form new patches. |
| **Flowers** | 1% | 2 per 5x5 area | Requires 10+ block separation | Rare and precious, forming small, isolated clusters. |
| **Cherry Petals**| 8% | 3 per 5x5 area | Restricted to Cherry canopy | Drops from cherry leaves. If a tree has 0 petals beneath it, the mod kickstarts the process by dropping 3-6 clusters simultaneously. |
| **Fireflies** | 5% | 1 per 7x7 area | Requires 4+ block separation | Highly specialized bushes restricted entirely to the immediate edge of water sources. |

### Water and Shade Preference

The environment dictates how fast ground cover spreads. Plants located near water sources experience a significant boost, growing two and a half times faster. The system uses a scoring logic that draws grass and bushes toward rivers and coastlines. Similarly, many plants prefer shade, focusing their growth beneath heavy tree canopies, while fungi will naturally seek out the darkest available spots.

### Pioneer Spreading

When a cluster of plants reaches its density limit, the system allows for "pioneer jumps," where a plant might establish a new, isolated cluster 10 to 30 blocks away. This ensures that vegetation spreads naturally across the terrain rather than bunching up in a single location.

### Structure Protection

Crucially, the mod respects your builds. Using a smart ray-marching algorithm, it ensures plants will not spread through player-made structures like fences, walls, gates, doors, slabs, or stairs. Nature will reclaim the wild, but it will respect your boundaries.

## Weather and Environment

The prevailing weather and time of day have a direct, measurable impact on the speed and health of the ecosystem.

| Condition | Ecosystem Impact | Technical Detail |
| :--- | :--- | :--- |
| **Rain** | **Rapid Expansion** | The internal growth tick interval is halved during precipitation, making stormy weather a time of massive growth. |
| **Nighttime** | **Dormancy** | Growth activity drops by 99% at night, as plants depend heavily on sunlight to fuel their expansion. |
| **TPS Scaling** | **Consistent Simulation**| The growth intervals automatically scale with the server's tick rate. If the game speed is increased, nature accelerates proportionally. |

## Technical Constraints and Compatibility

Physis is designed to be lightweight and works seamlessly on both clients and servers. Whether you are playing in a singleplayer world or running a large multiplayer community, the mod manages the environment's growth independently. It uses probabilistic ticking (approximately once every 10 seconds per chunk) to ensure minimal performance impact.

### Biome Integrity
Expansion is active across most overworld biomes but is intentionally restricted to maintain vanilla consistency:
*   **The Nether, The End, & Caves:** Growth is strictly disabled.

## License

Physis is open source software released under the MIT License. You are free to use, modify, and distribute it, provided proper credit is given to the original project and DevGBX9.

*Developed by DevGBX9*
