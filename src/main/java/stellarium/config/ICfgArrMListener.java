package stellarium.config;

import stellarium.config.core.ICategoryEntry;

/**
 * Category Arrangement Modification Listener.
 * */
public interface ICfgArrMListener {

	/**
	 * check if a category can be created.
	 * @return <code>false</code> to cancel creating category.
	 * */
	public boolean canCreate(ICategoryEntry parent, String name);
	
	/**called after a category is created in any ways.*/
	public void onPostCreated(IConfigCategory cat);
	
	/**called before a category is removed in any ways.*/
	public void onRemove(IConfigCategory cat);
	
	/**
	 * check if a category can be migrated
	 * @return <code>false</code> to cancel migrating category.
	 * */
	public boolean canMigrate(ICategoryEntry parent, String name, ICategoryEntry before);
	
	/**called after a category is migrated in any ways.*/
	public void onMigrate(IConfigCategory cat, ICategoryEntry before);
	
	/**Always called when the name of category is changed.*/
	public void onNameChange(IConfigCategory cat, String before);
	
}
