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
//$$    private final Map<Long, Long> deadBushes = new HashMap<>();
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
//$$    public void addDeadBush(BlockPos pos, long gameTime) {
//$$        deadBushes.put(pos.asLong(), gameTime);
//$$        save();
//$$    }
//$$
//$$    public Map<Long, Long> getAllTrackedSaplings() {
//$$        return new HashMap<>(modPlantedSaplings);
//$$    }
//$$
//$$    public Map<Long, Long> getAllDeadBushes() {
//$$        return new HashMap<>(deadBushes);
//$$    }
//$$
//$$    public void removeSapling(BlockPos pos) {
//$$        if (modPlantedSaplings.remove(pos.asLong()) != null) {
//$$            save();
//$$        }
//$$    }
//$$
//$$    public void removeDeadBush(BlockPos pos) {
//$$        if (deadBushes.remove(pos.asLong()) != null) {
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
//$$                loadMap(nbt, "saplings", "times", modPlantedSaplings);
//$$                loadMap(nbt, "dead_bushes", "death_times", deadBushes);
//$$            }
//$$        } catch (IOException e) {
//$$            e.printStackTrace();
//$$        }
//$$    }
//$$
//$$    private void loadMap(CompoundTag nbt, String posKey, String timeKey, Map<Long, Long> map) {
//$$        long[] positions = nbt.getLongArray(posKey).orElse(new long[0]);
//$$        long[] times = nbt.getLongArray(timeKey).orElse(new long[0]);
//$$        map.clear();
//$$        for (int i = 0; i < Math.min(positions.length, times.length); i++) {
//$$            map.put(positions[i], times[i]);
//$$        }
//$$    }
//$$
//$$    private void save() {
//$$        try {
//$$            CompoundTag nbt = new CompoundTag();
//$$            saveMap(nbt, "saplings", "times", modPlantedSaplings);
//$$            saveMap(nbt, "dead_bushes", "death_times", deadBushes);
//$$            NbtIo.write(nbt, saveFile.toPath());
//$$        } catch (IOException e) {
//$$            e.printStackTrace();
//$$        }
//$$    }
//$$
//$$    private void saveMap(CompoundTag nbt, String posKey, String timeKey, Map<Long, Long> map) {
//$$        long[] positions = new long[map.size()];
//$$        long[] times = new long[map.size()];
//$$        int i = 0;
//$$        for (Map.Entry<Long, Long> entry : map.entrySet()) {
//$$            positions[i] = entry.getKey();
//$$            times[i] = entry.getValue();
//$$            i++;
//$$        }
//$$        nbt.putLongArray(posKey, positions);
//$$        nbt.putLongArray(timeKey, times);
//$$    }
//$$ }
//#endif
