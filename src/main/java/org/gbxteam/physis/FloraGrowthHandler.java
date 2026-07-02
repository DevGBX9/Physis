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
    // ║         القسم ١: التحديث العالمي للتشونكات (tickChunk)          ║
    // ║   يعمل على كل تشونك محمّل في العالم بشكل مستقل عن اللاعبين    ║
    // ║   المهام: انتشار نباتات                                        ║
    // ╚══════════════════════════════════════════════════════════════════╝
//$$    public static void tickChunk(net.minecraft.world.level.chunk.LevelChunk chunk, ServerLevel level, int randomTickSpeed) {
//$$        if (randomTickSpeed <= 0) return;
//#if MC >= 11700
//$$        if (!level.isLoaded(chunk.getPos().getMiddleBlockPosition(0))) return;
//#else
//$$        if (!level.isLoaded(new BlockPos(chunk.getPos().x * 16 + 8, 0, chunk.getPos().z * 16 + 8))) return;
//#endif
//$$
//$$        CompatibleRandom random = new CompatibleRandom(level.getRandom());
//$$        net.minecraft.world.level.ChunkPos pos = chunk.getPos();
//#if MC >= 11700
//$$        BlockPos center = pos.getMiddleBlockPosition(0);
//#else
//$$        BlockPos center = new BlockPos(pos.x * 16 + 8, 0, pos.z * 16 + 8);
//#endif
//$$
//$$        // تشغيل المحاكاة بمعدل منخفض جداً لكل تشونك لتقليل اللاغ بشكل كبير
//$$        if (random.nextInt(30) == 0) {
//$$            int ox = random.nextInt(16) - 8;
//$$            int oz = random.nextInt(16) - 8;
//#if MC >= 11700
//$$            BlockPos targetPos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, center.offset(ox, 0, oz));
//#else
//$$            BlockPos targetPos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, new BlockPos(center.getX() + ox, 0, center.getZ() + oz));
//#endif
//$$            processVegetationExpansion(level, targetPos);
//$$        }
//$$
//$$        // تشغيل المراقبة والتشذيب بمعدل أقل بكثير لمنع استهلاك المعالج
//$$        if (random.nextInt(150) == 0) {
//$$            int ox2 = random.nextInt(16) - 8;
//$$            int oz2 = random.nextInt(16) - 8;
//#if MC >= 11700
//$$            BlockPos monitorPos = center.offset(ox2, 0, oz2);
//#else
//$$            BlockPos monitorPos = new BlockPos(center.getX() + ox2, 0, center.getZ() + oz2);
//#endif
//$$
//$$            monitorPlantDistribution(level, monitorPos, "minecraft:short_grass", 6, 2.0,  5,  10, 3);
//$$            monitorPlantDistribution(level, monitorPos, "minecraft:tall_grass",   2, 6.0,  8,  20, 5);
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
//$$        CompatibleRandom random = new CompatibleRandom(level.getRandom());
//$$        Block targetBlock = getBlockById(blockId);
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
//$$    private static boolean manageVegetationBalance(ServerLevel level, BlockPos pos, int density, boolean isGrass, boolean isPlainBush, boolean isFlower, CompatibleRandom random) {
//$$        if (isGrass && density > 5 && random.nextFloat() < 0.70f) {
//$$            level.setBlock(pos, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 3);
//$$            return true;
//$$        }
//$$        if (isPlainBush && density >= 6 && random.nextFloat() < 0.30f) {
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
    // ║   يبحث عن الفراغات أولاً ثم يفحص الجيران لنشر النباتات إليها     ║
    // ╚══════════════════════════════════════════════════════════════════╝
