package stellarium.config.core;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import stellarium.config.IConfigCategory;

public class TreeCategoryContainer extends CategoryContainer {
	
	public CategoryNode root = new CategoryNode(null);
	public Map<IConfigCategory, CategoryNode> map;
	
	@Override
	public void addCategory(IConfigCategory cat) {
		CategoryNode node = new CategoryNode(root, cat);
		
		map.put(cat, node);
	}

	@Override
	public void removeCategory(String cid) {
		Iterator<CategoryNode> ite = root.subcats.iterator();
		
		while(ite.hasNext())
		{
			CategoryNode node = ite.next();
			
			if(node.cat.getID().equals(cid))
			{
				map.remove(cid);
				ite.remove();
			}
		}
	}

	@Override
	public IConfigCategory getCategory(String cid) {
		for(CategoryNode node : root.subcats)
		{
			if(node.cat.getID().equals(cid))
				return node.cat;
		}
		
		return null;
	}

	@Override
	public List<IConfigCategory> getAllCategories() {
		List list = Lists.newArrayList();
		
		for(CategoryNode node : root.subcats)
			list.add(node.cat);

		return list;
	}

	
	@Override
	public void addSubCategory(IConfigCategory parent,
			IConfigCategory cat) {
		CategoryNode parnode = map.get(parent);
		CategoryNode node = new CategoryNode(parnode, cat);
		
		map.put(cat, node);
	}

	@Override
	public void removeSubCategory(IConfigCategory parent, String subid) {
		
		CategoryNode parnode = map.get(parent);

		Iterator<CategoryNode> ite = parnode.subcats.iterator();
		
		while(ite.hasNext())
		{
			CategoryNode node = ite.next();
			
			if(node.cat.getID().equals(subid))
			{					
				map.remove(subid);
				ite.remove();
			}
		}
	}

	@Override
	public IConfigCategory getSubCategory(IConfigCategory parent, String subid) {
		
		CategoryNode parnode = map.get(parent);

		for(CategoryNode node : parnode.subcats)
		{
			if(node.cat.getID().equals(subid))
				return node.cat;
		}
		
		return null;
	}

	@Override
	public List<IConfigCategory> getAllSubCategories(IConfigCategory parent) {
		List list = Lists.newArrayList();
		
		CategoryNode parnode = map.get(parent);
		
		for(CategoryNode node : parnode.subcats)
			list.add(node.cat);

		return list;
	}
	
	
	public class CategoryNode {
		public CategoryNode par;
		public IConfigCategory cat;
		public List<CategoryNode> subcats = Lists.newArrayList();
		
		public CategoryNode(IConfigCategory pcat)
		{
			this(null, pcat);
		}
		
		public CategoryNode(CategoryNode ppar, IConfigCategory pcat)
		{
			par = ppar;
			cat = pcat;
			
			if(par != null)
				par.subcats.add(this);
		}
	}

}
