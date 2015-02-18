package stellarium.config;

import stellarium.config.core.ConfigEntry;

public interface IConfigCategory {
	
	/**Gets category id.*/
	public String getID();

	
	/**Gets displayed name of this category.*/
	public String getDisplayName();
	
	/**Sets displayed name of this category. (Not will be changed if the name is same)*/
	public void setDisplayName(String name);
	
	
	/**Gets configuration this category belongs to*/
	public IStellarConfig getConfig();
	
	/**
	 * Gets (current) parent category of this category.
	 * gives null when this category has no parent.
	 * */
	public IConfigCategory getParCategory();
	
	/**
	 * Gets the Entry for this Category.
	 * */
	public ConfigEntry getConfigEntry();
	
	
	/**Adds property in this category.*/
	public <T> IConfigProperty<T> addProperty(String proptype, String propname, T def);
	
	/**Removes property in this category.*/
	public void removeProperty(String propname);
	
	/**Gets property in this category.*/
	public <T> IConfigProperty<T> getProperty(String propname);
	
	
	/**Adds property relation in this category.*/
	public void addPropertyRelation(IPropertyRelation rel, IConfigProperty... relprops);
	
}
