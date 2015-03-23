package stellarium.config;

import stellarium.config.core.ICategoryEntry;

public interface IConfigCategory {
	
	/**Gets the name of this category.*/
	public String getName();
	
	
	/**Gets configuration this category belongs to.*/
	public IStellarConfig getConfig();
	
	/**Gets the Entry for this category.*/
	public ICategoryEntry getCategoryEntry();
	
	
	/**
	 * Marks this category as Immutable. (in configuration editor's manner)
	 * So this category should not be migrated or removed.
	 * */
	public void markImmutable();
	
	/**
	 * Checks if the category is Immutable.
	 * @return the Immutable flag of the category
	 * */
	public boolean isImmutable();
	
	
	/**Adds property in this category.*/
	public <T> IConfigProperty<T> addProperty(String proptype, String propname, T def);
	
	/**Removes property in this category.*/
	public void removeProperty(String propname);
	
	/**Gets property in this category.*/
	public <T> IConfigProperty<T> getProperty(String propname);
	
	
	/**
	 * Sets property addition entry. every property would be added after this property.
	 * if the parameter is null, then it sets addition entry to the end.
	 * @return the previous entry.
	 * */
	public IConfigProperty setPropAddEntry(IConfigProperty prop);
	
	
	/**Adds property relation in this category.*/
	public void addPropertyRelation(IPropertyRelation rel, IConfigProperty... relprops);
	
}
