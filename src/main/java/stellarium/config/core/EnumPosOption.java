package stellarium.config.core;

/**
 * Category Position Option, which determines the position of category for creation/migration.
 * Following Options are available : {@link #Previous}, {@link #Next}, {@link #Child}
 * */
public enum EnumPosOption {
	/**previous sibling position option*/
	Previous(true),
	
	/**next sibling position option*/
	Next(true),
	
	/**(last) child position option*/
	Child(false);
	
	boolean onSameLevel;
	
	EnumPosOption(boolean onSameLevel)
	{
		this.onSameLevel = onSameLevel;
	}
	
	public boolean isOnSameLevel()
	{
		return this.onSameLevel;
	}
}
