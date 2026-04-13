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
//$$ import net.minecraft.core.registries.BuiltInRegistries;
//$$ import net.minecraft.server.level.ServerLevel;
//$$ import net.minecraft.util.RandomSource;
//$$ import net.minecraft.world.level.block.Block;
//$$ import net.minecraft.world.level.block.Blocks;
//$$ import net.minecraft.world.level.block.LeavesBlock;
//$$ import net.minecraft.world.level.block.RotatedPillarBlock;
//$$ import net.minecraft.world.level.block.state.BlockState;
//$$ import java.util.Optional;
//#endif

public class ForestGrowthHandler {
    
    //#if MC >= 260100
//$$    public static void tick(ServerLevel level) {
//$$        RandomSource random = level.getRandom();
//$$        
//$$        // Only run occasionally (e.g., 1 in 100 ticks per world)
//$$        if (random.nextInt(100) != 0) return;
//$$
//$$        // Pick a random player to tick around, or just random location
//$$        level.players().forEach(player -> {
//$$            if (random.nextInt(20) != 0) return; // Reduce frequency per player
//$$
//$$            BlockPos playerPos = player.blockPosition();
//$$            int rx = random.nextInt(64) - 32;
//$$            int rz = random.nextInt(64) - 32;
//$$            BlockPos targetPos = level.getHeightmapPos(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING, playerPos.offset(rx, 0, rz));
//$$
//$$            if (isSuitableForSapling(level, targetPos)) {
//$$                // Look for a forest edge nearby (6-10 blocks away)
//$$                findNearbyForestType(level, targetPos, 6, 12).ifPresent(sapling -> {
//$$                    level.setBlock(targetPos, sapling.defaultBlockState(), 3);
//$$                });
//$$            }
//$$        });
//$$    }
//$$
//$$    private static boolean isSuitableForSapling(ServerLevel level, BlockPos pos) {
//$$        BlockState state = level.getBlockState(pos);
//$$        BlockState ground = level.getBlockState(pos.below());
//$$        return state.isAir() && (ground.is(Blocks.GRASS_BLOCK) || ground.is(Blocks.DIRT));
//$$    }
//$$
//$$    private static Optional<Block> findNearbyForestType(ServerLevel level, BlockPos pos, int minRadius, int maxRadius) {
//$$        // Randomly search in a ring around the target position
//$$        RandomSource random = level.getRandom();
//$$        for (int i = 0; i < 10; i++) {
//$$            double angle = random.nextDouble() * 2 * Math.PI;
//$$            int dist = minRadius + random.nextInt(maxRadius - minRadius);
//$$            int dx = (int) (Math.cos(angle) * dist);
//$$            int dz = (int) (Math.sin(angle) * dist);
//$$            
//$$            BlockPos checkPos = level.getHeightmapPos(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING, pos.offset(dx, 0, dz)).below();
//$$            BlockState state = level.getBlockState(checkPos);
//$$            
//$$            // Check if it's a log or leaves
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
//$$            return Optional.of(Blocks.OAK_SAPLING);
//$$        }
//$$        if (name.contains("spruce")) return Optional.of(Blocks.SPRUCE_SAPLING);
//$$        if (name.contains("birch")) return Optional.of(Blocks.BIRCH_SAPLING);
//$$        if (name.contains("jungle")) return Optional.of(Blocks.JUNGLE_SAPLING);
//$$        if (name.contains("acacia")) return Optional.of(Blocks.ACACIA_SAPLING);
//$$        if (name.contains("cherry")) return Optional.of(Blocks.CHERRY_SAPLING);
//$$        if (name.contains("mangrove")) return Optional.of(Blocks.MANGROVE_PROPAGULE);
//$$        
//$$        return Optional.empty();
//$$    }
    //#else
    public static void tick(Object level) {}
    //#endif
}
