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
//$$ import net.minecraft.world.level.block.RotatedPillarBlock;
//$$ import net.minecraft.world.level.block.state.BlockState;
//$$ import net.minecraft.world.level.levelgen.Heightmap;
//$$ import java.util.Optional;
//#endif

// ╔══════════════════════════════════════════════════════════════════╗
// ║       نظام ملفات الغابات البيئية (Biome Forest Profiles)        ║
// ║   يحتوي على بارامترات مُعايَرة لكل بيئة حيوية في ماينكرافت     ║
// ║   لضمان أن الغابات المتولدة تطابق نمط الفانيلا تماماً          ║
// ╚══════════════════════════════════════════════════════════════════╝
public class BiomeForestProfile {

    //#if MC >= 260100

//$$    public final String name;
//$$    public final int minTreeSpacing;     // أقل مسافة بين جذوع الأشجار
//$$    public final int maxTreeSpacing;     // أقصى مسافة بين جذوع الأشجار
//$$    public final int interiorThreshold;  // عدد الاتجاهات المغطاة لتعتبر الشجرة داخلية
//$$    public final float edgeChance;       // احتمال النشر لأشجار الحافة
//$$    public final float pioneerChance;    // احتمال النشر للأشجار الرائدة
//$$    public final int spreadMin;          // أقل مسافة انتشار
//$$    public final int spreadMax;          // أقصى مسافة انتشار
//$$    public final int scanRadius;         // نصف قطر المسح لتصنيف الشجرة
//$$    public final float mega2x2Chance;    // فرصة زراعة شتلات 2x2 عملاقة
//$$    public final int canopyTolerance;    // عدد طبقات الأوراق المسموح بها فوق الشتلة
//$$    public final int maxLocalDensity;    // أقصى عدد أشجار في منطقة 32x32

//$$    private BiomeForestProfile(String name, int minTreeSpacing, int maxTreeSpacing,
//$$                               int interiorThreshold, float edgeChance, float pioneerChance,
//$$                               int spreadMin, int spreadMax, int scanRadius,
//$$                               float mega2x2Chance, int canopyTolerance, int maxLocalDensity) {
//$$        this.name = name;
//$$        this.minTreeSpacing = minTreeSpacing;
//$$        this.maxTreeSpacing = maxTreeSpacing;
//$$        this.interiorThreshold = interiorThreshold;
//$$        this.edgeChance = edgeChance;
//$$        this.pioneerChance = pioneerChance;
//$$        this.spreadMin = spreadMin;
//$$        this.spreadMax = spreadMax;
//$$        this.scanRadius = scanRadius;
//$$        this.mega2x2Chance = mega2x2Chance;
//$$        this.canopyTolerance = canopyTolerance;
//$$        this.maxLocalDensity = maxLocalDensity;
//$$    }

    // ═══════════════════════════════════════════════════════════════
    //  ملفات الغابات المُعايَرة على أساس بارامترات فانيلا ماينكرافت
    //  Vanilla-calibrated forest profiles
    // ═══════════════════════════════════════════════════════════════

    // غابة عادية: كثافة متوسطة، خليط بلوط وبيرش
    // Vanilla: count=10, ~6-8 trees/chunk after failures
//$$    public static final BiomeForestProfile FOREST = new BiomeForestProfile(
//$$        "forest", 4, 6, 6, 0.025f, 0.03f, 5, 8, 10, 0.0f, 3, 10);

    // غابة أزهار: أقل كثافة لإفساح المجال للأزهار
    // Vanilla: count=10 with extra_chance=2, ~4-6 trees/chunk
//$$    public static final BiomeForestProfile FLOWER_FOREST = new BiomeForestProfile(
//$$        "flower_forest", 5, 7, 5, 0.02f, 0.025f, 6, 10, 10, 0.0f, 3, 7);

    // غابة بيرش: توزيع منتظم جداً
    // Vanilla: count=10, ~6-8 trees/chunk
//$$    public static final BiomeForestProfile BIRCH_FOREST = new BiomeForestProfile(
//$$        "birch_forest", 5, 6, 6, 0.02f, 0.025f, 6, 9, 10, 0.0f, 3, 8);

    // الغابة المظلمة: كثيفة جداً، أشجار بلوط مظلم 2x2 دائماً، تاج متصل
    // Vanilla: count=16, ~12-18 trees/chunk, massive canopy
//$$    public static final BiomeForestProfile DARK_FOREST = new BiomeForestProfile(
//$$        "dark_forest", 2, 3, 7, 0.045f, 0.06f, 3, 5, 8, 1.0f, 6, 25);

    // تايغا: مجموعات صنوبر مع فجوات
    // Vanilla: count=10, ~8-10 trees/chunk
//$$    public static final BiomeForestProfile TAIGA = new BiomeForestProfile(
//$$        "taiga", 4, 5, 5, 0.025f, 0.035f, 5, 7, 10, 0.20f, 3, 12);

