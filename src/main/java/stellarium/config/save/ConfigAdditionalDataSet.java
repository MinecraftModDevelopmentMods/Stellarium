package stellarium.config.save;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import stellarium.config.IConfigAdditionalData;
import stellarium.config.core.ConfigDataPhysicalManager;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.WorldInfo;

public class ConfigAdditionalDataSet extends WorldSavedData {

	private static String key = "cfgadddata.dat";
	private Map<String, IConfigAdditionalData> dataMap;
	
	public ConfigAdditionalDataSet(ConfigDataPhysicalManager manager, File location, WorldInfo worldInfo) throws IOException {
		super(key);
		this.loadConfigData(manager, location, worldInfo);
	}
	
	public void loadConfigData(ConfigDataPhysicalManager manager, File location, WorldInfo worldInfo) throws IOException {
		File file = new File(location, key);
		
		if(!location.exists()) {
			location.createNewFile();
			dataMap = manager.getFormattedAdditionalDataMap(worldInfo);
			return;
		}
		
		dataMap = manager.getDefaultAdditionalDataMap();
		
		FileInputStream fi = new FileInputStream(location);
		BufferedInputStream inp = new BufferedInputStream(fi);
		
		NBTTagCompound comp = CompressedStreamTools.readCompressed(inp);
		this.readFromNBT(comp);
	}

	public void saveConfigData(File location) throws IOException {
		File file = new File(location, key);
		
		if(!location.exists())
			location.createNewFile();
		
		NBTTagCompound comp = new NBTTagCompound();
		this.writeToNBT(comp);

		FileOutputStream fo = new FileOutputStream(location);
		BufferedOutputStream outp = new BufferedOutputStream(fo);
		
		CompressedStreamTools.writeCompressed(comp, outp);
	}
	

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		for(Map.Entry<String, IConfigAdditionalData> entry : dataMap.entrySet())
			entry.getValue().fromNBT(tag.getCompoundTag(entry.getKey()));
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		for(Map.Entry<String, IConfigAdditionalData> entry : dataMap.entrySet())
			entry.getValue().toNBT(tag.getCompoundTag(entry.getKey()));
	}

}
