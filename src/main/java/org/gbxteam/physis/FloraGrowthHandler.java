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

//#if MC >= 12001
//$$ import net.minecraft.core.BlockPos;
//$$ import net.minecraft.core.registries.BuiltInRegistries;
//$$ import net.minecraft.server.level.ServerLevel;
//$$ import net.minecraft.util.RandomSource;
//$$ import net.minecraft.world.level.block.Block;
//$$ import net.minecraft.world.level.block.Blocks;
//$$ import net.minecraft.world.level.block.LeavesBlock;
//$$ import net.minecraft.world.level.block.state.BlockState;
//$$ import net.minecraft.world.level.levelgen.Heightmap;
//#endif

public class FloraGrowthHandler {
    
    //#if MC >= 12001
    
    // ╔══════════════════════════════════════════════════════════════════╗
    // ║         القسم ١: التحديث العالمي للتشونكات (tickChunk)          ║
    // ║   يعمل على كل تشونك محمّل في العالم بشكل مستقل عن اللاعبين    ║
    // ║   المهام: انتشار نباتات                                        ║
    // ╚══════════════════════════════════════════════════════════════════╝
//$$    public static void tickChunk(net.minecraft.world.level.chunk.LevelChunk chunk, ServerLevel level) {
//$$        if (!level.isLoaded(chunk.getPos().getMiddleBlockPosition(0))) return;
//$$
//$$        boolean isRaining = level.isRaining();
//$$
//$$        // سرعة انتشار طبيعية مطابقة للفانيلا (بدون مطر: ~40ث، مع مطر: ~20ث للتشونك)
//$$        float tps = getTickRate(level);
//$$        float speedRatio = Math.max(1.0f, tps / 20.0f);
//$$
//$$        int baseChance = isRaining ? 300 : 600;
//$$        int runChance = Math.max(1, (int)(baseChance / speedRatio));
//$$        if (level.getRandom().nextInt(runChance) != 0) return;
//$$
//$$        net.minecraft.world.level.ChunkPos pos = chunk.getPos();
//$$        BlockPos center = pos.getMiddleBlockPosition(0);
//$$
//$$        // محاولة انتشار واحدة لكل تشغيل — هادئة وطبيعية تماماً كالفانيلا
//$$        int ox = level.getRandom().nextInt(16) - 8;
//$$        int oz = level.getRandom().nextInt(16) - 8;
//$$        processVegetationExpansion(level, center.offset(ox, 0, oz));
//$$
//$$        // نظام مراقبة التوزيع (يعمل بشكل أحيائي نادر)
//$$        if (level.getRandom().nextInt(5) == 0) {
//$$            int ox2 = level.getRandom().nextInt(16) - 8;
//$$            int oz2 = level.getRandom().nextInt(16) - 8;
//$$            BlockPos monitorPos = center.offset(ox2, 0, oz2);
//$$
//$$            monitorPlantDistribution(level, monitorPos, "minecraft:short_grass", 6, 2.0,  5,  10, 3);
//$$            monitorPlantDistribution(level, monitorPos, "minecraft:bush",        5, 6.0,  10, 25, 6);
//$$            monitorPlantDistribution(level, monitorPos, "minecraft:fern",         2, 10.0, 15, 35, 8);
//$$            monitorPlantDistribution(level, monitorPos, "minecraft:tall_grass",   2, 6.0,  8,  20, 5);
//$$            monitorPlantDistribution(level, monitorPos, "minecraft:large_fern",   1, 12.0, 15, 35, 8);
//$$        }
//$$    }

