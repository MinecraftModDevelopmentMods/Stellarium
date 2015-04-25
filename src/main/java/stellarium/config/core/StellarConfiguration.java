package stellarium.config.core;

import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;

import stellarium.config.ConfigDataRegistry;
import stellarium.config.EnumCategoryType;
import stellarium.config.ICfgArrMListener;
import stellarium.config.ICfgMessage;
import stellarium.config.IConfigCategory;
import stellarium.config.IConfigFormatter;
import stellarium.config.IConfigurableData;
import stellarium.config.ILoadSaveHandler;
import stellarium.config.IStellarConfig;
import stellarium.config.core.handler.IConfigHandler;
import stellarium.config.json.JsonCfgCatHandler;
import stellarium.config.util.CfgCategoryIterator;

public class StellarConfiguration implements IStellarConfig, ILoadSaveHandler {
	
	public String title;
	protected final IConfigFormatter formatter;
	protected final IConfigurableData data;
	
	protected IConfigHandler handler;
	protected IConfigHandler invhandler;
	
	protected List<ICfgArrMListener> listenList = Lists.newArrayList();
	
	protected EnumCategoryType cattype;
	protected ICategoryEntry root;
	
	private boolean loadingSuccess = true;
	private boolean modifiable, warn;
	
	public StellarConfiguration(ConfigDataRegistry.ConfigRegistryData regdata)
	{
		this(regdata.title, regdata.formatter, regdata.data);
	}
	
	public StellarConfiguration(String title, IConfigFormatter formatter, IConfigurableData data)
	{
		this.title = title;
		this.formatter = formatter;
		this.data = data;
	}
	
	/**Should be called on first time*/
	public StellarConfiguration setHandler(IConfigHandler handler)
	{
		this.handler = handler;
		return this;
	}
	
	public IConfigHandler getHandler()
	{
		return this.handler;
	}
	
	public void setInvHandler(IConfigHandler inverthandler)
	{
		this.invhandler = inverthandler;
	}

	public IConfigHandler getInvHandler() {
		return invhandler;
	}

	
	@Override
	public void setCategoryType(EnumCategoryType t) {
		if(cattype != t) {
			cattype = t;
			root = t.getRootEntry(this);
			
			handler.setCategoryType(t);
			if(invhandler != null)
				invhandler.setCategoryType(t);
		}
	}
	
	public EnumCategoryType getCategoryType() {
		return cattype;
	}
	
	@Override
	public void setModifiable(boolean modif, boolean warn) {
		this.modifiable = modif;
		this.warn = warn;
		handler.setModifiable(modif, warn);
		if(invhandler != null)
			invhandler.setModifiable(modif, warn);
	}

	@Override
	public void addAMListener(ICfgArrMListener list) {
		listenList.add(list);
	}
	
	
	@Override
	public ICategoryEntry getRootEntry() {
		return this.root;
	}
	
	@Override
	public IStellarConfig getSubConfig(IConfigCategory cat) {
		return this.getSubConfigRaw(cat);
	}
	
	public StellarConfiguration getSubConfigRaw(IConfigCategory cat) {
		if(cattype.isConfigList() && cat instanceof StellarConfigCatCfg)
			return ((StellarConfigCatCfg) cat).getSubConfig();
		
		return null;
	}
	
	public boolean canMakeCategory(ICategoryEntry parent,
			String name) {
		for(ICfgArrMListener list : listenList)
		{
			if(!list.canCreate(parent, name))
				return false;
		}
		
		return true;
	}
	
	public StellarConfigCategory newCategory(ICategoryEntry parent, String name)
	{
		for(ICfgArrMListener list : listenList)
		{
			if(!list.canCreate(parent, name))
				return null;
		}
		
		if(cattype.isConfigList())
		{
			StellarConfigCatCfg category = new StellarConfigCatCfg(this, name);
			
			StellarConfiguration subConfig = new StellarConfiguration(name, 
					formatter.getSubFormatter(name), data.getSubData(name));
			
			category.setSubConfig(subConfig.setHandler(handler.getNewSubCfg(subConfig)));
			
			if(invhandler != null)
				subConfig.setInvHandler(invhandler.getNewSubCfg(subConfig));
			
			subConfig.onFormat();
			
			return category;
		} else {
			StellarConfigCategory category = new StellarConfigCategory(this, name);
			
			return category;
		}
	}
	
	public void postCreated(StellarConfigCategory theCategory) {
		theCategory.setHandler(handler.getNewCat(theCategory));
		if(invhandler != null)
			theCategory.setInvHandler(invhandler.getNewCat(theCategory));
		
		handler.onPostCreated(theCategory);
		if(invhandler != null)
			invhandler.onPostCreated(theCategory);
		
		for(ICfgArrMListener list : listenList)
			list.onPostCreated(theCategory);
	}
	
