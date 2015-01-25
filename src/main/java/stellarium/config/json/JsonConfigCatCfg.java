package stellarium.config.json;

import stellarium.config.IStellarConfig;

public class JsonConfigCatCfg extends JsonConfigCategory {

	protected JsonConfigHandler handler;
	
	public JsonConfigCatCfg(JsonConfigHandler pcfg, JsonConfigHandler handle, String pid) {
		super(pcfg, null, pid);
		handler = handle;
	}
	
	public IStellarConfig getHandler() {
		return handler;
	}

}