    // تايغا صنوبر عملاق: أشجار عملاقة سائدة
    // Vanilla: count=10, mega spruce dominant, ~5-8 trees/chunk
//$$    public static final BiomeForestProfile OLD_GROWTH_SPRUCE = new BiomeForestProfile(
//$$        "old_growth_spruce", 5, 7, 5, 0.02f, 0.02f, 6, 9, 12, 0.50f, 4, 10);

    // تايغا صنوبر قديم
    // Vanilla: similar to spruce but slightly different mix
//$$    public static final BiomeForestProfile OLD_GROWTH_PINE = new BiomeForestProfile(
//$$        "old_growth_pine", 5, 7, 5, 0.02f, 0.02f, 6, 9, 12, 0.40f, 4, 9);

    // أدغال: كثيفة جداً، متعددة الطبقات، بعض الأشجار العملاقة
    // Vanilla: count=50!, ~20-30 trees/chunk
//$$    public static final BiomeForestProfile JUNGLE = new BiomeForestProfile(
//$$        "jungle", 3, 4, 6, 0.04f, 0.05f, 4, 7, 10, 0.15f, 5, 20);

    // أدغال متفرقة
    // Vanilla: count=10, ~4-6 trees/chunk
//$$    public static final BiomeForestProfile SPARSE_JUNGLE = new BiomeForestProfile(
//$$        "sparse_jungle", 5, 8, 4, 0.015f, 0.02f, 7, 11, 12, 0.05f, 3, 6);

    // سافانا: متفرقة جداً، أشجار أكاسيا معزولة
    // Vanilla: count=2, ~1-2 trees/chunk
//$$    public static final BiomeForestProfile SAVANNA = new BiomeForestProfile(
//$$        "savanna", 12, 20, 3, 0.008f, 0.01f, 10, 18, 16, 0.0f, 1, 3);

    // بستان الكرز: مفتوح، مسافات واسعة
    // Vanilla: count=10 with extra_chance=3, ~3-5 trees/chunk
//$$    public static final BiomeForestProfile CHERRY_GROVE = new BiomeForestProfile(
//$$        "cherry_grove", 6, 8, 5, 0.015f, 0.02f, 7, 11, 12, 0.0f, 2, 5);

    // الحديقة الشاحبة: كثيفة ومظلمة، بلوط شاحب 2x2 دائماً
    // Vanilla: count=16, ~12-15 trees/chunk
//$$    public static final BiomeForestProfile PALE_GARDEN = new BiomeForestProfile(
//$$        "pale_garden", 3, 4, 7, 0.035f, 0.045f, 4, 6, 8, 1.0f, 6, 18);

    // سهول ومروج: نادر جداً، لكن لا يزال ممكناً
    // Vanilla: count=0, extra_chance=0.05, ~0-1 trees/chunk
//$$    public static final BiomeForestProfile PLAINS = new BiomeForestProfile(
//$$        "plains", 15, 25, 2, 0.008f, 0.01f, 16, 28, 16, 0.0f, 1, 2);

    // تايغا ثلجية
    // Vanilla: count=10, ~6-8 trees/chunk
//$$    public static final BiomeForestProfile SNOWY_TAIGA = new BiomeForestProfile(
//$$        "snowy_taiga", 4, 5, 5, 0.02f, 0.03f, 5, 8, 10, 0.15f, 3, 10);

    // بستان جبلي: صنوبر متوسط الكثافة
    // Vanilla: count=10 with chance=2, ~5-6 trees/chunk
//$$    public static final BiomeForestProfile GROVE = new BiomeForestProfile(
//$$        "grove", 5, 6, 5, 0.015f, 0.02f, 6, 9, 10, 0.10f, 3, 7);

    // غابة عاصفة: كثافة متوسطة
//$$    public static final BiomeForestProfile WINDSWEPT = new BiomeForestProfile(
//$$        "windswept", 5, 6, 5, 0.02f, 0.025f, 6, 9, 10, 0.0f, 3, 7);

    // ملف افتراضي لأي بيئة غير معرّفة
//$$    public static final BiomeForestProfile DEFAULT = new BiomeForestProfile(
//$$        "default", 5, 6, 6, 0.02f, 0.03f, 6, 8, 10, 0.0f, 3, 8);

