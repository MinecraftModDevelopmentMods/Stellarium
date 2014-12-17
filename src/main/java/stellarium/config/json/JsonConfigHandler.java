package stellarium.config.json;

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
	
	protected List<ICfgArrMListener> listenList = Lists.newArrayList();
	protected IJsonContainer con;
	protected EnumCategoryType cattype = EnumCategoryType.List;
	protected boolean modifiable = false;
	
	protected CategoryContainer catcon;
	
	public JsonConfigHandler(IJsonContainer pcon)
	{
		con = pcon;
	}
	
	
	@Override
	public void setCategoryType(EnumCategoryType t) {
		cattype = t;
		catcon = CategoryContainer.newCatContainer(t);
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
		else jcat = new JsonConfigCategory(this, cid);
		catcon.addCategory(jcat);
		// TODO Auto-generated method stub
		
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
	
		// TODO Auto-generated method stub

	}

	@Override
	public IConfigCategory getCategory(String cid) {
		return catcon.getCategory(cid);
	}

	@Override
	public List<IConfigCategory> getAllCategories() {
		// TODO Auto-generated method stub
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
		
		if(cattype != EnumCategoryType.ConfigList)
			return null;
		
		IConfigCategory jcat = new JsonConfigCategory(this, (JsonConfigCategory) parent, subid);
		catcon.addCategory(jcat);
		// TODO Auto-generated method stub
		
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
		
		// TODO Auto-generated method stub

	}

	@Override
	public IConfigCategory getSubCategory(IConfigCategory parent, String subid) {
		return catcon.getSubCategory(parent, subid);
	}

	@Override
	public List<IConfigCategory> getAllSubCategories(IConfigCategory parent) {
		// TODO Auto-generated method stub
		return catcon.getAllSubCategories(parent);
	}

	
	public Map<String, String> msgMap = Maps.newHashMap();
	
	@Override
	public void addLoadFailMessage(String title, String msg) {
		msgMap.put(title, msg);
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
			// TODO Handling for List Loading
			
			JsonObject jo = con.readJson();
			
			for(Entry<String, JsonElement> ent: jo.entrySet())
			{
				if(ent.getValue().isJsonObject())
					catcon.addCategory(new JsonConfigCategory(this, ent.getKey()));
			}
			
			break;
		case Tree:
			// TODO Handling for Tree Loading
			JsonObject tjo = con.readJson();
			addSubCategories(tjo);
			
			break;
		}

	}
	
	private static String cat = "category";
	
	private void addSubCategories(JsonObject jo)
	{
		for(Entry<String, JsonElement> ent: jo.entrySet())
		{
			if(ent.getValue().isJsonObject())
			{
				JsonObject je = ent.getValue().getAsJsonObject();
				
				if(je.has(cat))
					catcon.addCategory(new JsonConfigCategory(this, ent.getKey()));
			}
		}
	}

}
