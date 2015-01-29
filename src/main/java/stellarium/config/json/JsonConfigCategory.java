package stellarium.config.json;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import stellarium.config.ICfgArrMListener;
import stellarium.config.IConfigCategory;
import stellarium.config.IConfigProperty;
import stellarium.config.IMConfigProperty;
import stellarium.config.IPropertyRelation;
import stellarium.config.IStellarConfig;
import stellarium.config.json.JsonConfigProperty.CfgElement;

public class JsonConfigCategory implements IConfigCategory {

	protected String id, dispname;
	protected JsonConfigHandler cfg;
	protected JsonConfigCategory parcat;
	protected boolean isImmutable;
	
	protected JsonObject jobj;
	
	public JsonConfigCategory(JsonConfigHandler pcfg, JsonObject obj, String pid)
	{
		this(pcfg, null, obj, pid);
	}
	
	public JsonConfigCategory(JsonConfigHandler pcfg, JsonConfigCategory pcat, JsonObject obj, String pid)
	{
		cfg = pcfg;
		jobj = obj;
		parcat = pcat;
		id = pid;
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
		if(!cfg.modifiable || isImmutable || dispname.equals(name))
			return;
		
		String prev = dispname;
		dispname = name;
		
		for(ICfgArrMListener list : cfg.listenList)
			list.onDispNameChange(this, prev);
	}

	@Override
	public IStellarConfig getConfig() {
		return cfg;
	}

	@Override
	public IConfigCategory getParCategory() {
		return parcat;
	}

	
	protected Map<String, JsonConfigProperty> propmap = Maps.newHashMap();
	protected Map<String, List> proprels = Maps.newHashMap();

	
	@Override
	public <T> IConfigProperty<T> addProperty(String proptype, String propname,
			T def) {
		if(propmap.containsKey(propname))
			// TODO Loading Thingy
			return propmap.get(propname);
		
		JsonConfigProperty jcp = new JsonConfigProperty(this, proptype, propname, def);
		
		propmap.put(propname, jcp);
		
		if(jcp.isSingular())
		{
			String sub = (String) jcp.namelist.get(0);
			CfgElement el = jcp.getElementProt(sub);
			el.addToJson(jobj, propname);
		} else {
			JsonObject prop;
			
			if(jobj.has(propname) && jobj.get(propname).isJsonObject())
			{
				prop = jobj.getAsJsonObject(propname);
			} else {
				prop = new JsonObject();
				jobj.add(propname, prop);
			}
			
			for(Object sub1 : jcp.namelist)
			{
				String sub = (String) sub1;
				CfgElement el = jcp.getElementProt(sub);
				el.addToJson(prop, sub);
			}
		}
		
		return jcp;
	}

	@Override
	public void removeProperty(String propname) {
		
		if(!propmap.containsKey(propname))
			return;
		
		JsonConfigProperty jcp = propmap.get(propname);
		propmap.remove(propname);
		// TODO Something..?
		
		if(jcp.isSingular())
		{
			jobj.remove(propname);
		} else {
			JsonObject prop = jobj.getAsJsonObject(propname);
			
			for(Object sub1 : jcp.namelist)
			{
				String sub = (String) sub1;
				prop.remove(sub);
			}
			
			jobj.remove(propname);
		}
		
		//Clear Relations
		List<PropertyRelation> lr = proprels.get(propname);
		
		for(PropertyRelation pr : lr)
		{
			for(IConfigProperty prop : pr.relprops)
				proprels.get(prop.getName()).remove(pr);
		}
	}

	@Override
	public <T> IConfigProperty<T> getProperty(String propname) {
		return propmap.get(propname);
	}
	
	public void setExpl(JsonConfigProperty jcp, String expl)
	{
		cfg.setExpl(jobj.get(jcp.getName()), expl);
	}
	
	

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

	protected class PropertyRelation {
		public IPropertyRelation proprel;
		public IConfigProperty[] relprops;
		
		public PropertyRelation(IPropertyRelation propr, IConfigProperty... relp)
		{
			proprel = propr;
			relprops = relp;
		}
	}
}
