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
//$$ import net.minecraft.server.MinecraftServer;
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
    
    // ╔══════════════════════════════════════════════════════════════════╗
    // ║                    القسم ١: الثوابت والمتغيرات                   ║
    // ║  هنا نخزن المتغيرات العامة مثل اتجاه الرياح والاتجاهات الثمانية  ║
    // ╚══════════════════════════════════════════════════════════════════╝
    
    // --- متغيرات التحكم في سرعة المود والتسريع الزمني ---
//$$    public static float speedMultiplier = 1.0f;
//$$    public static long fastForwardTicks = 0;

    // --- نظام الرياح: يتغير اتجاه الرياح كل ٥ دقائق لعبة ---
//$$    private static double windAngle = 0;
//$$    private static long lastWindUpdate = 0;

    // --- الاتجاهات الثمانية: شمال، جنوب، غرب، شرق + الأقطار ---
//$$    private static final int[][] DIRECTIONS = {
//$$        {0, -1}, {0, 1}, {-1, 0}, {1, 0},  // N, S, W, E
//$$        {-1, -1}, {1, -1}, {-1, 1}, {1, 1}  // NW, NE, SW, SE
//$$    };

    // ╔══════════════════════════════════════════════════════════════════╗
    // ║              القسم ٢: نقطة الدخول الرئيسية (tick)               ║
    // ║   تُستدعى كل تيك من السيرفر. تدير الساعة الداخلية للمود        ║
    // ╚══════════════════════════════════════════════════════════════════╝
//$$    public static void tick(ServerLevel level) {
//$$        long gameTime = level.getGameTime();
//$$
//$$        // Update wind direction every 5 minutes (6000 ticks)
//$$        if (gameTime - lastWindUpdate > 6000) {
//$$            windAngle += (level.getRandom().nextDouble() - 0.5) * Math.PI * 0.5;
//$$            lastWindUpdate = gameTime;
//$$        }
//$$
//$$        // إذا المود متوقف وما فيه تسريع، لا تسوي شي
//$$        if (speedMultiplier <= 0 && fastForwardTicks <= 0) return;
//$$
//$$        if (fastForwardTicks > 0) fastForwardTicks--;
//$$
//$$        // صحة الشتلات وتسميد التربة - تتأثر بسرعة المود
//$$        // عادي: كل 200 تيك | سريع: أقل | تسريع زمني: كل 4 تيكات
//$$        int lifecycleInterval;
//$$        if (fastForwardTicks > 0) {
//$$            lifecycleInterval = 4;
//$$        } else {
//$$            lifecycleInterval = Math.max(1, (int)(200 / speedMultiplier));
//$$        }
//$$        if (gameTime % lifecycleInterval == 0) {
//$$            SaplingLifecycleManager.runHealthChecks(level);
//$$            SaplingLifecycleManager.runCompostChecks(level);
//$$        }
//$$    }
//$$
    // ╔══════════════════════════════════════════════════════════════════╗
    // ║         القسم ٣: التحديث العالمي للتشونكات (tickChunk)          ║
    // ║   يعمل على كل تشونك محمّل في العالم بشكل مستقل عن اللاعبين    ║
    // ║   المهام: رعد عشوائي + نمو أشجار + انتشار نباتات               ║
    // ╚══════════════════════════════════════════════════════════════════╝
//$$    public static void tickChunk(net.minecraft.world.level.chunk.LevelChunk chunk, ServerLevel level) {
//$$        if (!level.isLoaded(chunk.getPos().getMiddleBlockPosition(0))) return;
//$$        
//$$        boolean isRaining = level.isRaining();
//$$        if (speedMultiplier <= 0 && fastForwardTicks <= 0) return;
//$$        
//$$        float tps = level.getServer().tickRateManager().tickrate();
//$$        float serverSpeedRatio = tps / 20.0f;
//$$        if (serverSpeedRatio <= 0.01f) serverSpeedRatio = 1.0f;
//$$        
//$$        float currentSpeed = speedMultiplier;
//$$        if (fastForwardTicks > 0) currentSpeed = Math.max(currentSpeed, 50.0f);
//$$        
//$$        // فصل السرعة عن tick rate ماينكرافت
//$$        float effectiveSpeed = currentSpeed / serverSpeedRatio;
//$$        
//$$        // === المنطق الجديد: بدل آلاف الدورات، نزيد المحاولات ونضمن التشغيل ===
//$$        // cycles: عدد مرات تشغيل المنطق الكامل (محدود عشان ما يسبب لاق)
//$$        int cycles = Math.min(50, Math.max(1, (int)(effectiveSpeed / 5.0f)));
//$$        // attemptMultiplier: مضاعف المحاولات داخل كل دورة
//$$        int attemptMultiplier = Math.max(1, (int)(effectiveSpeed / cycles));
//$$        attemptMultiplier = Math.min(attemptMultiplier, 30);
//$$        
//$$        for (int c = 0; c < cycles; c++) {
//$$            // رعد
//$$            if (level.isThundering() && level.getRandom().nextInt(Math.max(1, (int)(100000 / effectiveSpeed))) == 0) {
//$$                BlockPos strikePos = chunk.getPos().getMiddleBlockPosition(0).offset(level.getRandom().nextInt(16) - 8, 0, level.getRandom().nextInt(16) - 8);
//$$                strikePos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, strikePos);
//$$                applyThunderDamage(level, strikePos);
//$$            }
//$$            
//$$            // فحص الاحتمال: عند سرعة عالية (>=10) نتخطى الفحص ونشغل دائماً
//$$            boolean shouldRun;
//$$            if (effectiveSpeed >= 10.0f) {
//$$                shouldRun = true;
//$$            } else {
//$$                int runChance = isRaining ? 100 : 200;
//$$                runChance = Math.max(1, (int)(runChance / effectiveSpeed));
//$$                shouldRun = level.getRandom().nextInt(runChance) == 0;
//$$            }
//$$            
//$$            if (shouldRun) {
//$$            net.minecraft.world.level.ChunkPos pos = chunk.getPos();
//$$            BlockPos center = pos.getMiddleBlockPosition(0);
//$$            
//$$            // [NIGHT SLOWDOWN] - يتعطل أثناء التسريع الزمني
//$$            if (fastForwardTicks <= 0) {
//$$                long dayTime = level.getGameTime();
//$$                boolean isDayTime = (dayTime % 24000) < 12000;
//$$                if (!isDayTime && level.getRandom().nextFloat() > 0.01f) continue;
//$$            }
//$$
//$$            int attempts = (isRaining ? 2 : 1) * attemptMultiplier;
//$$            
//$$            // Trees
//$$            for (int i = 0; i < attempts; i++) {
//$$                int ox = level.getRandom().nextInt(16) - 8;
//$$                int oz = level.getRandom().nextInt(16) - 8;
//$$                processEdgeExpansion(level, center.offset(ox, 0, oz));
//$$            }
//$$            
//$$            // Vegetation
//$$            for (int i = 0; i < attempts * 3; i++) {
//$$                int ox = level.getRandom().nextInt(16) - 8;
//$$                int oz = level.getRandom().nextInt(16) - 8;
//$$                processVegetationExpansion(level, center.offset(ox, 0, oz));
//$$            }
//$$            
//$$            // نظام مراقبة
//$$            if (level.getRandom().nextInt(3) == 0) {
//$$                int ox2 = level.getRandom().nextInt(16) - 8;
//$$                int oz2 = level.getRandom().nextInt(16) - 8;
//$$                BlockPos monitorPos = center.offset(ox2, 0, oz2);
//$$                
//$$                monitorPlantDistribution(level, monitorPos, "minecraft:short_grass", 6, 2.0,  5,  10, 3);
//$$                monitorPlantDistribution(level, monitorPos, "minecraft:bush",        2, 8.0,  12, 30, 8);
//$$                monitorPlantDistribution(level, monitorPos, "minecraft:fern",         2, 10.0, 15, 35, 8);
//$$                monitorPlantDistribution(level, monitorPos, "minecraft:tall_grass",   2, 6.0,  8,  20, 5);
//$$                monitorPlantDistribution(level, monitorPos, "minecraft:large_fern",   1, 12.0, 15, 35, 8);
//$$            }
//$$        }
//$$        } // end of cycles loop
//$$    }

    // ╔══════════════════════════════════════════════════════════════════╗
    // ║  القسم ٤أ: نظام مراقبة عام لتوزيع النباتات النادرة   ║
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
//$$                    // وجدنا نباتة! نحسب الجيران المباشرين وحجم المجموعة
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
//$$                            // تحقق من أنها من مجموعة مختلفة
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
//$$                    return; // فحصنا نبات واحد يكفي في هذه الدورة
//$$                }
//$$            }
//$$        }
//$$    }

    // ╔══════════════════════════════════════════════════════════════════╗
    // ║       القسم ٤: نظام ضبط التنظيم العشبي (المراقبة والتشذيب)      ║
    // ║   يفحص كثافة الأعشاب ويزيل الزائد منها للحفاظ على منظر طبيعي   ║
    // ║   يُستدعى عندما تصل الكثافة للحد الأقصى في منطقة معينة         ║
    // ╚══════════════════════════════════════════════════════════════════╝
