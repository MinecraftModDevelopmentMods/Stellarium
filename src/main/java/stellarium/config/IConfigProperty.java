package stellarium.config;

public interface IConfigProperty<T> {
	
	/**gives the value of this property*/
	public T getVal();
	
	
	/**gives the name of this property*/
	public String getName();
	
	/**
	 * Sets Explanation of this property
	 * @return this
	 * */
	public IConfigProperty<T> setExpl(String expl);
	
}
