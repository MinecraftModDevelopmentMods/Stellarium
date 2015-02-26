package stellarium.config.gui;

import java.util.List;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;

import cpw.mods.fml.client.config.GuiButtonExt;
import stellarium.config.ConfigDataRegistry;
import stellarium.config.EnumCategoryType;
import stellarium.config.ICfgArrMListener;
import stellarium.config.ICfgMessage;
import stellarium.config.IConfigCategory;
import stellarium.config.IConfigFormatter;
import stellarium.config.IConfigurableData;
import stellarium.config.IStellarConfig;
import stellarium.config.core.CategoryContainer;
import stellarium.config.core.IConfigCatCfg;
import stellarium.config.core.StellarConfigHandler;
import stellarium.config.json.JsonConfigCategory;
import stellarium.lang.CPropLangUtil;

public class GuiConfigHandler extends StellarConfigHandler implements IStellarConfig, ICfgChangeNotifier {
	
	protected boolean modifiable = false;
	protected boolean warn = false;
	
	protected List<String> loadfails = Lists.newArrayList();
	
	public GuiConfigHandler(ConfigDataRegistry.ConfigRegistryData data)
	{
		super(data);
	}
	
	
	public GuiConfigHandler(String cid, IConfigFormatter subFormatter, IConfigurableData subData) {
		super(cid, subFormatter, subData);
	}
	
	@Override
	public StellarConfigHandler newSubConfig(String cid, String title,
			IConfigFormatter formatter, IConfigurableData data) {
		return new GuiConfigHandler(title, formatter, data);
	}

	@Override
	public void setModifiable(boolean modif, boolean warn) {
		this.modifiable = modif;
		this.warn = warn;
	}

	@Override
	public void addAMListener(ICfgArrMListener list) {
		listenList.add(list);
	}

	@Override
	public void markImmutable(IConfigCategory cat) {
		GuiCfgCatHandler gcat = (GuiCfgCatHandler) cat;
		gcat.isImmutable = true;
		
		//TODO GUI Category Immutability change
		listener.onCfgChange(EnumCfgChangeType.AccessModified, gcat);
	}

	@Override
	public boolean isImmutable(IConfigCategory cat) {
		return ((GuiCfgCatHandler)cat).isImmutable;
	}

	
	@Override
	public IConfigCategory newCategory(String cid) {
		return new GuiCfgCatHandler(this, cid);
	}


	@Override
	public IConfigCatCfg newCfgCategory(String cid) {
		return new GuiCfgCatCfgHandler(this, cid);
	}
	
	@Override
	public void postAdded(IConfigCategory cat) {
		//TODO GUI Category add
		listener.onCfgChange(EnumCfgChangeType.Add, cat);
	}
	

	@Override
	public void onRemoveCategory(String cid) {
		IConfigCategory cat = this.getCategory(cid);
		
		//TODO GUI Category remove
		listener.onCfgChange(EnumCfgChangeType.Remove, cat);
	}

	@Override
	public IConfigCategory addSubCategory(IConfigCategory parent, String subid) {
		if(cattype != EnumCategoryType.Tree)
			return null;
				
		GuiCfgCatHandler par = (GuiCfgCatHandler) parent;
				
		IConfigCategory gcat = new GuiCfgCatHandler(this, par, subid);
		catcon.addCategory(gcat);
		
		for(ICfgArrMListener list : listenList)
			list.onNew(gcat);
		
		//TODO GUI SubCategory add
		listener.onCfgChange(EnumCfgChangeType.Add, gcat);
		
		return gcat;
	}

	@Override
	public void removeSubCategory(IConfigCategory parent, String subid) {
		if(catcon.getSubCategory(parent, subid) == null)
			return;
		
		GuiCfgCatHandler cat = (GuiCfgCatHandler) catcon.getSubCategory(parent, subid);
		
		for(IConfigCategory sub : catcon.getAllSubCategories(cat))
			this.removeSubCategory(cat, sub.getID());
		
		for(ICfgArrMListener list : listenList)
			list.onRemove(cat);
		
		//TODO GUI SubCategory remove
		listener.onCfgChange(EnumCfgChangeType.Remove, cat);
		
		catcon.removeSubCategory(parent, subid);
	}
	

	@Override
	public void loadCategories() {
		//GUI Configuration doesn't have any preloaded category.
	}
	
	
	public List<ICfgArrMListener> getAMListenerList() {
		return this.listenList;
	}

	
	@Override
	public void addLoadFailMessage(String title, ICfgMessage msg) {
		String out = String.format("[%s]: %s",
				CPropLangUtil.getLocalizedFromID(title), CPropLangUtil.getLocalizedMessage(msg));
		loadfails.add(out);
	}
	
	public boolean hasLoadFailMessages() {
		return !loadfails.isEmpty();
	}
	
	public List<String> getLoadFailMessages() {
		return loadfails;
	}
	

	private ICfgChangeListener listener = null;
	
	@Override
	public void setListener(ICfgChangeListener listener) {
		this.listener = listener;
	}

}