//$$    private static boolean manageVegetationBalance(ServerLevel level, BlockPos pos, int density, boolean isGrass, boolean isPlainBush, boolean isFlower, RandomSource random) {
//$$        // العشب: إذا الكثافة تجاوزت الحد المحلي (يتغير حسب البقعة) → نزيل الزائد
//$$        if (isGrass && density > 5 && random.nextFloat() < 0.6f) {
//$$            level.setBlock(pos, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 3);
//$$            return true;
//$$        }
//$$        // البوش: إذا الكثافة تجاوزت ٢ → نزيل
//$$        if (isPlainBush && density >= 2 && random.nextFloat() < 0.8f) {
//$$            level.setBlock(pos, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 3);
//$$            return true;
//$$        }
//$$        // الأزهار: إذا الكثافة تجاوزت ٢ → نزيل
//$$        if (isFlower && density >= 2 && random.nextFloat() < 0.6f) {
//$$            level.setBlock(pos, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 3);
//$$            return true;
//$$        }
//$$        return false;
//$$    }

    // ╔══════════════════════════════════════════════════════════════════╗
    // ║        القسم ٥: نظام انتشار الأعشاب والنباتات الأرضية          ║
    // ║   يبحث عن نبتة موجودة ثم يحاول نشرها للأماكن القريبة           ║
    // ║   يشمل: عشب، سراخس، أزهار، شجيرات، بتلات، فطريات              ║
    // ╚══════════════════════════════════════════════════════════════════╝
