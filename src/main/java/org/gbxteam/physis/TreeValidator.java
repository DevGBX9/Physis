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
//$$    public static final int MIN_TRUNK_HEIGHT = 3;

    // ═══════════════════════════════════════════════════════
    // الدالة الرئيسية: isValidTree
    // ═══════════════════════════════════════════════════════

    /**
     * يتحقق ما إذا كان الموقع يحتوي على شجرة حقيقية ومكتملة.
     * يستخدم خوارزمية BFS لمسح هيكل الشجرة بالكامل وتحليل جذورها وأوراقها.
     * 
     * @param level عالم السيرفر
     * @param pos   موقع أي بلوك خشب أو ورقة شجر يُراد فحصها
     * @return true إذا كانت شجرة طبيعية حقيقية، false إذا كانت مبنى أو غير مكتملة
     */
//$$    public static boolean isValidTree(ServerLevel level, BlockPos pos) {
//$$        BlockPos startLog = findAssociatedLog(level, pos);
//$$        if (startLog == null) return false;
//$$        
//$$        java.util.Set<BlockPos> visitedLogs = new java.util.HashSet<>();
//$$        java.util.Queue<BlockPos> queue = new java.util.LinkedList<>();
//$$        
//$$        queue.add(startLog);
//$$        visitedLogs.add(startLog);
//$$        
//$$        int minY = startLog.getY();
//$$        int maxY = startLog.getY();
//$$        int naturalLeavesCount = 0;
//$$        int playerLeavesCount = 0;
//$$        
//$$        // مسح الشجرة بالكامل بحد أقصى 1024 بلوك خشب
//$$        // تم رفع الحد لدعم أشجار المانجروف العملاقة والمائية التي تمتلك شبكة جذور هائلة
//$$        while (!queue.isEmpty() && visitedLogs.size() < 1024) {
//$$            BlockPos current = queue.poll();
//$$            
//$$            if (current.getY() < minY) minY = current.getY();
//$$            if (current.getY() > maxY) maxY = current.getY();
//$$            
//$$            // فحص الجيران في جميع الاتجاهات الـ 26 (مكعب 3x3)
//$$            for (int dy = -1; dy <= 1; dy++) {
//$$                for (int dx = -1; dx <= 1; dx++) {
//$$                    for (int dz = -1; dz <= 1; dz++) {
//$$                        if (dx == 0 && dy == 0 && dz == 0) continue;
//$$                        
//$$                        BlockPos neighbor = current.offset(dx, dy, dz);
//$$                        BlockState neighborState = level.getBlockState(neighbor);
//$$                        
//$$                        if (isTreeLog(neighborState)) {
//$$                            if (!visitedLogs.contains(neighbor)) {
//$$                                visitedLogs.add(neighbor);
//$$                                queue.add(neighbor);
//$$                            }
//$$                        } else if (isTreeLeafOrAccessory(neighborState)) {
//$$                            // نعد الأوراق وملحقات الشجرة (مثل الشتلات والطحالب)
//$$                            if (isNaturalLeafOrAccessory(neighborState)) {
//$$                                naturalLeavesCount++;
//$$                            } else {
//$$                                playerLeavesCount++;
//$$                            }
//$$                        }
//$$                    }
//$$                }
//$$            }
//$$        }
//$$        
//$$        int height = (maxY - minY) + 1;
//$$        
//$$        // الشروط الصارمة لتكون شجرة:
//$$        // 1. ارتفاع الجذع 3 بلوكات على الأقل (نظام جذور المانجروف يفي بهذا الغرض بسهولة)
//$$        if (height < MIN_TRUNK_HEIGHT) return false;
//$$        
//$$        // 2. يجب أن تحتوي على أوراق أو ملحقات. الأشجار الحقيقية تحتوي على غطاء نباتي.
//$$        if (naturalLeavesCount < 3) return false;
//$$        
//$$        // 3. تأمين صارم: يجب أن تكون جميع الأوراق طبيعية 100%
//$$        // إذا وجدنا ولو ورقة واحدة وضعها لاعب، نعتبر أن اللاعب تدخل في هذه الشجرة ونلغي اعتبارها "شجرة برية طبيعية"
//$$        if (playerLeavesCount > 0) return false;
//$$        
//$$        return true;
//$$    }

    // ═══════════════════════════════════════════════════════
    // الدوال المساعدة للتعرف على البلوكات
    // ═══════════════════════════════════════════════════════

    /**
     * يبحث عن أقرب بلوك خشب متصل. يفيد عند استهداف أوراق الشجر بدلاً من الجذع.
     */
