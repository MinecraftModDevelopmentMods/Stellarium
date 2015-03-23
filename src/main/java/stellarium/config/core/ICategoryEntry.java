package stellarium.config.core;

import stellarium.config.IConfigCategory;
import stellarium.config.IStellarConfig;

/**
 * Configuration Entry class which represents specific location of a category.
 * Also this can be used as key for hash tables.
 * */
public interface ICategoryEntry extends Iterable<ICategoryEntry> {
	
	/**
	 * Gives the base configuration for this entry.
	 * @return the base config object for this entry.
	 * */
	public IStellarConfig getConfig();
	
	/**
	 * Gives category for this entry.
	 * @return the category for this entry.
	 * */
	public IConfigCategory getCategory();
	
	
	/**
	 * Checks if this entry is root entry.
	 * @return <code>false</code> iff. the parent entry exists.
	 * */
	public boolean isRootEntry();
	
	/**
	 * Gives the parent entry for this entry.
	 * @return the parent entry, or <code>null</code> if it does not exist
	 * */
	public ICategoryEntry getParentEntry();
	
	/**
	 * Checks if this entry has any child entry.
	 * @return <code>true</code> iff. any child entry exists.
	 * */
	public boolean hasChildEntry();
	
	/**
	 * Gives the iteration for child entries.
	 * @return the new iterator on child entries starting on front
	 * */
	public IEntryIterator<ICategoryEntry> getChildEntryIterator();
	
	/**
	 * Gives the backward iteration for child entries.
	 * @return the new iterator on child entries starting on back
	 * */
	public IEntryIterator<ICategoryEntry> getChildEntryBackwardIte();
	
	/**
	 * Gives the first child entry.
	 * @return the first child entry, or <code>null</code> if it does not exist
	 * */
	public ICategoryEntry getFirstChildEntry();
	
	/**
	 * Gives the last child entry.
	 * @return the last child entry, or <code>null</code> if it does not exist
	 * */
	public ICategoryEntry getLastChildEntry();
	
	/**
	 * Gives child entry for specific name.
	 * @param name the name to find the child entry
	 * @return the child entry, or <code>null</code> if it does not exist
	 * */
	public ICategoryEntry getChildEntry(String name);
	
	/**
	 * Checks if the previous sibling entry exists.
	 * @return <code>true</code> iff. previous sibling entry exists
	 * */
	public boolean hasPreviousEntry();
	
	/**
	 * Checks if the next sibling entry exists.
	 * @return <code>true</code> iff. next sibling entry exists
	 * */
	public boolean hasNextEntry();
	
	/**
	 * Gives the previous sibling entry.
	 * @return the previous sibling entry, or <code>null</code> if it does not exist
	 * */
	public ICategoryEntry getPreviousEntry();
	
	/**
	 * Gives the next sibling entry.
	 * @return the next sibling entry, or <code>null</code> if it does not exist
	 * */
	public ICategoryEntry getNextEntry();
	
	
	/**
	 * Creates category on the specific position denoted by {@link EnumPosOption},
	 * Or there already exists a category with same name in same entry,
	 *   then just returns the pre-existing category.
	 *  NOTE: Some Category would already exist(be loaded) on Text Configuration.
	 * @param name the name of the category being created
	 * @param option the position option the category will be on
	 * @return the created category
	 * @throws IllegalArgumentException for invalid option
	 * */
	public IConfigCategory createCategory(String name, EnumPosOption option);
	
	
	/**
	 * Removes the category (and all its child categories) for this entry.
	 * This will remove the category and invalidates this entry,
	 * So changing/setting operation would throw {@link IllegalStateException} from then on.
	 * @return <code>true</code> if the category is successfully removed,
	 * 	 or <code>false</code> otherwise
	 * */
	public boolean removeCategory();
	
	
	/**
	 * Gives the name of the category for this entry.
	 * @return the name of the category for this entry.
	 * */
	public String getName();
	
	/**
	 * Changes the name of the category for this entry.
	 * @param name the name for this entry.
	 * @return <code>true</code> if the category name is successfully changed,
	 * 	 or <code>false</code> otherwise
	 * */
	public boolean changeName(String name);
	
	
	/**
	 * Copies the category to specific position denoted by {@link EnumPosOption}.
	 * If there already exists category with same name in same entry,
	 *   then this copy doesn't have any effect and returns the pre-existing category.
	 * @param category the category to copy
	 * @param name the name for the copied category on new entry
	 * @param option the position option the category will be on
	 * @return the copied category on new entry
	 * */
	public IConfigCategory copyCategory(IConfigCategory category, String name, EnumPosOption option);
	
	/**
	 * Migrates the category to specific position denoted by {@link EnumPosOption}.
	 * If there already exists category with same name in same entry, then this migration fails.
	 * Note that the entry instance for the category being migrated invalidates,
	 *  while the category instance remains constant only with the entry changed.
	 * @param category the category to migrate
	 * @param option the position option the category will be on
	 * @return <code>true</code> if the category is successfully migrated,
	 * 	 or <code>false</code> otherwise
	 * */
	public boolean migrateCategory(IConfigCategory category, EnumPosOption option);
	
	
	public boolean equals(Object o);
	
	public int hashCode();

}
