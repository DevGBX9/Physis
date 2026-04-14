package org.gbxteam.physis.mixins;

//#if MC >= 260100
//$$ import net.minecraft.core.BlockPos;
//$$ import net.minecraft.server.level.ServerLevel;
//$$ import net.minecraft.util.RandomSource;
//$$ import net.minecraft.world.level.block.SaplingBlock;
//$$ import net.minecraft.world.level.block.state.BlockState;
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
//$$        if (!(level.getBlockState(pos).getBlock() instanceof SaplingBlock) && data.isModPlanted(pos)) {
//$$            ForestGrowthHandler.getRelatedBiomeKey(state.getBlock()).ifPresent(biomeKey -> {
//$$                ForestGrowthHandler.executeFillBiome(level, pos, biomeKey);
//$$            });
//$$            // Remove from tracker after the tree has grown and biome is changed
//$$            data.removeSapling(pos);
//$$        }
//$$    }
//$$ }
//#endif