//$$    private static void processVegetationExpansion(ServerLevel level, BlockPos searchPos) {
//$$        if (!level.isLoaded(searchPos)) return;
//$$        RandomSource random = level.getRandom();
//$$        
//$$        // Find a valid vegetation block by scanning a 5x5 area around the random point
//$$        // This prevents the mod from "missing" sparse plants and makes growth beautifully consistent
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
//$$                    if (b == Blocks.GRASS_BLOCK || b == Blocks.MOSS_BLOCK || b == Blocks.DIRT || b == Blocks.SAND || name.contains("leaves") || name.contains("log") || name.contains("wood")) {
//$$                        break; // Hit terrain floor, skip to next column
//$$                    }
//$$                    
//$$                    isVegetation = (name.contains("grass") || name.contains("fern") || name.contains("flower") || name.contains("lily") || 
//$$                                   name.contains("mushroom") || name.contains("fungus") || name.contains("kelp") || 
//$$                                   name.contains("seagrass") || name.contains("pickle") || name.contains("coral") ||
//$$                                   name.contains("sugar_cane") || (name.contains("bush") && !name.contains("dead")) || name.contains("moss") || 
//$$                                   name.contains("azalea") || name.contains("spore") || name.contains("dripleaf") || 
//$$                                   name.contains("cave_vines") || name.contains("hanging_roots") || name.contains("glow_berries") ||
//$$                                   name.contains("petal") || name.contains("nether_wart") || name.contains("roots") || name.contains("sprouts"));
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
//$$        // --- استثناء النباتات المزدوجة (طويلة) والشجيرات الميتة ---
//$$        // هذه النباتات لا ينبغي أن تتكاثر لأنها إما مكونة من جزئين أو ميتة
//$$        if (name.contains("sunflower") || name.contains("lilac") || name.contains("rose_bush") || 
//$$            name.contains("peony") || name.contains("tall") || name.contains("large") || 
//$$            name.contains("pitcher") || name.contains("dead_bush") || name.contains("berry_bush") ||
//$$            name.contains("lily")) {
//$$            return;
//$$        }
//$$        
//$$        BlockPos sourcePos = mut.immutable();
//$$        
//$$        // ═══════ نظام الفحص البيئي والحظر (Environment Blacklist) ═══════
//$$        String dim = level.dimension().toString();
//$$        boolean isNether = dim.contains("nether");
//$$        boolean isEnd = dim.contains("end");
//$$
//$$        // تصنيف النباتات حسب البيئة (للتحقق من الحظر)
//$$        boolean isNetherFlora = name.contains("fungus") || name.contains("nether_wart") || name.contains("roots") || name.contains("sprouts") || name.contains("vines") || name.contains("mushroom");
//$$        boolean isWaterFlora = name.contains("kelp") || name.contains("seagrass") || name.contains("pickle") || name.contains("coral");
//$$        boolean isCaveFlora = name.contains("moss") || name.contains("azalea") || name.contains("spore") || name.contains("dripleaf") || name.contains("cave_vines") || name.contains("glow_berries");
//$$
//$$        // تطبيق قوانين الحظر التام:
//$$        // 1. منع انتشار نباتات الكهوف والماء والنذر والفطريات نهائياً
//$$        if (isNetherFlora || isWaterFlora || isCaveFlora) return;
//$$
//$$        // 2. منع أي انتشار في بيئة النذر أو النهاية
//$$        if (isNether || isEnd) return;
//$$
//$$        // 3. منع الانتشار إذا كان الأصل (sourcePos) تحت الماء أو في كهف عميق
//$$        boolean sourceInWater = level.getFluidState(sourcePos).is(net.minecraft.world.level.material.Fluids.WATER);
//$$        boolean sourceInCave = !level.canSeeSky(sourcePos) && level.getHeightmapPos(net.minecraft.world.level.levelgen.Heightmap.Types.WORLD_SURFACE, sourcePos).getY() > sourcePos.getY() + 25;
//$$        if (sourceInWater || sourceInCave) return;
//$$
//$$        // ═══════ تصنيف النبات ═══════
//$$        // كل نبتة لها قواعد انتشار مختلفة، لذلك نصنفها هنا
//$$        int density = 0;
//$$        boolean isGrass = name.equals("grass") || name.equals("short_grass");  // أعشاب قصيرة
//$$        
//$$        // ترقية العشب القصير إلى عشب طويل إذا كان قرب الماء (ضمن بلوكة واحدة)
//$$        if (isGrass && name.equals("short_grass") && isNearWater(level, sourcePos, 1)) {
//$$            if (random.nextFloat() < 0.15f && level.getBlockState(sourcePos.above()).isAir()) {
//$$                BlockState tallGrassState = net.minecraft.world.level.block.Blocks.TALL_GRASS.defaultBlockState();
//$$                level.setBlock(sourcePos, tallGrassState.setValue(net.minecraft.world.level.block.state.properties.BlockStateProperties.DOUBLE_BLOCK_HALF, net.minecraft.world.level.block.state.properties.DoubleBlockHalf.LOWER), 3);
//$$                level.setBlock(sourcePos.above(), tallGrassState.setValue(net.minecraft.world.level.block.state.properties.BlockStateProperties.DOUBLE_BLOCK_HALF, net.minecraft.world.level.block.state.properties.DoubleBlockHalf.UPPER), 3);
//$$                return;
//$$            }
//$$        }
//$$
//$$        boolean isFern = name.equals("fern");                  // سرخس
//$$        boolean isPlainBush = name.equals("bush");              // شجيرة minecraft:bush الزخرفية فقط
//$$        boolean isFireflyBush = name.contains("firefly_bush");  // شجيرة اليراعات (قرب الماء فقط)
//$$        boolean isPetal = name.contains("petal");               // بتلات الكرز الوردية
//$$        boolean isFungus = name.contains("mushroom") || name.contains("fungus");  // فطريات
//$$        boolean isWaterPlant = isWaterFlora; // إعادة الاسم القديم للتوافق
//$$        boolean isFlower = !isGrass && !isFern && !isPlainBush && !isFireflyBush && !isPetal && !isFungus && !isWaterPlant; // أي نبتة أخرى تعتبر من الأزهار
//$$        
//$$        // --- شروط خاصة لبعض النباتات ---
//$$        // شجيرة اليراعات لا تنتشر إلا بجوار الماء مباشرة
//$$        if (isFireflyBush && !isNearWater(level, sourcePos, 2)) return;
//$$        
//$$        // البتلات الوردية لا تنتشر إلا بالقرب من أشجار الكرز
//$$        if (isPetal) {
//$$            boolean hasCherryTree = false;
//$$            for (BlockPos cp : BlockPos.betweenClosed(sourcePos.offset(-8, 0, -8), sourcePos.offset(8, 15, 8))) {
//$$                if (net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(level.getBlockState(cp).getBlock()).getPath().contains("cherry_leaves")) {
//$$                    hasCherryTree = true;
//$$                    break;
//$$                }
//$$            }
//$$            if (!hasCherryTree) return;
//$$        }
//$$        
//$$        // ═══════ سرعات الانتشار ═══════
//$$        // النباتات قرب الماء تنمو أسرع بـ ٢.٥ مرة
//$$        boolean nearWaterSource = isNearWater(level, sourcePos, 6);
//$$        float waterBoost = nearWaterSource ? 2.5f : 1.0f;
//$$        
//$$        // نسب الانتشار (جاف → رطب):
//$$        //   عشب: ٣٠٪ → ٧٥٪  |  شجيرة: ٢٥٪ → ٦٢٪  |  بتلات: ٣٥٪ → ٨٧٪
//$$        //   يراعات: ٢٠٪ → ٥٠٪  |  أزهار: ٢٠٪ → ٥٠٪
//$$        if (isGrass) {
//$$            if (random.nextFloat() > 0.12f * waterBoost) return; // العشب (١٢٪) - أسرع قليلاً لتكوين بقع كثيفة
//$$        } else if (isFern) {
//$$            if (random.nextFloat() > 0.015f * waterBoost) return; // السرخس نادر جداً (١.٥٪)
//$$        } else if (isPlainBush) {
//$$            if (random.nextFloat() > 0.03f * waterBoost) return; // البوش نادر (٣٪)
//$$        } else if (isFireflyBush) {
//$$            if (random.nextFloat() > 0.05f * waterBoost) return; // يراعات (٥٪)
//$$        } else if (isPetal) {
//$$            if (random.nextFloat() > 0.08f * waterBoost) return; // بتلات (٨٪)
//$$        } else {
//$$            if (random.nextFloat() > 0.01f * waterBoost) return;  // أزهار عادية (١٪)
//$$        }
//$$        
//$$        // Smart Level Spreading for Pink Petals
//$$        if (isPetal) {
//$$            for (net.minecraft.world.level.block.state.properties.Property<?> prop : state.getProperties()) {
//$$                String pName = prop.getName().toLowerCase();
//$$                if (pName.contains("amount") || pName.contains("flower")) {
//$$                    @SuppressWarnings("unchecked")
//$$                    net.minecraft.world.level.block.state.properties.Property<Integer> intProp = (net.minecraft.world.level.block.state.properties.Property<Integer>) prop;
//$$                    int currentAmount = state.getValue(intProp);
//$$                    // 15% chance to grow in place, but stop at level 2 or 3 so it's not fully filled
//$$                    if (currentAmount < 3 && random.nextFloat() < 0.15f) {
//$$                        level.setBlock(sourcePos, state.setValue(intProp, currentAmount + 1), 3);
//$$                        return; // Successfully grew in place!
//$$                    }
//$$                    // If we spread, randomize the new block's amount to just 1 or 2 max
//$$                    state = state.setValue(intProp, 1 + random.nextInt(2));
//$$                } else if (pName.contains("facing") || pName.contains("direction")) {
//$$                    @SuppressWarnings("unchecked")
//$$                    net.minecraft.world.level.block.state.properties.Property<net.minecraft.core.Direction> dirProp = (net.minecraft.world.level.block.state.properties.Property<net.minecraft.core.Direction>) prop;
//$$                    state = state.setValue(dirProp, net.minecraft.core.Direction.Plane.HORIZONTAL.getRandomDirection(random));
//$$                }
//$$            }
//$$        }
//$$        
//$$        // ═══════ نظام الكثافة والتوزيع ═══════
//$$        // نحسب عدد النباتات المشابهة في المنطقة المحيطة
//$$        // شجيرة اليراعات تفحص مساحة أكبر (٧×٧) لأنها تحتاج عزل أكثر
//$$        int checkRadius = (isFireflyBush) ? 3 : 2;
//$$        for (BlockPos p : BlockPos.betweenClosed(sourcePos.offset(-checkRadius, -2, -checkRadius), sourcePos.offset(checkRadius, 2, checkRadius))) {
//$$            if (p.equals(sourcePos)) continue;
//$$            if (level.getBlockState(p).getBlock() == block) {
//$$                density++;
//$$            }
//$$        }
//$$        
//$$        // الحد الأقصى للكثافة في المنطقة:
//$$        // العشب يستخدم نظام البقع الكثيفة: بعض المناطق تسمح بكثافة أعلى (حتى ٩) وبعضها أقل (٥)
//$$        int grassMaxDensity = 6;
//$$        if (isGrass) {
//$$            // نظام البقع الكثيفة: استخدام الإحداثيات لتحديد ما إذا كانت المنطقة "بقعة كثيفة" أم لا
//$$            // نقسم العالم لمربعات 8x8 ونستخدم hash لتحديد الكثافة
//$$            int gridX = Math.floorDiv(sourcePos.getX(), 8);
//$$            int gridZ = Math.floorDiv(sourcePos.getZ(), 8);
//$$            int patchHash = (gridX * 73856093) ^ (gridZ * 19349663);
//$$            boolean isDensePatch = (Math.abs(patchHash) % 5) < 2; // ~40% من المناطق تكون بقع كثيفة
//$$            grassMaxDensity = isDensePatch ? 9 : 5;
//$$        }
//$$        int maxDensity = isGrass ? grassMaxDensity : (isFireflyBush ? 1 : (isPlainBush ? 3 : (isFern ? 2 : (isPetal ? 3 : (isFlower ? 2 : 2)))));
//$$        int searchSpread = (isGrass || isPlainBush) ? 5 : 4;
//$$        
//$$        if (density >= maxDensity) {
//$$            // الكثافة المحلية وصلت للحد الأقصى! نقوم بالتشذيب أولاً للتحكم في الحجم
//$$            manageVegetationBalance(level, sourcePos, density, isGrass, isPlainBush, isFlower, random);
//$$            
//$$            // نظام المستكشف: نسمح للنبتة بالقفز لمكان بعيد لبدء مجموعة جديدة متباعدة
//$$            float pioneerChance = isPlainBush ? 0.40f : (isFlower ? 0.15f : 0.05f);
//$$            if (random.nextFloat() < pioneerChance) {
//$$                searchSpread = isPlainBush ? 12 : (isFlower ? 24 : 8); // الشجيرة تقفز مسافة متوسطة لبدء غابة جديدة
//$$            } else {
//$$                return;
//$$            }
//$$        } else {
//$$            // النمو العادي: الشجيرات تنمو قريبة (نصف قطر ٢) لتشكيل مجموعات متلاصقة
//$$            searchSpread = (isPlainBush || isFlower) ? 2 : (isGrass ? 5 : 4);
//$$        }
//$$        
//$$        BlockPos bestTarget = null;
//$$        int bestScore = -1;
//$$        for (int i = 0; i < (isGrass ? 8 : ((isPlainBush || isFlower) ? 8 : 4)); i++) { // العشب والشجيرات والأزهار تفحص 8 محاولات
//$$            int ox = random.nextInt(searchSpread * 2 + 1) - searchSpread;
//$$            int oz = random.nextInt(searchSpread * 2 + 1) - searchSpread;
//$$            if (ox == 0 && oz == 0) continue; // Skip source position
//$$            
//$$            BlockPos target;
//$$            if (isWaterPlant) {
//$$                target = level.getHeightmapPos(Heightmap.Types.OCEAN_FLOOR, sourcePos.offset(ox, 0, oz));
//$$            } else {
//$$                target = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, sourcePos.offset(ox, 0, oz));
//$$            }
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
//$$            // [GRASS BLOCK ONLY] النباتات البرية تنمو فقط على العشب الأخضر (Grass Block)
//$$            {
//$$                BlockState below = level.getBlockState(target.below());
//$$                if (!below.is(net.minecraft.world.level.block.Blocks.GRASS_BLOCK)) continue;
//$$            }
//$$
//$$            // [BARRIER CHECK] منع القفز فوق الأسوار أو الجدران أو دخول البيوت
//$$            // العشب يحتاج لثقب بارتفاع بلوك واحد فقط ليمر (واقعية)
//$$            if (isSpreadBlocked(level, sourcePos, target, 1)) continue;
//$$
//$$            // [DESTINATION CHECK] منع النمو في الكهوف أو الماء
//$$            if (level.getFluidState(target).is(net.minecraft.world.level.material.Fluids.WATER)) continue;
//$$            if (!level.canSeeSky(target) && level.getHeightmapPos(net.minecraft.world.level.levelgen.Heightmap.Types.WORLD_SURFACE, target).getY() > target.getY() + 25) continue;
//$$            
//$$            // Firefly bush: target must be directly adjacent to water (within 2 blocks)
//$$            if (isFireflyBush && !isNearWater(level, target, 2)) continue;
//$$            
//$$            // --- فحص المسافة الدنيا بين النباتات ---
//$$            // الشجيرة الزخرفية (bush) والأزهار: مسافة ٠ = تلاصق مسموح لتكوين مجموعات
//$$            // شجيرة اليراعات: مسافة ٤ بلوكات = متباعدة
//$$            // العشب: مسافة ٠ = تلاصق مسموح
//$$            boolean tooClose = false;
//$$            int minSpacing = isFireflyBush ? 4 : 0;  // الشجيرة الزخرفية والعشب والأزهار = ٠
//$$            
//$$            // الشجيرة والأزهار المستكشفة (المجموعة الجديدة) يجب أن تبدأ بعيدة عن أي مجموعة مساوية
//$$            if ((isPlainBush || isFlower || isFern) && density >= maxDensity) {
//$$                if (isFern) {
//$$                    minSpacing = 20 + random.nextInt(11); // السرخس يبتعد ٢٠-٣٠ بلوكة
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
//$$            // ═══════ نظام التقييم: أفضل مكان للنمو ═══════
//$$            int score = 0;
//$$            // العشب والشجيرات تحب الماء - نبحث في نطاق أوسع
//$$            if (!isWaterPlant && isNearWater(level, target, (isGrass || isPlainBush) ? 8 : 4)) score += (isGrass || isPlainBush) ? 10 : 5;
//$$            // العشب والشجيرات تفضل النمو تحت الأشجار
//$$            if ((isGrass || isPlainBush) && hasHeavyCanopy(level, target)) score += 6;
//$$            if (!(isGrass || isPlainBush) && hasHeavyCanopy(level, target)) score += isFungus ? 8 : 2;
//$$            // شجيرة اليراعات تحصل على أفضلية كبيرة بجوار الماء مباشرة
//$$            if (isFireflyBush && isNearWater(level, target, 1)) score += 15;
//$$            // عشوائية بسيطة لتنويع المنظر
//$$            score += random.nextInt(4);
//$$            
//$$            if (score > bestScore) {
//$$                bestScore = score;
//$$                bestTarget = target;
//$$            }
//$$        }
//$$        
//$$        // --- وضع النبتة في المكان الأفضل ---
//$$        if (bestTarget != null) {
//$$            level.setBlock(bestTarget, state, 3);
//$$        }
//$$    }

    // ╔══════════════════════════════════════════════════════════════════╗
    // ║           القسم ٦: نظام بتلات الكرز (Cherry Petals)            ║
    // ║   يفحص أشجار الكرز ويضع بتلات وردية على الأرض تحتها           ║
    // ║   إذا لم توجد أي بتلة، يرمي ٣-٦ مجموعات بكميات عشوائية       ║
    // ╚══════════════════════════════════════════════════════════════════╝
