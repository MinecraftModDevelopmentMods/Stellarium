package stellarium.config;

/**
 * Category Arrangement Modification Listener.
 * */
public interface ICfgArrMListener {

	/**
	 * called after a category is added in any ways.
	 * for configlist, this method is called before sub-config is formatted.
	 * */
	public void onNew(IConfigCategory cat);
	
	/**called before a category is removed in any ways. (all of sub-configurations will be removed)*/
	public void onRemove(IConfigCategory cat);
	
	/**called after a category changed its parent, only for tree-type arrangement*/
	public void onChangeParent(IConfigCategory cat, IConfigCategory from, IConfigCategory to);
	
	/**called after a category changed its order*/
	public void onChangeOrder(IConfigCategory cat, int before, int after);
	
	/**Always called when display name of category is changed.*/
	public void onDispNameChange(IConfigCategory cat, String before);
	
}
