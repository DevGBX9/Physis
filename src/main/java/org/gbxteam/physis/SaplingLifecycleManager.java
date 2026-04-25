package org.gbxteam.physis;

//#if MC >= 260100
//$$ import net.minecraft.core.BlockPos;
//$$ import net.minecraft.core.Holder;
//$$ import net.minecraft.server.level.ServerLevel;
//$$ import net.minecraft.util.RandomSource;
//$$ import net.minecraft.world.level.biome.Biome;
//$$ import net.minecraft.world.level.biome.Biomes;
//$$ import net.minecraft.world.level.block.Block;
//$$ import net.minecraft.world.level.block.Blocks;
//$$ import net.minecraft.world.level.block.RotatedPillarBlock;
//$$ import net.minecraft.world.level.block.SaplingBlock;
//$$ import net.minecraft.world.level.block.state.BlockState;
//$$ import net.minecraft.sounds.SoundEvents;
//$$ import net.minecraft.sounds.SoundSource;
//#endif

// ╔══════════════════════════════════════════════════════════════════╗
// ║         نظام دورة حياة الشتلات (Sapling Lifecycle Manager)         ║
// ║   يراقب الشتلات المزروعة وينظمها. يحول الشتلات المكتظة أو الميتة    ║
// ║   إلى شجيرات ميتة (Dead Bush)، وبعد فترة تتحلل إلى سماد (Compost) ║
// ║   لينبت مكانها أزهار وأعشاب طبيعية.                                 ║
// ╚══════════════════════════════════════════════════════════════════╝
public class SaplingLifecycleManager {

    //#if MC >= 260100

    // ==================== HEALTH CHECKS (صحة الشتلات) ====================
    
    /**
     * يفحص جميع الشتلات المسجلة في النظام. إذا كانت الشتلة مختنقة أو لم تنمُ لفترة طويلة،
     * تموت وتتحول إلى شجيرة ميتة (Dead Bush).
     */
//$$    public static void runHealthChecks(ServerLevel level) {
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
//$$            // التحقق من العمر: بعد مرور فترة، يتم فحص المساحة المتاحة
//$$            if ((age >= 600 && age < 640) || (age >= 1200)) {
//$$                int spacing = ForestGrowthHandler.getRequiredSpacing(state.getBlock());
//$$                
//$$                // --- مرونة في المستنقعات ---
//$$                Holder<Biome> biome = level.getBiome(pos);
//$$                if (biome.is(Biomes.SWAMP) || biome.is(Biomes.MANGROVE_SWAMP)) {
//$$                    spacing = Math.max(1, spacing - 1); // السماح بكثافة أكبر في المستنقعات
//$$                }
//$$
//$$                if (!isAreaClearForHealthCheck(level, pos, spacing)) {
//$$                    // مساحة غير كافية = تموت الشتلة
//$$                    level.setBlock(pos, Blocks.DEAD_BUSH.defaultBlockState(), 3);
//$$                    data.removeSapling(pos);
//$$                    data.addDeadBush(pos, currentTime);
//$$                } else {
//$$                    // تحديث وقت الفحص
//$$                    data.updateSaplingCheckTime(pos, currentTime);
//$$                }
//$$            }
//$$        });
//$$    }

    // ==================== COMPOSTING (التحلل العضوي) ====================
    
    /**
     * يفحص الشجيرات الميتة. بعد مرور الوقت الكافي، تتحلل الشجيرة
     * وتختفي في التربة كسماد عضوي لإنبات الزهور.
     */
//$$    public static void runCompostChecks(ServerLevel level) {
//$$        long currentTime = level.getGameTime();
//$$        ForestGrowthData data = ForestGrowthData.get(level);
//$$
//$$        data.getAllDeadBushes().forEach((posLong, deathTime) -> {
//$$            BlockPos pos = BlockPos.of(posLong);
//$$            if (!level.isLoaded(pos)) return;
//$$
//$$            // بعد 300 تيك من الموت، يبدأ التحلل
//$$            if (currentTime - deathTime >= 300) {
//$$                if (level.getBlockState(pos).is(Blocks.DEAD_BUSH)) {
//$$                    level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
//$$                    applyCompostEffect(level, pos);
//$$                }
//$$                data.removeDeadBush(pos);
//$$            }
//$$        });
//$$    }

    /**
     * يطبق تأثير التحلل (بون ميل مجاني) على العشب المحيط بالشجيرة الميتة
     */
//$$    private static void applyCompostEffect(ServerLevel level, BlockPos pos) {
//$$        RandomSource random = level.getRandom();
//$$        for (int x = -2; x <= 2; x++) {
//$$            for (int z = -2; z <= 2; z++) {
//$$                BlockPos target = pos.offset(x, -1, z);
//$$                BlockPos above = target.above();
//$$
//$$                if (level.getBlockState(target).is(Blocks.GRASS_BLOCK) && level.isEmptyBlock(above)) {
//$$                    // 70% فرصة لإنبات عشب أو زهور
//$$                    if (random.nextFloat() < 0.7f) {
//$$                        level.levelEvent(2005, above, 0); // Bone meal particles
//$$                        level.playSound(null, above, SoundEvents.BONE_MEAL_USE, SoundSource.BLOCKS, 1.0f, 1.0f); // صوت البون ميل
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

    // ==================== AREA SCAN UTILITY ====================
    
    /**
     * يتأكد من أن المساحة كافية حول الشتلة لكي تنمو الشجرة ولا تختنق
     */
//$$    private static boolean isAreaClearForHealthCheck(ServerLevel level, BlockPos pos, int radius) {
//$$        BlockState currentState = level.getBlockState(pos);
//$$        Block currentBlock = currentState.getBlock();
//$$        boolean isFungus = currentBlock == Blocks.CRIMSON_FUNGUS || currentBlock == Blocks.WARPED_FUNGUS;
//$$        
//$$        for (BlockPos checkPos : BlockPos.betweenClosed(pos.offset(-radius, 0, -radius), pos.offset(radius, 5, radius))) {
//$$            if (checkPos.equals(pos)) continue;
//$$            
//$$            BlockState state = level.getBlockState(checkPos);
//$$            Block block = state.getBlock();
//$$            
//$$            // التسامح مع العشب والزهور
//$$            if (block == Blocks.AIR || block == Blocks.SHORT_GRASS || block == Blocks.TALL_GRASS || 
//$$                block == Blocks.FERN || block == Blocks.LARGE_FERN || 
//$$                block == Blocks.DANDELION || block == Blocks.POPPY) {
//$$                continue;
//$$            }
//$$            
//$$            // استثناءات للفطر
//$$            if (isFungus) {
//$$                if (block == Blocks.NETHERRACK || block == Blocks.WARPED_NYLIUM || 
//$$                    block == Blocks.CRIMSON_NYLIUM || block == Blocks.SOUL_SAND || 
//$$                    block == Blocks.SOUL_SOIL || block == Blocks.CRIMSON_ROOTS || 
//$$                    block == Blocks.WARPED_ROOTS || block == Blocks.NETHER_SPROUTS) {
//$$                    continue;
//$$                }
//$$            }
//$$            
//$$            if (block instanceof RotatedPillarBlock || block instanceof SaplingBlock || state.isSolidRender()) {
//$$                double distSq = checkPos.distSqr(pos);
//$$                if (distSq < (radius * radius)) { 
//$$                    return false;
//$$                }
//$$            }
//$$        }
//$$        return true;
//$$    }

    //#endif
}
