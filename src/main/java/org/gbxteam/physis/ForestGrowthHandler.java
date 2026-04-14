/*
 * This file is part of the Physis project, licensed under the MIT License.
 *
 * Copyright (C) 2026 GBX Team and contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.gbxteam.physis;

//#if MC >= 260100
//$$ import net.minecraft.core.BlockPos;
//$$ import net.minecraft.core.Holder;
//$$ import net.minecraft.core.registries.BuiltInRegistries;
//$$ import net.minecraft.resources.ResourceKey;
//$$ import net.minecraft.server.level.ServerLevel;
//$$ import net.minecraft.util.RandomSource;
//$$ import net.minecraft.world.level.biome.Biome;
//$$ import net.minecraft.world.level.biome.Biomes;
//$$ import net.minecraft.world.level.block.Block;
//$$ import net.minecraft.world.level.block.Blocks;
//$$ import net.minecraft.world.level.block.LeavesBlock;
//$$ import net.minecraft.world.level.block.RotatedPillarBlock;
//$$ import net.minecraft.world.level.block.SaplingBlock;
//$$ import net.minecraft.world.level.block.state.BlockState;
//$$ import net.minecraft.world.level.levelgen.Heightmap;
//$$ import java.util.Optional;
//$$ import java.util.List;
//$$ import java.util.ArrayList;
//#endif

public class ForestGrowthHandler {
    
    //#if MC >= 260100
    
    // ==================== [1] WIND SYSTEM ====================
    // Wind direction changes slowly over time for natural-looking seed dispersal
//$$    private static double windAngle = 0;
//$$    private static long lastWindUpdate = 0;

    // ==================== MAIN TICK ====================
//$$    public static void tick(ServerLevel level) {
//$$        long gameTime = level.getGameTime();
//$$
//$$        // Update wind direction every 5 minutes (6000 ticks)
//$$        if (gameTime - lastWindUpdate > 6000) {
//$$            windAngle += (level.getRandom().nextDouble() - 0.5) * Math.PI * 0.5; // Shift ±45°
//$$            lastWindUpdate = gameTime;
//$$        }
//$$
//$$        // [10] WEATHER IMPACT: Rain boosts growth frequency
//$$        boolean isRaining = level.isRaining();
//$$        boolean isThundering = level.isThundering();
//$$        int growthInterval = isRaining ? 200 : 300; // 10s in rain, 15s normally
//$$
//$$        if (gameTime % growthInterval == 0) {
//$$            RandomSource random = level.getRandom();
//$$            int nearbyAttempts = isRaining ? 6 : 4; // More growth during rain
//$$            int globalAttempts = isRaining ? 15 : 10;
//$$
//$$            level.players().forEach(player -> {
//$$                BlockPos playerPos = player.blockPosition();
//$$                
//$$                // NEARBY (Scattering with wind bias)
//$$                for (int i = 0; i < nearbyAttempts; i++) {
//$$                    int windBiasX = (int) (Math.cos(windAngle) * 8);
//$$                    int windBiasZ = (int) (Math.sin(windAngle) * 8);
//$$                    int ox = random.nextInt(64) - 32 + windBiasX;
//$$                    int oz = random.nextInt(64) - 32 + windBiasZ;
//$$                    processGrowth(level, playerPos.offset(ox, 0, oz), false);
//$$                }
//$$                
//$$                // GLOBAL (Expansion with wind bias)
//$$                for (int i = 0; i < globalAttempts; i++) {
//$$                    int windBiasX = (int) (Math.cos(windAngle) * 40);
//$$                    int windBiasZ = (int) (Math.sin(windAngle) * 40);
//$$                    int ox = random.nextInt(1200) - 600 + windBiasX;
//$$                    int oz = random.nextInt(1200) - 600 + windBiasZ;
//$$                    processGrowth(level, playerPos.offset(ox, 0, oz), true);
//$$                }
//$$            });
//$$        }
//$$
//$$        // Periodic checks (Every 2 seconds)
//$$        if (gameTime % 40 == 0) {
//$$            runHealthChecks(level);
//$$            runCompostChecks(level);
//$$        }
//$$
//$$        // [10] THUNDER DAMAGE: Lightning strikes can clear small areas
//$$        if (isThundering && gameTime % 600 == 0) {
//$$            RandomSource random = level.getRandom();
//$$            if (random.nextFloat() < 0.15f) { // 15% chance every 30s during thunder
//$$                level.players().forEach(player -> {
//$$                    BlockPos strikePos = player.blockPosition().offset(random.nextInt(200) - 100, 0, random.nextInt(200) - 100);
//$$                    strikePos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, strikePos);
//$$                    applyThunderDamage(level, strikePos);
//$$                });
//$$            }
//$$        }
//$$    }

    // ==================== HEALTH CHECKS ====================
//$$    private static void runHealthChecks(ServerLevel level) {
//$$        long currentTime = level.getGameTime();
//$$        ForestGrowthData data = ForestGrowthData.get(level);
//$$        
//$$        data.getAllTrackedSaplings().forEach((posLong, lastTime) -> {
//$$            BlockPos pos = BlockPos.of(posLong);
//$$            if (!level.isLoaded(pos)) return;
//$$
//$$            BlockState state = level.getBlockState(pos);
//$$            if (!(state.getBlock() instanceof SaplingBlock || state.getBlock() == Blocks.AZALEA || state.getBlock() == Blocks.MANGROVE_PROPAGULE)) {
//$$                data.removeSapling(pos);
//$$                return;
//$$            }
//$$
//$$            long age = currentTime - lastTime;
//$$            // Checks: first at 30s, then recurring every 60s
//$$            if ((age >= 600 && age < 640) || (age >= 1200)) {
//$$                int spacing = getRequiredSpacing(state.getBlock());
//$$                if (!isAreaClearForHealthCheck(level, pos, spacing)) {
//$$                    level.setBlock(pos, Blocks.DEAD_BUSH.defaultBlockState(), 3);
//$$                    data.removeSapling(pos);
//$$                    data.addDeadBush(pos, currentTime);
//$$                } else {
//$$                    data.updateSaplingCheckTime(pos, currentTime);
//$$                }
//$$            }
//$$        });
//$$    }

    // ==================== COMPOSTING ====================
//$$    private static void runCompostChecks(ServerLevel level) {
//$$        long currentTime = level.getGameTime();
//$$        ForestGrowthData data = ForestGrowthData.get(level);
//$$
//$$        data.getAllDeadBushes().forEach((posLong, deathTime) -> {
//$$            BlockPos pos = BlockPos.of(posLong);
//$$            if (!level.isLoaded(pos)) return;
//$$
//$$            if (currentTime - deathTime >= 300) { // 15 seconds
//$$                if (level.getBlockState(pos).is(Blocks.DEAD_BUSH)) {
//$$                    level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
//$$                    applyCompostEffect(level, pos);
//$$                }
//$$                data.removeDeadBush(pos);
//$$            }
//$$        });
//$$    }
//$$
//$$    private static void applyCompostEffect(ServerLevel level, BlockPos pos) {
//$$        // 5x5 Natural Bonemeal area
//$$        RandomSource random = level.getRandom();
//$$        for (int x = -2; x <= 2; x++) {
//$$            for (int z = -2; z <= 2; z++) {
//$$                BlockPos target = pos.offset(x, -1, z);
//$$                BlockPos above = target.above();
//$$
//$$                if (level.getBlockState(target).is(Blocks.GRASS_BLOCK) && level.isEmptyBlock(above)) {
//$$                    if (random.nextFloat() < 0.7f) {
//$$                        level.levelEvent(2005, above, 0);
//$$                        
//$$                        String blockToPlace = "short_grass";
//$$                        float r = random.nextFloat();
//$$                        
//$$                        if (r < 0.15f) { // 15% Flowers
//$$                            String[] flowers = {"dandelion", "poppy", "oxeye_daisy", "azure_bluet", "cornflower"};
//$$                            blockToPlace = flowers[random.nextInt(flowers.length)];
//$$                        } else if (r < 0.25f) { // 10% Ferns
//$$                            blockToPlace = "fern";
//$$                        }
//$$                        
//$$                        level.getServer().getCommands().performPrefixedCommand(
//$$                            level.getServer().createCommandSourceStack().withLevel(level).withSuppressedOutput(),
//$$                            String.format("setblock %d %d %d %s keep", above.getX(), above.getY(), above.getZ(), blockToPlace));
//$$                    }
//$$                }
//$$            }
//$$        }
//$$    }

    // ==================== [10] THUNDER DAMAGE ====================
//$$    private static void applyThunderDamage(ServerLevel level, BlockPos center) {
//$$        RandomSource random = level.getRandom();
//$$        int radius = 2 + random.nextInt(2); // 2-3 blocks radius
//$$        for (BlockPos p : BlockPos.betweenClosed(center.offset(-radius, -1, -radius), center.offset(radius, 5, radius))) {
//$$            BlockState state = level.getBlockState(p);
//$$            if (state.getBlock() instanceof LeavesBlock) {
//$$                if (random.nextFloat() < 0.3f) {
//$$                    level.setBlock(p, Blocks.AIR.defaultBlockState(), 3);
//$$                }
//$$            }
//$$        }
//$$    }

    // ==================== HEALTH CHECK AREA SCAN ====================
//$$    private static boolean isAreaClearForHealthCheck(ServerLevel level, BlockPos pos, int radius) {
//$$        BlockState currentState = level.getBlockState(pos);
//$$        Block currentBlock = currentState.getBlock();
//$$        boolean is2x2Tree = (currentBlock == Blocks.DARK_OAK_SAPLING || currentBlock == Blocks.PALE_OAK_SAPLING);
//$$
//$$        for (BlockPos checkPos : BlockPos.betweenClosed(pos.offset(-radius, -1, -radius), pos.offset(radius, 3, radius))) {
//$$            if (checkPos.equals(pos)) continue;
//$$            
//$$            BlockState state = level.getBlockState(checkPos);
//$$            Block block = state.getBlock();
//$$            
//$$            if (block instanceof RotatedPillarBlock || block instanceof SaplingBlock || block == Blocks.AZALEA) {
//$$                // Special 2x2 bypass
//$$                if (is2x2Tree && block == currentBlock) {
//$$                    int dx = Math.abs(checkPos.getX() - pos.getX());
//$$                    int dz = Math.abs(checkPos.getZ() - pos.getZ());
//$$                    if (dx <= 1 && dz <= 1) {
//$$                        continue;
//$$                    }
//$$                }
//$$                return false;
//$$            }
//$$        }
//$$        return true;
//$$    }

    // ==================== CORE GROWTH LOGIC ====================
//$$    private static void processGrowth(ServerLevel level, BlockPos pos, boolean checkLoaded) {
//$$        BlockPos targetPos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pos);
//$$        if (checkLoaded && !level.isLoaded(targetPos)) return;
//$$
//$$        // [4] TERRAIN CHECK: Reject steep slopes
//$$        if (!isTerrainFlat(level, targetPos)) return;
//$$
//$$        // [5] CANOPY DENSITY: Don't plant under heavy canopy
//$$        if (hasHeavyCanopy(level, targetPos)) return;
//$$
//$$        // Determine what to plant: Biome-aware first, then nearby tree fallback
//$$        Optional<Block> saplingOpt = determineSapling(level, targetPos);
//$$        if (saplingOpt.isEmpty()) return;
//$$        
//$$        Block sapling = saplingOpt.get();
//$$        int spacing = getRequiredSpacing(sapling);
//$$        boolean needs2x2 = (sapling == Blocks.DARK_OAK_SAPLING || sapling == Blocks.PALE_OAK_SAPLING);
//$$        long currentTime = level.getGameTime();
//$$
//$$        // [2] LIGHT CHECK: Ensure adequate light
//$$        if (!hasAdequateLight(level, targetPos, sapling)) return;
//$$
//$$        // [9] SOIL FERTILITY: Better soil = higher success rate
//$$        float fertilityBonus = getSoilFertility(level, targetPos);
//$$        if (level.getRandom().nextFloat() > fertilityBonus) return; // Skip if soil is poor
//$$
//$$        // [3] WATER PROXIMITY: Boost chance near water
//$$        // Skip this rejection if water is nearby (already passed fertility)
//$$
//$$        if (needs2x2) {
//$$            place2x2Saplings(level, targetPos, sapling, spacing, currentTime);
//$$        } else {
//$$            if (isSuitableForSapling(level, targetPos) && isAreaClear(level, targetPos, spacing)) {
//$$                level.setBlock(targetPos, sapling.defaultBlockState(), 3);
//$$                ForestGrowthData.get(level).addSapling(targetPos, currentTime);
//$$            }
//$$        }
//$$    }

    // ==================== [1] BIOME-AWARE TREE SELECTION ====================
//$$    private static Optional<Block> determineSapling(ServerLevel level, BlockPos pos) {
//$$        // [7] SPECIES COMPETITION: First check what's dominant nearby
//$$        Optional<Block> nearby = findNearbyForestType(level, pos, 4, 20);
//$$        if (nearby.isPresent()) {
//$$            Block nearbyTree = nearby.get();
//$$            // Verify this tree belongs in this biome (biome-aware filter)
//$$            if (isSaplingValidForBiome(level, pos, nearbyTree)) {
//$$                return nearby;
//$$            }
//$$            // [7] Competition: 30% chance to still plant even if wrong biome (invasive species)
//$$            if (level.getRandom().nextFloat() < 0.3f) {
//$$                return nearby;
//$$            }
//$$        }
//$$
//$$        // If no nearby trees, pick the biome's native tree
//$$        return getBiomeNativeSapling(level, pos);
//$$    }
//$$
//$$    private static boolean isSaplingValidForBiome(ServerLevel level, BlockPos pos, Block sapling) {
//$$        Holder<Biome> biomeHolder = level.getBiome(pos);
//$$        Optional<ResourceKey<Biome>> biomeKeyOpt = biomeHolder.unwrapKey();
//$$        if (biomeKeyOpt.isEmpty()) return true; // If can't resolve, allow it
//$$
//$$        ResourceKey<Biome> biomeKey = biomeKeyOpt.get();
//$$
//$$        // Map: which saplings are valid in which biomes
//$$        if (sapling == Blocks.OAK_SAPLING) {
//$$            return biomeKey == Biomes.FOREST || biomeKey == Biomes.PLAINS || biomeKey == Biomes.FLOWER_FOREST 
//$$                || biomeKey == Biomes.SWAMP || biomeKey == Biomes.MEADOW || biomeKey == Biomes.WINDSWEPT_FOREST;
//$$        }
//$$        if (sapling == Blocks.BIRCH_SAPLING) {
//$$            return biomeKey == Biomes.BIRCH_FOREST || biomeKey == Biomes.OLD_GROWTH_BIRCH_FOREST 
//$$                || biomeKey == Biomes.FOREST || biomeKey == Biomes.FLOWER_FOREST || biomeKey == Biomes.MEADOW;
//$$        }
//$$        if (sapling == Blocks.SPRUCE_SAPLING) {
//$$            return biomeKey == Biomes.TAIGA || biomeKey == Biomes.OLD_GROWTH_SPRUCE_TAIGA 
//$$                || biomeKey == Biomes.OLD_GROWTH_PINE_TAIGA || biomeKey == Biomes.SNOWY_TAIGA
//$$                || biomeKey == Biomes.WINDSWEPT_FOREST || biomeKey == Biomes.GROVE;
//$$        }
//$$        if (sapling == Blocks.JUNGLE_SAPLING) {
//$$            return biomeKey == Biomes.JUNGLE || biomeKey == Biomes.SPARSE_JUNGLE || biomeKey == Biomes.BAMBOO_JUNGLE;
//$$        }
//$$        if (sapling == Blocks.ACACIA_SAPLING) {
//$$            return biomeKey == Biomes.SAVANNA || biomeKey == Biomes.SAVANNA_PLATEAU || biomeKey == Biomes.WINDSWEPT_SAVANNA;
//$$        }
//$$        if (sapling == Blocks.DARK_OAK_SAPLING) {
//$$            return biomeKey == Biomes.DARK_FOREST;
//$$        }
//$$        if (sapling == Blocks.MANGROVE_PROPAGULE) {
//$$            return biomeKey == Biomes.MANGROVE_SWAMP || biomeKey == Biomes.SWAMP;
//$$        }
//$$        if (sapling == Blocks.CHERRY_SAPLING) {
//$$            return biomeKey == Biomes.CHERRY_GROVE || biomeKey == Biomes.MEADOW;
//$$        }
//$$        if (sapling == Blocks.AZALEA) {
//$$            return biomeKey == Biomes.LUSH_CAVES || biomeKey == Biomes.FOREST || biomeKey == Biomes.FLOWER_FOREST;
//$$        }
//$$        if (sapling == Blocks.PALE_OAK_SAPLING) {
//$$            return biomeKey == Biomes.PALE_GARDEN;
//$$        }
//$$        if (sapling == Blocks.CRIMSON_FUNGUS) {
//$$            return biomeKey == Biomes.CRIMSON_FOREST;
//$$        }
//$$        if (sapling == Blocks.WARPED_FUNGUS) {
//$$            return biomeKey == Biomes.WARPED_FOREST;
//$$        }
//$$        return true;
//$$    }
//$$
//$$    private static Optional<Block> getBiomeNativeSapling(ServerLevel level, BlockPos pos) {
//$$        Holder<Biome> biomeHolder = level.getBiome(pos);
//$$        Optional<ResourceKey<Biome>> biomeKeyOpt = biomeHolder.unwrapKey();
//$$        if (biomeKeyOpt.isEmpty()) return Optional.empty();
//$$
//$$        ResourceKey<Biome> biomeKey = biomeKeyOpt.get();
//$$        RandomSource random = level.getRandom();
//$$
//$$        if (biomeKey == Biomes.FOREST || biomeKey == Biomes.FLOWER_FOREST || biomeKey == Biomes.WINDSWEPT_FOREST) {
//$$            return Optional.of(random.nextFloat() < 0.7f ? Blocks.OAK_SAPLING : Blocks.BIRCH_SAPLING);
//$$        }
//$$        if (biomeKey == Biomes.BIRCH_FOREST || biomeKey == Biomes.OLD_GROWTH_BIRCH_FOREST) {
//$$            return Optional.of(Blocks.BIRCH_SAPLING);
//$$        }
//$$        if (biomeKey == Biomes.TAIGA || biomeKey == Biomes.OLD_GROWTH_SPRUCE_TAIGA 
//$$            || biomeKey == Biomes.OLD_GROWTH_PINE_TAIGA || biomeKey == Biomes.SNOWY_TAIGA || biomeKey == Biomes.GROVE) {
//$$            return Optional.of(Blocks.SPRUCE_SAPLING);
//$$        }
//$$        if (biomeKey == Biomes.JUNGLE || biomeKey == Biomes.SPARSE_JUNGLE || biomeKey == Biomes.BAMBOO_JUNGLE) {
//$$            return Optional.of(Blocks.JUNGLE_SAPLING);
//$$        }
//$$        if (biomeKey == Biomes.SAVANNA || biomeKey == Biomes.SAVANNA_PLATEAU || biomeKey == Biomes.WINDSWEPT_SAVANNA) {
//$$            return Optional.of(Blocks.ACACIA_SAPLING);
//$$        }
//$$        if (biomeKey == Biomes.DARK_FOREST) {
//$$            return Optional.of(Blocks.DARK_OAK_SAPLING);
//$$        }
//$$        if (biomeKey == Biomes.MANGROVE_SWAMP) {
//$$            return Optional.of(Blocks.MANGROVE_PROPAGULE);
//$$        }
//$$        if (biomeKey == Biomes.CHERRY_GROVE) {
//$$            return Optional.of(Blocks.CHERRY_SAPLING);
//$$        }
//$$        if (biomeKey == Biomes.PALE_GARDEN) {
//$$            return Optional.of(Blocks.PALE_OAK_SAPLING);
//$$        }
//$$        if (biomeKey == Biomes.CRIMSON_FOREST) {
//$$            return Optional.of(Blocks.CRIMSON_FUNGUS);
//$$        }
//$$        if (biomeKey == Biomes.WARPED_FOREST) {
//$$            return Optional.of(Blocks.WARPED_FUNGUS);
//$$        }
//$$        if (biomeKey == Biomes.PLAINS || biomeKey == Biomes.MEADOW || biomeKey == Biomes.SUNFLOWER_PLAINS) {
//$$            // Plains: very rare tree growth (only near existing trees)
//$$            if (random.nextFloat() < 0.3f) return Optional.of(Blocks.OAK_SAPLING);
//$$            return Optional.empty();
//$$        }
//$$        if (biomeKey == Biomes.SWAMP) {
//$$            return Optional.of(Blocks.OAK_SAPLING);
//$$        }
//$$        return Optional.empty(); // Desert, ocean, etc. - no growth
//$$    }

    // ==================== [2] LIGHT CHECK ====================
//$$    private static boolean hasAdequateLight(ServerLevel level, BlockPos pos, Block sapling) {
//$$        int light = level.getMaxLocalRawBrightness(pos);
//$$        // Shade-tolerant species need less light
//$$        if (sapling == Blocks.DARK_OAK_SAPLING || sapling == Blocks.PALE_OAK_SAPLING 
//$$            || sapling == Blocks.CRIMSON_FUNGUS || sapling == Blocks.WARPED_FUNGUS) {
//$$            return light >= 4; // Can grow in shade
//$$        }
//$$        if (sapling == Blocks.SPRUCE_SAPLING) {
//$$            return light >= 6; // Moderate shade tolerance
//$$        }
//$$        return light >= 9; // Full sun species (Oak, Birch, Acacia, Cherry, etc.)
//$$    }

    // ==================== [3] WATER PROXIMITY ====================
//$$    private static boolean isNearWater(ServerLevel level, BlockPos pos, int radius) {
//$$        for (BlockPos p : BlockPos.betweenClosed(pos.offset(-radius, -2, -radius), pos.offset(radius, 1, radius))) {
//$$            if (level.getBlockState(p).is(Blocks.WATER)) {
//$$                return true;
//$$            }
//$$        }
//$$        return false;
//$$    }

    // ==================== [4] TERRAIN CHECK ====================
//$$    private static boolean isTerrainFlat(ServerLevel level, BlockPos pos) {
//$$        // Check height difference in a 3x3 area - reject steep slopes
//$$        int centerY = pos.getY();
//$$        int maxDiff = 0;
//$$        for (int x = -1; x <= 1; x++) {
//$$            for (int z = -1; z <= 1; z++) {
//$$                int y = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pos.offset(x, 0, z)).getY();
//$$                maxDiff = Math.max(maxDiff, Math.abs(y - centerY));
//$$            }
//$$        }
//$$        return maxDiff <= 2; // Allow up to 2 blocks height difference
//$$    }

    // ==================== [5] CANOPY DENSITY ====================
//$$    private static boolean hasHeavyCanopy(ServerLevel level, BlockPos pos) {
//$$        int leafCount = 0;
//$$        for (int y = 1; y <= 8; y++) {
//$$            BlockState above = level.getBlockState(pos.above(y));
//$$            if (above.getBlock() instanceof LeavesBlock) {
//$$                leafCount++;
//$$            }
//$$        }
//$$        return leafCount >= 3; // Too many leaves above = too dense
//$$    }

    // ==================== [9] SOIL FERTILITY ====================
//$$    private static float getSoilFertility(ServerLevel level, BlockPos pos) {
//$$        float fertility = 0.5f; // Base fertility
//$$        BlockState ground = level.getBlockState(pos.below());
//$$
//$$        // Rich soil types give bonus
//$$        if (ground.is(Blocks.MOSS_BLOCK)) fertility += 0.3f;
//$$        if (ground.is(Blocks.ROOTED_DIRT)) fertility += 0.2f;
//$$        if (ground.is(Blocks.GRASS_BLOCK)) fertility += 0.1f;
//$$        if (ground.is(Blocks.PODZOL)) fertility += 0.35f;
//$$        if (ground.is(Blocks.MYCELIUM)) fertility += 0.15f;
//$$
//$$        // [3] WATER PROXIMITY: Boost fertility near water
//$$        if (isNearWater(level, pos, 6)) fertility += 0.25f;
//$$
//$$        // Nearby organic matter (dead bushes, composted areas)
//$$        for (BlockPos p : BlockPos.betweenClosed(pos.offset(-2, -1, -2), pos.offset(2, 0, 2))) {
//$$            BlockState state = level.getBlockState(p);
//$$            if (state.is(Blocks.DEAD_BUSH)) fertility += 0.05f;
//$$            if (state.getBlock() instanceof LeavesBlock) fertility += 0.02f;
//$$        }
//$$
//$$        return Math.min(fertility, 1.0f); // Cap at 100%
//$$    }

    // ==================== 2x2 PLACEMENT ====================
//$$    private static void place2x2Saplings(ServerLevel level, BlockPos targetPos, Block sapling, int spacing, long currentTime) {
//$$        boolean clear = true;
//$$        for (int x = 0; x < 2; x++) {
//$$            for (int z = 0; z < 2; z++) {
//$$                BlockPos subPos = targetPos.offset(x, 0, z);
//$$                if (!isSuitableForSapling(level, subPos) || !isAreaClear(level, subPos, spacing)) {
//$$                    clear = false;
//$$                    break;
//$$                }
//$$            }
//$$            if (!clear) break;
//$$        }
//$$        
//$$        if (clear) {
//$$            for (int x = 0; x < 2; x++) {
//$$                for (int z = 0; z < 2; z++) {
//$$                    BlockPos subPos = targetPos.offset(x, 0, z);
//$$                    level.setBlock(subPos, sapling.defaultBlockState(), 3);
//$$                    ForestGrowthData.get(level).addSapling(subPos, currentTime);
//$$                }
//$$            }
//$$        }
//$$    }

    // ==================== AREA CLEAR CHECK ====================
//$$    private static boolean isAreaClear(ServerLevel level, BlockPos pos, int radius) {
//$$        for (BlockPos checkPos : BlockPos.betweenClosed(pos.offset(-radius, -1, -radius), pos.offset(radius, 3, radius))) {
//$$            BlockState state = level.getBlockState(checkPos);
//$$            Block block = state.getBlock();
//$$            if (block instanceof RotatedPillarBlock || block instanceof SaplingBlock || block == Blocks.AZALEA) {
//$$                return false;
//$$            }
//$$        }
//$$        return true;
//$$    }

    // ==================== SPACING ====================
//$$    private static int getRequiredSpacing(Block sapling) {
//$$        if (sapling == Blocks.OAK_SAPLING) return 4;
//$$        if (sapling == Blocks.BIRCH_SAPLING) return 3;
//$$        if (sapling == Blocks.SPRUCE_SAPLING) return 4;
//$$        if (sapling == Blocks.JUNGLE_SAPLING) return 4;
//$$        if (sapling == Blocks.ACACIA_SAPLING) return 5;
//$$        if (sapling == Blocks.DARK_OAK_SAPLING) return 6;
//$$        if (sapling == Blocks.MANGROVE_PROPAGULE) return 9;
//$$        if (sapling == Blocks.CHERRY_SAPLING) return 6;
//$$        if (sapling == Blocks.AZALEA) return 4;
//$$        if (sapling == Blocks.PALE_OAK_SAPLING) return 7;
//$$        if (sapling == Blocks.CRIMSON_FUNGUS || sapling == Blocks.WARPED_FUNGUS) return 3;
//$$        return 4; // Default
//$$    }

    // ==================== BIOME FILL UTILITY ====================
//$$    public static void executeFillBiome(ServerLevel level, BlockPos pos, ResourceKey<Biome> biomeKey) {
//$$        int radius = 5;
//$$        BlockPos min = pos.offset(-radius, -2, -radius);
//$$        BlockPos max = pos.offset(radius, 2, radius);
//$$        
//$$        String keyStr = biomeKey.toString();
//$$        String biomeName = keyStr.substring(keyStr.lastIndexOf("/") + 1, keyStr.length() - 1).trim();
//$$        
//$$        String command = String.format("fillbiome %d %d %d %d %d %d %s", 
//$$            min.getX(), min.getY(), min.getZ(), 
//$$            max.getX(), max.getY(), max.getZ(), 
//$$            biomeName);
//$$
//$$        level.getServer().getCommands().performPrefixedCommand(
//$$            level.getServer().createCommandSourceStack().withLevel(level).withSuppressedOutput(), 
//$$            command);
//$$    }

    // ==================== SUITABILITY CHECK ====================
//$$    private static boolean isSuitableForSapling(ServerLevel level, BlockPos pos) {
//$$        BlockState state = level.getBlockState(pos);
//$$        BlockState ground = level.getBlockState(pos.below());
//$$        return state.isAir() && (
//$$            ground.is(Blocks.GRASS_BLOCK) || 
//$$            ground.is(Blocks.DIRT) || 
//$$            ground.is(Blocks.MOSS_BLOCK) ||
//$$            ground.is(Blocks.CRIMSON_NYLIUM) ||
//$$            ground.is(Blocks.WARPED_NYLIUM) ||
//$$            ground.is(Blocks.ROOTED_DIRT) ||
//$$            ground.is(Blocks.PODZOL) ||
//$$            ground.is(Blocks.MYCELIUM) ||
//$$            ground.is(Blocks.MUD)
//$$        );
//$$    }

    // ==================== NEARBY FOREST DETECTION ====================
//$$    private static Optional<Block> findNearbyForestType(ServerLevel level, BlockPos pos, int minRadius, int maxRadius) {
//$$        RandomSource random = level.getRandom();
//$$        // [7] SPECIES COMPETITION: Count species to find dominant
//$$        java.util.Map<Block, Integer> speciesCount = new java.util.HashMap<>();
//$$        
//$$        for (int i = 0; i < 20; i++) {
//$$            double angle = random.nextDouble() * 2 * Math.PI;
//$$            int dist = minRadius + random.nextInt(maxRadius - minRadius);
//$$            int dx = (int) (Math.cos(angle) * dist);
//$$            int dz = (int) (Math.sin(angle) * dist);
//$$            
//$$            BlockPos checkPos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pos.offset(dx, 0, dz)).below();
//$$            BlockState state = level.getBlockState(checkPos);
//$$            
//$$            if (state.getBlock() instanceof RotatedPillarBlock || state.getBlock() instanceof LeavesBlock) {
//$$                getRelatedSapling(state.getBlock()).ifPresent(sapling -> {
//$$                    speciesCount.merge(sapling, 1, Integer::sum);
//$$                });
//$$            }
//$$        }
//$$
//$$        if (speciesCount.isEmpty()) return Optional.empty();
//$$
//$$        // Return the dominant species (most found nearby)
//$$        return speciesCount.entrySet().stream()
//$$            .max(java.util.Map.Entry.comparingByValue())
//$$            .map(java.util.Map.Entry::getKey);
//$$    }
//$$
//$$    private static Optional<Block> getRelatedSapling(Block forestBlock) {
//$$        String name = BuiltInRegistries.BLOCK.getKey(forestBlock).getPath();
//$$        
//$$        if (name.contains("oak")) {
//$$            if (name.contains("dark_oak")) return Optional.of(Blocks.DARK_OAK_SAPLING);
//$$            if (name.contains("pale_oak")) return Optional.of(Blocks.PALE_OAK_SAPLING);
//$$            return Optional.of(Blocks.OAK_SAPLING);
//$$        }
//$$        if (name.contains("spruce")) return Optional.of(Blocks.SPRUCE_SAPLING);
//$$        if (name.contains("birch")) return Optional.of(Blocks.BIRCH_SAPLING);
//$$        if (name.contains("jungle")) return Optional.of(Blocks.JUNGLE_SAPLING);
//$$        if (name.contains("acacia")) return Optional.of(Blocks.ACACIA_SAPLING);
//$$        if (name.contains("cherry")) return Optional.of(Blocks.CHERRY_SAPLING);
//$$        if (name.contains("mangrove")) return Optional.of(Blocks.MANGROVE_PROPAGULE);
//$$        if (name.contains("azalea")) return Optional.of(Blocks.AZALEA);
//$$        if (name.contains("crimson")) return Optional.of(Blocks.CRIMSON_FUNGUS);
//$$        if (name.contains("warped")) return Optional.of(Blocks.WARPED_FUNGUS);
//$$        
//$$        return Optional.empty();
//$$    }

    // ==================== BIOME KEY LOOKUP ====================
//$$    public static Optional<ResourceKey<Biome>> getRelatedBiomeKey(Block sapling) {
//$$        if (sapling == Blocks.OAK_SAPLING) return Optional.of(Biomes.FOREST);
//$$        if (sapling == Blocks.BIRCH_SAPLING) return Optional.of(Biomes.BIRCH_FOREST);
//$$        if (sapling == Blocks.SPRUCE_SAPLING) return Optional.of(Biomes.TAIGA);
//$$        if (sapling == Blocks.JUNGLE_SAPLING) return Optional.of(Biomes.JUNGLE);
//$$        if (sapling == Blocks.ACACIA_SAPLING) return Optional.of(Biomes.SAVANNA);
//$$        if (sapling == Blocks.DARK_OAK_SAPLING) return Optional.of(Biomes.DARK_FOREST);
//$$        if (sapling == Blocks.MANGROVE_PROPAGULE) return Optional.of(Biomes.MANGROVE_SWAMP);
//$$        if (sapling == Blocks.CHERRY_SAPLING) return Optional.of(Biomes.CHERRY_GROVE);
//$$        if (sapling == Blocks.AZALEA) return Optional.of(Biomes.LUSH_CAVES);
//$$        if (sapling == Blocks.PALE_OAK_SAPLING) return Optional.of(Biomes.PALE_GARDEN);
//$$        if (sapling == Blocks.CRIMSON_FUNGUS) return Optional.of(Biomes.CRIMSON_FOREST);
//$$        if (sapling == Blocks.WARPED_FUNGUS) return Optional.of(Biomes.WARPED_FOREST);
//$$        return Optional.empty();
//$$    }
    //#else
    public static void tick(Object level) {}
    //#endif
}
