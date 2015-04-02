package stellarium.config.core.handler;

import stellarium.config.EnumCategoryType;
import stellarium.config.ICfgMessage;
import stellarium.config.IConfigCategory;
import stellarium.config.core.ICategoryEntry;
import stellarium.config.core.StellarConfiguration;
import stellarium.config.core.StellarConfigCategory;

public interface IConfigHandler {
	
	public void setCategoryType(EnumCategoryType t);
	
	public void setModifiable(boolean modif, boolean warn);
	
	public ICategoryHandler getNewCat(StellarConfigCategory cat);
	
	public IConfigHandler getNewSubCfg(StellarConfiguration subConfig);
	
	public void onPostCreated(StellarConfigCategory cat);
	
	/**called before remove*/
	public void onRemove(StellarConfigCategory cat);
	
	/**called after migrated*/
	public void onMigrate(StellarConfigCategory cat, ICategoryEntry before);
	
	/**called before name change*/
	public boolean isValidNameChange(StellarConfigCategory cat, String postName);

	/**called after name change*/
	public void onNameChange(StellarConfigCategory cat, String before);

	public void onMarkImmutable(StellarConfigCategory cat);

	public void loadCategories(StellarConfiguration config);

	public void addLoadFailMessage(String title, ICfgMessage msg);

	public void onSave(StellarConfiguration config);
	
}