//$$    private static void tryCherryPetalDrop(ServerLevel level, BlockPos treePos) {
//$$        BlockState state = level.getBlockState(treePos);
//$$        String name = net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(state.getBlock()).getPath();
//$$        if (!name.contains("cherry_leaves") && !name.contains("cherry_log") && !name.contains("cherry_wood")) return;
//$$        
//$$        // It's a cherry tree!
//$$        BlockPos surfaceCenter = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, treePos);
//$$        int petalBlockCount = 0;
//$$        
//$$        // فحص مساحة أوسع (١١×١١) لضمان عدم تجاوز حد الكثافة المسموح به لكل شجرة
//$$        int scanRadius = 5;
//$$        for (BlockPos p : BlockPos.betweenClosed(surfaceCenter.offset(-scanRadius, -2, -scanRadius), surfaceCenter.offset(scanRadius, 2, scanRadius))) {
//$$            if (level.getBlockState(p).is(net.minecraft.world.level.block.Blocks.PINK_PETALS)) {
//$$                petalBlockCount++;
//$$            }
//$$        }
//$$        
//$$        // حد الكثافة: لتكون مثالية وغير ممتلئة (٥ إلى ٧ مجموعات فقط في المحيط)
//$$        int maxPetals = 5 + level.getRandom().nextInt(3);
//$$        if (petalBlockCount >= maxPetals) return;
//$$
//$$        RandomSource random = level.getRandom();
//$$        // فرصة ضئيلة لتساقط البتلات لضمان نمو بطيء وطبيعي (١٠٪ فرصة في كل دورة فحص)
//$$        if (random.nextFloat() < 0.9f) return;
//$$        
//$$        int drops = 1 + random.nextInt(3); // تقليل عدد المجموعات المضافة (١ إلى ٣ مجموعات فقط)
//$$        for (int i = 0; i < drops; i++) {
//$$            int ox = random.nextInt(11) - 5; // توزيع أوسع قليلاً (-٥ إلى ٥)
//$$            int oz = random.nextInt(11) - 5;
//$$            
//$$            BlockPos target = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, surfaceCenter.offset(ox, 0, oz));
//$$            BlockState targetState = level.getBlockState(target);
//$$            
//$$            if (targetState.canBeReplaced() || targetState.isAir()) {
//$$                BlockState soil = level.getBlockState(target.below());
//$$                if (soil.is(net.minecraft.world.level.block.Blocks.GRASS_BLOCK) || soil.is(net.minecraft.world.level.block.Blocks.DIRT) || soil.is(net.minecraft.world.level.block.Blocks.PODZOL)) {
//$$                    // تنويع الكميات: غالباً ١ أو ٢ كحد أقصى للحفاظ على شكل متناثر وجميل
//$$                    int amount = 1 + random.nextInt(2);
//$$                    BlockState petalState = net.minecraft.world.level.block.Blocks.PINK_PETALS.defaultBlockState();
//$$                    for (net.minecraft.world.level.block.state.properties.Property<?> prop : petalState.getProperties()) {
//$$                        String pName = prop.getName().toLowerCase();
//$$                        if (pName.contains("amount") || pName.contains("flower")) {
//$$                            @SuppressWarnings("unchecked")
//$$                            net.minecraft.world.level.block.state.properties.Property<Integer> intProp = (net.minecraft.world.level.block.state.properties.Property<Integer>) prop;
//$$                            petalState = petalState.setValue(intProp, amount);
//$$                        } else if (pName.contains("facing") || pName.contains("direction")) {
//$$                            @SuppressWarnings("unchecked")
//$$                            net.minecraft.world.level.block.state.properties.Property<net.minecraft.core.Direction> dirProp = (net.minecraft.world.level.block.state.properties.Property<net.minecraft.core.Direction>) prop;
//$$                            petalState = petalState.setValue(dirProp, net.minecraft.core.Direction.Plane.HORIZONTAL.getRandomDirection(random));
//$$                        }
//$$                    }
//$$                    level.setBlock(target, petalState, 3);
//$$                }
//$$            }
//$$        }
//$$    }
//$$
    // ╔══════════════════════════════════════════════════════════════════╗
