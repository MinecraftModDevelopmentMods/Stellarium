package stellarium;

import stellarium.catalog.CCatalogAdditionalData;
import stellarium.catalog.CCatalogCfgData;
import stellarium.config.IConfigAdditionalData;
import stellarium.config.IPhysicalHandler;
import stellarium.config.IPhysicalHandlerProvider;
import net.minecraft.world.storage.WorldInfo;

public class StellarManagerProvider implements IPhysicalHandlerProvider {

	@Override
	public IPhysicalHandler providePhysicalHandler(boolean isRemote) {
		return new StellarManager(isRemote);
	}

	@Override
	public IConfigAdditionalData provideAdditionalData() {
		return new CCatalogAdditionalData();
	}

	@Override
	public IConfigAdditionalData provideFormattedAdditionalData(WorldInfo worldInfo) {
		return new CCatalogAdditionalData(worldInfo.getGeneratorOptions());
	}

}
