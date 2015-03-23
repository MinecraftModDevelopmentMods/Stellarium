package stellarium.config;

import java.util.List;

import stellarium.config.core.ICategoryEntry;

public interface IStellarConfig {
	
	/**
	 * Sets category type of this configuration.<p>
	 * Should be called at first time, on format.
	 * @param t the category arrangement type
	 * */
	public void setCategoryType(EnumCategoryType t);
	
	/**
	 * Sets whether category is modifiable or not, in configuration editer's manner.<p>
	 *  ('Modifiable' means it can be added/removed/copy-pasted and name change is available)<p>
	 * Only be effective on formatting phase.
	 * @param modif modifiability flag
	 * @param warn warning flag. when it is enabled, any modification will be warned.
	 * */
	public void setModifiable(boolean modif, boolean warn);
	
	/**Adds Configuration Arrangement Modification Listener.*/
	public void addAMListener(ICfgArrMListener list);
	
	
	/**
	 * Gets the root entry for this configuration.<p>
	 *  Note: a root entry is kind of invalidated entry, so it cannot be modified.
	 * @param entry the root category entry
	 * */
	public ICategoryEntry getRootEntry();
	
	
	/**
	 * Gets sub-configuration from category.<p>
	 * Only valid for <code>{@link EnumCategoryType#ConfigList}</code> 
	 * */
	public IStellarConfig getSubConfig(IConfigCategory cat);
	
	
	/**
	 * Automatically detects and loads Categories from this configuration.
	 * */
	public void loadCategories();
	
	
	/**
	 * Adds loading-failure message.
	 * */
	public void addLoadFailMessage(String title, ICfgMessage msg);
}