//$$    // ╔══════════════════════════════════════════════════════════════════╗
//$$    // ║             القسم ٧: نظام توسع الغابات (الشتلات)               ║
//$$    // ║   الهدف: توسيع الغابات بنمط مطابق للفانيلا لكل بيئة حيوية     ║
//$$    // ║   يستخدم BiomeForestProfile لتحديد المسافات والاحتمالات        ║
//$$    // ║   ● شجرة داخلية → لا تنشر (حد يتغير حسب البيئة)              ║
//$$    // ║   ● شجرة حافة → تنشر للخارج بمسافة خاصة بالبيئة              ║
//$$    // ║   ● شجرة رائدة → تؤسس غابة جديدة بكثافة البيئة               ║
//$$    // ╚══════════════════════════════════════════════════════════════════╝
//$$    private static void processEdgeExpansion(ServerLevel level, BlockPos searchPos) {
//$$        if (!level.isLoaded(searchPos)) return;
//$$        
//$$        // [ENVIRONMENT] تعطيل في الأبعاد غير الأوفرورلد
//$$        String dim = level.dimension().toString();
//$$        if (dim.contains("nether") || dim.contains("end")) return;
//$$        
//$$        searchPos = level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE, searchPos);
//$$        if (!level.canSeeSky(searchPos)) return;
//$$
//$$        // --- لا تعطيل بيئات: كل الغابات تعمل بالنظام الجديد ---
//$$
//$$        // [BIOME PROFILE] جلب ملف الغابة من موقع **الشجرة** وليس موقع البحث
//$$        // هذا يضمن أن شجرة الغابة على حافة البلينز تحتفظ بسلوكها الأصلي
//$$        // بينما شجرة Oak الأصلية في البلينز تستخدم ملف البلينز البطيء
//$$
//$$        RandomSource random = level.getRandom();
//$$        
//$$        // === الخطوة ١: البحث عن شجرة قريبة ===
//$$        BlockPos treePos = findNearbyTree(level, searchPos, 16);
//$$        if (treePos == null) return;
//$$
//$$        // فحص البيئة عند مستوى الأرض تحت الشجرة (ليس القمة)
//$$        // لأن findNearbyTree يرجع موقع الأوراق العلوية، والبيئة قد تختلف بسبب 3D biomes
//$$        int biomeCheckY = findActualGroundY(level, treePos);
//$$        BlockPos groundPos = new BlockPos(treePos.getX(), biomeCheckY, treePos.getZ());
//$$        BiomeForestProfile profile = BiomeForestProfile.getProfile(level, groundPos);
//$$
//$$        // ميزة: تساقط بتلات الكرز
//$$        tryCherryPetalDrop(level, treePos);
//$$
//$$        // === الخطوة ٢: تصنيف الشجرة حسب ملف البيئة ===
//$$        int forestedDirs = 0;
//$$        List<int[]> openDirections = new ArrayList<>();
//$$
//$$        for (int[] dir : DIRECTIONS) {
//$$            boolean hasForest = false;
//$$            for (int dist = 3; dist <= profile.scanRadius; dist += 3) {
//$$                BlockPos checkPos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, 
//$$                    treePos.offset(dir[0] * dist, 0, dir[1] * dist)).below();
//$$                BlockState state = level.getBlockState(checkPos);
//$$                if ((state.getBlock() instanceof RotatedPillarBlock || state.getBlock() instanceof LeavesBlock)
//$$                    && isValidTree(level, checkPos)) {
//$$                    hasForest = true;
//$$                    break;
//$$                }
//$$            }
//$$            if (hasForest) {
//$$                forestedDirs++;
//$$            } else {
//$$                openDirections.add(dir);
//$$            }
//$$        }
//$$
//$$        // شجرة داخلية: وصلت لحد البيئة → لا تنشر
//$$        // (الغابة المظلمة تحتاج ٧ اتجاهات، السافانا ٣ فقط)
//$$        if (forestedDirs >= profile.interiorThreshold) return;
//$$
//$$        // شجرة رائدة: ٠-١ اتجاه → تؤسس غابة جديدة
//$$        if (forestedDirs <= 1) {
//$$            if (random.nextFloat() < profile.pioneerChance) {
//$$                double angle = random.nextDouble() * 2 * Math.PI;
//$$                int dist = profile.randomSpreadDist(random);
//$$                int ox = (int) (Math.cos(angle) * dist);
//$$                int oz = (int) (Math.sin(angle) * dist);
//$$                int groundY = findActualGroundY(level, treePos.offset(ox, 0, oz));
//$$                BlockPos targetPos = new BlockPos(treePos.getX() + ox, groundY + 1, treePos.getZ() + oz);
//$$                plantAtPosition(level, targetPos, treePos, profile);
//$$            }
//$$            return;
//$$        }
//$$
//$$        // شجرة حافة: تنشر للخارج بمسافة مخصصة للبيئة
//$$        if (openDirections.isEmpty()) return;
//$$        if (random.nextFloat() >= profile.edgeChance) return;
//$$
//$$        // اختيار اتجاه مفتوح (مع تأثير الرياح للانجراف الطبيعي)
//$$        int[] chosenDir;
//$$        if (openDirections.size() > 1 && random.nextFloat() < 0.4f) {
//$$            chosenDir = openDirections.stream()
//$$                .min((a, b) -> {
//$$                    double angleA = Math.atan2(a[1], a[0]);
//$$                    double angleB = Math.atan2(b[1], b[0]);
//$$                    return Double.compare(Math.abs(angleA - windAngle), Math.abs(angleB - windAngle));
//$$                })
//$$                .orElse(openDirections.get(0));
//$$        } else {
//$$            chosenDir = openDirections.get(random.nextInt(openDirections.size()));
//$$        }
//$$
//$$        // حساب موقع الشتلة بمسافة مخصصة للبيئة
//$$        int spreadDist = profile.randomSpreadDist(random);
//$$        int perpX = (int) ((random.nextFloat() - 0.5f) * 4);
//$$        int perpZ = (int) ((random.nextFloat() - 0.5f) * 4);
//$$        int groundY = findActualGroundY(level, treePos.offset(chosenDir[0] * spreadDist + perpX, 0, chosenDir[1] * spreadDist + perpZ));
//$$        BlockPos targetPos = new BlockPos(treePos.getX() + chosenDir[0] * spreadDist + perpX, groundY + 1, treePos.getZ() + chosenDir[1] * spreadDist + perpZ);
//$$
//$$        plantAtPosition(level, targetPos, treePos, profile);
//$$    }
//$$
//$$    /**
//$$     * يتحقق ما إذا كان الموقع يحتوي على شجرة حقيقية وليس مجرد بلوكة خشب.
//$$     * يستخدم نظام TreeValidator المنفصل للفحص.
//$$     * @see TreeValidator#isValidTree
//$$     */
//$$    private static boolean isValidTree(ServerLevel level, BlockPos pos) {
//$$        return TreeValidator.isValidTree(level, pos);
//$$    }

