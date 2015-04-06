package stellarium;

import java.util.EnumMap;

import com.google.common.collect.Maps;

import cpw.mods.fml.relauncher.Side;
import stellarium.catalog.CCatalogManager;
import stellarium.catalog.ICatalogDataHandler;

/**
 * StellarManager for management on each World.
 * */
public class StellarManager {
	//Singletons via side
	private static EnumMap<Side, StellarManager> managerMap = Maps.newEnumMap(Side.class);
	
	public static void resetManager(Side side)
	{
		managerMap.put(side, new StellarManager(side.isClient()));
	}
	
	public static StellarManager getManager(Side side)
	{
		return managerMap.get(side);
	}
	
	public boolean isRemote;
	protected CCatalogManager catalog;
	
	public StellarManager(boolean remote)
	{
		isRemote = remote;
		catalog = new CCatalogManager(this);
	}
	
	public void setupCatalogs(ICatalogDataHandler defHandler)
	{
		catalog.setupCatalogs(defHandler);
	}
	
	public void setupCatalogs(String id, ICatalogDataHandler defHandler)
	{
		catalog.setupCatalogs(id, defHandler);
	}
	
	public CCatalogManager getCatalogManager()
	{
		return catalog;
	}
	
}