    // ╔══════════════════════════════════════════════════════════════════╗
    // ║  القسم ٢: نظام مراقبة عام لتوزيع النباتات النادرة   ║
    // ║  يطبق ثلاثة قوانين:                                           ║
    // ║   1. أكثر من maxCluster → احذف الزائد                       ║
    // ║   2. مجموعة قريبة من أخرى < minSep → احذف                  ║
    // ║   3. نبات منفرد → انقله relocateMin-relocateMax بلوكة        ║
    // ╚══════════════════════════════════════════════════════════════════╝
//$$    private static void monitorPlantDistribution(
//$$            ServerLevel level, BlockPos searchPos,
//$$            String blockId,
//$$            int maxCluster,       // الحد الأقصى لعدد النبات في المجموعة
//$$            double minSeparation, // المسافة الدنيا بين مجموعتين
//$$            int relocateMin,      // أقل مسافة نقل للمنفرد
//$$            int relocateMax,      // أقصى مسافة نقل للمنفرد
//$$            int clearRadius       // نصف قطر المنطقة الخالية للمكان الجديد
//$$    ) {
//$$        RandomSource random = level.getRandom();
//$$        Block targetBlock = net.minecraft.core.registries.BuiltInRegistries.BLOCK.stream()
//$$            .filter(b -> net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(b).toString().equals(blockId))
//$$            .findFirst()
//$$            .orElse(null);
//$$        if (targetBlock == null || targetBlock == net.minecraft.world.level.block.Blocks.AIR) return;
//$$        
//$$        BlockPos surfaceStart = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, searchPos);
//$$        
//$$        for (int ox = -4; ox <= 4; ox++) {
//$$            for (int oz = -4; oz <= 4; oz++) {
//$$                BlockPos checkPos = surfaceStart.offset(ox, 0, oz);
//$$                for (int dy = -2; dy <= 2; dy++) {
//$$                    BlockPos plantPos = checkPos.offset(0, dy, 0);
//$$                    if (level.getBlockState(plantPos).getBlock() != targetBlock) continue;
//$$                    
//$$                    int directNeighbors = 0;
//$$                    for (BlockPos nb : BlockPos.betweenClosed(plantPos.offset(-1,-1,-1), plantPos.offset(1,1,1))) {
//$$                        if (!nb.equals(plantPos) && level.getBlockState(nb).getBlock() == targetBlock) directNeighbors++;
//$$                    }
//$$                    int clusterSize = 0;
//$$                    for (BlockPos cp : BlockPos.betweenClosed(plantPos.offset(-3,-2,-3), plantPos.offset(3,2,3))) {
//$$                        if (!cp.equals(plantPos) && level.getBlockState(cp).getBlock() == targetBlock) clusterSize++;
//$$                    }
//$$                    
//$$                    // === القانون ١: تجاوز الحد الأقصى → احذف ===
//$$                    if (clusterSize >= maxCluster) {
//$$                        level.setBlock(plantPos, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 3);
//$$                        return;
//$$                    }
//$$                    
//$$                    // === القانون ٢: مجموعة قريبة من أخرى ===
//$$                    if (clusterSize >= 1) {
//$$                        int scanRange = (int)(minSeparation + 4);
//$$                        boolean tooCloseToOtherGroup = false;
//$$                        for (BlockPos fp : BlockPos.betweenClosed(
//$$                                plantPos.offset(-scanRange,-2,-scanRange),
//$$                                plantPos.offset(scanRange,2,scanRange))) {
//$$                            if (fp.equals(plantPos)) continue;
//$$                            double d = Math.sqrt(fp.distSqr(plantPos));
//$$                            if (d < minSeparation || d > scanRange) continue;
//$$                            if (level.getBlockState(fp).getBlock() != targetBlock) continue;
//$$                            boolean fromSameCluster = false;
//$$                            for (BlockPos sc : BlockPos.betweenClosed(plantPos.offset(-3,-2,-3), plantPos.offset(3,2,3))) {
//$$                                if (sc.equals(fp)) { fromSameCluster = true; break; }
//$$                            }
//$$                            if (!fromSameCluster) { tooCloseToOtherGroup = true; break; }
//$$                        }
//$$                        if (tooCloseToOtherGroup && random.nextFloat() < 0.7f) {
//$$                            level.setBlock(plantPos, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 3);
//$$                            return;
//$$                        }
//$$                    }
//$$                    
//$$                    // === القانون ٣: نبات منفرد → انقله بعيداً ===
//$$                    if (clusterSize == 0 && directNeighbors == 0 && random.nextFloat() < 0.20f) {
//$$                        BlockState plantState = level.getBlockState(plantPos);
//$$                        int dist = relocateMin + random.nextInt(relocateMax - relocateMin + 1);
//$$                        double angle = random.nextDouble() * Math.PI * 2;
//$$                        BlockPos newPos = level.getHeightmapPos(
//$$                            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
//$$                            plantPos.offset((int)(Math.cos(angle)*dist), 0, (int)(Math.sin(angle)*dist))
//$$                        );
//$$                        if (plantState.canSurvive(level, newPos) && level.getBlockState(newPos).isAir()) {
//$$                            boolean clearArea = true;
//$$                            for (BlockPos np : BlockPos.betweenClosed(
//$$                                    newPos.offset(-clearRadius,-2,-clearRadius),
//$$                                    newPos.offset(clearRadius,2,clearRadius))) {
//$$                                if (level.getBlockState(np).getBlock() == targetBlock) { clearArea = false; break; }
//$$                            }
//$$                            if (clearArea) {
//$$                                level.setBlock(plantPos, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 3);
//$$                                level.setBlock(newPos, plantState, 3);
//$$                            }
//$$                        }
//$$                    }
//$$                    return;
//$$                }
//$$            }
//$$        }
//$$    }

