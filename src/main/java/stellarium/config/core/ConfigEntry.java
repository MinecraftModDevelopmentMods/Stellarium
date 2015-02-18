package stellarium.config.core;

import java.util.ArrayList;
import java.util.List;

public class ConfigEntry {
	protected List<String> elist = new ArrayList();
	
	public ConfigEntry() { }
	
	public ConfigEntry(String id)
	{
		elist.add(id);
	}
	
	public ConfigEntry(ConfigEntry par, String id)
	{
		elist.addAll(par.elist);
		elist.add(id);
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o instanceof ConfigEntry)
			return ((ConfigEntry) o).elist.equals(this.elist);
		return false;
	}
	
	@Override
	public int hashCode()
	{
		return elist.hashCode();
	}
}
