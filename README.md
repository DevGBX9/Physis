<div align="center">

# Physis: Living Ecosystems for Minecraft

![PHYSIS](https://cdn.modrinth.com/data/cached_images/db5faeade782f5804349a61311c6a6194cde14df.png)

[![Available on Modrinth](https://img.shields.io/badge/Available_on-Modrinth-E0E0E0?style=for-the-badge&logo=modrinth&logoColor=white&labelColor=1c1c1c)](https://modrinth.com/mod/physis)

</div>

Physis is a mod for both clients and servers designed to bring a sense of natural evolution to Minecraft. It introduces autonomous growth systems where forests expand, grass spreads toward water, and plants react to their environment in a way that feels organic and alive. Everything happens automatically, requiring no player intervention.

The name Physis (φύσις) is the ancient Greek word for nature, representing the innate force that drives growth and change in all living things.

## Core Simulation

Rather than relying on simple random events tied to the player's presence, Physis operates as a global simulation. The growth logic is hooked directly into chunk ticking, meaning that forests and vegetation continue to grow, expand, and compete across all loaded chunks independently of player distance. 

## Forest Expansion

Forests in Physis are no longer static. The mod constantly scans the world and classifies trees based on their surroundings to determine how they should expand.

### Tree Classification and Spread Behavior

| Tree Position | Local Environment | Expansion Behavior | Base Spread Probability |
| :--- | :--- | :--- | :--- |
| **Interior** | Surrounded by other trees on 6 or more sides. | Remains stable. The heart of the forest does not produce new saplings, preventing unnatural density. | 0% |
| **Edge** | Bordering open space with 2 to 5 covered sides. | Expands the forest outward into adjacent open territory. | ~2% per growth tick |
| **Pioneer** | Isolated with 0 to 1 covered sides. | Acts as a founder, attempting to establish an entirely new grove around itself in empty plains. | ~3% per growth tick |

**Species-Specific Behaviors:** Certain trees exhibit unique expansion traits. For example, Spruce trees have a 20% chance to plant 4 saplings in a 2x2 grid, allowing them to naturally grow into Giant Spruce trees over time.

### Natural Influences and Biome Integration

A global wind system shifts every few minutes, subtly influencing the direction of forest growth. This results in asymmetric, realistic forest shapes that evolve over time. 

The mod is highly species-aware and integrates deeply with the world's biomes. It identifies the parent tree and ensures the correct matching sapling is planted. Furthermore, species compete for territory:
*   **Native Species:** Saplings inherently suited to the current biome thrive effortlessly.
*   **Invasive Species:** Trees planted outside their native biome face resistance, but they maintain a 60% chance to adapt and spread, allowing players to deliberately introduce foreign forests that will slowly naturalize over time.

### Placement Rules and Soil Fertility

Before a sapling is planted, the system verifies several environmental factors. The ground must be relatively flat, and there must be enough light (though shade-tolerant fungi have lower requirements). 

Soil fertility plays a massive role in successful expansion. Richer soils provide mathematical bonuses to growth chances: Podzol (+35%), Moss (+30%), Rooted Dirt (+20%), Mycelium (+15%), and Grass (+10%). Furthermore, the presence of decaying organic matter, such as nearby dead bushes or fallen leaves, acts as a natural fertilizer, providing an additional minor boost to fertility.

Crucially, the mod respects your builds. Using a smart ray-marching algorithm, it ensures trees and plants will not spread through player-made structures like fences, walls, gates, doors, slabs, or stairs. Nature will reclaim the wild, but it will respect your boundaries.

## Vegetation and Ground Cover

The ground-level flora also spreads organically from existing plants. The system looks at nearby vegetation and attempts to spread it based on its specific ecological role.

### Spread Dynamics and Auto-Pruning

Every plant has its own growth rate, density limit, and required spacing. When a cluster of grass or plants exceeds its maximum density, the system does not simply halt growth. Instead, it utilizes an **auto-pruning** mechanic, randomly removing excess vegetation. This prevents the landscape from looking like an artificial, solid carpet and maintains the natural, scattered aesthetic of vanilla Minecraft.

| Plant Type | Spread Rate | Max Local Density | Spacing Rules | Ecological Role |
| :--- | :--- | :--- | :--- | :--- |
| **Grass** | 12% | 5 to 9 per 5x5 area | Tolerates clustering | The primary ground cover, spreading quickly across open terrain. |
| **Bushes** | 3% | 3 per 5x5 area | Can cluster tightly | Forms dense, cozy thickets in the landscape. |
| **Ferns** | 1.5% | 2 per 5x5 area | Requires 20-30 block separation | Spreads slowly and jumps to distant spots to form new patches. |
| **Flowers** | 1% | 2 per 5x5 area | Requires 10+ block separation | Rare and precious, forming small, isolated clusters. |
| **Cherry Petals**| 8% | 3 per 5x5 area | Restricted to Cherry canopy | Drops from cherry leaves. If a tree has 0 petals beneath it, the mod kickstarts the process by dropping 3-6 clusters simultaneously. |
| **Fireflies** | 5% | 1 per 7x7 area | Requires 4+ block separation | Highly specialized bushes restricted entirely to the immediate edge of water sources. |

### Water and Shade Preference

The environment dictates how fast ground cover spreads. Plants located near water sources experience a significant boost, growing two and a half times faster. The system uses a scoring logic that draws grass and bushes toward rivers and coastlines. Similarly, many plants prefer shade, focusing their growth beneath heavy tree canopies, while fungi will naturally seek out the darkest available spots.

### Pioneer Spreading

When a cluster of plants reaches its density limit, the system allows for "pioneer jumps," where a plant might establish a new, isolated cluster 10 to 30 blocks away. This ensures that vegetation spreads naturally across the terrain rather than bunching up in a single location.

## Life Cycles and Composting

Physis implements a true ecological cycle of life and death. 

All saplings planted by the simulation are carefully monitored. If a sapling ends up in a spot that becomes overcrowded, loses access to adequate light, or no longer has suitable soil, it will fail to grow and wither into a dead bush. 

These dead bushes do not remain forever. After a set duration, they naturally decompose into the soil. This decomposition triggers a localized "bone-meal" effect, transferring the organic matter back into the earth to spontaneously spawn fresh grass, ferns, or rare flowers in the immediate vicinity.

### Dynamic Growth Audio & Biome Adaptation

Whenever a mod-planted sapling successfully advances into a tree, the mod triggers a specialized immersive audio sequence simulating the earth cracking, roots expanding, and canopy rustling. The volume and pitch of these sounds dynamically scale with the tree's category—giant trees (like Dark Oak or Pale Oak) can be heard from up to 192 blocks away, while smaller trees are quieter. 

Furthermore, upon growth, the tree actively terraforms its immediate area by converting the local blocks' biome to match its native species, ensuring the environment truly adapts to the new forest.

## Weather and Environment

The prevailing weather and time of day have a direct, measurable impact on the speed and health of the ecosystem.

| Condition | Ecosystem Impact | Technical Detail |
| :--- | :--- | :--- |
| **Rain** | **Rapid Expansion** | The internal growth tick interval is halved during precipitation, making stormy weather a time of massive growth. |
| **Thunderstorms** | **Canopy Damage** | Lightning strikes can occasionally damage and remove tree leaves, creating natural clearings and letting light into dense forests. |
| **Nighttime** | **Dormancy** | Growth activity drops by 99% at night, as plants depend heavily on sunlight to fuel their expansion. |
| **TPS Scaling** | **Consistent Simulation**| The growth intervals automatically scale with the server's tick rate. If the game speed is increased, nature accelerates proportionally. |

## Technical Constraints and Compatibility

Physis is designed to be lightweight and works seamlessly on both clients and servers. Whether you are playing in a singleplayer world or running a large multiplayer community, the mod manages the environment's growth independently. It uses probabilistic ticking (approximately once every 10 seconds per chunk) to ensure minimal performance impact, while maintaining immutable data views to prevent memory leaks during massive forest updates.

### Biome Integrity
Expansion is active across most overworld biomes but is intentionally restricted to maintain vanilla consistency:
*   **The Nether, The End, & Caves:** Growth is strictly disabled.
*   **Swamps:** Tree expansion is completely disabled to prevent naturally dense areas from becoming entirely impassable.
*   **Plains & Meadows:** Open biomes allow tree growth, but at a severely reduced rate (30% success chance, strictly adjacent to existing trees). This preserves their identity as wide-open landscapes while permitting extremely slow, natural forest encroachment.


## License

Physis is open source software released under the MIT License. You are free to use, modify, and distribute it, provided proper credit is given to the original project and the GBX Team.

*Developed by the GBX Team*
