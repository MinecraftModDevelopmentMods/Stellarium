package stellarium.catalog;

import stellarium.config.IStellarConfig;


public interface ICatalogCfgManager {

	public void onFormat();
	
	public void onApply();

	public void onSave();
	
	public CCatalogCfgData getDataHandler();

}
