package org.gbxteam.physis.mixins;

//#if MC >= 260100
//$$ import net.minecraft.core.BlockPos;
//$$ import net.minecraft.server.level.ServerLevel;
//$$ import net.minecraft.util.RandomSource;
//$$ import net.minecraft.world.level.block.SaplingBlock;
//$$ import net.minecraft.world.level.block.state.BlockState;
//$$ import net.minecraft.world.level.block.Blocks;
//$$ import net.minecraft.sounds.SoundEvents;
//$$ import net.minecraft.sounds.SoundSource;
//$$ import org.gbxteam.physis.ForestGrowthHandler;
//$$ import org.spongepowered.asm.mixin.Mixin;
//$$ import org.spongepowered.asm.mixin.injection.At;
//$$ import org.spongepowered.asm.mixin.injection.Inject;
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//$$ @Mixin(SaplingBlock.class)
//$$ public class SaplingBlockMixin {
//$$    @Inject(method = "advanceTree", at = @At("TAIL"))
//$$    private void onAdvanceTree(ServerLevel level, BlockPos pos, BlockState state, RandomSource random, CallbackInfo ci) {
//$$        // Check if a tree grew (block changed) AND if it was planted by the mod
//$$        org.gbxteam.physis.ForestGrowthData data = org.gbxteam.physis.ForestGrowthData.get(level);
//$$        if (!(level.getBlockState(pos).getBlock() instanceof SaplingBlock)) {
//$$            // [SOUND EFFECTS]
//$$            net.minecraft.world.level.block.Block saplingBlock = state.getBlock();
//$$            float volume = 5.0f;  // الأشجار العادية: مدى سماع ~80 بلوك
//$$            float pitch = 0.8f;
//$$            
//$$            // تضخيم الصوت للأشجار الكبيرة والعملاقة
//$$            if (saplingBlock == Blocks.DARK_OAK_SAPLING || saplingBlock == Blocks.PALE_OAK_SAPLING) {
//$$                volume = 12.0f;  // أشجار عملاقة (2x2): مدى سماع ~192 بلوك
//$$                pitch = 0.4f;
//$$            } else if (saplingBlock == Blocks.JUNGLE_SAPLING || saplingBlock == Blocks.SPRUCE_SAPLING) {
//$$                volume = 10.0f;  // أشجار كبيرة: مدى سماع ~160 بلوك
//$$                pitch = 0.5f;
//$$            } else if (saplingBlock == Blocks.MANGROVE_PROPAGULE) {
//$$                volume = 8.0f;   // مانغروف: مدى سماع ~128 بلوك
//$$                pitch = 0.6f;
//$$            } else if (saplingBlock == Blocks.OAK_SAPLING || saplingBlock == Blocks.ACACIA_SAPLING || saplingBlock == Blocks.CHERRY_SAPLING) {
//$$                volume = 6.0f;   // أشجار متوسطة: مدى سماع ~96 بلوك
//$$                pitch = 0.7f;
//$$            }
//$$
//$$            // [SOUND MIX - EVOLVED]
//$$            // ١. صوت القاعدة (شق التربة والجذور)
//$$            level.playSound(null, pos, SoundEvents.GRASS_BREAK, SoundSource.BLOCKS, volume * 0.8f, pitch + 0.1f);
//$$            level.playSound(null, pos, SoundEvents.BONE_MEAL_USE, SoundSource.BLOCKS, volume * 0.5f, pitch);
//$$
//$$            // ٢. صوت الجذع (تكسر الخشب - مرتفع قليلاً)
//$$            BlockPos trunkPos = pos.above(2);
//$$            level.playSound(null, trunkPos, SoundEvents.WOOD_BREAK, SoundSource.BLOCKS, volume, pitch - 0.1f);
//$$            
//$$            // ٣. صوت التاج (حفيف الأوراق والأغصان - مرتفع أكثر)
//$$            BlockPos canopyPos = pos.above(4);
//$$            level.playSound(null, canopyPos, SoundEvents.AZALEA_LEAVES_PLACE, SoundSource.BLOCKS, volume, pitch + 0.2f);
//$$            level.playSound(null, canopyPos, SoundEvents.MOSS_STEP, SoundSource.BLOCKS, volume * 0.7f, pitch + 0.4f);
//$$
//$$            // ٤. الطبقة السحرية (رنين خفيف يعطي إيحاءً بالحيوية)
//$$            level.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, volume * 0.3f, 2.0f); 
//$$
//$$            // [BIOME FILL & TRACKING]
//$$            if (data.isModPlanted(pos)) {
//$$                ForestGrowthHandler.getRelatedBiomeKey(level, pos, saplingBlock).ifPresent(biomeKey -> {
//$$                    ForestGrowthHandler.executeFillBiome(level, pos, biomeKey);
//$$                });
//$$                data.removeSapling(pos);
//$$            }
//$$        }
//$$    }
//$$ }
//#endif
