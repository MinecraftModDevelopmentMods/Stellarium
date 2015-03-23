package stellarium.config.core;

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

}
