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
//$$ import net.minecraft.server.level.ServerLevel;
//$$ import net.minecraft.world.level.block.Block;
//$$ import net.minecraft.world.level.block.LeavesBlock;
//$$ import net.minecraft.world.level.block.RotatedPillarBlock;
//$$ import net.minecraft.world.level.block.state.BlockState;
//#endif

// ╔══════════════════════════════════════════════════════════════════╗
// ║       نظام التحقق من صحة الأشجار (Tree Validator)              ║
// ║   يفحص ما إذا كان بلوك الخشب ينتمي لشجرة حقيقية أم مجرد      ║
// ║   بلوك وضعه لاعب. يُستخدم قبل زراعة الشتلات وتساقط البتلات    ║
// ╚══════════════════════════════════════════════════════════════════╝
public class TreeValidator {

    //#if MC >= 260100

    // ═══════════════════════════════════════════════════════
    // الثوابت القابلة للتعديل (Constants)
    // ═══════════════════════════════════════════════════════

    /** الحد الأدنى لعدد بلوكات الخشب المتتالية لاعتبارها جذع شجرة */
//$$    public static final int MIN_TRUNK_HEIGHT = 2;

    /** الحد الأقصى للبحث عن ارتفاع الجذع (حماية من الحلقات اللانهائية) */
//$$    public static final int MAX_TRUNK_SCAN = 20;

    /** نصف قطر البحث عن أوراق الشجر حول قمة الجذع (أفقياً) */
//$$    public static final int LEAF_SEARCH_RADIUS_XZ = 3;

    /** نطاق البحث عن أوراق الشجر تحت وفوق قمة الجذع */
//$$    public static final int LEAF_SEARCH_BELOW = 1;
//$$    public static final int LEAF_SEARCH_ABOVE = 5;

    // ═══════════════════════════════════════════════════════
    // الدالة الرئيسية: isValidTree
    // ═══════════════════════════════════════════════════════

    /**
     * يتحقق ما إذا كان الموقع يحتوي على شجرة حقيقية وليس مجرد بلوكة خشب.
     * 
     * الشروط:
     *   ١. وجود MIN_TRUNK_HEIGHT بلوكات خشب على الأقل فوق بعضها (جذع حقيقي)
     *   ٢. وجود أوراق شجر فوق الجذع مطابقة لنفس نوع الخشب
     *   ٣. الأوراق يجب أن تكون طبيعية (persistent = false يعني مولّدة من العالم)
     *
     * @param level عالم السيرفر
     * @param pos   موقع أي بلوك خشب أو ورقة شجر يُراد فحصها
     * @return true إذا كانت شجرة حقيقية، false إذا كانت خشب وضعه لاعب
     */
//$$    public static boolean isValidTree(ServerLevel level, BlockPos pos) {
//$$        // قد يكون الموقع المرسل عبارة عن أوراق شجر (خاصة في الأشجار العريضة مثل الكرز)
//$$        // لذا نبحث عن الجذع المرتبط أولاً
//$$        BlockPos logPos = findAssociatedLog(level, pos);
//$$        if (logPos == null) return false;
//$$
//$$        // الخطوة ١: إيجاد قاعدة الجذع (ننزل حتى نجد أول بلوك ليس خشباً)
//$$        BlockPos basePos = findTrunkBase(level, logPos);
//$$        
//$$        // الخطوة ٢: عدّ بلوكات الخشب المتتالية وتحديد النوع
//$$        TrunkInfo trunk = scanTrunk(level, basePos);
//$$        
//$$        // الشرط ١: يجب أن يكون هناك MIN_TRUNK_HEIGHT بلوكات خشب على الأقل
//$$        if (trunk.height < MIN_TRUNK_HEIGHT) return false;
//$$        
//$$        // الخطوة ٣: البحث عن أوراق شجر مطابقة ومولّدة طبيعياً
//$$        return hasMatchingNaturalLeaves(level, basePos, trunk);
//$$    }

    // ═══════════════════════════════════════════════════════
    // الدوال المساعدة
    // ═══════════════════════════════════════════════════════

    /**
     * يبحث عن أقرب بلوك خشب متصل بالموقع.
     * يفيد عندما يكون الموقع المدخل هو أوراق شجر (مثل عند استخدام Heightmap).
     */
//$$    public static BlockPos findAssociatedLog(ServerLevel level, BlockPos pos) {
//$$        BlockState state = level.getBlockState(pos);
//$$        if (state.getBlock() instanceof RotatedPillarBlock) {
//$$            String name = BuiltInRegistries.BLOCK.getKey(state.getBlock()).getPath();
//$$            if (isTreeLog(name)) return pos;
//$$        }
//$$        
//$$        // بحث حلزوني للأسفل (بحثاً عن الجذع تحت الأوراق)
//$$        for (int y = 0; y <= 14; y++) {
//$$            for (int r = 0; r <= 3; r++) {
//$$                for (int x = -r; x <= r; x++) {
//$$                    for (int z = -r; z <= r; z++) {
//$$                        if (Math.abs(x) != r && Math.abs(z) != r) continue;
//$$                        BlockPos checkPos = pos.offset(x, -y, z);
//$$                        BlockState checkState = level.getBlockState(checkPos);
//$$                        if (checkState.getBlock() instanceof RotatedPillarBlock) {
//$$                            String name = BuiltInRegistries.BLOCK.getKey(checkState.getBlock()).getPath();
//$$                            if (isTreeLog(name)) return checkPos;
//$$                        }
//$$                    }
//$$                }
//$$            }
//$$        }
//$$        return null;
//$$    }

