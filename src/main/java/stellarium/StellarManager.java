package stellarium;

import java.util.EnumMap;

import com.google.common.collect.Maps;

import cpw.mods.fml.relauncher.Side;
import stellarium.catalog.CCatalogAdditionalData;
import stellarium.catalog.CCatalogCfgData;
import stellarium.catalog.CCatalogCfgDataPhysical;
import stellarium.catalog.CCatalogManager;
import stellarium.catalog.ICCatalogDataSet;
import stellarium.catalog.ICatalogDataHandler;
import stellarium.catalog.IStellarCatalog;
import stellarium.catalog.IStellarCatalogData;
import stellarium.config.IConfigFormatter;
import stellarium.config.IConfigurableData;
import stellarium.config.IPhysicalHandler;

/**
 * StellarManager for management on each World.
 * */
public class StellarManager implements IPhysicalHandler<ICatalogDataHandler, ICatalogDataHandler, CCatalogAdditionalData> {
	//Singletons via side
	private static EnumMap<Side, StellarManager> managerMap = Maps.newEnumMap(Side.class);
	
	public static StellarManager getManager(Side side)
	{
		return managerMap.get(side);
	}
	
	public boolean isRemote;
	protected CCatalogManager catalog;
	
	public StellarManager(boolean remote)
	{	
		this.isRemote = remote;
		this.catalog = new CCatalogManager(this);
		
		if(this.isRemote)
			managerMap.put(Side.CLIENT, this);
		else managerMap.put(Side.SERVER, this);
	}
	
	public CCatalogManager getCatalogManager()
	{
		return catalog;
	}
	
	
	public void updateTick(long worldTime) {
		catalog.updateTick(worldTime);
	}
	
	
	@Override
	public void endSetup() {
		catalog.setupCatalogs();
	}
	
	@Override
	public IConfigFormatter createPhysicalFormatter(ICatalogDataHandler formatter, CCatalogAdditionalData addData) {	
		return catalog.createPhysicalFormatter(formatter, addData);
	}

	@Override
	public IConfigurableData createPhysicalData(ICatalogDataHandler data, CCatalogAdditionalData addData) {
		return catalog.createPhysicalData(data, addData);
	}

	@Override
	public IConfigFormatter createPhysicalFormatter(CCatalogAdditionalData adddata) {
		return catalog.createPhysicalFormatter();
	}

	@Override
	public IConfigurableData createPhysicalData(CCatalogAdditionalData adddata) {
		return catalog.createPhysicalData();
	}

}
