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
//$$ import java.util.Optional;
//#endif

public class ForestGrowthHandler {
    
    //#if MC >= 260100
//$$    public static void tick(ServerLevel level) {
//$$        RandomSource random = level.getRandom();
//$$        
//$$        // Extreme Mixed Strategy: Fast nearby + Global expansion
//$$        level.players().forEach(player -> {
//$$            BlockPos playerPos = player.blockPosition();
//$$
//$$            // 1. NEARBY EXPLOSION (30 attempts)
//$$            for (int i = 0; i < 30; i++) { 
//$$                int rx = random.nextInt(64) - 32;
//$$                int rz = random.nextInt(64) - 32;
//$$                processGrowth(level, playerPos.offset(rx, 0, rz), false);
//$$            }
//$$
//$$            // 2. GLOBAL EXPANSION (150 attempts)
//$$            for (int i = 0; i < 150; i++) { 
//$$                int rx = random.nextInt(2000) - 1000;
//$$                int rz = random.nextInt(2000) - 1000;
//$$                processGrowth(level, playerPos.offset(rx, 0, rz), true);
//$$            }
//$$        });
//$$    }
//$$
//$$    private static void processGrowth(ServerLevel level, BlockPos pos, boolean checkLoaded) {
//$$        BlockPos targetPos = level.getHeightmapPos(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING, pos);
//$$        if (checkLoaded && !level.isLoaded(targetPos)) return;
//$$
//$$        if (isSuitableForSapling(level, targetPos)) {
//$$            findNearbyForestType(level, targetPos, 4, 20).ifPresent(sapling -> {
//$$                int spacing = getRequiredSpacing(sapling);
//$$                
//$$                // Smart Space-Aware Planting
//$$                if (isAreaClear(level, targetPos, spacing)) {
//$$                    level.setBlock(targetPos, sapling.defaultBlockState(), 3);
//$$                    // Biome transformation removed from here - it will happen in SaplingBlockMixin
//$$                }
//$$            });
//$$        }
//$$    }
//$$
//$$    private static boolean isAreaClear(ServerLevel level, BlockPos pos, int radius) {
//$$        // Fast scan for logs or saplings in the required radius
//$$        for (BlockPos checkPos : BlockPos.betweenClosed(pos.offset(-radius, -1, -radius), pos.offset(radius, 3, radius))) {
//$$            BlockState state = level.getBlockState(checkPos);
//$$            Block block = state.getBlock();
//$$            
//$$            // If we found a trunk, a sapling, or azalea, then the area is NOT clear
//$$            if (block instanceof RotatedPillarBlock || block instanceof SaplingBlock || block == Blocks.AZALEA) {
//$$                return false;
//$$            }
//$$        }
//$$        return true;
//$$    }
//$$
//$$    private static int getRequiredSpacing(Block sapling) {
//$$        if (sapling == Blocks.OAK_SAPLING) return 3;
//$$        if (sapling == Blocks.BIRCH_SAPLING) return 3;
//$$        if (sapling == Blocks.SPRUCE_SAPLING) return 5; // Defaulting to 5 for spruce
//$$        if (sapling == Blocks.JUNGLE_SAPLING) return 5; // 5 for jungle (balance for giant vs small)
//$$        if (sapling == Blocks.ACACIA_SAPLING) return 5;
//$$        if (sapling == Blocks.DARK_OAK_SAPLING) return 4;
//$$        if (sapling == Blocks.MANGROVE_PROPAGULE) return 5;
//$$        if (sapling == Blocks.CHERRY_SAPLING) return 6;
//$$        if (sapling == Blocks.AZALEA) return 4;
//$$        if (sapling == Blocks.PALE_OAK_SAPLING) return 5;
//$$        if (sapling == Blocks.CRIMSON_FUNGUS || sapling == Blocks.WARPED_FUNGUS) return 4;
//$$        return 3; // Default
//$$    }
//$$
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
//$$
//$$    private static boolean isSuitableForSapling(ServerLevel level, BlockPos pos) {
//$$        BlockState state = level.getBlockState(pos);
//$$        BlockState ground = level.getBlockState(pos.below());
//$$        return state.isAir() && (
//$$            ground.is(Blocks.GRASS_BLOCK) || 
//$$            ground.is(Blocks.DIRT) || 
//$$            ground.is(Blocks.MOSS_BLOCK) ||
//$$            ground.is(Blocks.CRIMSON_NYLIUM) ||
//$$            ground.is(Blocks.WARPED_NYLIUM) ||
//$$            ground.is(Blocks.ROOTED_DIRT)
//$$        );
//$$    }
//$$
//$$    private static Optional<Block> findNearbyForestType(ServerLevel level, BlockPos pos, int minRadius, int maxRadius) {
//$$        RandomSource random = level.getRandom();
//$$        for (int i = 0; i < 20; i++) {
//$$            double angle = random.nextDouble() * 2 * Math.PI;
//$$            int dist = minRadius + random.nextInt(maxRadius - minRadius);
//$$            int dx = (int) (Math.cos(angle) * dist);
//$$            int dz = (int) (Math.sin(angle) * dist);
//$$            
//$$            BlockPos checkPos = level.getHeightmapPos(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING, pos.offset(dx, 0, dz)).below();
//$$            BlockState state = level.getBlockState(checkPos);
//$$            
//$$            if (state.getBlock() instanceof RotatedPillarBlock || state.getBlock() instanceof LeavesBlock) {
//$$                return getRelatedSapling(state.getBlock());
//$$            }
//$$        }
//$$        return Optional.empty();
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
//$$
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
