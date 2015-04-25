package stellarium.config.save;

import stellarium.config.core.StellarConfiguration;
import stellarium.config.json.JsonConfigHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;

public class ConfigSaveData extends WorldSavedData {
		
	private StellarConfiguration config;
	private SaveFormatJsonContainer container;
	private JsonConfigHandler handler;
	
	public ConfigSaveData(StellarConfiguration config) {
		super(config.title);
		this.config = config;
		this.container = new SaveFormatJsonContainer();
		this.handler = new JsonConfigHandler(container);
	}
	
	public String getTitle() {
		return config.title;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		this.preLoadSave(tag);
		config.onApply();
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		this.preLoadSave(tag);
		config.onSave();
	}
	
	private void preLoadSave(NBTTagCompound tag) {
		container.bindNBT(tag);
		config.setHandler(handler);
		config.onFormat();
	}

}