    // ╔══════════════════════════════════════════════════════════════════╗
    // ║  القسم ٣: نظام ضبط التنظيم العشبي (المراقبة والتشذيب)      ║
    // ║   يفحص كثافة الأعشاب ويزيل الزائد منها للحفاظ على منظر طبيعي   ║
    // ║   يُستدعى عندما تصل الكثافة للحد الأقصى في منطقة معينة         ║
    // ╚══════════════════════════════════════════════════════════════════╝
//$$    private static boolean manageVegetationBalance(ServerLevel level, BlockPos pos, int density, boolean isGrass, boolean isPlainBush, boolean isFlower, RandomSource random) {
//$$        if (isGrass && density > 5 && random.nextFloat() < 0.70f) {
//$$            level.setBlock(pos, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 3);
//$$            return true;
//$$        }
//$$        if (isPlainBush && density >= 2 && random.nextFloat() < 0.8f) {
//$$            level.setBlock(pos, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 3);
//$$            return true;
//$$        }
//$$        if (isFlower && density >= 2 && random.nextFloat() < 0.6f) {
//$$            level.setBlock(pos, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 3);
//$$            return true;
//$$        }
//$$        return false;
//$$    }

    // ╔══════════════════════════════════════════════════════════════════╗
    // ║        القسم ٤: نظام انتشار الأعشاب والنباتات الأرضية          ║
    // ║   يبحث عن نبتة موجودة ثم يحاول نشرها للأماكن القريبة           ║
    // ║   يشمل: عشب، سراخس، أزهار، شجيرات، بتلات، فطريات              ║
    // ╚══════════════════════════════════════════════════════════════════╝
//$$    private static void processVegetationExpansion(ServerLevel level, BlockPos searchPos) {
//$$        if (!level.isLoaded(searchPos)) return;
//$$        RandomSource random = level.getRandom();
//$$        
//$$        BlockPos surfaceStart = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, searchPos);
//$$        BlockPos.MutableBlockPos mut = new BlockPos.MutableBlockPos();
//$$        BlockState state = null;
//$$        Block block = null;
//$$        String name = "";
//$$        boolean isVegetation = false;
//$$        
//$$        searchLoop:
//$$        for (int ox = -2; ox <= 2; ox++) {
//$$            for (int oz = -2; oz <= 2; oz++) {
//$$                mut.setWithOffset(surfaceStart, ox, 2, oz);
//$$                for (int y = 0; y < 8; y++) {
//$$                    BlockState s = level.getBlockState(mut);
//$$                    Block b = s.getBlock();
//$$                    if (b == Blocks.AIR || b == Blocks.WATER) { mut.move(0, -1, 0); continue; }
//$$                    
//$$                    name = net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(b).getPath();
//$$                    if (b == Blocks.GRASS_BLOCK || b == Blocks.MOSS_BLOCK || b == Blocks.DIRT || b == Blocks.SAND ||
//$$                        name.endsWith("grass_block") || name.contains("leaves") || name.contains("log") || name.contains("wood")) {
//$$                        break;
//$$                    }
//$$                    
//$$                    isVegetation = (name.contains("grass") || name.contains("fern") || name.contains("flower") || name.contains("lily") || 
//$$                                   name.contains("mushroom") || name.contains("fungus") || name.contains("kelp") || 
//$$                                   name.contains("seagrass") || name.contains("pickle") || name.contains("coral") ||
//$$                                   name.contains("sugar_cane") || (name.contains("bush") && !name.contains("dead")) || name.contains("moss") || 
//$$                                   name.contains("azalea") || name.contains("spore") || name.contains("dripleaf") || 
//$$                                   name.contains("cave_vines") || name.contains("hanging_roots") || name.contains("glow_berries") ||
//$$                                   name.contains("nether_wart") || name.contains("roots") || name.contains("sprouts"));
//$$                    
//$$                    if (isVegetation) {
//$$                        state = s;
//$$                        block = b;
//$$                        break searchLoop;
//$$                    }
//$$                    mut.move(0, -1, 0);
//$$                }
//$$            }
//$$        }
//$$        
//$$        if (!isVegetation || state == null) return;
//$$        
//$$        if (name.contains("sunflower") || name.contains("lilac") || name.contains("rose_bush") || 
//$$            name.contains("peony") || name.contains("tall") || name.contains("large") || 
//$$            name.contains("pitcher") || name.contains("dead_bush") || name.contains("berry_bush") ||
//$$            name.contains("lily")) {
//$$            return;
//$$        }
//$$        
//$$        BlockPos sourcePos = mut.immutable();
//$$        
//$$        FloraDictionary.VegetationType type = FloraDictionary.categorizeVegetation(name);
//$$        if (type == FloraDictionary.VegetationType.INVALID || type == FloraDictionary.VegetationType.NETHER_FLORA || type == FloraDictionary.VegetationType.WATER_FLORA || type == FloraDictionary.VegetationType.CAVE_FLORA) return;
//$$        
//$$        int density = 0;
//$$        boolean isGrass = (type == FloraDictionary.VegetationType.GRASS);
//$$        
//$$        if (isGrass && name.equals("short_grass") && isNearWater(level, sourcePos, 1)) {
//$$            if (random.nextFloat() < 0.15f && level.getBlockState(sourcePos.above()).isAir()) {
//$$                BlockState tallGrassState = net.minecraft.world.level.block.Blocks.TALL_GRASS.defaultBlockState();
//$$                level.setBlock(sourcePos, tallGrassState.setValue(net.minecraft.world.level.block.state.properties.BlockStateProperties.DOUBLE_BLOCK_HALF, net.minecraft.world.level.block.state.properties.DoubleBlockHalf.LOWER), 3);
//$$                level.setBlock(sourcePos.above(), tallGrassState.setValue(net.minecraft.world.level.block.state.properties.BlockStateProperties.DOUBLE_BLOCK_HALF, net.minecraft.world.level.block.state.properties.DoubleBlockHalf.UPPER), 3);
//$$                return;
//$$            }
//$$        }
//$$
//$$        boolean isFern = (type == FloraDictionary.VegetationType.FERN);
//$$        boolean isPlainBush = (type == FloraDictionary.VegetationType.PLAIN_BUSH);
//$$        boolean isFireflyBush = (type == FloraDictionary.VegetationType.FIREFLY_BUSH);
//$$        boolean isFlower = (type == FloraDictionary.VegetationType.FLOWER);
//$$        
//$$        if (isFireflyBush && !isNearWater(level, sourcePos, 2)) return;
//$$        
//$$        boolean nearWaterSource = isNearWater(level, sourcePos, 6);
//$$        float waterBoost = nearWaterSource ? 1.5f : 1.0f;
//$$
//$$        // ══════ NOISE SOURCE CHECK: فحص نويس منطقة المصدر ══════
//$$        // النباتات في مناطق النويس المنخفض تنتشر ببطء شديد (تحاكي حدود biomes)
//$$        long wSeed = level.getSeed();
//$$        float srcNoise = isPlainBush
//$$            ? bushNoise(wSeed, sourcePos.getX(), sourcePos.getZ())
//$$            : vegetationNoise(wSeed, sourcePos.getX(), sourcePos.getZ());
//$$        float srcMin = isPlainBush ? 0.35f : 0.08f;
//$$        if (srcNoise < srcMin && random.nextFloat() > srcNoise * 2.5f) return;
//$$
//$$        if (isGrass) {
//$$            if (random.nextFloat() > 0.38f * waterBoost) return;
//$$        } else if (isFern) {
//$$            if (random.nextFloat() > 0.02f * waterBoost) return;
//$$        } else if (isPlainBush) {
//$$            if (random.nextFloat() > 0.11f * waterBoost) return;
//$$        } else if (isFireflyBush) {
//$$            if (random.nextFloat() > 0.07f * waterBoost) return;
//$$        } else {
//$$            if (random.nextFloat() > 0.015f * waterBoost) return;
//$$        }
//$$        
//$$        int checkRadius = (isFireflyBush) ? 3 : 2;
//$$        for (BlockPos p : BlockPos.betweenClosed(sourcePos.offset(-checkRadius, -2, -checkRadius), sourcePos.offset(checkRadius, 2, checkRadius))) {
//$$            if (p.equals(sourcePos)) continue;
//$$            if (level.getBlockState(p).getBlock() == block) {
//$$                density++;
//$$            }
//$$        }
//$$        
//$$        int gridX = Math.floorDiv(sourcePos.getX(), 8);
//$$        int gridZ = Math.floorDiv(sourcePos.getZ(), 8);
//$$        int patchHash = (gridX * 73856093) ^ (gridZ * 19349663);
//$$        boolean isDensePatch = (Math.abs(patchHash) % 5) < 2;
//$$        
//$$        int maxDensity = FloraDictionary.getMaxDensity(type, isDensePatch);
//$$        int searchSpread = (isGrass || isPlainBush) ? 5 : 4;
//$$        
//$$        if (density >= maxDensity) {
//$$            manageVegetationBalance(level, sourcePos, density, isGrass, isPlainBush, isFlower, random);
//$$            
//$$            float pioneerChance = isPlainBush ? 0.40f : (isFlower ? 0.15f : 0.05f);
//$$            if (random.nextFloat() < pioneerChance) {
//$$                searchSpread = isPlainBush ? 16 : (isFlower ? 32 : 12);
//$$            } else {
//$$                return;
//$$            }
//$$        } else {
//$$            searchSpread = isFlower ? 4 : (isPlainBush ? 6 : (isGrass ? 8 : 6));
//$$        }
//$$        
//$$        BlockPos bestTarget = null;
//$$        int bestScore = -1;
//$$        for (int i = 0; i < (isGrass ? 12 : ((isPlainBush || isFlower) ? 8 : 4)); i++) {
//$$            int ox = random.nextInt(searchSpread * 2 + 1) - searchSpread;
//$$            int oz = random.nextInt(searchSpread * 2 + 1) - searchSpread;
//$$            if (ox == 0 && oz == 0) continue;
//$$            
//$$            BlockPos target = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, sourcePos.offset(ox, 0, oz));
//$$            
//$$            if (!state.canSurvive(level, target)) {
//$$                if (state.canSurvive(level, target.below())) {
//$$                    target = target.below();
//$$                } else if (state.canSurvive(level, target.above())) {
//$$                    target = target.above();
//$$                } else {
//$$                    continue;
//$$                }
//$$            }
//$$            
//$$            BlockState tState = level.getBlockState(target);
//$$            if (!tState.isAir()) continue;
//$$
//$$            // [GRASS BLOCK ONLY]
//$$            {
//$$                BlockState below = level.getBlockState(target.below());
//$$                if (!below.is(net.minecraft.world.level.block.Blocks.GRASS_BLOCK)) continue;
//$$            }
//$$
//$$            // [BARRIER CHECK]
//$$            if (isSpreadBlocked(level, sourcePos, target, 1)) continue;
//$$
//$$            // [DESTINATION CHECK]
//$$            if (level.getFluidState(target).is(net.minecraft.world.level.material.Fluids.WATER)) continue;
//$$            if (!level.canSeeSky(target) && level.getHeightmapPos(net.minecraft.world.level.levelgen.Heightmap.Types.WORLD_SURFACE, target).getY() > target.getY() + 25) continue;
//$$            
//$$            if (isFireflyBush && !isNearWater(level, target, 2)) continue;
//$$            
//$$            boolean tooClose = false;
//$$            int minSpacing = isFireflyBush ? 4 : 0;
//$$            
//$$            if ((isPlainBush || isFlower || isFern) && density >= maxDensity) {
//$$                if (isFern) {
//$$                    minSpacing = 20 + random.nextInt(11);
//$$                } else {
//$$                    minSpacing = isFlower ? (10 + random.nextInt(11)) : 10;
//$$                }
//$$            }
//$$            
//$$            if (minSpacing > 0) {
//$$                for (BlockPos sp : BlockPos.betweenClosed(target.offset(-minSpacing, -1, -minSpacing), target.offset(minSpacing, 1, minSpacing))) {
//$$                    if (sp.equals(sourcePos)) continue;
//$$                    if (level.getBlockState(sp).getBlock() == block) {
//$$                        tooClose = true;
//$$                        break;
//$$                    }
//$$                }
//$$            }
//$$            if (tooClose) continue;
//$$            
//$$            // ══════ NOISE GATE: الموضع الهدف يجب أن يكون في منطقة نويس مناسبة ══════
//$$            long worldSeed = level.getSeed();
//$$            float targetNoise = isPlainBush
//$$                ? bushNoise(worldSeed, target.getX(), target.getZ())
//$$                : vegetationNoise(worldSeed, target.getX(), target.getZ());
//$$            // حد أدنى للنويس بحسب نوع النبتة — يحاكي توزيع الفانيلا
//$$            float noiseMin = isGrass ? 0.12f : (isPlainBush ? 0.42f : (isFern ? 0.38f : 0.25f));
//$$            if (targetNoise < noiseMin) continue;
//$$
//$$            int score = 0;
//$$            // نويس يعطي أولوية عالية للمناطق الكثيفة (مثل patches الفانيلا)
//$$            score += (int)(targetNoise * 14f);
//$$            if (isNearWater(level, target, (isGrass || isPlainBush) ? 6 : 4)) score += (isGrass || isPlainBush) ? 4 : 3;
//$$            if ((isGrass || isPlainBush) && hasHeavyCanopy(level, target)) score += 6;
//$$            if (!(isGrass || isPlainBush) && hasHeavyCanopy(level, target)) score += 2;
//$$            if (isFireflyBush && isNearWater(level, target, 1)) score += 15;
//$$            score += random.nextInt(4);
//$$
//$$            if (score > bestScore) {
//$$                bestScore = score;
//$$                bestTarget = target;
//$$            }
//$$        }
//$$        
//$$        if (bestTarget != null) {
//$$            level.setBlock(bestTarget, state, 3);
//$$        }
//$$    }

