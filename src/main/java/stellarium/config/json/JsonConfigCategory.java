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
import stellarium.config.core.ConfigEntry;
import stellarium.config.core.StellarConfigCategory;
import stellarium.config.json.JsonConfigProperty.CfgElement;

public class JsonConfigCategory extends StellarConfigCategory implements IConfigCategory {

	protected boolean isImmutable;
	
	protected Map<String, JsonConfigProperty> propmap = Maps.newHashMap();
	
	protected JsonObject jobj;
	
	public JsonConfigCategory(JsonConfigHandler pcfg, JsonObject obj, String pid)
	{
		this(pcfg, null, obj, pid);
	}
	
	public JsonConfigCategory(JsonConfigHandler pcfg, JsonConfigCategory pcat, JsonObject obj, String pid)
	{
		super(pcfg, pcat, pid);
		jobj = obj;
	}

	
	@Override
	public <T> IConfigProperty<T> addProperty(String proptype, String propname,
			T def) {
		if(propmap.containsKey(propname))
			return propmap.get(propname);
		
		JsonConfigProperty jcp = new JsonConfigProperty(this, proptype, propname, def);
		
		propmap.put(propname, jcp);
		
		if(jcp.isSingular() && !jobj.has(propname) && jobj.get(propname).isJsonPrimitive())
		{
			CfgElement el = jcp.getElementProt(propname);
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
				
				if(!prop.has(sub))
				{
					CfgElement el = jcp.getElementProt(sub);
					el.addToJson(prop, sub);
				}
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
		((JsonConfigHandler) handler).setExpl(new ConfigEntry(this.getConfigEntry(), jcp.getName()), expl);
	}
}
