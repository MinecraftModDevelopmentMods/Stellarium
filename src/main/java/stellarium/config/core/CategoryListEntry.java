package stellarium.config.core;

import java.util.Iterator;

import stellarium.config.IConfigCategory;
import stellarium.config.IStellarConfig;

public class CategoryListEntry implements ICategoryEntry {
	
	protected StellarConfigCategory theCategory;
	
	protected CategoryListRoot root;
	protected CategoryListEntry previous, next;

	public CategoryListEntry(StellarConfigCategory category, CategoryListRoot root, CategoryListEntry previous, CategoryListEntry next) {
		this.theCategory = category;
		this.root = root;
		this.previous = previous;
		this.next = next;
	}

	@Override
	public IStellarConfig getConfig() {
		return root.getConfig();
	}

	@Override
	public IConfigCategory getCategory() {
		return this.theCategory;
	}

	@Override
	public boolean isRootEntry() {
		return false;
	}

	@Override
	public ICategoryEntry getParentEntry() {
		return this.root;
	}

	@Override
	public boolean hasChildEntry() {
		return false;
	}
	
	@Override
	public ICategoryEntry getFirstChildEntry() {
		return null;
	}
	
	@Override
	public ICategoryEntry getLastChildEntry() {
		return null;
	}

	@Override
	public IEntryIterator<ICategoryEntry> getChildEntryIterator() {
		return null;
	}

	@Override
	public IEntryIterator<ICategoryEntry> getChildEntryBackwardIte() {
		return null;
	}

	@Override
	public ICategoryEntry getChildEntry(String name) {
		return null;
	}

	@Override
	public boolean hasPreviousEntry() {
		return this.previous != null;
	}

	@Override
	public boolean hasNextEntry() {
		return this.next != null;
	}

	@Override
	public ICategoryEntry getPreviousEntry() {
		return this.previous;
	}

	@Override
	public ICategoryEntry getNextEntry() {
		return this.next;
	}

	@Override
	public IConfigCategory createCategory(String name, EnumPosOption option) {
		if(!option.isOnSameLevel())
			throw new IllegalArgumentException("Option " + option.name() + " is not available for list entry.");
		
		else if(option.isOnSameLevel() && root.getChildEntry(name) != null)
			return root.getChildEntry(name).getCategory();
		
		
		StellarConfigCategory category = root.config.newCategory(this.root, name);

		addNewEntry(category, option);
		
		root.config.postCreated(category);
		
		return category;
	}

	@Override
	public boolean removeCategory() {
		
		if(this.theCategory == null)
			return false;
		
		root.config.preRemoved(theCategory);
		
		
		boolean hasPrevious = this.hasPreviousEntry();
		boolean hasNext = this.hasNextEntry();
		
		if(hasPrevious) {
			if(hasNext)	{
				previous.next = this.next;
				next.previous = this.previous;
			} else {
				previous.next = null;
				root.lastChild = this.previous;
			}
		} else {
			if(hasNext) {
				next.previous = null;
				root.firstChild = this.previous;
			} else {
				root.firstChild = root.lastChild = null;
			}
		}
		
		this.theCategory = null;
		
		return true;
	}

	@Override
	public String getName() {
		if(this.theCategory == null)
			throw new IllegalStateException("Cannot get the name from the Invalidated Entry!");
		
		return theCategory.getName();
	}

	@Override
	public boolean changeName(String name) {
		if(this.theCategory == null)
			throw new IllegalStateException("Cannot change the name of the Invalidated Entry!");
		
		else if(root.getChildEntry(name) != null)
			return false;
		
		return theCategory.setName(name);
	}

	@Override
	public IConfigCategory copyCategory(IConfigCategory category, String name,
			EnumPosOption option) {
		if(!option.isOnSameLevel())
			throw new IllegalArgumentException("Option " + option.name() + " is not available for list entry.");
		
		else if(root.getChildEntry(name) != null)
			return root.getChildEntry(name).getCategory();
		
		
		StellarConfigCategory copiedCategory = root.config.newCategory(this.root, name);
		
		addNewEntry(copiedCategory, option);
		
		root.config.postCreated(copiedCategory);
		
		copiedCategory.copy(category);
		
		return copiedCategory;
	}

	@Override
	public boolean migrateCategory(IConfigCategory category,
			EnumPosOption option) {
		
		if(!option.isOnSameLevel())
			return false;
		
		else if(option.isOnSameLevel() && root.getChildEntry(category.getName()) != null)
			return false;		
		
		StellarConfigCategory mcategory = (StellarConfigCategory) category;
		
		//Invalidates this entry
		((CategoryListEntry)mcategory.getCategoryEntry()).theCategory = null;
		
		try {
			addNewEntry(mcategory, option);
		} catch(Exception e) {
			return false;
		}
		
		return true;
	}
	
	private void addNewEntry(StellarConfigCategory category, EnumPosOption option) {
		CategoryListEntry list;
		
		switch(option)
		{
		case Previous:
			if(this.hasPreviousEntry()) {
				list = new CategoryListEntry(category, this.root, this.previous, this);
				previous.next = list;
				this.previous = list;
			} else {
				list = new CategoryListEntry(category, this.root, null, this);
				root.firstChild = list;
				this.previous = list;
			}
			break;
			
		case Next:
			if(this.hasNextEntry())	{
				list = new CategoryListEntry(category, this.root, this, this.next);
				next.previous = list;
				this.next = list;
			} else {
				list = new CategoryListEntry(category, this.root, this, null);
				root.lastChild = list;
				this.next = list;
			}
			break;
			
		default:
			throw new IllegalArgumentException("Invalid Option: " + option);
		}
		
		category.setEntry(list);
	}
	
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof CategoryListEntry)
		{			
			return this.getName().equals(((CategoryListEntry) obj).getName());
		}
		return false;
	}
	
	@Override
	public int hashCode()
	{
		return this.getName().hashCode();
	}
	
	
	@Override
	public Iterator<ICategoryEntry> iterator() {
		return null;
	}

}