//$$
//$$    private static BlockPos findNearbyTree(ServerLevel level, BlockPos center, int maxRadius) {
//$$        // بحث حلزوني مكثف لضمان العثور على الشجرة حتى لو كانت وحيدة
//$$        for (int r = 1; r <= maxRadius; r++) {
//$$            for (int x = -r; x <= r; x++) {
//$$                for (int z = -r; z <= r; z++) {
//$$                    if (Math.abs(x) != r && Math.abs(z) != r) continue;
//$$                    BlockPos checkPos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, center.offset(x, 0, z)).below();
//$$                    BlockState state = level.getBlockState(checkPos);
//$$                    if (state.getBlock() instanceof RotatedPillarBlock || state.getBlock() instanceof LeavesBlock) {
//$$                        // تحقق إضافي: هل هذه شجرة حقيقية وليست مجرد بلوكة خشب؟
//$$                        if (isValidTree(level, checkPos)) {
//$$                            return checkPos;
//$$                        }
//$$                    }
//$$                }
//$$            }
//$$        }
//$$        return null;
//$$    }
//$$
//$$    private static void plantAtPosition(ServerLevel level, BlockPos targetPos, BlockPos sourceTreePos, BiomeForestProfile sourceProfile) {
//$$        if (!level.isLoaded(targetPos)) return;
//$$
//$$        // --- لا حظر بيئات: كل الغابات تستخدم ملفها الخاص ---
//$$
//$$        // [BIOME PROFILE] نستخدم ملف الشجرة المصدر (ليس الهدف)
//$$        // هذا يسمح للغابات بالزحف نحو السهول والمروج بمسافاتها الأصلية
//$$        BiomeForestProfile profile = sourceProfile;
//$$
//$$        // [4] TERRAIN CHECK
//$$        if (!isTerrainFlat(level, targetPos)) return;
//$$
//$$        // [5] CANOPY DENSITY - نستخدم حد بيئة الهدف لأن التاج فعلياً يعتمد على المكان
//$$        BiomeForestProfile targetProfile = BiomeForestProfile.getProfile(level, targetPos);
//$$        if (hasHeavyCanopy(level, targetPos, targetProfile.canopyTolerance)) return;
//$$
//$$        // Determine sapling type from the source edge tree
//$$        BlockState sourceState = level.getBlockState(sourceTreePos);
//$$        Optional<Block> saplingOpt = getRelatedSapling(sourceState.getBlock());
//$$        
//$$        // If can't determine from source tree, use biome-aware selection
//$$        if (saplingOpt.isEmpty()) {
//$$            saplingOpt = determineSapling(level, targetPos);
//$$        }
//$$
//$$        if (saplingOpt.isEmpty()) return;
//$$
//$$        Block sapling = saplingOpt.get();
//$$        // --- حماية نهائية: منع زراعة الشجيرات الميتة نهائياً كشتلة ---
//$$        if (sapling == Blocks.DEAD_BUSH || BuiltInRegistries.BLOCK.getKey(sapling).getPath().contains("dead_bush")) return;
//$$
//$$        // [BIOME-AWARE SPACING] مسافة مخصصة للبيئة بدلاً من القيمة العامة
//$$        int spacing = profile.randomSpacing(level.getRandom());
//$$
//$$        // [BIOME-AWARE 2x2] فرصة الشتلات العملاقة حسب ملف البيئة
//$$        boolean needs2x2 = profile.shouldPlant2x2(sapling, level.getRandom());
//$$
//$$        long currentTime = level.getGameTime();
//$$
//$$        // [2] LIGHT CHECK
//$$        if (!hasAdequateLight(level, targetPos, sapling)) return;
//$$
//$$        // [9] SOIL FERTILITY
//$$        float fertilityBonus = getSoilFertility(level, targetPos);
//$$        if (level.getRandom().nextFloat() > fertilityBonus) return;
//$$        // [BARRIER CHECK] منع الأشجار من الانتشار داخل المناطق المسورة
//$$        if (isSpreadBlocked(level, sourceTreePos, targetPos, 2)) return;
//$$
//$$        if (needs2x2) {
//$$            place2x2Saplings(level, targetPos, sapling, spacing, currentTime, profile.shouldTerraform);
//$$        } else {
//$$            if (isSuitableForSapling(level, targetPos) && isAreaClear(level, targetPos, spacing)) {
//$$                level.setBlock(targetPos, sapling.defaultBlockState(), 3);
//$$                ForestGrowthData.get(level).addSapling(targetPos, currentTime, profile.shouldTerraform);
//$$            }
//$$        }
//$$    }
//$$


    // ╔══════════════════════════════════════════════════════════════════╗
    // ║            القسم ٩: نظام أضرار الرعد (Thunder Damage)          ║
    // ║   أثناء العواصف الرعدية: البرق يدمر بعض أوراق الأشجار          ║
    // ╚══════════════════════════════════════════════════════════════════╝
