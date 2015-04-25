package stellarium;

import net.minecraft.world.WorldServer;
import stellarium.catalog.CCatalogAdditionalData;
import stellarium.catalog.CCatalogCfgData;
import stellarium.config.IConfigAdditionalData;
import stellarium.config.IPhysicalHandler;
import stellarium.config.IPhysicalHandlerProvider;

public class StellarManagerProvider implements IPhysicalHandlerProvider<CCatalogCfgData, CCatalogCfgData> {

	@Override
	public IPhysicalHandler providePhysicalHandler(boolean isRemote) {
		return new StellarManager(isRemote);
	}

	@Override
	public IConfigAdditionalData provideAdditionalData() {
		return new CCatalogAdditionalData();
	}

	@Override
	public IConfigAdditionalData provideFormattedAdditionalData(WorldServer world) {
		return new CCatalogAdditionalData(world.getWorldInfo().getGeneratorOptions());
	}

}
