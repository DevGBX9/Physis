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
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
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
//$$ import net.minecraft.core.registries.Registries;
//$$ import net.minecraft.resources.ResourceKey;
//$$ import net.minecraft.server.level.ServerLevel;
//$$ import net.minecraft.util.RandomSource;
//$$ import net.minecraft.world.level.biome.Biome;
//$$ import net.minecraft.world.level.biome.Biomes;
//$$ import net.minecraft.world.level.block.Block;
//$$ import net.minecraft.world.level.block.Blocks;
//$$ import net.minecraft.world.level.block.LeavesBlock;
//$$ import net.minecraft.world.level.block.RotatedPillarBlock;
//$$ import net.minecraft.world.level.block.state.BlockState;
//$$ import net.minecraft.world.level.chunk.ChunkAccess;
//$$ import java.util.Optional;
//$$ import java.util.HashSet;
//$$ import java.util.Set;
//#endif

public class ForestGrowthHandler {
    
    //#if MC >= 260100
//$$    public static void tick(ServerLevel level) {
//$$        RandomSource random = level.getRandom();
//$$        
//$$        level.players().forEach(player -> {
//$$            for (int i = 0; i < 5; i++) {
//$$                BlockPos playerPos = player.blockPosition();
//$$                int rx = random.nextInt(48) - 24;
//$$                int rz = random.nextInt(48) - 24;
//$$                BlockPos targetPos = level.getHeightmapPos(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING, playerPos.offset(rx, 0, rz));
//$$
//$$                if (isSuitableForSapling(level, targetPos)) {
//$$                    findNearbyForestType(level, targetPos, 2, 10).ifPresent(sapling -> {
//$$                        level.setBlock(targetPos, sapling.defaultBlockState(), 3);
//$$                        
//$$                        // New Feature: Transform Biome
//$$                        getRelatedBiome(sapling).ifPresent(biomeKey -> {
//$$                            transformBiomeArea(level, targetPos, biomeKey);
//$$                        });
//$$                    });
//$$                }
//$$            }
//$$        });
//$$    }
//$$
//$$    private static boolean isSuitableForSapling(ServerLevel level, BlockPos pos) {
//$$        BlockState state = level.getBlockState(pos);
//$$        BlockState ground = level.getBlockState(pos.below());
//$$        // Allowed ground: Grass, Dirt, Moss, Nylium (for nether)
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
//$$        for (int i = 0; i < 15; i++) {
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
//$$    private static Optional<ResourceKey<Biome>> getRelatedBiome(Block sapling) {
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
//$$
//$$    private static void transformBiomeArea(ServerLevel level, BlockPos pos, ResourceKey<Biome> biomeKey) {
//$$        Holder<Biome> biomeHolder = level.registryAccess().registryOrThrow(Registries.BIOME).getHolderOrThrow(biomeKey);
//$$        int radius = 5;
//$$        Set<ChunkAccess> modifiedChunks = new HashSet<>();
//$$
//$$        for (int x = -radius; x <= radius; x++) {
//$$            for (int z = -radius; z <= radius; z++) {
//$$                // Biomes are 3D in 4x4x4 increments. We modify a small vertical range.
//$$                for (int y = -2; y <= 2; y++) {
//$$                    BlockPos target = pos.offset(x, y, z);
//$$                    // Convert block pos to quart pos (x/4)
//$$                    int qX = target.getX() >> 2;
//$$                    int qY = target.getY() >> 2;
//$$                    int qZ = target.getZ() >> 2;
//$$                    
//$$                    ChunkAccess chunk = level.getChunk(target);
//$$                    chunk.setBiome(qX, qY, qZ, biomeHolder);
//$$                    modifiedChunks.add(chunk);
//$$                }
//$$            }
//$$        }
//$$
//$$        // Sync changes to clients
//$$        modifiedChunks.forEach(chunk -> {
//$$            level.getChunkSource().onChunkBiomesUpdated(java.util.List.of(chunk.getPos()));
//$$        });
//$$    }
    //#else
    public static void tick(Object level) {}
    //#endif
}
