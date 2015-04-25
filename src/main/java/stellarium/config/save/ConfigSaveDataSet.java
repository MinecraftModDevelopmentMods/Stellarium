package stellarium.config.save;

import java.util.List;
import java.util.Map;

import stellarium.config.core.ConfigDataPhysicalManager;
import stellarium.config.core.StellarConfiguration;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cpw.mods.fml.relauncher.Side;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;

public class ConfigSaveDataSet extends WorldSavedData {
	
	private static final String key = "stellarium.config";
	private List<ConfigSaveData> dataList = Lists.newArrayList();
	
	public static void loadConfigData(World world) {		
		MapStorage storage = world.mapStorage;
		ConfigSaveDataSet result = (ConfigSaveDataSet)storage.loadData(ConfigSaveDataSet.class, key);
		if (result == null) {
			result = new ConfigSaveDataSet();
			storage.setData(key, result);
		}
		
		//CompressedStreamTools.readCompressed(p_74796_0_);
	}
	
	public ConfigSaveDataSet() {
		this(key, false);
	}
	
	public ConfigSaveDataSet(String key) {
		this(key, true);
	}
	
	public ConfigSaveDataSet(String key, boolean loading) {
		super(key);
		
		ConfigDataPhysicalManager manager = ConfigDataPhysicalManager.getManager(Side.SERVER);
		
		if(loading)
			manager.onFormatToLoad();
		else manager.onFormatWithLogicalData();
		
		for(StellarConfiguration config : manager.getImmutableCfgList())
			dataList.add(new ConfigSaveData(config));
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		for(ConfigSaveData data : dataList) {
			data.readFromNBT(tag.getCompoundTag(data.getTitle()));
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		for(ConfigSaveData data : dataList) {
			data.writeToNBT(tag.getCompoundTag(data.getTitle()));
		}
	}
	
}
