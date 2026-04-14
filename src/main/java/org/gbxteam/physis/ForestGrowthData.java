package org.gbxteam.physis;

//#if MC >= 260100
//$$ import net.minecraft.core.BlockPos;
//$$ import net.minecraft.core.HolderLookup;
//$$ import net.minecraft.nbt.CompoundTag;
//$$ import net.minecraft.server.level.ServerLevel;
//$$ import net.minecraft.world.level.saveddata.SavedData;
//$$ import java.util.HashSet;
//$$ import java.util.Set;

//$$ public class ForestGrowthData extends SavedData {
//$$    private final Set<Long> modPlantedSaplings = new HashSet<>();
//$$
//$$    public static ForestGrowthData get(ServerLevel level) {
//$$        // Support for newer SavedData factory pattern in 26.1x
//$$        SavedData.Factory<ForestGrowthData> factory = new SavedData.Factory<>(
//$$            ForestGrowthData::new, 
//$$            ForestGrowthData::load, 
//$$            null
//$$        );
//$$        return level.getDataStorage().computeIfAbsent(factory, "physis_forest_data");
//$$    }
//$$
//$$    public ForestGrowthData() {}
//$$
//$$    public void addSapling(BlockPos pos) {
//$$        modPlantedSaplings.add(pos.asLong());
//$$        setDirty();
//$$    }
//$$
//$$    public boolean isModPlanted(BlockPos pos) {
//$$        return modPlantedSaplings.contains(pos.asLong());
//$$    }
//$$
//$$    public void removeSapling(BlockPos pos) {
//$$        if (modPlantedSaplings.remove(pos.asLong())) {
//$$            setDirty();
//$$        }
//$$    }
//$$
//$$    public static ForestGrowthData load(CompoundTag nbt, HolderLookup.Provider registries) {
//$$        ForestGrowthData data = new ForestGrowthData();
//$$        long[] array = nbt.getLongArray("saplings");
//$$        for (long l : array) {
//$$            data.modPlantedSaplings.add(l);
//$$        }
//$$        return data;
//$$    }
//$$
//$$    @Override
//$$    public CompoundTag save(CompoundTag nbt, HolderLookup.Provider registries) {
//$$        nbt.putLongArray("saplings", modPlantedSaplings.stream().mapToLong(Long::longValue).toArray());
//$$        return nbt;
//$$    }
//$$ }
//#endif
