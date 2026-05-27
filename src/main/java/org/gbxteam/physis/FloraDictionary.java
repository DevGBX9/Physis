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

// ╔══════════════════════════════════════════════════════════════════╗
// ║                    قاموس النباتات والأشجار (Flora Dictionary)          ║
// ║   هنا يتم تصنيف جميع أنواع الأشجار والنباتات، وتحديد مناطق انتشارها    ║
// ║   لتبسيط البرمجة وجعل الكود منظماً وسهل التعديل لاحقاً                 ║
// ╚══════════════════════════════════════════════════════════════════╝
public class FloraDictionary {

    //#if MC >= 11802

    // ═══════════════════════════════════════════════════════════════
    //  ١. تصنيف الشجيرات والنباتات الأرضية (Vegetation)
    // ═══════════════════════════════════════════════════════════════
//$$    public enum VegetationType {
//$$        GRASS,          // العشب العادي والقصير
//$$        FERN,           // السرخس
//$$        PLAIN_BUSH,     // الشجيرة الزخرفية الميتة أو العادية
//$$        FIREFLY_BUSH,   // شجيرة اليراعات
//$$        PETAL,          // بتلات الكرز
//$$        FUNGUS,         // الفطريات
//$$        FLOWER,         // الأزهار
//$$        WATER_FLORA,    // نباتات بحرية
//$$        CAVE_FLORA,     // نباتات الكهوف
//$$        NETHER_FLORA,   // نباتات النذر
//$$        INVALID         // نباتات لا تقبل الانتشار (مثل الميتة أو المزدوجة)
//$$    }
//$$
//$$    public static VegetationType categorizeVegetation(String name) {
//$$        // استثناء النباتات المزدوجة والميتة
//$$        if (name.contains("sunflower") || name.contains("lilac") || name.contains("rose_bush") || 
//$$            name.contains("peony") || name.contains("tall") || name.contains("large") || 
//$$            name.contains("pitcher") || name.contains("dead_bush") || name.contains("berry_bush") ||
//$$            name.contains("lily") || name.contains("petal")) {
//$$            return VegetationType.INVALID;
//$$        }
//$$        
//$$        if (name.contains("fungus") || name.contains("nether_wart") || name.contains("roots") || name.contains("sprouts") || name.contains("vines") || name.contains("mushroom")) return VegetationType.NETHER_FLORA;
//$$        if (name.contains("kelp") || name.contains("seagrass") || name.contains("pickle") || name.contains("coral")) return VegetationType.WATER_FLORA;
//$$        if (name.contains("moss") || name.contains("azalea") || name.contains("spore") || name.contains("dripleaf") || name.contains("cave_vines") || name.contains("glow_berries")) return VegetationType.CAVE_FLORA;
//$$        if (name.equals("grass") || name.equals("short_grass")) return VegetationType.GRASS;
//$$        if (name.equals("fern")) return VegetationType.FERN;
//$$        if (name.equals("bush")) return VegetationType.PLAIN_BUSH;
//$$        if (name.contains("firefly_bush")) return VegetationType.FIREFLY_BUSH;
//$$        
//$$        // الباقي يعتبر أزهار
//$$        if (name.contains("flower") || name.contains("allium") || name.contains("orchid") || name.contains("tulip") || name.contains("bluet") || name.contains("daisy") || name.contains("cornflower") || name.contains("lily_of_the_valley")) {
//$$            return VegetationType.FLOWER;
//$$        }
//$$        
//$$        return VegetationType.FLOWER; // افتراضي للأشياء الصغيرة الأخرى
//$$    }
//$$
//$$    public static int getMaxDensity(VegetationType type, boolean isDensePatch) {
//$$        switch (type) {
//$$            case GRASS: return isDensePatch ? 12 : 7;
//$$            case FIREFLY_BUSH: return 1;
//$$            case PLAIN_BUSH: return isDensePatch ? 7 : 3;
//$$            case FERN: return 2;
//$$            case FLOWER: return 2;
//$$            default: return 2;
//$$        }
//$$    }
    //#endif
}
