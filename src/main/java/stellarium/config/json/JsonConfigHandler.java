package stellarium.config.json;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.*;

import stellarium.config.ConfigDataRegistry;
import stellarium.config.EnumCategoryType;
import stellarium.config.ICfgArrMListener;
import stellarium.config.ICfgMessage;
import stellarium.config.IConfigCategory;
import stellarium.config.IConfigFormatter;
import stellarium.config.IConfigurableData;
import stellarium.config.IStellarConfig;
import stellarium.config.core.EnumPosOption;
import stellarium.config.core.ICategoryEntry;
import stellarium.config.core.StellarConfiguration;
import stellarium.config.core.StellarConfigCategory;
import stellarium.config.core.handler.ICategoryHandler;
import stellarium.config.core.handler.IConfigHandler;

public class JsonConfigHandler implements IConfigHandler {
	
	protected static String CATEGORY_INDICATOR = "__category";
	
	protected EnumCategoryType cattype;
	
	protected IJsonContainer con;
	
	protected Map<ICategoryEntry, JsonCfgCatHandler> categoryMap = Maps.newHashMap();
	
	protected JsonObject jobj;
	
	public JsonConfigHandler(IJsonContainer pcon)
	{
		this.con = pcon;
		con.applyFactoryToGson(new JsonCfgTypeAdapterFactory(con.getPropertyWriter()));
	}
	
	
	@Override
	public IConfigHandler getNewSubCfg(StellarConfiguration subConfig) {
		return new JsonConfigHandler(con.makeSubContainer(subConfig.title));
	}


	@Override
	public void setCategoryType(EnumCategoryType t) {
		this.cattype = t;
		
		if(t != EnumCategoryType.ConfigList)
		{
			jobj = con.readJson();
		}
	}
	

	@Override
	public void setModifiable(boolean modif, boolean warn) { }
	
	@Override
	public void onMarkImmutable(StellarConfigCategory cat) { }

	
	@Override
	public ICategoryHandler getNewCat(StellarConfigCategory cat) {
		if(!cattype.isConfigList())
		{
			JsonCfgCatHandler category = new JsonCfgCatHandler(this, cat.getCategoryEntry());
			categoryMap.put(cat.getCategoryEntry(), category);
			return category;
		}
		else return null;
	}
	
	@Override
	public void onPostCreated(StellarConfigCategory cat) {
		if(!cattype.isConfigList())
		{
			ICategoryEntry entry = cat.getCategoryEntry();
			JsonCfgCatHandler jcat = categoryMap.get(entry);
			
			if(entry.getParentEntry().isRootEntry())
			{
				if(jobj.has(cat.getName()))
					jcat.setLoadedJsonObject(jobj.getAsJsonObject(cat.getName()));
			} else {
				JsonCfgCatHandler parcat = categoryMap.get(entry.getParentEntry());
				
				if(parcat.hasLoadedJsonObject())
					jcat.setLoadedJsonObject(parcat.getLoadedJsonObject().getAsJsonObject(cat.getName()));
			}
		}
	}
	
	
	@Override
	public void onRemove(StellarConfigCategory cat) {
		if(cattype.isConfigList())
		{
			con.removeSubContainer(cat.getName());
		} else {
			if(cat.getCategoryEntry().getParentEntry().isRootEntry())
			{
				jobj.remove(cat.getName());
			} else {
				ICategoryEntry entry = cat.getCategoryEntry().getParentEntry();
				JsonObject obj = categoryMap.get(entry).getLoadedJsonObject();
				if(obj != null)
					obj.remove(cat.getName());
			}
		}
	}
	
	
	@Override
	public boolean isValidNameChange(StellarConfigCategory cat, String postName) {
		return true;
	}


	@Override
	public void onNameChange(StellarConfigCategory cat, String before) {
		if(cattype.isConfigList())
		{
			con.moveSubContainer(before, cat.getName());
		} else {
			
			ICategoryEntry entry = cat.getCategoryEntry();
			
			if(entry.getParentEntry().isRootEntry())
			{
				if(jobj.has(before))
					jobj.add(cat.getName(), jobj.remove(before));
			} else {
				JsonCfgCatHandler parcat = categoryMap.get(entry.getParentEntry());
				
				if(parcat.hasLoadedJsonObject())
					parcat.getLoadedJsonObject().add(cat.getName(), parcat.getLoadedJsonObject().get(before));
			}
		}
	}
	
	@Override
	public void onMigrate(StellarConfigCategory cat, ICategoryEntry before) {
		if(cattype.isConfigList())
			return;
		
		JsonCfgCatHandler jcat = categoryMap.remove(before);
		categoryMap.put(cat.getCategoryEntry(), jcat);
		
		JsonElement tobj = null;
		
		if(jcat.hasLoadedJsonObject())
		{
			if(before.getParentEntry().isRootEntry())
			{
				if(jobj.has(before.getName()))
					tobj = jobj.remove(before.getName());
			} else {
				JsonCfgCatHandler parcat = categoryMap.get(before.getParentEntry());
				tobj = parcat.getLoadedJsonObject().remove(cat.getName());
			}
		}
		
		if(tobj != null)
		{
			ICategoryEntry entry = cat.getCategoryEntry();
			
			if(entry.getParentEntry().isRootEntry())
			{
				jobj.add(cat.getName(), tobj);
			} else {
				JsonCfgCatHandler parcat = categoryMap.get(entry.getParentEntry());
				
				if(parcat.hasLoadedJsonObject())
					parcat.getLoadedJsonObject().add(cat.getName(), tobj);
			}
		}
	}

	
	@Override
	public void addLoadFailMessage(String title, ICfgMessage msg) {
		con.addLoadFailMessage(title, msg);
	}


	@Override
	public void loadCategories(StellarConfiguration config) {
		if(cattype.isConfigList())
		{
			for(String name : con.getAllSubContainerNames())
				config.getRootEntry().createCategory(name, EnumPosOption.Child);
		} else {
			jobj = con.readJson();
			
			addSubCategories(config.getRootEntry(), jobj);
		}
	}
	
	@Override
	public void onSave(StellarConfiguration config) {
		
		if(!cattype.isConfigList())
			con.writeJson(this);
	}
		
	private void addSubCategories(ICategoryEntry entry, JsonObject jo)
	{
		for(Entry<String, JsonElement> ent: jo.entrySet())
		{
			if(ent.getValue().isJsonObject())
			{
				JsonObject je = ent.getValue().getAsJsonObject();
				
				if(je.has(CATEGORY_INDICATOR))
				{
					entry.createCategory(ent.getKey(), EnumPosOption.Child);
					
					addSubCategories(entry, je);
				}
			}
		}
	}

}
