package stellarium.catalog.cfgmanager;

import stellarium.catalog.CCatalogCfgData;
import stellarium.catalog.ICatalogCfgManager;
import stellarium.catalog.ICatalogDataHandler;
import stellarium.config.IStellarConfig;

public class GuiCfgManager implements ICatalogCfgManager {
	
	private IStellarConfig cfg;
	private FileCfgManager fm;
	
	public GuiCfgManager(FileCfgManager fm)
	{
		this.fm = fm;
	}

	@Override
	public void onFormat() {
		fm.getDataHandler().formatConfig(cfg);
	}

	/**
	 * Loading GUI Settings
	 * */
	@Override
	public void onApply() {
		fm.onApply();
		fm.getDataHandler().saveConfig(cfg);
	}

	/**
	 * Saving GUI Settings
	 * */
	@Override
	public void onSave() {
		fm.getDataHandler().applyConfig(cfg);
		fm.onSave();
	}

	@Override
	public CCatalogCfgData getDataHandler() {
		return fm.getDataHandler();
	}

}