//$$    private static void applyThunderDamage(ServerLevel level, BlockPos center) {
//$$        RandomSource random = level.getRandom();
//$$        int radius = 2 + random.nextInt(2);
//$$        for (BlockPos p : BlockPos.betweenClosed(center.offset(-radius, -1, -radius), center.offset(radius, 5, radius))) {
//$$            BlockState state = level.getBlockState(p);
//$$            if (state.getBlock() instanceof LeavesBlock) {
//$$                if (random.nextFloat() < 0.3f) {
//$$                    level.setBlock(p, Blocks.AIR.defaultBlockState(), 3);
//$$                }
//$$            }
//$$        }
//$$    }



    // ╔══════════════════════════════════════════════════════════════════╗
    // ║     القسم ١٠: أنظمة البيئة الحيوية (Biome Systems)             ║
    // ║   تحديد نوع الشتلة المناسبة لكل بيئة حيوية                    ║
    // ║   التحقق من صلاحية الشتلة للبيئة + المنافسة بين الأنواع        ║
    // ╚══════════════════════════════════════════════════════════════════╝
//$$    private static Optional<Block> determineSapling(ServerLevel level, BlockPos pos) {
//$$        // [7] SPECIES COMPETITION: First check what's dominant nearby
//$$        Optional<Block> nearby = findNearbyForestType(level, pos, 4, 20);
//$$        if (nearby.isPresent()) {
//$$            Block nearbyTree = nearby.get();
//$$            // Verify this tree belongs in this biome (biome-aware filter)
//$$            if (isSaplingValidForBiome(level, pos, nearbyTree)) {
//$$                return nearby;
//$$            }
//$$            // [7] Competition: 60% chance to still plant even if wrong biome (invasive species)
//$$            if (level.getRandom().nextFloat() < 0.6f) {
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
//$$                || biomeKey == Biomes.MEADOW || biomeKey == Biomes.WINDSWEPT_FOREST;
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
//$$        if (biomeKey == Biomes.SWAMP || biomeKey == Biomes.MANGROVE_SWAMP) {
//$$            return Optional.empty(); // Disabled swamp growth
//$$        }
//$$        return Optional.empty(); // Desert, ocean, etc. - no growth
//$$    }

    // ╔══════════════════════════════════════════════════════════════════╗
    // ║          القسم ١١: أدوات مساعدة (Utility Functions)            ║
    // ║   دوال صغيرة تُستخدم في أماكن متعددة من الكود                  ║
    // ║   فحص الإضاءة، قرب الماء، استواء الأرض، كثافة الأوراق، الخ     ║
    // ╚══════════════════════════════════════════════════════════════════╝
    // --- فحص الإضاءة: هل المكان مضاء كفاية لنمو الشتلة؟ ---
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
//$$    /**
//$$     * يفحص المسار بين نقطتين في الأبعاد الثلاثة للتأكد من وجود ممر متاح
//$$     * @param height الارتفاع المطلوب للممر (1 للعشب، 2 للأشجار)
//$$     */
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
//$$            // فحص الممر بالكامل حسب الارتفاع المطلوب
//$$            if (!checkPos.equals(source) && !checkPos.equals(target)) {
//$$                for (int h = 0; h < height; h++) {
//$$                    if (isBarrier(level, checkPos.above(h), h > 0)) return true;
//$$                }
//$$            }
//$$        }
//$$        return false;
//$$    }
//$$
//$$    /**
//$$     * يحدد ما إذا كان البلوك يعتبر حاجزاً (سياج، جدار، بناء صناعي)
//$$     */
//$$    private static boolean isBarrier(ServerLevel level, BlockPos pos, boolean isAbove) {
//$$        BlockState state = level.getBlockState(pos);
//$$        if (state.isAir()) return false;
//$$        
//$$        Block block = state.getBlock();
//$$        // الأسوار، الجدران، الأبواب، البوابات، وقضبان الحديد/الزجاج هي حواجز دائماً
//$$        // نستخدم الفحص بالاسم لضمان التوافق مع جميع إصدارات ماينكرافت
//$$        String name = net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(block).getPath();
//$$        if (name.contains("fence") || name.contains("wall") || name.contains("gate") || 
//$$            name.contains("door") || name.contains("pane") || name.contains("bars") ||
//$$            name.contains("slab") || name.contains("stairs")) return true;
//$$            
//$$        // الأشجار والنباتات الطبيعية لا تعتبر حواجز أبداً للسماح بالانتشار
//$$        if (name.contains("leaves") || name.contains("log") || name.contains("wood") || 
//$$            name.contains("grass") || name.contains("fern") || name.contains("flower") ||
//$$            name.contains("sapling") || name.contains("bush")) return false;
//$$            
//$$        // البلوكات الصلبة الكاملة التي ليست تضاريس طبيعية
//$$        if (state.isRedstoneConductor(level, pos)) {
//$$            // السماح بالانتشار عبر هذه البلوكات الطبيعية الأرضية
//$$            if (name.contains("dirt") || name.contains("sand") || name.contains("gravel") || 
//$$                name.contains("stone") || name.contains("moss") || name.contains("mud") || 
//$$                name.contains("clay") || name.contains("snow") || name.contains("ice") || 
//$$                name.contains("mycelium") || name.contains("podzol")) return false;
//$$            
//$$            // أي بلوك صلب صناعي آخر يعتبر حاجزاً
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

    // ==================== [4] TERRAIN CHECK ====================
