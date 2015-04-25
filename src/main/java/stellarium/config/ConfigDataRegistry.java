package stellarium.config;

import java.util.List;
import java.util.Map;

import stellarium.config.core.StellarConfiguration;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class ConfigDataRegistry {
	
	private static ConfigDataRegistry ins = new ConfigDataRegistry();
	
	public List<ConfigRegistryData> cfglist = Lists.newArrayList();
	private Map<ConfigRegistryData, StellarConfiguration> cfgmap = Maps.newHashMap();
	
	/**
	 * Registers Configurable Data Form.
	 * @param title the id of the title for this data form.
	 * @param formatter the configuration formatter for this data form.
	 * @param data the configurable data.
	 * */
	public static void register(String title, IConfigFormatter formatter, IConfigurableData data)
	{
		ins.cfglist.add(new ConfigRegistryData(title, formatter, data));
	}
	
	/**
	 * Registers Configurable Data Form.
	 * @param title the id of the title for this data form.
	 * @param formatter the configuration formatter for this data form.
	 * @param data the configurable data.
	 * @param handler the physical handler provider, to handle physical configuration.
	 * */
	public static void register(String title, IConfigFormatter formatter, IConfigurableData data, IPhysicalHandlerProvider handler)
	{
		ins.cfglist.add(new ConfigRegistryData(title, formatter, data, handler));
	}
	
	public static ImmutableList<ConfigRegistryData> getImmutableList()
	{
		return ImmutableList.copyOf(ins.cfglist);
	}
	
	public static ImmutableList<StellarConfiguration> getImmutableCfgList()
	{
		return ImmutableList.copyOf(ins.cfgmap.values());
	}
	
	public static StellarConfiguration getConfig(ConfigRegistryData data)
	{
		return ins.cfgmap.get(data);
	}
	
	
	public static void onFormat() {
		for(ConfigRegistryData data : ins.cfglist)
		{
			StellarConfiguration config = new StellarConfiguration(data);
			ins.cfgmap.put(data, config);
		}
	}
	
	public static class ConfigRegistryData {
		public final String title;
		public final IConfigFormatter formatter;
		public final IConfigurableData data;
		public final IPhysicalHandlerProvider handler;
		
		public ConfigRegistryData(String title, IConfigFormatter formatter, IConfigurableData data)
		{
			this(title, formatter, data, null);
		}
		
		public ConfigRegistryData(String title, IConfigFormatter formatter, IConfigurableData data, IPhysicalHandlerProvider handler)
		{
			this.title = title;
			this.formatter = formatter;
			this.data = data;
			this.handler = handler;
		}
		
		@Override
		public boolean equals(Object o)
		{
			return (o instanceof ConfigRegistryData)?
					title.equals(((ConfigRegistryData)o).title) : false;
		}
		
		@Override
		public int hashCode()
		{
			return title.hashCode();
		}
	}

}
