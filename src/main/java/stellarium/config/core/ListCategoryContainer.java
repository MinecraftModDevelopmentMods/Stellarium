package stellarium.config.core;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import stellarium.config.IConfigCategory;

public class ListCategoryContainer extends CategoryContainer {
	
	public List<IConfigCategory> catList = Lists.newArrayList();

	
	@Override
	public void addCategory(IConfigCategory cat) {
		catList.add(cat);
	}

	@Override
	public void removeCategory(String cid) {
		Iterator<IConfigCategory> ite = catList.iterator();
		
		while(ite.hasNext())
		{
			if(ite.next().getID().equals(cid))
				ite.remove();
		}
	}

	@Override
	public IConfigCategory getCategory(String cid) {
		for(IConfigCategory cat : catList)
		{
			if(cat.getID().equals(cid))
				return cat;
		}
		
		return null;
	}

	@Override
	public List<IConfigCategory> getAllCategories() {
		return catList;
	}

	@Override
	public void addSubCategory(IConfigCategory parent,
			IConfigCategory cat) { }

	@Override
	public void removeSubCategory(IConfigCategory parent, String subid) { }

	@Override
	public IConfigCategory getSubCategory(IConfigCategory parent, String subid) {
		return null;
	}

	@Override
	public List<IConfigCategory> getAllSubCategories(IConfigCategory parent) {
		return Lists.newArrayList();
	}

}
