package stellarium.config.json;

import java.io.IOException;
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
import stellarium.config.core.CategoryContainer;
import stellarium.config.core.ConfigEntry;
import stellarium.config.core.IConfigCatCfg;
import stellarium.config.core.StellarConfigHandler;

public class JsonConfigHandler extends StellarConfigHandler implements IStellarConfig {
	
	private static String cat = "__category";
	
	protected IJsonContainer con;
	
	protected JsonCommentedObj jobj;
	
	public JsonConfigHandler(IJsonContainer pcon, ConfigDataRegistry.ConfigRegistryData data)
	{
		super(data);
		this.con = pcon;
	}
	
	
	public JsonConfigHandler(IJsonContainer pcon,
			String cid, IConfigFormatter subFormatter, IConfigurableData subData) {
		super(cid, subFormatter, subData);
		this.con = pcon;
	}
	
	@Override
	public StellarConfigHandler newSubConfig(String cid, String title,
			IConfigFormatter formatter, IConfigurableData data) {
		return new JsonConfigHandler(con.makeSubContainer(cid), title,
					formatter, data);
	}


	@Override
	public void setCategoryType(EnumCategoryType t) {
		
		super.setCategoryType(t);
		
		if(t != EnumCategoryType.ConfigList)
		{
			jobj = con.readJson();
		}
	}
	

	@Override
	public void setModifiable(boolean modif, boolean warn) { }

	@Override
	public void markImmutable(IConfigCategory cat) {
		JsonConfigCategory jcat = (JsonConfigCategory) cat;
		jcat.isImmutable = true;
	}

	@Override
	public boolean isImmutable(IConfigCategory cat) {
		return ((JsonConfigCategory)cat).isImmutable;
	}

	
	@Override
	public IConfigCategory newCategory(String cid) {
		JsonObject tobj;
		
		if(jobj.getObj().has(cid))
			tobj = jobj.getObj().getAsJsonObject(cid);
		else {
			tobj = new JsonObject();
			jobj.add(cid, tobj);
			tobj.addProperty(cat, true);
		}
		
		return new JsonConfigCategory(this, tobj, cid);
	}

	@Override
	public IConfigCatCfg newCfgCategory(String cid) {
		return new JsonConfigCatCfg(this, cid);
	}
	
	@Override
	public void postAdded(IConfigCategory cat) {
		if(cattype == EnumCategoryType.ConfigList)
			((StellarConfigHandler)this.getSubConfig(cat)).onFormat();
	}
	
	@Override
	public void onRemoveCategory(String cid) {
		if(cattype == EnumCategoryType.ConfigList)
			con.removeSubContainer(cid);
		else jobj.remove(cid);
	}


	@Override
	public IConfigCategory addSubCategory(IConfigCategory parent, String subid) {
		
		if(cattype != EnumCategoryType.Tree)
			return null;
		
		JsonObject sub;
		
		JsonConfigCategory par = (JsonConfigCategory) parent;
		
		if(par.jobj.has(subid))
			sub = par.jobj.getAsJsonObject(subid);
		else {
			sub = new JsonObject();
			par.jobj.add(subid, sub);
			sub.addProperty(cat, true);
		}
				
		IConfigCategory jcat = new JsonConfigCategory(this, par, sub, subid);
		catcon.addCategory(jcat);
		
		for(ICfgArrMListener list : listenList)
			list.onNew(jcat);
		
		return jcat;
	}

	@Override
	public void removeSubCategory(IConfigCategory parent, String subid) {
		
		if(catcon.getSubCategory(parent, subid) == null)
			return;
		
		JsonConfigCategory cat = (JsonConfigCategory) catcon.getSubCategory(parent, subid);
		
		for(IConfigCategory sub : catcon.getAllSubCategories(cat))
			this.removeSubCategory(cat, sub.getID());
		
		for(ICfgArrMListener list : listenList)
			list.onRemove(cat);
		
		catcon.removeSubCategory(parent, subid);
		
		JsonConfigCategory par = (JsonConfigCategory) parent;
		par.jobj.remove(subid);
		
	}
	
	
	public void setExpl(ConfigEntry ent, String expl)
	{
		jobj.setComment(ent, expl);
	}

	
	@Override
	public void addLoadFailMessage(String title, ICfgMessage msg) {
		con.addLoadFailMessage(title, msg);
	}


	@Override
	public void loadCategories() {
		switch(cattype)
		{
		case ConfigList:
			for(String scid : con.getAllSubContainerNames())
				this.addCategory(scid);
			
			break;
			
		case List:			
			jobj = con.readJson();
			
			for(Entry<String, JsonElement> ent: jobj.getObj().entrySet())
			{
				if(ent.getValue().isJsonObject() && ent.getValue().getAsJsonObject().has(cat))
					this.addCategory(ent.getKey());
			}
			
			break;
		case Tree:
			
			jobj = con.readJson();
			
			addSubCategories(null, jobj.getObj());
			
			break;
		}
	}
	
	@Override
	public void onSave() {
		super.onSave();
		
		if(this.cattype != EnumCategoryType.ConfigList)
			con.writeJson(jobj);
	}
		
	private void addSubCategories(JsonConfigCategory ca, JsonObject jo)
	{
		for(Entry<String, JsonElement> ent: jo.entrySet())
		{
			if(ent.getValue().isJsonObject())
			{
				JsonObject je = ent.getValue().getAsJsonObject();
				
				if(je.has(cat))
				{
					JsonConfigCategory subc;
					
					if(ca != null)
						subc = (JsonConfigCategory) this.addSubCategory(ca, ent.getKey());
					else subc = (JsonConfigCategory) this.addCategory(ent.getKey());
					
					addSubCategories(subc, je);
				}
			}
		}
	}

}
