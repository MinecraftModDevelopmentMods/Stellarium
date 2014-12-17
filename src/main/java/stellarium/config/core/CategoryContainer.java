package stellarium.config.core;

import java.util.List;

import stellarium.config.EnumCategoryType;
import stellarium.config.IConfigCategory;
import stellarium.config.IStellarConfig;

public abstract class CategoryContainer {
	
	public static CategoryContainer newCatContainer(EnumCategoryType type)
	{
		if(type == EnumCategoryType.Tree)
			return new TreeCategoryContainer();
		else return new ListCategoryContainer();
	}
	
	public abstract void addCategory(IConfigCategory cat);
	
	public abstract void removeCategory(String cid);
	
	public abstract IConfigCategory getCategory(String cid);
	
	public abstract List<IConfigCategory> getAllCategories();
	
	
	public abstract void addSubCategory(IConfigCategory parent, IConfigCategory cat);

	public abstract void removeSubCategory(IConfigCategory parent, String subid);
	
	public abstract IConfigCategory getSubCategory(IConfigCategory parent, String subid);

	public abstract List<IConfigCategory> getAllSubCategories(IConfigCategory parent);
}
