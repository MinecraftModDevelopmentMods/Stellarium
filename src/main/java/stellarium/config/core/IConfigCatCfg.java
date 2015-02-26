package stellarium.config.core;

import stellarium.config.IConfigCategory;
import stellarium.config.IStellarConfig;
import stellarium.config.json.JsonConfigHandler;

public interface IConfigCatCfg extends IConfigCategory {

	public void setHandler(IStellarConfig handle);

	public IStellarConfig getHandler();
	
}
