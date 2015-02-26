package stellarium.config.core;

import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import stellarium.config.ICfgArrMListener;
import stellarium.config.IConfigCategory;
import stellarium.config.IConfigProperty;
import stellarium.config.IMConfigProperty;
import stellarium.config.IPropertyRelation;
import stellarium.config.IStellarConfig;

public abstract class StellarConfigCategory implements IConfigCategory {
	
	protected final StellarConfigHandler handler;
	protected final String id;
	protected String dispname;
	protected final StellarConfigCategory parCategory;
	
	public StellarConfigCategory(StellarConfigHandler handler, StellarConfigCategory parCategory, String id)
	{
		this.handler = handler;
		this.id = id;
		this.parCategory = parCategory;
		
		this.dispname = id;
	}

	@Override
	public String getID() {
		return id;
	}

	@Override
	public String getDisplayName() {
		return dispname;
	}

	@Override
	public void setDisplayName(String name) {
		if(dispname.equals(name))
			return;
		
		String prev = dispname;
		dispname = name;
		
		for(ICfgArrMListener list : handler.listenList)
			list.onDispNameChange(this, prev);
	}

	@Override
	public IStellarConfig getConfig() {
		return handler;
	}

	@Override
	public IConfigCategory getParCategory() {
		return parCategory;
	}

	@Override
	public ConfigEntry getConfigEntry() {
		if(parCategory != null)
			return new ConfigEntry(parCategory.getConfigEntry(), id);
		else return new ConfigEntry(id);
	}

	
	public Map<String, List> proprels = Maps.newHashMap();

	@Override
	public void addPropertyRelation(IPropertyRelation rel,
			IConfigProperty... relprops) {
		
		PropertyRelation pr = new PropertyRelation(rel, relprops);
		
		List<IMConfigProperty> lp = Lists.newArrayList();
		
		for(IConfigProperty cp : relprops)
		{
			IMConfigProperty mp = (IMConfigProperty) cp;
			lp.add(mp);
		}
		
		IMConfigProperty[] mp = lp.toArray(new IMConfigProperty[0]);
		rel.setProps(mp);
		
		for(IConfigProperty prop : relprops)
		{
			if(!proprels.containsKey(prop.getName()))
				proprels.put(prop.getName(), Lists.newArrayList());
			
			List<PropertyRelation> l = proprels.get(prop.getName());
			l.add(pr);
		}
	}
	
	public class PropertyRelation {
		public IPropertyRelation proprel;
		public IConfigProperty[] relprops;
		
		public PropertyRelation(IPropertyRelation propr, IConfigProperty... relp)
		{
			proprel = propr;
			relprops = relp;
		}
	}

}
