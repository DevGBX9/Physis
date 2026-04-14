package org.gbxteam.physis;

//#if MC >= 260100
//$$ import net.minecraft.core.BlockPos;
//$$ import net.minecraft.nbt.CompoundTag;
//$$ import net.minecraft.nbt.ListTag;
//$$ import net.minecraft.nbt.LongTag;
//$$ import net.minecraft.nbt.Tag;
//$$ import net.minecraft.server.level.ServerLevel;
//$$ import net.minecraft.world.level.saveddata.SavedData;
//$$ import java.util.HashSet;
//$$ import java.util.Set;

//$$ public class ForestGrowthData extends SavedData {
//$$    private final Set<Long> modPlantedSaplings = new HashSet<>();
//$$
//$$    public static ForestGrowthData get(ServerLevel level) {
//$$        return level.getDataStorage().computeIfAbsent(
//$$            new SavedData.Factory<>(ForestGrowthData::new, ForestGrowthData::load, null), 
//$$            "physis_forest_data"
//$$        );
//$$    }
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
//$$    public static ForestGrowthData load(CompoundTag nbt) {
//$$        ForestGrowthData data = new ForestGrowthData();
//$$        ListTag list = nbt.getList("saplings", Tag.TAG_LONG);
//$$        for (int i = 0; i < list.size(); i++) {
//$$            data.modPlantedSaplings.add(list.getLong(i));
//$$        }
//$$        return data;
//$$    }
//$$
//$$    @Override
//$$    public CompoundTag save(CompoundTag nbt) {
//$$        ListTag list = new ListTag();
//$$        for (long l : modPlantedSaplings) {
//$$            list.add(LongTag.valueOf(l));
//$$        }
//$$        nbt.put("saplings", list);
//$$        return nbt;
//$$    }
//$$ }
//#endif
