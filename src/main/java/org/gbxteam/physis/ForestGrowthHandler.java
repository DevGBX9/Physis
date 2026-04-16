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
//$$        // [10] WEATHER IMPACT
//$$        boolean isRaining = level.isRaining();
//$$        boolean isThundering = level.isThundering();
//$$        float tps = 20.0f;
//$$        //#if MC >= 1_20_04
//$$        tps = level.getServer().tickRateManager().tickrate();
//$$        //#endif
//$$
//$$        // Multiplier based on /tick rate. Standard is 20.
//$$        int speedMultiplier = Math.max(1, (int)(tps / 20.0f)); 
//$$        
//$$        // Natural growth interval: ~10 seconds normally, ~5 seconds in rain
//$$        int growthInterval = isRaining ? (100 / speedMultiplier) : (200 / speedMultiplier);
//$$        growthInterval = Math.max(1, growthInterval);
//$$
//$$        if (gameTime % growthInterval == 0) {
//$$            // Player-centric loop has been removed!
//$$            // Growth is now globally managed by tickChunk() for every loaded chunk.
//$$        }
//$$
//$$        // Periodic checks (Every 1 minute)
//$$        if (gameTime % 1200 == 0) {
//$$            runHealthChecks(level);
//$$            runCompostChecks(level);
//$$        }
//$$
//$$        // Thunder damage is now handled globally in tickChunk()
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
//$$        float tps = 20.0f;
//$$        //#if MC >= 1_20_04
//$$        tps = level.getServer().tickRateManager().tickrate();
//$$        //#endif
//$$        int speedMultiplier = Math.max(1, (int)(tps / 20.0f)); 
//$$        
//$$        // Thunder strikes randomly globally per chunk (very rare, approx 1 strike per 5 seconds per 1000 chunks)
//$$        if (level.isThundering() && level.getRandom().nextInt(100000 / speedMultiplier) == 0) {
//$$            BlockPos strikePos = chunk.getPos().getMiddleBlockPosition(0).offset(level.getRandom().nextInt(16) - 8, 0, level.getRandom().nextInt(16) - 8);
//$$            strikePos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, strikePos);
//$$            applyThunderDamage(level, strikePos);
//$$        }
//$$        
//$$        // 1/200 chance per tick means a chunk runs every 10 seconds on average (1/100 in rain = 5 seconds)
//$$        int runChance = isRaining ? 100 : 200;
//$$        runChance = Math.max(1, runChance / speedMultiplier);
//$$        
//$$        if (level.getRandom().nextInt(runChance) == 0) {
//$$            net.minecraft.world.level.ChunkPos pos = chunk.getPos();
//$$            BlockPos center = pos.getMiddleBlockPosition(0);
//$$            
//$$            // We run few attempts per chunk, because this fires for EVERY chunk globally.
//$$            int attempts = isRaining ? 2 : 1;
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
//$$            // نظام مراقبة: يعمل بشكل دوري لضبط توزيع الشجيرات والأعشاب النادرة
//$$            if (level.getRandom().nextInt(3) == 0) {
//$$                int ox2 = level.getRandom().nextInt(16) - 8;
//$$                int oz2 = level.getRandom().nextInt(16) - 8;
//$$                BlockPos monitorPos = center.offset(ox2, 0, oz2);
//$$                
//$$                // شجيرة bush: حد 3 بالمجموعة, 6 بلوكات فصل, نقل 10-30 بلوكة
//$$                monitorPlantDistribution(level, monitorPos, "minecraft:bush",       3, 6.0,  10, 30, 6);
//$$                // سرخس: حد 4 بالمجموعة, 5 بلوكات فصل, نقل 8-20 بلوكة
//$$                monitorPlantDistribution(level, monitorPos, "minecraft:fern",        4, 5.0,  8,  20, 5);
//$$                // عشب قصير (tall grass legacy): حد 4, فصل 4, نقل 6-15
//$$                monitorPlantDistribution(level, monitorPos, "minecraft:grass",       4, 4.0,  6,  15, 4);
//$$            }
//$$        }
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
//$$        Block targetBlock = net.minecraft.core.registries.BuiltInRegistries.BLOCK
//$$            .get(net.minecraft.resources.ResourceLocation.parse(blockId));
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
//$$        // إذا كانت الأعشاب مكتظة جداً (أكثر من ٨ في مساحة ٥×٥)، نزيل بعضها عشوائياً
//$$        if (isGrass && density > 8 && random.nextBoolean()) {
//$$            level.setBlock(pos, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 3);
//$$            return true; // تم التشذيب
//$$        }
//$$        // نظام التشذيب للشجيرات والأزهار أيضاً: منع تشكيل مجموعات تتجاوز الحد (٤ للشجيرات و ٣ للأزهار)
//$$        if ((isPlainBush && density >= 4 && random.nextBoolean()) || (isFlower && density >= 3 && random.nextBoolean())) {
//$$            level.setBlock(pos, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 3);
//$$            return true;
//$$        }
//$$        return false; // لا حاجة للتشذيب
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
//$$                    isVegetation = name.contains("grass") || name.contains("fern") || name.contains("flower") || name.contains("lily") || 
//$$                                   name.contains("mushroom") || name.contains("fungus") || name.contains("kelp") || 
//$$                                   name.contains("sugar_cane") || name.contains("bush") || name.equals("moss_carpet") || name.equals("moss_block") || 
//$$                                   name.contains("azalea") || name.contains("spore") || name.contains("bluet") || 
//$$                                   name.contains("dandelion") || name.contains("poppy") || name.contains("orchid") || 
//$$                                   name.contains("allium") || name.contains("tulip") || name.contains("daisy") || 
//$$                                   name.contains("peony") || name.contains("lilac") || name.contains("rose") || 
//$$                                   name.contains("sunflower") || name.contains("pickle") || name.contains("petal");
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
//$$            name.contains("pitcher") || name.equals("dead_bush") || name.contains("berry_bush")) {
//$$            return;
//$$        }
//$$        
//$$        BlockPos sourcePos = mut.immutable();
//$$        
//$$        // ═══════ تصنيف النبات ═══════
//$$        // كل نبتة لها قواعد انتشار مختلفة، لذلك نصنفها هنا
//$$        int density = 0;
//$$        boolean isGrass = name.equals("grass") || name.equals("short_grass") || name.equals("fern");  // أعشاب قصيرة
//$$        boolean isPlainBush = name.equals("bush");              // شجيرة minecraft:bush الزخرفية فقط
//$$        boolean isFireflyBush = name.contains("firefly_bush");  // شجيرة اليراعات (قرب الماء فقط)
//$$        boolean isPetal = name.contains("petal");               // بتلات الكرز الوردية
//$$        boolean isFungus = name.contains("mushroom") || name.contains("fungus");  // فطريات
//$$        boolean isWaterPlant = name.contains("kelp") || name.contains("seagrass") || name.contains("pickle"); // نباتات مائية
//$$        boolean isFlower = !isGrass && !isPlainBush && !isFireflyBush && !isPetal && !isFungus && !isWaterPlant; // أي نبتة أخرى تعتبر من الأزهار
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
//$$            if (random.nextFloat() > 0.30f * waterBoost) return;
//$$        } else if (isPlainBush) {
//$$            if (random.nextFloat() > 0.05f * waterBoost) return; // جعلنا الشجيرات أندر بكثير (٥٪ فرصة)
//$$        } else if (isFireflyBush) {
//$$            if (random.nextFloat() > 0.20f * waterBoost) return;
//$$        } else if (isPetal) {
//$$            if (random.nextFloat() > 0.35f * waterBoost) return;
//$$        } else {
//$$            if (random.nextFloat() > 0.015f * waterBoost) return;  // أزهار عادية (مبطأة جدا لتصبح أبطأ من انتشار الأشجار)
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
//$$                    // 50% chance to grow in place instead of spreading to a new block
//$$                    if (currentAmount < 4 && random.nextBoolean()) {
//$$                        level.setBlock(sourcePos, state.setValue(intProp, currentAmount + 1), 3);
//$$                        return; // Successfully grew in place!
//$$                    }
//$$                    // If we spread, randomize the new block's amount instead of forcing level 1
//$$                    state = state.setValue(intProp, 1 + random.nextInt(4));
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
//$$        //   عشب: ٦  |  شجيرة: ٤  |  يراعات: ١  |  بتلات: ٣  |  أزهار: ٣ (تكوّن مجموعات)
//$$        int maxDensity = isGrass ? 6 : (isFireflyBush ? 1 : (isPlainBush ? 4 : (isPetal ? 3 : (isFlower ? 3 : 2))));
//$$        int searchSpread = isGrass ? 5 : 4;
//$$        
//$$        if (density >= maxDensity) {
//$$            // الكثافة المحلية وصلت للحد الأقصى! نقوم بالتشذيب أولاً للتحكم في الحجم
//$$            manageVegetationBalance(level, sourcePos, density, isGrass, isPlainBush, isFlower, random);
//$$            
//$$            // نظام المستكشف: نسمح للنبتة بالقفز لمكان بعيد لبدء مجموعة جديدة متباعدة
//$$            float pioneerChance = isPlainBush ? 0.30f : (isFlower ? 0.15f : 0.05f);
//$$            if (random.nextFloat() < pioneerChance) {
//$$                searchSpread = isPlainBush ? 18 : (isFlower ? 24 : 8); // قفزة أبعد بكثير للشجيرات والأزهار لخلق تنويع!
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
//$$            if (isWaterPlant && !tState.is(Blocks.WATER)) continue;
//$$            if (!isWaterPlant && !tState.isAir()) continue;
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
//$$            if ((isPlainBush || isFlower) && density >= maxDensity) {
//$$                minSpacing = isFlower ? (10 + random.nextInt(11)) : 10; // الشجيرة 10, والأزهار 10-20 блоكات متباعدة
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
//$$        if (bestTarget != null && ((isGrass || isPlainBush) ? bestScore >= 0 : bestScore >= 1)) {
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
//$$        int petalCount = 0;
//$$        
//$$        // Scan a 5x5 area under the tree
//$$        for (BlockPos p : BlockPos.betweenClosed(surfaceCenter.offset(-2, -2, -2), surfaceCenter.offset(2, 2, 2))) {
//$$            if (level.getBlockState(p).is(net.minecraft.world.level.block.Blocks.PINK_PETALS)) {
//$$                petalCount++;
//$$            }
//$$        }
//$$        
//$$        // If exactly 0 petals exist, we drop some gracefully.
//$$        if (petalCount == 0) {
//$$            RandomSource random = level.getRandom();
//$$            if (random.nextFloat() < 0.3f) return; // Add a small delay/chance so it doesn't happen instantly 100% of the time.
//$$            
//$$            int drops = 3 + random.nextInt(4); // 3 to 6 petal clusters
//$$            for (int i = 0; i < drops; i++) {
//$$                int ox = random.nextInt(7) - 3; // -3 to 3
//$$                int oz = random.nextInt(7) - 3;
//$$                
//$$                BlockPos target = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, surfaceCenter.offset(ox, 0, oz));
//$$                BlockState targetState = level.getBlockState(target);
//$$                
//$$                if (targetState.canBeReplaced() || targetState.isAir()) {
//$$                    BlockState soil = level.getBlockState(target.below());
//$$                    if (soil.is(net.minecraft.world.level.block.Blocks.GRASS_BLOCK) || soil.is(net.minecraft.world.level.block.Blocks.DIRT) || soil.is(net.minecraft.world.level.block.Blocks.PODZOL)) {
//$$                        int amount = 2 + random.nextInt(3); // Levels 2 to 4 (guaranteed to not be level 1)
//$$                        BlockState petalState = net.minecraft.world.level.block.Blocks.PINK_PETALS.defaultBlockState();
//$$                        for (net.minecraft.world.level.block.state.properties.Property<?> prop : petalState.getProperties()) {
//$$                            String pName = prop.getName().toLowerCase();
//$$                            if (pName.contains("amount") || pName.contains("flower")) {
//$$                                // Suppress generic warnings and forcefully set it
//$$                                @SuppressWarnings("unchecked")
//$$                                net.minecraft.world.level.block.state.properties.Property<Integer> intProp = (net.minecraft.world.level.block.state.properties.Property<Integer>) prop;
//$$                                petalState = petalState.setValue(intProp, amount);
//$$                            } else if (pName.contains("facing") || pName.contains("direction")) {
//$$                                @SuppressWarnings("unchecked")
//$$                                net.minecraft.world.level.block.state.properties.Property<net.minecraft.core.Direction> dirProp = (net.minecraft.world.level.block.state.properties.Property<net.minecraft.core.Direction>) prop;
//$$                                petalState = petalState.setValue(dirProp, net.minecraft.core.Direction.Plane.HORIZONTAL.getRandomDirection(random));
//$$                            }
//$$                        }
//$$                        level.setBlock(target, petalState, 3);
//$$                    }
//$$                }
//$$            }
//$$        }
//$$    }
//$$
    // ╔══════════════════════════════════════════════════════════════════╗
    // ║             القسم ٧: نظام انتشار الأشجار (الشتلات)             ║
    // ║   يبحث عن أشجار على حواف الغابات وينشر شتلات جديدة            ║
    // ║   معدل: ١-٢ شتلة كل نصف يوم ماينكرافت (بطيء جداً)            ║
    // ╚══════════════════════════════════════════════════════════════════╝
