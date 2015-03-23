package stellarium.config;

import stellarium.config.core.ICategoryEntry;

/**
 * Category Arrangement Modification Listener.
 * */
public interface ICfgArrMListener {

	/**called before a category is created in any ways.*/
	public void onNew(ICategoryEntry parent, String name);
	
	/**called after a category is created in any ways.*/
	public void onPostCreated(IConfigCategory cat);
	
	/**called before a category is removed in any ways.*/
	public void onRemove(IConfigCategory cat);
	
	/**called after a category is migrated or copied in any ways.*/
	public void onMigrate(IConfigCategory cat, ICategoryEntry before);
	
	/**Always called when the name of category is changed.*/
	public void onNameChange(IConfigCategory cat, String before);
	
}