    // ═══════════════════════════════════════════════════════════════
    //  البحث عن الملف المناسب حسب البيئة الحيوية
    // ═══════════════════════════════════════════════════════════════

//$$    public static BiomeForestProfile getProfile(ServerLevel level, BlockPos pos) {
//$$        Holder<Biome> biomeHolder = level.getBiome(pos);
//$$        Optional<ResourceKey<Biome>> keyOpt = biomeHolder.unwrapKey();
//$$        if (keyOpt.isEmpty()) return DEFAULT;
//$$        ResourceKey<Biome> key = keyOpt.get();
//$$
//$$        if (key == Biomes.FOREST) return FOREST;
//$$        if (key == Biomes.FLOWER_FOREST) return FLOWER_FOREST;
//$$        if (key == Biomes.BIRCH_FOREST || key == Biomes.OLD_GROWTH_BIRCH_FOREST) return BIRCH_FOREST;
//$$        if (key == Biomes.DARK_FOREST) return DARK_FOREST;
//$$        if (key == Biomes.TAIGA) return TAIGA;
//$$        if (key == Biomes.OLD_GROWTH_SPRUCE_TAIGA) return OLD_GROWTH_SPRUCE;
//$$        if (key == Biomes.OLD_GROWTH_PINE_TAIGA) return OLD_GROWTH_PINE;
//$$        if (key == Biomes.SNOWY_TAIGA) return SNOWY_TAIGA;
//$$        if (key == Biomes.GROVE) return GROVE;
//$$        if (key == Biomes.JUNGLE || key == Biomes.BAMBOO_JUNGLE) return JUNGLE;
//$$        if (key == Biomes.SPARSE_JUNGLE) return SPARSE_JUNGLE;
//$$        if (key == Biomes.SAVANNA || key == Biomes.SAVANNA_PLATEAU || key == Biomes.WINDSWEPT_SAVANNA) return SAVANNA;
//$$        if (key == Biomes.CHERRY_GROVE) return CHERRY_GROVE;
//$$        if (key == Biomes.PALE_GARDEN) return PALE_GARDEN;
//$$        if (key == Biomes.PLAINS || key == Biomes.MEADOW || key == Biomes.SUNFLOWER_PLAINS) return PLAINS;
//$$        if (key == Biomes.WINDSWEPT_FOREST || key == Biomes.WINDSWEPT_HILLS || key == Biomes.WINDSWEPT_GRAVELLY_HILLS) return WINDSWEPT;
//$$        return DEFAULT;
//$$    }

    // ═══════════════════════════════════════════════════════════════
    //  دوال مساعدة
    // ═══════════════════════════════════════════════════════════════

    /** يولد مسافة عشوائية بين الأشجار ضمن نطاق الملف */
//$$    public int randomSpacing(RandomSource random) {
//$$        return minTreeSpacing;
//$$    }

    /** يولد مسافة انتشار عشوائية ضمن نطاق الملف */
//$$    public int randomSpreadDist(RandomSource random) {
//$$        return spreadMin + random.nextInt(spreadMax - spreadMin + 1);
//$$    }

    /**
     * يحدد ما إذا كان يجب زراعة شتلات 2x2 بناءً على الملف ونوع الشجرة.
     * البلوط المظلم والشاحب دائماً 2x2 (متطلب فانيلا).
     * الصنوبر والأدغال يحصلون على فرصة بناءً على الملف.
     */
//$$    public boolean shouldPlant2x2(Block sapling, RandomSource random) {
//$$        if (sapling == Blocks.DARK_OAK_SAPLING || sapling == Blocks.PALE_OAK_SAPLING) {
//$$            return true;
//$$        }
//$$        if (sapling == Blocks.SPRUCE_SAPLING || sapling == Blocks.JUNGLE_SAPLING) {
//$$            return random.nextFloat() < mega2x2Chance;
//$$        }
//$$        return false;
//$$    }

    /**
     * يتحقق ما إذا كانت المنطقة المحلية قد وصلت للحد الأقصى من كثافة الأشجار.
     * يأخذ عينات من 24 موقع عشوائي في نطاق 32x32 لتقدير الكثافة بكفاءة.
     */
//$$    public boolean isLocalAreaSaturated(ServerLevel level, BlockPos center) {
//$$        int treeCount = 0;
//$$        int sampleRadius = 16;
//$$        RandomSource random = level.getRandom();
//$$
//$$        for (int i = 0; i < 24; i++) {
//$$            int x = random.nextInt(sampleRadius * 2 + 1) - sampleRadius;
//$$            int z = random.nextInt(sampleRadius * 2 + 1) - sampleRadius;
//$$            BlockPos p = level.getHeightmapPos(
//$$                Heightmap.Types.MOTION_BLOCKING, center.offset(x, 0, z)).below();
//$$            BlockState state = level.getBlockState(p);
//$$            String n = BuiltInRegistries.BLOCK.getKey(state.getBlock()).getPath();
//$$            if (n.contains("log") || n.contains("stem")) {
//$$                treeCount++;
//$$            }
//$$        }
//$$        // 24 عينة من منطقة 33x33
//$$        // إذا وجدنا (maxLocalDensity / 3) أشجار في العينات، المنطقة مشبعة على الأرجح
//$$        return treeCount >= Math.max(2, maxLocalDensity / 3);
//$$    }

    //#endif
}
