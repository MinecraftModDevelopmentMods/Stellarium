package stellarium.config.json;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
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
import stellarium.config.core.ICategoryEntry;
import stellarium.config.core.StellarConfigCategory;
import stellarium.config.core.StellarConfigCategory.PropertyRelation;
import stellarium.config.core.StellarConfigProperty;
import stellarium.config.core.handler.ICategoryHandler;
import stellarium.config.core.handler.IPropertyHandler;

public class JsonCfgCatHandler implements ICategoryHandler {
	
	protected JsonConfigHandler cfghandler;
	
	protected ICategoryEntry entry;
	
	protected Map<String, JsonCfgPropHandler> propmap = Maps.newHashMap();
	
	protected JsonObject loadedObj = null;
	
	public JsonCfgCatHandler(JsonConfigHandler pcfg, ICategoryEntry entry)
	{
		this.cfghandler = pcfg;
	}
	
	public JsonObject getLoadedJsonObject()
	{
		return this.loadedObj;
	}
	
	public void setLoadedJsonObject(JsonObject loadedObj)
	{
		this.loadedObj = loadedObj;
	}
	
	public boolean hasLoadedJsonObject()
	{
		return this.loadedObj != null;
	}

	
	@Override
	public IPropertyHandler getNewProp(StellarConfigProperty prop) {
		JsonElement elem = null;
		
		if(loadedObj != null && loadedObj.has(prop.getName()))
			elem = loadedObj.get(prop.getName());
		
		JsonCfgPropHandler jprop = new JsonCfgPropHandler(this, prop, prop.getName(), elem);
		propmap.put(prop.getName(), jprop);
		return jprop;
	}
	
	@Override
	public void onRemoveProp(StellarConfigProperty prop) {
		propmap.remove(prop.getName());
		
		if(loadedObj != null && loadedObj.has(prop.getName()))
			loadedObj.remove(prop.getName());
	}
	
	@Override
	public void onPropertyRelationAdded(PropertyRelation pr) { }

	@Override
	public void onPropertyRelationRemoved(PropertyRelation pr) { }
	
}
