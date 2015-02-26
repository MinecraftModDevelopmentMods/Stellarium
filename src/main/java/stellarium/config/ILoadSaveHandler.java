package stellarium.config;

public interface ILoadSaveHandler {

	/**
	 * called on formatting configuration to get certain input.
	 * always called before editing / loading configuration.
	 * */
	public void onFormat();
	
	/**
	 * called when configuration is finished to apply the settings.
	 * */
	public void onApply();

	/**
	 * called when saving information to configuration.
	 * */
	public void onSave();
	
}
