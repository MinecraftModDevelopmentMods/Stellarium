package stellarium.config;

public interface IConfigCategory {
	
	/**Gets category id.*/
	public String getID();

	
	/**Gets displayed name of this category.*/
	public String getDisplayName();
	
	/**Sets displayed name of this category.*/
	public void setDisplayName(String name);
	
	
	/**Adds property in this category.*/
	public <T> IConfigProperty<T> addProperty(String proptype, String propname, T def);
	
	/**Gets property in this category.*/
	public <T> IConfigProperty<T> getProperty(String propname);
	
	/**Adds property relation in this category.*/
	public void addPropertyRelation(IPropertyRelation rel, IConfigProperty... relprops);
	
}
