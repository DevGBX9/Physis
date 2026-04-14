package org.gbxteam.physis;

//#if MC >= 260100
//$$ import net.minecraft.core.BlockPos;
//$$ import net.minecraft.nbt.CompoundTag;
//$$ import net.minecraft.nbt.NbtIo;
//$$ import net.minecraft.server.level.ServerLevel;
//$$ import net.minecraft.world.level.storage.LevelResource;
//$$ import java.io.File;
//$$ import java.io.IOException;
//$$ import java.util.HashSet;
//$$ import java.util.Set;
//$$ import java.util.Map;
//$$ import java.util.HashMap;

//$$ public class ForestGrowthData {
//$$    private static final Map<ServerLevel, ForestGrowthData> instances = new HashMap<>();
//$$    private final Set<Long> modPlantedSaplings = new HashSet<>();
//$$    private final File saveFile;
//$$
//$$    private ForestGrowthData(ServerLevel level) {
//$$        // Save data in the world folder to ensure stability across versions
//$$        this.saveFile = level.getServer().getWorldPath(LevelResource.ROOT).resolve("physis_growth.dat").toFile();
//$$        load();
//$$    }
//$$
//$$    public static ForestGrowthData get(ServerLevel level) {
//$$        return instances.computeIfAbsent(level, ForestGrowthData::new);
//$$    }
//$$
//$$    public void addSapling(BlockPos pos) {
//$$        if (modPlantedSaplings.add(pos.asLong())) {
//$$            save();
//$$        }
//$$    }
//$$
//$$    public boolean isModPlanted(BlockPos pos) {
//$$        return modPlantedSaplings.contains(pos.asLong());
//$$    }
//$$
//$$    public void removeSapling(BlockPos pos) {
//$$        if (modPlantedSaplings.remove(pos.asLong())) {
//$$            save();
//$$        }
//$$    }
//$$
//$$    private void load() {
//$$        if (!saveFile.exists()) return;
//$$        try {
//$$            CompoundTag nbt = NbtIo.read(saveFile.toPath());
//$$            if (nbt != null) {
//$$                nbt.getLongArray("saplings").ifPresent(array -> {
//$$                    for (long l : array) {
//$$                        modPlantedSaplings.add(l);
//$$                    }
//$$                });
//$$            }
//$$        } catch (IOException e) {
//$$            e.printStackTrace();
//$$        }
//$$    }
//$$
//$$    private void save() {
//$$        try {
//$$            CompoundTag nbt = new CompoundTag();
//$$            nbt.putLongArray("saplings", modPlantedSaplings.stream().mapToLong(Long::longValue).toArray());
//$$            NbtIo.write(nbt, saveFile.toPath());
//$$        } catch (IOException e) {
//$$            e.printStackTrace();
//$$        }
//$$    }
//$$ }
//#endif
