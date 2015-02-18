package stellarium.config;

public interface IConfigFormatter {

	/**
	 * formats configuration to get certain input.
	 * always called before editing / loading configuration.
	 * */
	public void formatConfig(IStellarConfig cfg);
	
}
