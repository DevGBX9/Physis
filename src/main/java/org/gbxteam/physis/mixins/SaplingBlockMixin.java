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
//$$            float volume = 1.5f;
//$$            float pitch = 0.8f;
//$$            
//$$            // تضخيم الصوت للأشجار العملاقة
//$$            if (saplingBlock == Blocks.DARK_OAK_SAPLING || saplingBlock == Blocks.PALE_OAK_SAPLING || 
//$$                saplingBlock == Blocks.MANGROVE_PROPAGULE || saplingBlock == Blocks.JUNGLE_SAPLING || 
//$$                saplingBlock == Blocks.SPRUCE_SAPLING) {
//$$                volume = 3.5f;
//$$                pitch = 0.5f;
//$$            }
//$$
//$$            level.playSound(null, pos, SoundEvents.WOOD_BREAK, SoundSource.BLOCKS, volume, pitch);
//$$            level.playSound(null, pos, SoundEvents.GRASS_BREAK, SoundSource.BLOCKS, volume * 0.8f, pitch + 0.1f); // صوت شق التربة
//$$            level.playSound(null, pos, SoundEvents.AZALEA_LEAVES_PLACE, SoundSource.BLOCKS, volume, pitch + 0.2f);
//$$            level.playSound(null, pos, SoundEvents.MOSS_STEP, SoundSource.BLOCKS, volume * 0.7f, pitch + 0.3f); // حفيف الأغصان
//$$            level.playSound(null, pos, SoundEvents.BONE_MEAL_USE, SoundSource.BLOCKS, volume, pitch + 0.4f);
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
