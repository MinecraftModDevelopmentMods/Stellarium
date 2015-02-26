package stellarium.config.gui;

import stellarium.config.IStellarConfig;
import stellarium.config.core.IConfigCatCfg;

public class GuiCfgCatCfgHandler extends GuiCfgCatHandler implements IConfigCatCfg {

	private IStellarConfig handler;
	
	public GuiCfgCatCfgHandler(GuiConfigHandler handler, String cid) {
		super(handler, cid);
	}

	@Override
	public void setHandler(IStellarConfig handle) {
		this.handler = handle;
	}

	@Override
	public IStellarConfig getHandler() {
		return handler;
	}


}