//$$    public static BlockPos findAssociatedLog(ServerLevel level, BlockPos pos) {
//$$        if (isTreeLog(level.getBlockState(pos))) return pos;
//$$        
//$$        // بحث حلزوني للأسفل (بحثاً عن الجذع تحت الأوراق أو الجذور تحت الشتلة)
//$$        for (int y = 0; y <= 10; y++) {
//$$            for (int r = 0; r <= 3; r++) {
//$$                for (int x = -r; x <= r; x++) {
//$$                    for (int z = -r; z <= r; z++) {
//$$                        if (Math.abs(x) != r && Math.abs(z) != r) continue;
//$$                        BlockPos checkPos = pos.offset(x, -y, z);
//$$                        if (isTreeLog(level.getBlockState(checkPos))) return checkPos;
//$$                    }
//$$                }
//$$            }
//$$        }
//$$        return null;
//$$    }

    /**
     * يتعرف على جذوع الأشجار باستخدام Tags الرسمية لماينكرافت.
     * دقة 100% مع جميع المودات وبلوكات المانجروف وجذورها.
     */
//$$    public static boolean isTreeLog(BlockState state) {
//$$        if (state.is(net.minecraft.tags.BlockTags.LOGS)) return true;
//$$        
//$$        String name = BuiltInRegistries.BLOCK.getKey(state.getBlock()).getPath();
//$$        
//$$        // دعم كامل لشجرة المانجروف:
//$$        // 1. جذور المانجروف (Mangrove Roots)
//$$        // 2. جذور المانجروف الموحلة (Muddy Mangrove Roots)
//$$        if (name.contains("mangrove_roots")) return true;
//$$        
//$$        // دعم الفطريات والبيل أوك
//$$        return name.contains("mushroom_stem") || name.contains("creaking_heart");
//$$    }

    /**
     * يتعرف على الأوراق وملحقات الشجرة (مثل الشتلات المعلقة والطحالب والبتلات).
     */
//$$    public static boolean isTreeLeafOrAccessory(BlockState state) {
//$$        if (state.is(net.minecraft.tags.BlockTags.LEAVES) || state.is(net.minecraft.tags.BlockTags.WART_BLOCKS)) {
//$$            return true;
//$$        }
//$$        
//$$        String name = BuiltInRegistries.BLOCK.getKey(state.getBlock()).getPath();
//$$        
//$$        // دعم ملحقات شجرة المانجروف والكرز:
//$$        // 1. الشتلة المعلقة (Mangrove Propagule)
//$$        // 2. سجاد الطحالب (Moss Carpet) الذي ينمو فوق الأوراق
//$$        // 3. بتلات الكرز الوردية (Pink Petals) التي تنمو تحت الشجرة
//$$        if (name.equals("mangrove_propagule") || name.equals("moss_carpet") || name.equals("pink_petals")) return true;
//$$        
//$$        return name.contains("mushroom_block") || name.contains("pale_moss") || name.contains("azalea_leaves");
//$$    }

    /**
     * يحدد ما إذا كانت الأوراق أو الملحقات طبيعية أم من صنع لاعب.
     */
//$$    public static boolean isNaturalLeafOrAccessory(BlockState state) {
//$$        if (state.hasProperty(net.minecraft.world.level.block.state.properties.BlockStateProperties.PERSISTENT)) {
//$$            return !state.getValue(net.minecraft.world.level.block.state.properties.BlockStateProperties.PERSISTENT);
//$$        }
//$$        // الملحقات مثل (mangrove_propagule) أو (moss_carpet) أو (pink_petals) ليس لها خاصية Persistent، وتعتبر طبيعية دائماً إذا كانت متصلة بالشجرة
//$$        return true;
//$$    }

    //#endif
}