//$$    private static void processEdgeExpansion(ServerLevel level, BlockPos searchPos) {
//$$        if (!level.isLoaded(searchPos)) return;
//$$        RandomSource random = level.getRandom();
//$$        
//$$        // [Nerf] Extremely slow global tree spreading: roughly 1 to 2 saplings per half Minecraft day (5% base chance to even attempt check)
//$$        if (random.nextFloat() > 0.05f) return;
//$$
//$$        // Step 1: Find a tree near this position (search a spiral)
//$$        BlockPos treePos = findNearbyTree(level, searchPos, 12);
//$$        if (treePos == null) return;
//$$        
//$$        // [NEW] Feature: Check for Cherry Blossom petal dropping
//$$        tryCherryPetalDrop(level, treePos);
//$$
//$$        // Step 2: Check if this tree is on the forest EDGE
//$$        int scanRadius = 8; // How far to look for forest density in each direction
//$$        int forestedDirs = 0;
//$$        List<int[]> openDirections = new ArrayList<>();
//$$
//$$        for (int[] dir : DIRECTIONS) {
//$$            boolean hasForest = false;
//$$            // Sample 3 points along this direction at increasing distances
//$$            for (int dist = 3; dist <= scanRadius; dist += 3) {
//$$                BlockPos checkPos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, 
//$$                    treePos.offset(dir[0] * dist, 0, dir[1] * dist)).below();
//$$                BlockState state = level.getBlockState(checkPos);
//$$                if (state.getBlock() instanceof RotatedPillarBlock || state.getBlock() instanceof LeavesBlock) {
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
//$$        // PIONEER TREE: Isolated trees (0-1 forested neighbors) spread slowly nearby
//$$        if (forestedDirs <= 1) {
//$$            // Pioneer growth is rare (25% chance) to simulate slow natural seeding
//$$            if (random.nextFloat() < 0.25f) {
//$$                double angle = random.nextDouble() * 2 * Math.PI;
//$$                int dist = 2 + random.nextInt(4); // 2-5 blocks close range
//$$                int ox = (int) (Math.cos(angle) * dist);
//$$                int oz = (int) (Math.sin(angle) * dist);
//$$                BlockPos targetPos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
//$$                    treePos.offset(ox, 0, oz));
//$$                plantAtPosition(level, targetPos, treePos);
//$$            }
//$$            return;
//$$        }
//$$
//$$        // EDGE TREE: Must have at least 2 forested directions AND at least 1 open direction
//$$        if (openDirections.isEmpty()) return;
//$$
//$$        // Step 3: Choose an open direction to expand into (with wind influence)
//$$        int[] chosenDir;
//$$        if (openDirections.size() > 1 && random.nextFloat() < 0.4f) {
//$$            // 40% chance: pick the direction closest to wind angle for natural drift
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
//$$        // Step 4: Calculate target position (just outside the forest edge)
//$$        int spreadDist = 3 + random.nextInt(5); // 3-7 blocks from the edge tree
//$$        // Add some randomness perpendicular to the direction for a natural look
//$$        int perpX = (int) ((random.nextFloat() - 0.5f) * 4);
//$$        int perpZ = (int) ((random.nextFloat() - 0.5f) * 4);
//$$        BlockPos targetPos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
//$$            treePos.offset(chosenDir[0] * spreadDist + perpX, 0, chosenDir[1] * spreadDist + perpZ));
//$$
//$$        // Step 5: Apply all smart checks and plant
//$$        plantAtPosition(level, targetPos, treePos);
//$$    }
//$$
//$$    private static BlockPos findNearbyTree(ServerLevel level, BlockPos center, int maxRadius) {
//$$        RandomSource random = level.getRandom();
//$$        // Random sampling method (faster than spiral)
//$$        for (int attempt = 0; attempt < 15; attempt++) {
//$$            int ox = random.nextInt(maxRadius * 2) - maxRadius;
//$$            int oz = random.nextInt(maxRadius * 2) - maxRadius;
//$$            BlockPos checkPos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, center.offset(ox, 0, oz)).below();
//$$            BlockState state = level.getBlockState(checkPos);
//$$            if (state.getBlock() instanceof RotatedPillarBlock || state.getBlock() instanceof LeavesBlock) {
//$$                return checkPos; // Found a log or leaves = found a tree
//$$            }
//$$        }
//$$        return null;
//$$    }
//$$
//$$    private static void plantAtPosition(ServerLevel level, BlockPos targetPos, BlockPos sourceTreePos) {
//$$        if (!level.isLoaded(targetPos)) return;
//$$
//$$        // [4] TERRAIN CHECK
//$$        if (!isTerrainFlat(level, targetPos)) return;
//$$
//$$        // [5] CANOPY DENSITY
//$$        if (hasHeavyCanopy(level, targetPos)) return;
//$$
//$$        // Determine sapling type from the source edge tree
//$$        BlockState sourceState = level.getBlockState(sourceTreePos);
//$$        Optional<Block> saplingOpt = getRelatedSapling(sourceState.getBlock());
//$$        
//$$        // If can't determine from source tree, use biome-aware selection
//$$        if (saplingOpt.isEmpty()) {
//$$            saplingOpt = determineSapling(level, targetPos);
//$$        }
//$$        if (saplingOpt.isEmpty()) return;
//$$
//$$        Block sapling = saplingOpt.get();
//$$
//$$        // [1] DYNAMIC BIOME INVASION: Trees are now allowed to invade any biome.
//$$        // The strict biome check has been removed.
//$$        int spacing = getRequiredSpacing(sapling);
//$$        boolean needs2x2 = (sapling == Blocks.DARK_OAK_SAPLING || sapling == Blocks.PALE_OAK_SAPLING);
//$$        long currentTime = level.getGameTime();
//$$
//$$        // [2] LIGHT CHECK
//$$        if (!hasAdequateLight(level, targetPos, sapling)) return;
//$$
//$$        // [9] SOIL FERTILITY
//$$        float fertilityBonus = getSoilFertility(level, targetPos);
//$$        if (level.getRandom().nextFloat() > fertilityBonus) return;
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

    // ╔══════════════════════════════════════════════════════════════════╗
    // ║         القسم ٨: فحوصات الصحة والتسميد (صيانة الشتلات)         ║
    // ║   يفحص الشتلات المزروعة: هل نمت؟ هل ماتت؟ هل تحتاج تنظيف؟    ║
    // ╚══════════════════════════════════════════════════════════════════╝
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
//$$            if (currentTime - deathTime >= 300) {
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
//$$                        if (r < 0.15f) {
//$$                            String[] flowers = {"dandelion", "poppy", "oxeye_daisy", "azure_bluet", "cornflower"};
//$$                            blockToPlace = flowers[random.nextInt(flowers.length)];
//$$                        } else if (r < 0.25f) {
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
//$$                int y = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos.offset(x, 0, z)).getY();
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