    /**
     * يجد قاعدة الجذع بالنزول للأسفل حتى آخر بلوكة خشب متصلة.
     */
//$$    public static BlockPos findTrunkBase(ServerLevel level, BlockPos pos) {
//$$        BlockPos basePos = pos;
//$$        while (basePos.getY() > -64 && level.getBlockState(basePos.below()).getBlock() instanceof RotatedPillarBlock) {
//$$            String belowName = BuiltInRegistries.BLOCK.getKey(level.getBlockState(basePos.below()).getBlock()).getPath();
//$$            if (!isTreeLog(belowName)) break;
//$$            basePos = basePos.below();
//$$        }
//$$        return basePos;
//$$    }

    /**
     * يعدّ بلوكات الخشب المتتالية من القاعدة للأعلى ويستخرج نوع الخشب.
     */
//$$    public static TrunkInfo scanTrunk(ServerLevel level, BlockPos basePos) {
//$$        int logCount = 0;
//$$        BlockPos current = basePos;
//$$        String logSpecies = null;
//$$        
//$$        while (logCount < MAX_TRUNK_SCAN) {
//$$            BlockState currentState = level.getBlockState(current);
//$$            if (!(currentState.getBlock() instanceof RotatedPillarBlock)) break;
//$$            
//$$            String blockName = BuiltInRegistries.BLOCK.getKey(currentState.getBlock()).getPath();
//$$            if (!isTreeLog(blockName)) break;
//$$            
//$$            if (logSpecies == null) {
//$$                logSpecies = extractSpecies(blockName);
//$$            }
//$$            
//$$            logCount++;
//$$            current = current.above();
//$$        }
//$$        
//$$        return new TrunkInfo(logCount, logSpecies);
//$$    }

    /**
     * يبحث عن أوراق شجر مطابقة للنوع ومولّدة طبيعياً حول قمة الجذع.
     */
//$$    public static boolean hasMatchingNaturalLeaves(ServerLevel level, BlockPos basePos, TrunkInfo trunk) {
//$$        BlockPos topOfTrunk = basePos.above(trunk.height - 1);
//$$        
//$$        for (BlockPos leafCheck : BlockPos.betweenClosed(
//$$                topOfTrunk.offset(-LEAF_SEARCH_RADIUS_XZ, -LEAF_SEARCH_BELOW, -LEAF_SEARCH_RADIUS_XZ),
//$$                topOfTrunk.offset(LEAF_SEARCH_RADIUS_XZ, LEAF_SEARCH_ABOVE, LEAF_SEARCH_RADIUS_XZ))) {
//$$            BlockState leafState = level.getBlockState(leafCheck);
//$$            if (!(leafState.getBlock() instanceof LeavesBlock)) continue;
//$$            
//$$            // التحقق من أن الأوراق طبيعية (persistent = false)
//$$            // الأوراق التي يضعها اللاعب يدوياً تكون persistent = true
//$$            try {
//$$                if (leafState.getValue(LeavesBlock.PERSISTENT)) continue;
//$$            } catch (Exception e) {
//$$                // لبعض بلوكات النيذر مثل nether_wart_block التي لا تملك خاصية persistent
//$$                // إذا كانت فوق جذع الفطر، نعتبرها صالحة.
//$$                String leafName = BuiltInRegistries.BLOCK.getKey(leafState.getBlock()).getPath();
//$$                if (!leafName.contains("wart")) continue;
//$$            }
//$$            
//$$            // بمجرد إيجاد ورقة شجر طبيعية متصلة أو فوق الجذع، نعتبرها شجرة صالحة.
//$$            // إزالة شرط تطابق الأسماء يحل مشكلة أشجار الأزاليا (جذع بلوط + ورق أزاليا) وغيرها.
//$$            return true;
//$$        }
//$$        
//$$        return false;
//$$    }

    /**
     * يحدد ما إذا كان اسم البلوك ينتمي لخشب شجرة (وليس بلوك دوار آخر مثل hay_block أو basalt).
     */
//$$    public static boolean isTreeLog(String blockName) {
//$$        return blockName.contains("log") || blockName.contains("wood") || blockName.contains("stem") || blockName.contains("hyphae");
//$$    }

    /**
     * يستخرج اسم النوع من اسم بلوك الخشب.
     * مثال: "oak_log" → "oak", "stripped_birch_log" → "birch"
     */
//$$    public static String extractSpecies(String blockName) {
//$$        return blockName
//$$            .replace("_log", "")
//$$            .replace("_wood", "")
//$$            .replace("stripped_", "")
//$$            .replace("_stem", "")
//$$            .replace("_hyphae", "");
//$$    }

    /**
     * يحمل معلومات عن جذع الشجرة بعد الفحص.
     */
//$$    public static class TrunkInfo {
//$$        public final int height;
//$$        public final String species;
//$$        
//$$        public TrunkInfo(int height, String species) {
//$$            this.height = height;
//$$            this.species = species;
//$$        }
//$$    }

    //#endif
}
