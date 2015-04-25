package stellarium.config.core;

import stellarium.config.IConfigCategory;
import stellarium.config.core.handler.ICategoryHandler;
import stellarium.config.core.handler.NullCategoryHandler;

public class StellarConfigCatCfg extends StellarConfigCategory {
	
	protected StellarConfiguration subhandler;

	public StellarConfigCatCfg(StellarConfiguration handler, String name) {
		super(handler, name);
	}
	
	public void setSubConfig(StellarConfiguration handle) {
		this.subhandler = handle;
	}

	public StellarConfiguration getSubConfig() {
		return this.subhandler;
	}
	
	public void setHandler(ICategoryHandler handler) {
		super.setHandler(handler);
		subhandler.setHandler(config.getHandler().getNewSubCfg(subhandler));
	}
	
	public void setInvHandler(ICategoryHandler invhandler) {
		super.setInvHandler(invhandler);
		
		if(config.getInvHandler() != null)
			subhandler.setInvHandler(config.getInvHandler().getNewSubCfg(subhandler));
	}
	
	@Override
	public boolean setName(String name) {
		if(!super.setName(name))
			return false;
		
		subhandler.title = name;
		
		return true;
	}
	
	@Override
	public void copy(IConfigCategory category) {
		StellarConfigCatCfg cfgcat = (StellarConfigCatCfg) category;
		StellarConfiguration hcopy = cfgcat.subhandler;
		
		subhandler.setCategoryType(hcopy.getCategoryType());
		copyEntry(subhandler.getRootEntry(), hcopy.getRootEntry());
		
		subhandler.onFormat();
	}

	private void copyEntry(ICategoryEntry to, ICategoryEntry from) {
		if(!from.hasChildEntry())
			return;
		
		for(ICategoryEntry subFrom : from)
		{
			IConfigCategory subToCat = to.copyCategory(subFrom.getCategory(), subFrom.getName(), EnumPosOption.Child);
			ICategoryEntry subTo = subToCat.getCategoryEntry();
			
			copyEntry(subTo, subFrom);
		}
	}
}
