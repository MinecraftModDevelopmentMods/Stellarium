package stellarium.config;

public interface IConfigFormatter {

	/**
	 * formats configuration to get certain input.
	 * always called before editing / loading configuration.
	 * */
	public void formatConfig(IStellarConfig cfg);
	
	/**
	 * gives the sub-data formatter of this data formatter for certain category. 
	 * Only used for config-list form of configuration.
	 * called when formatter is added to the category.
	 * */
	public IConfigFormatter getSubFormatter(String name);
	
}
