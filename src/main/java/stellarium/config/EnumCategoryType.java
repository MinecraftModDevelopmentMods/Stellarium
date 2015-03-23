package stellarium.config;

import stellarium.config.core.CategoryListRoot;
import stellarium.config.core.CategoryNode;
import stellarium.config.core.ICategoryEntry;
import stellarium.config.core.StellarConfiguration;

/**
 * Category Arrangement Type for Configuration.
 * Following Types are available: {@link #ConfigList}, {@link #List}, {@link #Tree}.
 * Default: {@link #List}
 * */
public enum EnumCategoryType {
	
	/**list-form configuration arrangement. category is used as configuration.*/
	ConfigList(true, false),
	
	/**list-form category arrangement.*/
	List(false, false),
	
	/**tree-form category arrangement.*/
	Tree(false, true);
	
	
	boolean isConfigList;
	boolean isTree;
	
	EnumCategoryType(boolean isConfigList, boolean isTree)
	{
		this.isConfigList = isConfigList;
		this.isTree = isTree;
	}
	
	public ICategoryEntry getRootEntry(StellarConfiguration config)
	{
		if(this.isTree)
			return new CategoryNode(config, null, null); 
		else return new CategoryListRoot(config);
	}
	
	public boolean isConfigList()
	{
		return isConfigList;
	}
}
