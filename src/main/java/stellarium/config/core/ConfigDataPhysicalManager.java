package stellarium.config.core;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import net.minecraft.world.WorldServer;
import stellarium.Stellarium;
import stellarium.config.ConfigDataRegistry.ConfigRegistryData;
import stellarium.config.*;
import stellarium.config.save.ConfigSaveDataSet;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cpw.mods.fml.relauncher.Side;

public class ConfigDataPhysicalManager {
	//Singletons via side
	private static EnumMap<Side, ConfigDataPhysicalManager> instance = Maps.newEnumMap(Side.class);
	
	public static ConfigDataPhysicalManager getManager(Side side)
	{
		if(!instance.containsKey(side))
			instance.put(side, new ConfigDataPhysicalManager(side.isClient()));
		return instance.get(side);
	}
	
	private boolean isRemote;
	private List<ConfigRegistryData> cfglist = Lists.newArrayList();
	private Map<String, StellarConfiguration> cfgmap = Maps.newHashMap();
	private ImmutableMap<String, IConfigAdditionalData> dataMap;
	private ImmutableMap<String, IPhysicalHandler> handlerMap;
	
	public ConfigDataPhysicalManager(boolean isRemote) {
		this.isRemote = isRemote;
	}
	
	public ImmutableList<ConfigRegistryData> getImmutableList()
	{
		return ImmutableList.copyOf(cfglist);
	}
	
	public ImmutableList<StellarConfiguration> getImmutableCfgList()
	{
		return ImmutableList.copyOf(cfgmap.values());
	}
	
	public ImmutableMap<String, IConfigAdditionalData> getAdditionalDataMap() {
		return dataMap;
	}
	
	public StellarConfiguration getConfig(String title)
	{
		return cfgmap.get(title);
	}
	
	public void addMapping(ConfigRegistryData data) {
		cfglist.add(data);
		cfgmap.put(data.title, new StellarConfiguration(data));
	}
	
	public Map<String, IConfigAdditionalData> getCfgAdditionalDataMap() {
		Builder<String, IConfigAdditionalData> builder = ImmutableMap.builder();
		
		for(ConfigRegistryData data : ConfigDataRegistry.getImmutableList())
		{
			if(data.handler != null)
				builder.put(data.title, data.handler.provideAdditionalData());
		}
		
		return dataMap = builder.build();
	}
	
	public void onFormatWithLogicalData() {
		Builder<String, IPhysicalHandler> builder = ImmutableMap.builder();
		
		for(ConfigRegistryData data : ConfigDataRegistry.getImmutableList())
		{
			if(data.handler != null)
			{
				IPhysicalHandler handler = data.handler.providePhysicalHandler(this.isRemote);
				builder.put(data.title, handler);
				
				IConfigFormatter cformatter = handler.createPhysicalFormatter(data.formatter, dataMap.get(data.title));
				IConfigurableData cdata = handler.createPhysicalData(data.data, dataMap.get(data.title));
				this.addMapping(new ConfigRegistryData(data.title, cformatter, cdata));
			}
		}
		
		this.handlerMap = builder.build();
	}
	
	public void onFormat(Map<String, IConfigAdditionalData> dataMap) {
		Builder<String, IPhysicalHandler> builder = ImmutableMap.builder();

		for(ConfigRegistryData data : ConfigDataRegistry.getImmutableList())
		{
			if(data.handler != null)
			{
				IPhysicalHandler handler = data.handler.providePhysicalHandler(this.isRemote);
				builder.put(data.title, handler);
				
				IConfigFormatter cformatter = handler.createPhysicalFormatter(dataMap.get(data.title));
				IConfigurableData cdata = handler.createPhysicalData(dataMap.get(data.title));
				this.addMapping(new ConfigRegistryData(data.title, cformatter, cdata));
			}
		}
		
		this.handlerMap = builder.build();
	}
	
	public void onFormatToLoad() {
		this.onFormat(this.dataMap);
	}
	
	
	public void onFormatClient(Map<String, IConfigAdditionalData> addDataMap) {
    	Stellarium.instance.getNetHandler().refreshOrganizer(Side.CLIENT);
		this.onFormat(addDataMap);
	}

	public void onFormatServer(WorldServer world) {
    	Stellarium.instance.getNetHandler().refreshOrganizer(Side.SERVER);
    	
		Builder<String, IConfigAdditionalData> builder = ImmutableMap.builder();
		
		for(ConfigRegistryData data : ConfigDataRegistry.getImmutableList())
		{
			if(data.handler != null)
				builder.put(data.title, data.handler.provideFormattedAdditionalData(world));
		}
		
		this.dataMap = builder.build();
		
		ConfigSaveDataSet.loadConfigData(world);
		
		Stellarium.instance.getNetHandler().finishServerOrganization();
		this.onFinishSetup();
	}
	
	public void onFinishSetup() {
		for(IPhysicalHandler handler : handlerMap.values())
			handler.endSetup();
	}
}
