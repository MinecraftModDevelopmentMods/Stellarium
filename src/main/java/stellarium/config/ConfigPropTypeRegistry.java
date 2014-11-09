package stellarium.config;

import java.util.Map;

import com.google.common.collect.Maps;

public class ConfigPropTypeRegistry {

	private static ConfigPropTypeRegistry ins;
	
	public static ConfigPropTypeRegistry instance()
	{
		if(ins == null)
			ins = new ConfigPropTypeRegistry();
		return ins;
	}
	
	Map<String, IConfigPropHandler> hmap = Maps.newHashMap();
	
	public static <T> void register(String proptype, IConfigPropHandler<T> handler)
	{
		instance().hmap.put(proptype, handler);
	}
	
	public static <T> IConfigPropHandler<T> getHandler(String proptype)
	{
		return instance().hmap.get(proptype);
	}
	
}
