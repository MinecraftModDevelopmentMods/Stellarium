package stellarium.config.core;

import stellarium.config.IConfigCategory;

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
	
	public void copy(IConfigCategory category) {
		StellarConfigCatCfg cfgcat = (StellarConfigCatCfg) category;
		StellarConfiguration hcopy = cfgcat.config;
		copyEntry(subhandler.getRootEntry(), hcopy.getRootEntry());
	}

	private void copyEntry(ICategoryEntry to, ICategoryEntry from) {
		for(ICategoryEntry subFrom : from)
		{
			IConfigCategory subToCat = to.copyCategory(subFrom.getCategory(), subFrom.getName(), EnumPosOption.Child);
			ICategoryEntry subTo = subToCat.getCategoryEntry();
			copyEntry(subTo, subFrom);
		}
	}

}
