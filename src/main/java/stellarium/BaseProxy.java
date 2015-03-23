package stellarium;

import cpw.mods.fml.relauncher.Side;
import stellarium.catalog.StellarCatalogRegistry;
import stellarium.config.file.FileCfgManager;
import stellarium.settings.StellarSettings;

public interface BaseProxy {
	
	public void initSided(StellarSettings m);
	
	public void initCfgGui(FileCfgManager fm);
	
}
