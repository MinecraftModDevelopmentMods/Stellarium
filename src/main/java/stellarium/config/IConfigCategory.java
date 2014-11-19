package stellarium.config;

public interface IConfigCategory {
	
	/**Gets category id.*/
	public String getID();

	
	/**Gets displayed name of this category.*/
	public String getDisplayName();
	
	/**Sets displayed name of this category. (Not will be changed if the name is same)*/
	public void setDisplayName(String name);
	
	
	/**Gets (current) parent category of this category.*/
	public IConfigCategory getParCategory();
	
	
	/**Adds property in this category.*/
	public <T> IConfigProperty<T> addProperty(String proptype, String propname, T def);
	
	/**Removes property in this category.*/
	public void removeProperty(String propname);
	
	/**Gets property in this category.*/
	public <T> IConfigProperty<T> getProperty(String propname);
	
	
	/**Adds property relation in this category.*/
	public void addPropertyRelation(IPropertyRelation rel, IConfigProperty... relprops);
	
}
