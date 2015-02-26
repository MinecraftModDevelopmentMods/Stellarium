package stellarium.config.core;

import java.util.List;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;

import stellarium.config.ConfigDataRegistry;
import stellarium.config.EnumCategoryType;
import stellarium.config.ICfgArrMListener;
import stellarium.config.IConfigCategory;
import stellarium.config.IConfigFormatter;
import stellarium.config.IConfigurableData;
import stellarium.config.ILoadSaveHandler;
import stellarium.config.IStellarConfig;
import stellarium.config.json.JsonConfigCategory;

public abstract class StellarConfigHandler implements IStellarConfig, ILoadSaveHandler {
	
	protected final String title;
	protected final IConfigFormatter formatter;
	protected final IConfigurableData data;
	
	public List<ICfgArrMListener> listenList = Lists.newArrayList();
	
	protected EnumCategoryType cattype = EnumCategoryType.List;
	protected CategoryContainer catcon;
	
	public StellarConfigHandler(ConfigDataRegistry.ConfigRegistryData regdata)
	{
		this(regdata.title, regdata.formatter, regdata.data);
	}
	
	public StellarConfigHandler(String title, IConfigFormatter formatter, IConfigurableData data)
	{
		this.title = title;
		this.formatter = formatter;
		this.data = data;
	}
	
	abstract public StellarConfigHandler newSubConfig(String cid, String title,
			IConfigFormatter formatter, IConfigurableData data);

	
	@Override
	public void setCategoryType(EnumCategoryType t) {
		cattype = t;
		
		catcon = CategoryContainer.newCatContainer(t);
	}

	@Override
	public void addAMListener(ICfgArrMListener list) {
		listenList.add(list);
	}
	
	
	abstract public IConfigCategory newCategory(String cid);
	abstract public IConfigCatCfg newCfgCategory(String cid);
	abstract public void postAdded(IConfigCategory cat);

	@Override
	public IConfigCategory addCategory(String cid) {
		
		IConfigCategory cat;
		
		if(cattype == EnumCategoryType.ConfigList)
		{
			IConfigCatCfg cfgcat = this.newCfgCategory(cid);
			cfgcat.setHandler(this.newSubConfig(cid, cid,
					formatter.getSubFormatter(cfgcat), data.getSubData(cfgcat)));
			cat = cfgcat;
		}
		else cat = newCategory(cid);
		
		catcon.addCategory(cat);
		
		for(ICfgArrMListener list : listenList)
			list.onNew(cat);
		
		postAdded(cat);
		
		return cat;
	}
	
	abstract public void onRemoveCategory(String cid);

	@Override
	public void removeCategory(String cid) {
		
		if(catcon.getCategory(cid) == null)
			return;
		
		IConfigCategory cat = catcon.getCategory(cid);
		
		for(IConfigCategory sub : catcon.getAllSubCategories(cat))
			this.removeSubCategory(cat, sub.getID());
		
		for(ICfgArrMListener list : listenList)
			list.onRemove(cat);
		
		onRemoveCategory(cid);
		
		catcon.removeCategory(cid);
		
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
		return ((IConfigCatCfg) cat).getHandler();
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
	public void onFormat() {
		formatter.formatConfig(this);
	}

	@Override
	public void onApply() {
		if(cattype == EnumCategoryType.ConfigList)
		{
			for(IConfigCategory cat : this.getAllCategories())
			{
				StellarConfigHandler cfg = (StellarConfigHandler) this.getSubConfig(cat);
				cfg.onApply();
			}
		}
		else data.applyConfig(this);
	}

	@Override
	public void onSave() {
		if(cattype == EnumCategoryType.ConfigList)
		{
			for(IConfigCategory cat : this.getAllCategories())
			{
				StellarConfigHandler cfg = (StellarConfigHandler) this.getSubConfig(cat);
				cfg.onSave();
			}
		}
		else data.saveConfig(this);
	}

}