    // ==================== [3] WATER PROXIMITY ====================
//$$    private static boolean isSpreadBlocked(ServerLevel level, BlockPos source, BlockPos target, int height) {
//$$        int x1 = source.getX(), y1 = source.getY(), z1 = source.getZ();
//$$        int x2 = target.getX(), y2 = target.getY(), z2 = target.getZ();
//$$        
//$$        int dx = Math.abs(x2 - x1), dy = Math.abs(y2 - y1), dz = Math.abs(z2 - z1);
//$$        int steps = Math.max(dx, Math.max(dy, dz));
//$$        if (steps == 0) return false;
//$$
//$$        for (int i = 1; i <= steps; i++) {
//$$            float t = (float) i / steps;
//$$            int x = Math.round(x1 + (x2 - x1) * t);
//$$            int y = Math.round(y1 + (y2 - y1) * t);
//$$            int z = Math.round(z1 + (z2 - z1) * t);
//$$            
//$$            BlockPos checkPos = new BlockPos(x, y, z);
//$$            
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
//$$        
//$$        Block block = state.getBlock();
//$$        String name = net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(block).getPath();
//$$        if (name.contains("fence") || name.contains("wall") || name.contains("gate") || 
//$$            name.contains("door") || name.contains("pane") || name.contains("bars") ||
//$$            name.contains("slab") || name.contains("stairs")) return true;
//$$            
//$$        if (name.contains("leaves") || name.contains("log") || name.contains("wood") || 
//$$            name.contains("grass") || name.contains("fern") || name.contains("flower") ||
//$$            name.contains("bush")) return false;
//$$            
//$$        if (state.isRedstoneConductor(level, pos)) {
//$$            if (name.contains("dirt") || name.contains("sand") || name.contains("gravel") || 
//$$                name.contains("stone") || name.contains("moss") || name.contains("mud") || 
//$$                name.contains("clay") || name.contains("snow") || name.contains("ice") || 
//$$                name.contains("mycelium") || name.contains("podzol")) return false;
//$$            
//$$            return true;
//$$        }
//$$        
//$$        return false;
//$$    }
//$$
//$$    private static boolean isNearWater(ServerLevel level, BlockPos pos, int radius) {
//$$        for (BlockPos p : BlockPos.betweenClosed(pos.offset(-radius, -2, -radius), pos.offset(radius, 1, radius))) {
//$$            if (level.getBlockState(p).is(Blocks.WATER)) {
//$$                return true;
//$$            }
//$$        }
//$$        return false;
//$$    }

