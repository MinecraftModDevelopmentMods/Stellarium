package stellarium.config;

public interface IConfigurableData {

	/**
	 * called when configuration is finished to apply the settings.
	 * */
	public void applyConfig(IStellarConfig config);

	/**
	 * called when saving information as configuration
	 * */
	public void saveConfig(IStellarConfig config);
	
	/**
	 * gives the sub-data of this data for certain category. 
	 * Only used for config-list form of configuration.
	 * */
	public IConfigurableData getSubData(IConfigCategory cat);
	
}
