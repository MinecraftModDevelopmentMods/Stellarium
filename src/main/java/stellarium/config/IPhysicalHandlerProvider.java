package stellarium.config;

import net.minecraft.world.WorldServer;

public interface IPhysicalHandlerProvider<CF extends IConfigFormatter, CD extends IConfigurableData> {
	
	/**
	 * Provides the physical handler before physical environment is established.
	 * @param isRemote <code>true</code> on Client side, <code>false</code> otherwise.
	 * */
	public IPhysicalHandler providePhysicalHandler(boolean isRemote);

	/**
	 * Provides additional data instance on physical environment.
	 * */
	public IConfigAdditionalData provideAdditionalData();
	
	/**
	 * Provides formatted additional data for physical environment.
	 * (Server side)
	 * @param world loaded overworld instance
	 * */
	public IConfigAdditionalData provideFormattedAdditionalData(WorldServer world);
	
}