    // ==================== [4] VEGETATION NOISE (multi-octave value noise, seeded by world seed) ====================
//$$    /**
//$$     * دالة هاش سريعة وحتمية لنقطة (x, z) مع seed
//$$     * تُنتج قيمة عائمة بين 0.0 و 1.0
//$$     */
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
//$$    /**
//$$     * أوكتاف نويس واحدة بإنترپولاسيون cubic smoothstep (Perlin-style value noise)
//$$     * scale = حجم الخلية بالبلوكات
//$$     */
//$$    private static float valueNoise(long seed, int x, int z, int scale) {
//$$        int ix = Math.floorDiv(x, scale);
//$$        int iz = Math.floorDiv(z, scale);
//$$        float fx = (float)Math.floorMod(x, scale) / scale;
//$$        float fz = (float)Math.floorMod(z, scale) / scale;
//$$        // Cubic smoothstep: يجعل الانتقال ناعماً بين النقاط
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
//$$    /**
//$$     * نويس متعدد الأكتاف للنباتات العامة (عشب، سراخس، أزهار)
//$$     * 3 أكتاف: منطقة عريضة (بيوم) + مجموعة + تكستر ناعم
//$$     * النتيجة: 0.0 (فارغ) → 1.0 (كثيف) — مثل الفانيلا بالضبط
//$$     */
//$$    private static float vegetationNoise(long seed, int x, int z) {
//$$        float n1 = valueNoise(seed,                          x, z, 40); // نطاق عريض (بيوم)
//$$        float n2 = valueNoise(seed * 6364136223L + 1442695L, x, z, 14); // مجموعة محلية
//$$        float n3 = valueNoise(seed * 1442695040L + 6364136L, x, z,  5); // تكستر ناعم
//$$        return n1 * 0.50f + n2 * 0.35f + n3 * 0.15f;
//$$    }
//$$
//$$    /**
//$$     * نويس مخصص للبوش — patches أكبر وأكثر تجمعاً كالفانيلا
//$$     */
//$$    private static float bushNoise(long seed, int x, int z) {
//$$        float n1 = valueNoise(seed ^ 0xDEADBEEFL,  x, z, 28); // patch عريض
//$$        float n2 = valueNoise(seed ^ 0xCAFEBABEL,  x, z,  9); // clustering محلي
//$$        return n1 * 0.60f + n2 * 0.40f;
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
//$$        return leafCount >= 3;
//$$    }
//$$
//$$    private static java.lang.reflect.Method tickRateManagerMethod = null;
//$$    private static java.lang.reflect.Method tickrateMethod = null;
//$$    private static boolean reflectionInitialized = false;
//$$
//$$    private static float getTickRate(ServerLevel level) {
//$$        if (!reflectionInitialized) {
//$$            try {
//$$                tickRateManagerMethod = level.getServer().getClass().getMethod("tickRateManager");
//$$                reflectionInitialized = true;
//$$            } catch (Exception e) {
//$$                reflectionInitialized = true;
//$$            }
//$$        }
//$$        if (tickRateManagerMethod != null) {
//$$            try {
//$$                Object trm = tickRateManagerMethod.invoke(level.getServer());
//$$                if (tickrateMethod == null) {
//$$                    tickrateMethod = trm.getClass().getMethod("tickrate");
//$$                }
//$$                return ((Number) tickrateMethod.invoke(trm)).floatValue();
//$$            } catch (Exception e) {
//$$                // fallback
//$$            }
//$$        }
//$$        return 20.0f;
//$$    }

    //#else
    public static void tickChunk(Object chunk, Object level) {}
    //#endif
}
