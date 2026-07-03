/*
 * This file is part of the Physis project, licensed under the MIT License.
 *
 * Copyright (C) 2026 DevGBX9 and contributors
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

//#if MC >= 11600
//$$ import net.minecraft.core.BlockPos;
//$$ import net.minecraft.server.level.ServerLevel;
//$$ import net.minecraft.world.level.block.Block;
//$$ import net.minecraft.world.level.block.Blocks;
//$$ import net.minecraft.world.level.block.LeavesBlock;
//$$ import net.minecraft.world.level.block.state.BlockState;
//$$ import net.minecraft.world.level.levelgen.Heightmap;
//#endif

public class FloraGrowthHandler {
    
    //#if MC >= 11600
    
    // ╔══════════════════════════════════════════════════════════════════╗
    // ║  النظام الجديد: الشفاء (Wound Healing)                          ║
    // ║  العشب كائن واحد عاقل — ينتظر حتى يزيل اللاعب العشب،            ║
    // ║  ثم يشفي الجرح عن طريق الانتشار من الحواف إلى الداخل             ║
    // ╚══════════════════════════════════════════════════════════════════╝
//$$    public static void tickChunk(net.minecraft.world.level.chunk.LevelChunk chunk, ServerLevel level, int randomTickSpeed) {
//$$        if (randomTickSpeed <= 0) return;
//$$
//$$        CompatibleRandom random = new CompatibleRandom(level.getRandom());
//$$
//$$        if (random.nextInt(50) != 0) return;
//$$
//$$        net.minecraft.world.level.ChunkPos pos = chunk.getPos();
//#if MC >= 11700
//$$        if (!level.isLoaded(pos.getMiddleBlockPosition(0))) return;
//$$        BlockPos center = pos.getMiddleBlockPosition(0);
//#else
//$$        if (!level.isLoaded(new BlockPos(pos.x * 16 + 8, 0, pos.z * 16 + 8))) return;
//$$        BlockPos center = new BlockPos(pos.x * 16 + 8, 0, pos.z * 16 + 8);
//#endif
//$$
//$$        int ox = random.nextInt(16);
//$$        int oz = random.nextInt(16);
//#if MC >= 11700
//$$        BlockPos targetPos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, center.offset(ox, 0, oz));
//#else
//$$        BlockPos targetPos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, new BlockPos(center.getX() + ox, 0, center.getZ() + oz));
//#endif
//$$
//$$        healWound(level, targetPos, random);
//$$    }

    // ╔══════════════════════════════════════════════════════════════════╗
    // ║  شفاء الجرح: ينشر العشب فقط إذا كان هناك عشب مجاور               ║
    // ╚══════════════════════════════════════════════════════════════════╝
//$$    private static void healWound(ServerLevel level, BlockPos pos, CompatibleRandom random) {
//$$        if (!level.isLoaded(pos)) return;
//$$
//$$        BlockPos below = pos.below();
//$$        BlockState state = level.getBlockState(pos);
//$$        BlockState belowState = level.getBlockState(below);
//$$
//$$        if (!state.isAir()) return;
//$$
//$$        boolean isGrassBelow = false;
//#if MC >= 11800
//$$        isGrassBelow = belowState.is(net.minecraft.world.level.block.Blocks.GRASS_BLOCK);
//#else
//$$        isGrassBelow = (belowState.getBlock() == net.minecraft.world.level.block.Blocks.GRASS_BLOCK);
//#endif
//$$        if (!isGrassBelow) return;
//$$
//$$        if (level.getMaxLocalRawBrightness(pos) < 9) return;
//$$
//$$        BlockPos sourcePos = null;
//$$        BlockState sourceState = null;
//$$
//$$        for (int dx = -1; dx <= 1; dx++) {
//$$            for (int dz = -1; dz <= 1; dz++) {
//$$                if (dx == 0 && dz == 0) continue;
//$$
//$$                BlockPos checkPos = pos.offset(dx, 0, dz);
//$$                BlockState checkState = level.getBlockState(checkPos);
//$$
//$$                if (checkState.isAir()) continue;
//$$
//$$                String name = getBlockPathString(checkState.getBlock());
//$$                if (name.contains("grass") || name.contains("fern")) {
//$$                    if (checkState.canSurvive(level, pos)) {
//$$                        sourcePos = checkPos;
//$$                        sourceState = checkState;
//$$                        break;
//$$                    }
//$$                }
//$$            }
//$$            if (sourcePos != null) break;
//$$        }
//$$
//$$        if (sourcePos == null || sourceState == null) return;
//$$
//$$        if (isSpreadBlocked(level, sourcePos, pos, 1)) return;
//$$
//$$        long worldSeed = level.getSeed();
//$$        float noise = vegetationNoise(worldSeed, pos.getX(), pos.getZ());
//$$        if (noise < 0.12f) return;
//$$
//$$        float chance = 0.38f;
//$$        if (level.isRaining()) chance *= 2.0f;
//$$        if (isNearWater(level, pos, 6)) chance *= 1.5f;
//$$
//$$        if (random.nextFloat() <= chance) {
//$$            level.setBlock(pos, sourceState, 3);
//$$        }
//$$    }

    // ==================== STRUCTURE PROTECTION (Ray-marching) ====================
//$$    private static boolean isSpreadBlocked(ServerLevel level, BlockPos source, BlockPos target, int height) {
//$$        int x1 = source.getX(), y1 = source.getY(), z1 = source.getZ();
//$$        int x2 = target.getX(), y2 = target.getY(), z2 = target.getZ();
//$$        int dx = Math.abs(x2 - x1), dy = Math.abs(y2 - y1), dz = Math.abs(z2 - z1);
//$$        int steps = Math.max(dx, Math.max(dy, dz));
//$$        if (steps == 0) return false;
//$$        for (int i = 1; i <= steps; i++) {
//$$            float t = (float) i / steps;
//$$            int x = Math.round(x1 + (x2 - x1) * t);
//$$            int y = Math.round(y1 + (y2 - y1) * t);
//$$            int z = Math.round(z1 + (z2 - z1) * t);
//$$            BlockPos checkPos = new BlockPos(x, y, z);
//$$            if (!checkPos.equals(source) && !checkPos.equals(target)) {
//$$                for (int h = 0; h < height; h++) {
//$$                    if (isBarrier(level, checkPos.above(h), h > 0)) return true;
//$$                }
//$$            }
//$$        }
//$$        return false;
//$$    }
//$$
//$$    private static boolean isBarrier(ServerLevel level, BlockPos pos, boolean isAbove) {
//$$        BlockState state = level.getBlockState(pos);
//$$        if (state.isAir()) return false;
//$$        Block block = state.getBlock();
//$$        String name = getBlockPathString(block);
//$$        if (name.contains("fence") || name.contains("wall") || name.contains("gate") ||
//$$            name.contains("door") || name.contains("pane") || name.contains("bars") ||
//$$            name.contains("slab") || name.contains("stairs")) return true;
//$$        if (name.contains("leaves") || name.contains("log") || name.contains("wood") ||
//$$            name.contains("grass") || name.contains("fern") || name.contains("flower") ||
//$$            name.contains("bush")) return false;
//$$        if (state.isRedstoneConductor(level, pos)) {
//$$            if (name.contains("dirt") || name.contains("sand") || name.contains("gravel") ||
//$$                name.contains("stone") || name.contains("moss") || name.contains("mud") ||
//$$                name.contains("clay") || name.contains("snow") || name.contains("ice") ||
//$$                name.contains("mycelium") || name.contains("podzol")) return false;
//$$            return true;
//$$        }
//$$        return false;
//$$    }

    // ==================== WATER PROXIMITY ====================
//$$    private static boolean isNearWater(ServerLevel level, BlockPos pos, int radius) {
//$$        for (BlockPos p : BlockPos.betweenClosed(pos.offset(-radius, -2, -radius), pos.offset(radius, 1, radius))) {
//#if MC >= 11800
//$$            if (level.getBlockState(p).is(Blocks.WATER)) {
//#else
//$$            if (level.getBlockState(p).getBlock() == Blocks.WATER) {
//#endif
//$$                return true;
//$$            }
//$$        }
//$$        return false;
//$$    }

    // ==================== VEGETATION NOISE (multi-octave value noise, seeded by world seed) ====================
//$$    private static float hashFloat(long seed, int x, int z) {
//$$        long h = seed ^ ((long)x * 0x9e3779b97f4a7c15L) ^ ((long)z * 0x6c62272e07bb0142L);
//$$        h ^= h >>> 33;
//$$        h *= 0xff51afd7ed558ccdL;
//$$        h ^= h >>> 33;
//$$        h *= 0xc4ceb9fe1a85ec53L;
//$$        h ^= h >>> 33;
//$$        return (float)((h & 0x7FFFFFFFL) / (double)0x7FFFFFFFL);
//$$    }
//$$
//$$    private static float valueNoise(long seed, int x, int z, int scale) {
//$$        int ix = Math.floorDiv(x, scale);
//$$        int iz = Math.floorDiv(z, scale);
//$$        float fx = (float)Math.floorMod(x, scale) / scale;
//$$        float fz = (float)Math.floorMod(z, scale) / scale;
//$$        fx = fx * fx * (3f - 2f * fx);
//$$        fz = fz * fz * (3f - 2f * fz);
//$$        float v00 = hashFloat(seed,          ix,   iz);
//$$        float v10 = hashFloat(seed,          ix+1, iz);
//$$        float v01 = hashFloat(seed,          ix,   iz+1);
//$$        float v11 = hashFloat(seed,          ix+1, iz+1);
//$$        float top    = v00 * (1f - fx) + v10 * fx;
//$$        float bottom = v01 * (1f - fx) + v11 * fx;
//$$        return top * (1f - fz) + bottom * fz;
//$$    }
//$$
//$$    private static float vegetationNoise(long seed, int x, int z) {
//$$        float n1 = valueNoise(seed,                          x, z, 40);
//$$        float n2 = valueNoise(seed * 6364136223L + 1442695L, x, z, 14);
//$$        float n3 = valueNoise(seed * 1442695040L + 6364136L, x, z,  5);
//$$        return n1 * 0.50f + n2 * 0.35f + n3 * 0.15f;
//$$    }

    // ==================== CANOPY DENSITY ====================
//$$    private static boolean hasHeavyCanopy(ServerLevel level, BlockPos pos) {
//$$        int leafCount = 0;
//$$        for (int y = 1; y <= 8; y++) {
//$$            BlockState above = level.getBlockState(pos.above(y));
//$$            if (above.getBlock() instanceof LeavesBlock) {
//$$                leafCount++;
//$$            }
//$$        }
//$$        return leafCount >= 3;
//$$    }

    // ==================== COMPATIBLE RANDOM ====================
//$$    public static class CompatibleRandom {
//$$        private final Object obj;
//$$        private final boolean isRandomSource;
//$$
//$$        public CompatibleRandom(Object obj) {
//$$            this.obj = obj;
//$$            this.isRandomSource = !(obj instanceof java.util.Random);
//$$        }
//$$
//$$        public int nextInt(int bound) {
//$$            if (isRandomSource) {
//$$                try {
//$$                    return ((Number) obj.getClass().getMethod("nextInt", int.class).invoke(obj, bound)).intValue();
//$$                } catch (Exception e) {
//$$                    return 0;
//$$                }
//$$            } else {
//$$                return ((java.util.Random) obj).nextInt(bound);
//$$            }
//$$        }
//$$
//$$        public float nextFloat() {
//$$            if (isRandomSource) {
//$$                try {
//$$                    return ((Number) obj.getClass().getMethod("nextFloat").invoke(obj)).floatValue();
//$$                } catch (Exception e) {
//$$                    return 0.0f;
//$$                }
//$$            } else {
//$$                return ((java.util.Random) obj).nextFloat();
//$$            }
//$$        }
//$$
//$$        public double nextDouble() {
//$$            if (isRandomSource) {
//$$                try {
//$$                    return ((Number) obj.getClass().getMethod("nextDouble").invoke(obj)).doubleValue();
//$$                } catch (Exception e) {
//$$                    return 0.0;
//$$                }
//$$            } else {
//$$                return ((java.util.Random) obj).nextDouble();
//$$            }
//$$        }
//$$    }

    // ==================== REGISTRY REFLECTION ====================
//$$    private static java.lang.reflect.Method getKeyMethod = null;
//$$    private static Object blockRegistry = null;
//$$    private static boolean registryInitialized = false;
//$$
//$$    private static void initRegistryReflection() {
//$$        if (registryInitialized) return;
//$$        try {
//$$            Class<?> birClass = Class.forName("net.minecraft.core.registries.BuiltInRegistries");
//$$            blockRegistry = birClass.getField("BLOCK").get(null);
//$$        } catch (Exception e) {
//$$            try {
//$$                Class<?> regClass = Class.forName("net.minecraft.core.Registry");
//$$                blockRegistry = regClass.getField("BLOCK").get(null);
//$$            } catch (Exception ex) {
//$$            }
//$$        }
//$$        if (blockRegistry != null) {
//$$            try {
//$$                getKeyMethod = blockRegistry.getClass().getMethod("getKey", Object.class);
//$$            } catch (Exception e) {
//$$            }
//$$        }
//$$        registryInitialized = true;
//$$    }
//$$
//$$    private static String getBlockPathString(Block block) {
//$$        initRegistryReflection();
//$$        if (getKeyMethod != null && blockRegistry != null) {
//$$            try {
//$$                Object resourceLocation = getKeyMethod.invoke(blockRegistry, block);
//$$                return (String) resourceLocation.getClass().getMethod("getPath").invoke(resourceLocation);
//$$            } catch (Exception e) {
//$$            }
//$$        }
//$$        return "";
//$$    }

    //#else
    public static void tickChunk(Object chunk, Object level) {}
    //#endif
}
