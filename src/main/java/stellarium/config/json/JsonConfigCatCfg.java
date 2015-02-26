package stellarium.config.json;

import stellarium.config.IStellarConfig;
import stellarium.config.core.IConfigCatCfg;

public class JsonConfigCatCfg extends JsonConfigCategory implements IConfigCatCfg {

	protected IStellarConfig handler;
	
	public JsonConfigCatCfg(JsonConfigHandler pcfg, String pid) {
		super(pcfg, null, pid);
	}
	
	@Override
	public void setHandler(IStellarConfig handle)
	{
		this.handler = handle;
	}
	
	@Override
	public IStellarConfig getHandler() {
		return handler;
	}

}