	public void preRemoved(StellarConfigCategory theCategory) {
		handler.onRemove(theCategory);
		if(invhandler != null)
			invhandler.onRemove(theCategory);
		
		for(ICfgArrMListener list : listenList)
			list.onRemove(theCategory);
	}
	
	public boolean canMigrate(ICategoryEntry parentEntry, String name, ICategoryEntry prevEntry) {
		for(ICfgArrMListener list : listenList)
			if(!list.canMigrate(parentEntry, name, prevEntry))
				return false;
		return true;
	}

	
	public void onMigrate(StellarConfigCategory theCategory, ICategoryEntry prevEntry) {
		handler.onMigrate(theCategory, prevEntry);
		if(invhandler != null)
			invhandler.onMigrate(theCategory, prevEntry);
		
		for(ICfgArrMListener list : listenList)
			list.onMigrate(theCategory, prevEntry);
	}
	
	public StellarConfigCategory copyCategory(ICategoryEntry parent, String name, IConfigCategory catFrom)
	{
		for(ICfgArrMListener list : listenList)
		{
			if(!list.canCreate(parent, name))
				return null;
		}
		
		if(cattype.isConfigList())
		{
			StellarConfigCatCfg category = new StellarConfigCatCfg(this, name);
			
			StellarConfiguration subConfig = new StellarConfiguration(name, 
					formatter.getSubFormatter(name), data.getSubData(name));
			
			category.setSubConfig(subConfig.setHandler(handler.getNewSubCfg(subConfig)));
			
			if(invhandler != null)
				subConfig.setInvHandler(invhandler.getNewSubCfg(subConfig));
			
			return category;
		} else {
			StellarConfigCategory category = new StellarConfigCategory(this, name);
			
			return category;
		}
	}
	
	public void postCopied(StellarConfigCategory theCategory, IConfigCategory catFrom) {
		
		theCategory.setHandler(handler.getNewCat(theCategory));
		if(invhandler != null)
			theCategory.setInvHandler(invhandler.getNewCat(theCategory));
		
		theCategory.copy(catFrom);
		
		handler.onPostCreated(theCategory);
		if(invhandler != null)
			invhandler.onPostCreated(theCategory);
		
		for(ICfgArrMListener list : listenList)
			list.onPostCreated(theCategory);
	}
	
	public boolean preNameChange(StellarConfigCategory theCategory, String name) {
		return handler.isValidNameChange(theCategory, name);
	}

	public void postNameChange(StellarConfigCategory theCategory, String prename) {
		handler.onNameChange(theCategory, prename);
		if(invhandler != null)
			invhandler.onNameChange(theCategory, prename);
		
		for(ICfgArrMListener list : listenList)
			list.onNameChange(theCategory, prename);
	}

	
	@Override
	public void loadCategories() {
		CfgCategoryIterator iterator = new CfgCategoryIterator(this);
		while(iterator.hasNext()) {
			StellarConfigCategory category = (StellarConfigCategory) iterator.next();
			for(ICfgArrMListener list : listenList)
			{
				list.canCreate(category.getCategoryEntry().getParentEntry(), category.getName());
				list.onPostCreated(category);
			}
		}
		
		handler.loadCategories(this);
	}

	@Override
	public void addLoadFailMessage(String title, ICfgMessage msg) {
		handler.addLoadFailMessage(title, msg);
		loadingSuccess = false;
	}
	
	
	@Override
	public void onFormat() {
		if(this.cattype != null)
			this.onRefresh();
		
		formatter.formatConfig(this);
		
		if(this.cattype != null && cattype.isConfigList())
		{
			CfgCategoryIterator iterator = new CfgCategoryIterator(this);
			while(iterator.hasNext()) {
				StellarConfigCatCfg category = (StellarConfigCatCfg) iterator.next();
				category.subhandler.onFormat();
			}
		}
	}

	private void onRefresh() {
		listenList.clear();
		
		CfgCategoryIterator iterator = new CfgCategoryIterator(this);
		
		while(iterator.hasNext()) {
			StellarConfigCategory category = (StellarConfigCategory) iterator.next();
			category.onRefresh();
		}
		
		handler.setCategoryType(this.cattype);
		if(invhandler != null)
			invhandler.setCategoryType(this.cattype);
		
		this.setModifiable(this.modifiable, this.warn);
		
		iterator = new CfgCategoryIterator(this);
		while(iterator.hasNext()) {
			StellarConfigCategory category = (StellarConfigCategory) iterator.next();
			category.setHandler(handler.getNewCat(category));
			handler.onPostCreated(category);
			
			if(this.invhandler != null)
			{
				category.setInvHandler(invhandler.getNewCat(category));
				invhandler.onPostCreated(category);
			}
		}
	}

	@Override
	public void onApply() {
		data.applyConfig(this);
		if(this.loadingSuccess && invhandler != null)
			invhandler.onSave(this);
		
		this.loadingSuccess = true;
	}

	@Override
	public void onSave() {
		data.saveConfig(this);
		handler.onSave(this);
	}

}