//$$    private static void processVegetationExpansion(ServerLevel level, BlockPos targetPos) {
//$$        if (!level.isLoaded(targetPos)) return;
//$$
//$$        // ١. التحقق من أن الموضع المستهدف فارغ وتحته بلوك عشب (grass_block)
//$$        BlockPos blockBelow = targetPos.below();
//$$        BlockState targetState = level.getBlockState(targetPos);
//$$        BlockState belowState = level.getBlockState(blockBelow);
//$$
//$$        boolean isTargetEmpty = targetState.isAir();
//$$        if (!isTargetEmpty) return; // خروج مبكر وسريع جداً
//$$
//$$        boolean isGrassBlock = false;
//$$        Block belowBlock = belowState.getBlock();
//#if MC >= 11800
//$$        isGrassBlock = belowState.is(net.minecraft.world.level.block.Blocks.GRASS_BLOCK);
//#else
//$$        isGrassBlock = (belowBlock == net.minecraft.world.level.block.Blocks.GRASS_BLOCK);
//#endif
//$$        if (!isGrassBlock) return; // خروج مبكر
//$$
//$$        // ٢. فحص مستوى الإضاءة: تحتاج النباتات للنمو مستوى إضاءة لا يقل عن 9
//$$        if (level.getMaxLocalRawBrightness(targetPos) < 9) return;
//$$
//$$        CompatibleRandom random = new CompatibleRandom(level.getRandom());
//$$
//$$        // ٣. فحص الجيران المباشرين أفقياً للبحث عن نباتات مجاورة صالحة للانتشار
//$$        BlockPos sourcePos = null;
//$$        BlockState sourceState = null;
//$$        FloraDictionary.VegetationType vegType = FloraDictionary.VegetationType.INVALID;
//$$
//$$        outerLoop:
//$$        for (int dx = -2; dx <= 2; dx++) {
//$$            for (int dz = -2; dz <= 2; dz++) {
//$$                if (dx == 0 && dz == 0) continue;
//$$
//$$                BlockPos neighborPos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, targetPos.offset(dx, 0, dz));
//$$                for (int dy = -1; dy <= 1; dy++) {
//$$                    BlockPos checkPos = neighborPos.above(dy);
//$$                    BlockState state = level.getBlockState(checkPos);
//$$                    Block b = state.getBlock();
//$$                    String name = getBlockPathString(b);
//$$
//$$                    FloraDictionary.VegetationType type = FloraDictionary.categorizeVegetation(name);
//$$                    if (type != FloraDictionary.VegetationType.INVALID) {
//$$                        if (state.canSurvive(level, targetPos)) {
//$$                            sourcePos = checkPos;
//$$                            sourceState = state;
//$$                            vegType = type;
//$$                            break outerLoop; // العثور على جار صالح للانتشار
//$$                        }
//$$                    }
//$$                }
//$$            }
//$$        }
//$$
//$$        if (sourcePos == null || sourceState == null || vegType == FloraDictionary.VegetationType.INVALID) return;
//$$
//$$        // ٤. فحص حجم الفراغ حول الموضع المستهدف في مساحة 5x5 (هل يستحق العناء؟)
//$$        int emptyCount = 0;
//$$        for (int dx = -2; dx <= 2; dx++) {
//$$            for (int dz = -2; dz <= 2; dz++) {
//$$                BlockPos checkPos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, targetPos.offset(dx, 0, dz));
//$$                BlockState checkState = level.getBlockState(checkPos);
//$$                BlockState checkBelowState = level.getBlockState(checkPos.below());
//$$
//$$                boolean isCheckGrassBlock = false;
//#if MC >= 11800
//$$                isCheckGrassBlock = checkBelowState.is(net.minecraft.world.level.block.Blocks.GRASS_BLOCK);
//#else
//$$                isCheckGrassBlock = (checkBelowState.getBlock() == net.minecraft.world.level.block.Blocks.GRASS_BLOCK);
//#endif
//$$
//$$                if (isCheckGrassBlock && checkState.isAir()) {
//$$                    emptyCount++;
//$$                }
//$$            }
//$$        }
//$$
//$$        // ٥. تطبيق قوانين حجم الفراغ بناءً على طلب المستخدم
//$$        if (emptyCount <= 1) {
//$$            // الفراغ صغير جداً (بلوكة واحدة معزولة) -> يتم تجاهله لتوفير الأداء ومنع الازدحام
//$$            return;
//$$        }
//$$
//$$        float spaceModifier = 1.0f;
//$$        if (emptyCount <= 3) {
//$$            // الفراغ صغير (٢ إلى ٣ بلوكات) -> تقليل احتمالية الانتشار لـ 10% ليمتلئ ببطء شديد
//$$            spaceModifier = 0.1f;
//$$        }
//$$
//$$        // ٦. العوامل البيئية ومحفزات النمو
//$$        boolean isRaining = level.isRaining();
//$$        float weatherBoost = isRaining ? 2.0f : 1.0f;
//$$
//$$        boolean nearWaterSource = isNearWater(level, targetPos, 6);
//$$        float waterBoost = nearWaterSource ? 1.5f : 1.0f;
//$$
//$$        // ٧. التحقق من عدم وجود حواجز أو مبانٍ للاعبين (Ray-marching)
//$$        if (isSpreadBlocked(level, sourcePos, targetPos, 1)) return;
//$$
//$$        // ٨. التحقق من بوابة النويس والنسب الأساسية
//$$        long worldSeed = level.getSeed();
//$$        float targetNoise = vegType == FloraDictionary.VegetationType.PLAIN_BUSH
//$$            ? 1.0f
//$$            : vegetationNoise(worldSeed, targetPos.getX(), targetPos.getZ());
//$$
//$$        float noiseMin = 0.0f;
//$$        float baseChance = 0.0f;
//$$
//$$        switch (vegType) {
//$$            case GRASS:
//$$                noiseMin = 0.12f;
//$$                baseChance = 0.38f;
//$$                break;
//$$            case FERN:
//$$                noiseMin = 0.38f;
//$$                baseChance = 0.02f;
//$$                break;
//$$            case PLAIN_BUSH:
//$$                noiseMin = 0.01f;
//$$                baseChance = 0.04f;
//$$                break;
//$$            case FIREFLY_BUSH:
//$$                if (!isNearWater(level, targetPos, 2)) return;
//$$                noiseMin = 0.25f;
//$$                baseChance = 0.07f;
//$$                break;
//$$            default:
//$$                noiseMin = 0.25f;
//$$                baseChance = 0.015f;
//$$                break;
//$$        }
//$$
//$$        if (targetNoise < noiseMin) return;
//$$
//$$        // ٩. حساب النسبة النهائية والانتشار
//$$        float finalChance = baseChance * waterBoost * weatherBoost * spaceModifier;
//$$        if (random.nextFloat() <= finalChance) {
//$$            level.setBlock(targetPos, sourceState, 3);
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
//$$        String name = getBlockPathString(block);
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
//$$     * نويس خلوي للبوش — نظام جُزر مضمون التباعد
//$$     * العالم مقسم لخلايا كبيرة (48×48)، فقط ~12% منها نشطة
//$$     * كل خلية نشطة فيها نقطة مركزية عشوائية بنصف قطر 2-4.5 بلوك
//$$     * النتيجة: مجموعات صغيرة (1-7 بوش) متباعدة بمسافات كبيرة مضمونة
//$$     */
//$$    private static float bushNoise(long seed, int x, int z) {
//$$        int cellSize = 48;
//$$        int cellX = Math.floorDiv(x, cellSize);
//$$        int cellZ = Math.floorDiv(z, cellSize);
//$$        
//$$        float bestValue = 0.0f;
//$$        
//$$        // فحص الخلية الحالية والـ 8 المجاورة لمعالجة الحدود
//$$        for (int dx = -1; dx <= 1; dx++) {
//$$            for (int dz = -1; dz <= 1; dz++) {
//$$                int cx = cellX + dx;
//$$                int cz = cellZ + dz;
//$$                
//$$                // هل الخلية نشطة؟ ~25% من الخلايا تحتوي بوش
//$$                float activity = hashFloat(seed ^ 0xDEADBEEFL, cx, cz);
//$$                if (activity > 0.25f) continue;
//$$                
//$$                // مركز المجموعة داخل الخلية (عشوائي)
//$$                float px = cx * cellSize + hashFloat(seed ^ 0xCAFEBABEL, cx, cz) * cellSize;
//$$                float pz = cz * cellSize + hashFloat(seed ^ 0xBAADF00DL, cx, cz) * cellSize;
//$$                
//$$                // المسافة من مركز المجموعة
//$$                float distX = x - px;
//$$                float distZ = z - pz;
//$$                float dist = (float)Math.sqrt(distX * distX + distZ * distZ);
//$$                
//$$                // نصف قطر المجموعة: 2-4.5 بلوك (تكفي لـ 1-7 بوشات)
//$$                float radius = 2.0f + hashFloat(seed ^ 0xFEEDFACEL, cx, cz) * 2.5f;
//$$                
//$$                if (dist <= radius) {
//$$                    float t = 1.0f - (dist / radius);
//$$                    float value = t * t; // تدرج تربيعي ناعم من المركز
//$$                    if (value > bestValue) bestValue = value;
//$$                }
//$$            }
//$$        }
//$$        return bestValue;
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
//$$
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
//$$
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
//$$                // fallback
//$$            }
//$$        }
//$$        if (blockRegistry != null) {
//$$            try {
//$$                getKeyMethod = blockRegistry.getClass().getMethod("getKey", Object.class);
//$$            } catch (Exception e) {
//$$                // fallback
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
//$$                // fallback
//$$            }
//$$        }
//$$        return "";
//$$    }
//$$
//$$    private static Block getBlockById(String id) {
//$$        initRegistryReflection();
//$$        if (blockRegistry != null) {
//$$            try {
//$$                Class<?> rlClass;
//$$                try {
//                    rlClass = Class.forName("net.minecraft.resources.ResourceLocation");
//                } catch (ClassNotFoundException e) {
//                    rlClass = Class.forName("net.minecraft.util.Identifier");
//                }
//                Object rl = rlClass.getConstructor(String.class).newInstance(id);
//                java.lang.reflect.Method getMethod = blockRegistry.getClass().getMethod("get", rlClass);
//                return (Block) getMethod.invoke(blockRegistry, rl);
//            } catch (Exception e) {
//                // fallback
//            }
//        }
//        return null;
//    }

    //#else
    public static void tickChunk(Object chunk, Object level) {}
    //#endif
}
