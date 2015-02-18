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
	
}
