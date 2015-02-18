package stellarium;

import stellarium.catalog.CCatalogManager;

/**
 * StellarManager for management on each World.
 * */
public class StellarManager {
	public boolean isRemote;
	
	protected CCatalogManager catalog;
	
	public StellarManager(boolean remote)
	{
		isRemote = remote;
		catalog = new CCatalogManager(this);
	}
	
}
