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
//$$    private final Map<Long, Long> modPlantedSaplings = new HashMap<>();
//$$    private final File saveFile;
//$$
//$$    private ForestGrowthData(ServerLevel level) {
//$$        this.saveFile = level.getServer().getWorldPath(LevelResource.ROOT).resolve("physis_growth.dat").toFile();
//$$        load();
//$$    }
//$$
//$$    public static ForestGrowthData get(ServerLevel level) {
//$$        return instances.computeIfAbsent(level, ForestGrowthData::new);
//$$    }
//$$
//$$    public void addSapling(BlockPos pos, long gameTime) {
//$$        modPlantedSaplings.put(pos.asLong(), gameTime);
//$$        save();
//$$    }
//$$
//$$    public boolean isModPlanted(BlockPos pos) {
//$$        return modPlantedSaplings.containsKey(pos.asLong());
//$$    }
//$$
//$$    public Map<Long, Long> getAllTrackedSaplings() {
//$$        return new HashMap<>(modPlantedSaplings);
//$$    }
//$$
//$$    public void removeSapling(BlockPos pos) {
//$$        if (modPlantedSaplings.remove(pos.asLong()) != null) {
//$$            save();
//$$        }
//$$    }
//$$
//$$    public void updateSaplingCheckTime(BlockPos pos, long newTime) {
//$$        modPlantedSaplings.put(pos.asLong(), newTime);
//$$        save();
//$$    }
//$$
//$$    private void load() {
//$$        if (!saveFile.exists()) return;
//$$        try {
//$$            CompoundTag nbt = NbtIo.read(saveFile.toPath());
//$$            if (nbt != null) {
//$$                long[] positions = nbt.getLongArray("saplings").orElse(new long[0]);
//$$                long[] times = nbt.getLongArray("times").orElse(new long[0]);
//$$                
//$$                modPlantedSaplings.clear();
//$$                for (int i = 0; i < Math.min(positions.length, times.length); i++) {
//$$                    modPlantedSaplings.put(positions[i], times[i]);
//$$                }
//$$            }
//$$        } catch (IOException e) {
//$$            e.printStackTrace();
//$$        }
//$$    }
//$$
//$$    private void save() {
//$$        try {
//$$            CompoundTag nbt = new CompoundTag();
//$$            long[] positions = new long[modPlantedSaplings.size()];
//$$            long[] times = new long[modPlantedSaplings.size()];
//$$            
//$$            int i = 0;
//$$            for (Map.Entry<Long, Long> entry : modPlantedSaplings.entrySet()) {
//$$                positions[i] = entry.getKey();
//$$                times[i] = entry.getValue();
//$$                i++;
//$$            }
//$$            
//$$            nbt.putLongArray("saplings", positions);
//$$            nbt.putLongArray("times", times);
//$$            NbtIo.write(nbt, saveFile.toPath());
//$$        } catch (IOException e) {
//$$            e.printStackTrace();
//$$        }
//$$    }
//$$ }
//#endif
