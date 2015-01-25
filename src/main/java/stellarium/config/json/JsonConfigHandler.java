package stellarium.config.json;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.*;

import stellarium.config.EnumCategoryType;
import stellarium.config.ICfgArrMListener;
import stellarium.config.IConfigCategory;
import stellarium.config.IStellarConfig;
import stellarium.config.core.CategoryContainer;

public class JsonConfigHandler implements IStellarConfig {
	
	private static String cat = "__category";
	
	protected List<ICfgArrMListener> listenList = Lists.newArrayList();
	protected IJsonContainer con;
	protected EnumCategoryType cattype = EnumCategoryType.List;
	protected boolean modifiable = false;
	
	protected CategoryContainer catcon;
	
	protected JsonObject jobj;
	
	public JsonConfigHandler(IJsonContainer pcon)
	{
		con = pcon;
	}
	
	
	@Override
	public void setCategoryType(EnumCategoryType t) {
		cattype = t;
		
		catcon = CategoryContainer.newCatContainer(t);
		if(t != EnumCategoryType.ConfigList)
		{
			try {
				jobj = con.readJson();
			} catch (IOException e) {
				//TODO IOException Handling
				e.printStackTrace();
			}
		}
	}

	@Override
	public void setModifiable(boolean modif, boolean warn) {
		modifiable = modif;
	}

	@Override
	public void addAMListener(ICfgArrMListener list) {
		listenList.add(list);
	}

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
	public IConfigCategory addCategory(String cid) {
		if(!modifiable)
			return null;
		
		IConfigCategory jcat;
		if(cattype == EnumCategoryType.ConfigList)
		{
			JsonConfigHandler handle = new JsonConfigHandler(con.makeSubContainer(cid));
			jcat = new JsonConfigCatCfg(this, handle, cid);
		}
		else {
			JsonObject tobj = new JsonObject();
			
			jobj.add(cid, tobj);
			tobj.addProperty(cat, true);
			
			jcat = new JsonConfigCategory(this, tobj, cid);
		}
		
		catcon.addCategory(jcat);
		
		for(ICfgArrMListener list : listenList)
			list.onNew(jcat);
		
		return jcat;
	}

	@Override
	public void removeCategory(String cid) {
		if(!modifiable)
			return;
		if(catcon.getCategory(cid) == null || ((JsonConfigCategory)catcon.getCategory(cid)).isImmutable)
			return;
		
		JsonConfigCategory cat = (JsonConfigCategory) catcon.getCategory(cid);
		
		for(IConfigCategory sub : catcon.getAllSubCategories(cat))
			this.removeSubCategory(cat, sub.getID());
		
		for(ICfgArrMListener list : listenList)
			list.onRemove(cat);
		
		catcon.removeCategory(cid);
		
		if(cattype == EnumCategoryType.ConfigList)
			con.removeSubContainer(cid);
		else jobj.remove(cid);

	}

	@Override
	public IConfigCategory getCategory(String cid) {
		return catcon.getCategory(cid);
	}

	@Override
	public List<IConfigCategory> getAllCategories() {
		return catcon.getAllCategories();
	}

	@Override
	public IStellarConfig getSubConfig(IConfigCategory cat) {
		if(cattype != EnumCategoryType.ConfigList)
			return null;
		
		return ((JsonConfigCatCfg)cat).getHandler();
	}

	@Override
	public IConfigCategory addSubCategory(IConfigCategory parent, String subid) {
		if(!modifiable)
			return null;
		
		if(cattype != EnumCategoryType.Tree)
			return null;
		
		JsonObject sub = new JsonObject();
		
		JsonConfigCategory par = (JsonConfigCategory) parent;
		par.jobj.add(subid, sub);
		sub.addProperty(cat, true);
		
		IConfigCategory jcat = new JsonConfigCategory(this, par, sub, subid);
		catcon.addCategory(jcat);
		
		for(ICfgArrMListener list : listenList)
			list.onNew(jcat);
		
		return jcat;
	}

	@Override
	public void removeSubCategory(IConfigCategory parent, String subid) {
		if(!modifiable)
			return;
		
		if(catcon.getSubCategory(parent, subid) == null
				|| ((JsonConfigCategory)catcon.getSubCategory(parent, subid)).isImmutable)
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

	@Override
	public IConfigCategory getSubCategory(IConfigCategory parent, String subid) {
		return catcon.getSubCategory(parent, subid);
	}

	@Override
	public List<IConfigCategory> getAllSubCategories(IConfigCategory parent) {
		return catcon.getAllSubCategories(parent);
	}

	
	@Override
	public void addLoadFailMessage(String title, String msg) {
		con.addLoadFailMessage(title, msg);
	}


	@Override
	public void loadCategories() {
		switch(cattype)
		{
		case ConfigList:
			for(String scid : con.getAllSubContainerNames())
			{
				JsonConfigHandler cjh = new JsonConfigHandler(con.getSubContainer(scid));
				catcon.addCategory(new JsonConfigCatCfg(this, cjh, scid));
			}
			
			break;
			
		case List:			
			JsonObject jo = null;
			
			try {
				jo = con.readJson();
			} catch (IOException e) {
				//TODO IOException Handling
				e.printStackTrace();
			}
			
			for(Entry<String, JsonElement> ent: jo.entrySet())
			{
				if(ent.getValue().isJsonObject() && ent.getValue().getAsJsonObject().has(cat))
					catcon.addCategory(new JsonConfigCategory(this, ent.getValue().getAsJsonObject(), ent.getKey()));
			}
			
			break;
		case Tree:
			
			JsonObject tjo = null;
			try {
				tjo = con.readJson();
			} catch (IOException e) {
				//TODO IOException Handling
				e.printStackTrace();
			}
			
			addSubCategories(null, tjo);
			
			break;
		}
	}
	
	public void saveJson()
	{
		if(this.cattype == EnumCategoryType.ConfigList)
		{
			for(IConfigCategory cat : this.getAllCategories())
				((JsonConfigHandler) cat.getConfig()).saveJson();
		}
		else con.writeJson(jobj);
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
					{
						catcon.addSubCategory(ca, new JsonConfigCategory(this, ca, je, ent.getKey()));
						subc = (JsonConfigCategory) catcon.getSubCategory(ca, ent.getKey());
					}
					else {
						catcon.addCategory(new JsonConfigCategory(this, je, ent.getKey()));
						subc = (JsonConfigCategory) catcon.getCategory(ent.getKey());
					}
					addSubCategories(subc, je);
				}
			}
		}
	}

}
