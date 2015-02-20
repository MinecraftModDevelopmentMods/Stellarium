package stellarium.catalog.cfgmanager;

import java.io.File;

import stellarium.catalog.CCatalogCfgData;
import stellarium.catalog.ICatalogCfgManager;
import stellarium.catalog.ICatalogDataHandler;
import stellarium.config.IStellarConfig;
import stellarium.config.file.FileJsonContainer;
import stellarium.config.json.JsonConfigHandler;

public class FileCfgManager implements ICatalogCfgManager {
	
	private IStellarConfig cfg;
	private CCatalogCfgData data;
	
	public FileCfgManager(File cfgdir)
	{
		cfgdir.mkdirs();
		
		FileJsonContainer root = FileJsonContainer.getRootContainer(cfgdir, "catalog");
		cfg = new JsonConfigHandler(root);
		data = new CCatalogCfgData(false);
	}

	@Override
	public void onFormat() {
		data.formatConfig(cfg);
	}

	@Override
	public void onApply() {
		data.applyConfig(cfg);
	}

	@Override
	public void onSave() {
		data.saveConfig(cfg);
	}

	@Override
	public CCatalogCfgData getDataHandler() {
		return data;
	}

}
