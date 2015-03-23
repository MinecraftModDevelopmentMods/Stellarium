package stellarium.config;

public interface IConfigurableData {

	/**
	 * called when configuration is finished to apply the settings.
	 *  (after SubConfigs are all applied)
	 * */
	public void applyConfig(IStellarConfig config);

	/**
	 * called when saving information as configuration
	 *  (after SubConfigs are all saved)
	 * */
	public void saveConfig(IStellarConfig config);
	
	/**
	 * gives the sub-data of this data for certain category. 
	 * Only used for config-list form of configuration.
	 * 	called when formatter is added to the category.
	 * */
	public IConfigurableData getSubData(String name);
	
}