//$$    private static boolean isTerrainFlat(ServerLevel level, BlockPos pos) {
//$$        // البحث عن مستوى الأرض الحقيقي وتجاهل الجذوع والأوراق التي قد تخدع الـ Heightmap
//$$        int centerY = findActualGroundY(level, pos);
//$$        int maxDiff = 0;
//$$        
//$$        for (int x = -1; x <= 1; x++) {
//$$            for (int z = -1; z <= 1; z++) {
//$$                int y = findActualGroundY(level, pos.offset(x, 0, z));
//$$                maxDiff = Math.max(maxDiff, Math.abs(y - centerY));
//$$            }
//$$        }
//$$        return maxDiff <= 3; // ٣ بلوكات فرق كافي للتلال الطبيعية
//$$    }
//$$
//$$    private static int findActualGroundY(ServerLevel level, BlockPos pos) {
//$$        // نبدأ من أعلى مكان وننزل لنجد أول بلوك صلب حقيقي (ليس شجرة)
//$$        BlockPos p = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos);
//$$        while (p.getY() > -64) { // حد آمن متوافق مع جميع النسخ
//$$            BlockState state = level.getBlockState(p);
//$$            String name = net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(state.getBlock()).getPath();
//$$            // نتوقف عند الوصول للتربة أو العشب الحقيقي
//$$            if (state.isAir() || name.contains("leaves") || name.contains("log") || name.contains("wood") || 
//$$                name.contains("flower") || name.contains("fern")) {
//$$                p = p.below();
//$$            } else {
//$$                break;
//$$            }
//$$        }
//$$        return p.getY();
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
//$$
//$$    /** نسخة مخصصة مع حد تحمّل متغير حسب البيئة */
//$$    private static boolean hasHeavyCanopy(ServerLevel level, BlockPos pos, int tolerance) {
//$$        int leafCount = 0;
//$$        for (int y = 1; y <= 8; y++) {
//$$            BlockState above = level.getBlockState(pos.above(y));
//$$            if (above.getBlock() instanceof LeavesBlock) {
//$$                leafCount++;
//$$            }
//$$        }
//$$        return leafCount >= tolerance;
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
//$$    private static void place2x2Saplings(ServerLevel level, BlockPos targetPos, Block sapling, int spacing, long currentTime, boolean shouldTerraform) {
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
//$$                    ForestGrowthData.get(level).addSapling(subPos, currentTime, shouldTerraform);
//$$                }
//$$            }
//$$        }
//$$    }

    // ==================== AREA CLEAR CHECK ====================
//$$    private static boolean isAreaClear(ServerLevel level, BlockPos pos, int radius) {
//$$        for (BlockPos checkPos : BlockPos.betweenClosed(pos.offset(-radius, -1, -radius), pos.offset(radius, 3, radius))) {
//$$            BlockState state = level.getBlockState(checkPos);
//$$            Block block = state.getBlock();
//$$            if (block instanceof RotatedPillarBlock || block instanceof SaplingBlock || block == Blocks.AZALEA || block == Blocks.MANGROVE_PROPAGULE) {
//$$                // التحقق من المسافة الصارمة (نصف قطر حقيقي)
//$$                double distSq = checkPos.distSqr(pos);
//$$                if (distSq < (radius * radius)) return false; 
//$$            }
//$$        }
//$$        return true;
//$$    }

    // ==================== SPACING ====================
//$$    public static int getRequiredSpacing(Block sapling) {
//$$        if (sapling == Blocks.OAK_SAPLING) return 5;
//$$        if (sapling == Blocks.BIRCH_SAPLING) return 5;
//$$        if (sapling == Blocks.SPRUCE_SAPLING) return 5;
//$$        if (sapling == Blocks.JUNGLE_SAPLING) return 5;
//$$        if (sapling == Blocks.ACACIA_SAPLING) return 6;
//$$        if (sapling == Blocks.DARK_OAK_SAPLING) return 6;
//$$        if (sapling == Blocks.MANGROVE_PROPAGULE) return 5;
//$$        if (sapling == Blocks.CHERRY_SAPLING) return 6;
//$$        if (sapling == Blocks.AZALEA) return 5;
//$$        if (sapling == Blocks.PALE_OAK_SAPLING) return 7;
//$$        if (sapling == Blocks.CRIMSON_FUNGUS || sapling == Blocks.WARPED_FUNGUS) return 5;
//$$        return 5; // Default (At least 5 blocks)
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
//$$        // السماح باستبدال الهواء أو النباتات الصغيرة أو الثلج
//$$        String blockName = net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(state.getBlock()).getPath();
//$$        boolean isReplaceable = state.isAir() || state.canBeReplaced() || 
//$$            blockName.contains("grass") || blockName.contains("fern") || 
//$$            blockName.contains("flower") || state.is(Blocks.SNOW);
//$$
//$$        return isReplaceable && (
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
//$$            if ((state.getBlock() instanceof RotatedPillarBlock || state.getBlock() instanceof LeavesBlock)
//$$                && isValidTree(level, checkPos)) {
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
//$$        // حماية: التأكد من عدم إرجاع شجيرة ميتة بالخطأ
//$$        if (name.contains("dead_bush")) return Optional.empty();
//$$        
//$$        return Optional.empty();
//$$    }

    // ==================== BIOME KEY LOOKUP ====================
//$$    public static Optional<ResourceKey<Biome>> getRelatedBiomeKey(ServerLevel level, BlockPos pos, Block sapling) {
//$$        Holder<Biome> currentBiome = level.getBiome(pos);
//$$        Optional<ResourceKey<Biome>> currentKeyOpt = currentBiome.unwrapKey();
//$$        
//$$        // --- حالة خاصة: البلوط في المستنقع ---
//$$        if (sapling == Blocks.OAK_SAPLING && currentKeyOpt.isPresent()) {
//$$            ResourceKey<Biome> currentKey = currentKeyOpt.get();
//$$            if (currentKey == Biomes.SWAMP || currentKey == Biomes.MANGROVE_SWAMP) {
//$$                return Optional.of(currentKey);
//$$            }
//$$        }
//$$
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
    public static void tickChunk(Object chunk, Object level) {}
    //#endif
}
