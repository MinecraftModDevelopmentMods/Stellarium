package stellarium.config;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class ConfigDataRegistry {
	
	private static ConfigDataRegistry ins = new ConfigDataRegistry();
	
	public List<ConfigRegistryData> cfglist = Lists.newArrayList();
	
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
	
	public static ImmutableList<ConfigRegistryData> getImmutableList()
	{
		return ImmutableList.copyOf(ins.cfglist);
	}
	
	public static class ConfigRegistryData {
		public final String title;
		public final IConfigFormatter formatter;
		public final IConfigurableData data;
		
		public ConfigRegistryData(String title, IConfigFormatter formatter, IConfigurableData data)
		{
			this.title = title;
			this.formatter = formatter;
			this.data = data;
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
