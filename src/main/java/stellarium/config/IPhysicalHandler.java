package stellarium.config;

public interface IPhysicalHandler<LCF extends IConfigFormatter, LCD extends IConfigurableData, CAD extends IConfigAdditionalData> {

	/**
	 * Creates physical formatter matching with the logical formatter.
	 * @param formatter the logical formatter
	 * @param adddata the additional data
	 * */
	public IConfigFormatter createPhysicalFormatter(LCF formatter, CAD adddata);

	/**
	 * Creates physical data matching with the logical data.
	 * @param data the logical data
	 * @param adddata the additional data
	 * */
	public IConfigurableData createPhysicalData(LCD data, CAD adddata);

	/**
	 * Creates physical formatter.
	 * @param adddata the additional data
	 * */
	public IConfigFormatter createPhysicalFormatter(CAD adddata);
	
	/**
	 * Creates physical data.
	 * @param adddata the additional data
	 * */
	public IConfigurableData createPhysicalData(CAD adddata);

	/**
	 * Called after data is loaded to setup this physical handler.
	 * */
	public void endSetup();
}
