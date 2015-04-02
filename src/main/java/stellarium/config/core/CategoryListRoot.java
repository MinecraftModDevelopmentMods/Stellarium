package stellarium.config.core;

import java.util.Iterator;
import java.util.NoSuchElementException;

import stellarium.config.IConfigCategory;
import stellarium.config.IStellarConfig;

public class CategoryListRoot implements ICategoryEntry {
	
	protected StellarConfiguration config;
	protected CategoryListEntry firstChild, lastChild;

	public CategoryListRoot(StellarConfiguration config) {
		this.config = config;
	}

	@Override
	public IStellarConfig getConfig() {
		return config;
	}

	@Override
	public IConfigCategory getCategory() {
		return null;
	}

	@Override
	public boolean isRootEntry() {
		return true;
	}

	@Override
	public ICategoryEntry getParentEntry() {
		return null;
	}

	@Override
	public boolean hasChildEntry() {
		return firstChild != null;
	}
	
	@Override
	public ICategoryEntry getFirstChildEntry() {
		return firstChild;
	}
	
	@Override
	public ICategoryEntry getLastChildEntry() {
		return lastChild;
	}

	@Override
	public IEntryIterator<ICategoryEntry> getChildEntryIterator() {
		return new ChildIterator(true);
	}

	@Override
	public IEntryIterator<ICategoryEntry> getChildEntryBackwardIte() {
		return new ChildIterator(false);
	}

	@Override
	public ICategoryEntry getChildEntry(String name) {
		
		for(ICategoryEntry find = this.firstChild; find != null; find = find.getNextEntry())
		{
			if(name.equals(find.getCategory().getName()))
				return find;
		}
		
		return null;
	}

	@Override
	public boolean hasPreviousEntry() {
		return false;
	}

	@Override
	public boolean hasNextEntry() {
		return false;
	}

	@Override
	public ICategoryEntry getPreviousEntry() {
		return null;
	}

	@Override
	public ICategoryEntry getNextEntry() {
		return null;
	}
	
	@Override
	public boolean canCreateCategory(String name, EnumPosOption option) {
		if(option.isOnSameLevel())
			return false;
		
		else if(this.getChildEntry(name) != null)
			return false;
		
		return config.canMakeCategory(this, name);
	}

	@Override
	public IConfigCategory createCategory(String name, EnumPosOption option) {
		if(option.isOnSameLevel())
			throw new IllegalArgumentException("Option " + option.name() + " is not available for root entry.");
		
		else if(this.getChildEntry(name) != null)
			return this.getChildEntry(name).getCategory();
		
		
		StellarConfigCategory category = config.newCategory(this, name);

		if(category == null)
			return null;
		
		addNewEntry(category);
		
		config.postCreated(category);
		
		return category;
	}
	
	@Override
	public boolean canRemoveCategory() {
		return false;
	}

	@Override
	public boolean removeCategory() {
		return false;
	}

	@Override
	public String getName() {
		throw new IllegalStateException("Cannot get the name from the Root Entry!");
	}
	
	@Override
	public boolean canChangeName(String name) {
		return false;
	}

	@Override
	public boolean changeName(String name) {
		throw new IllegalStateException("Cannot change the name of the Invalidated Entry!");
	}

	@Override
	public IConfigCategory copyCategory(IConfigCategory category, String name,
			EnumPosOption option) {
		if(option.isOnSameLevel())
			throw new IllegalArgumentException("Option " + option.name() + " is not available for root entry.");
		
		else if(!option.isOnSameLevel() && this.getChildEntry(name) != null)
			return this.getChildEntry(name).getCategory();
		
		
		StellarConfigCategory copiedCategory = config.newCategory(this, name);
		
		if(copiedCategory == null)
			return null;
		
		addNewEntry(copiedCategory);
		
		config.postCreated(copiedCategory);
		
		copiedCategory.copy(category);
		
		return copiedCategory;
	}
	
	@Override
	public boolean canMigrateCategory(IConfigCategory category, EnumPosOption option) {
		if(option.isOnSameLevel() && this.isRootEntry())
			return false;
		
		else if(!option.isOnSameLevel() && this.getChildEntry(category.getName()) != null)
			return false;
		
		if(!config.canMigrate(this, category.getName(), category.getCategoryEntry()))
			return false;
		
		return true;
	}

	@Override
	public boolean migrateCategory(IConfigCategory category,
			EnumPosOption option) {
		if(option.isOnSameLevel() && this.isRootEntry())
			return false;
		
		else if(!option.isOnSameLevel() && this.getChildEntry(category.getName()) != null)
			return false;
		
		if(!config.canMigrate(this, category.getName(), category.getCategoryEntry()))
			return false;
		
		StellarConfigCategory mcategory = (StellarConfigCategory) category;
		
		//Invalidates this entry
		((CategoryListEntry)mcategory.getCategoryEntry()).removeEntry();
		
		try {
			addNewEntry(mcategory);
		} catch(Exception e) {
			return false;
		}
		
		return true;
	}
	
	private void addNewEntry(StellarConfigCategory category) {
		CategoryListEntry list;
		
		if(this.hasChildEntry())
		{
			list = new CategoryListEntry(category, this, this.lastChild, null);
			lastChild.next = list;
			this.lastChild = list;
		} else {
			list = new CategoryListEntry(category, this, null, null);
			this.firstChild = this.lastChild = list;
		}		

		category.setEntry(list);
	}
	
	
	@Override
	public boolean equals(Object obj)
	{
		return (obj instanceof CategoryListRoot);
	}
	
	@Override
	public int hashCode()
	{
		return 0;
	}
	
	
	public class ChildIterator implements IEntryIterator<ICategoryEntry> {
		
		protected boolean onFront;
		protected CategoryListEntry current;
		
		public ChildIterator(boolean front)
		{
			this.onFront = front;
			current = null;
		}

		@Override
		public boolean hasNext() {
			if(current != null)
				return current.hasNextEntry();
			else if(onFront)
				return CategoryListRoot.this.hasChildEntry();
			else return false;
		}

		@Override
		public ICategoryEntry next() {
			if(!hasNext())
				throw new NoSuchElementException();
			if(current != null)
				return current = current.next;
			else return current = firstChild;
		}

		@Override
		public boolean hasPrevious() {
			if(current != null)
				return current.hasPreviousEntry();
			else if(!onFront)
				return CategoryListRoot.this.hasChildEntry();
			else return false;
		}

		@Override
		public ICategoryEntry previous() {
			if(!hasPrevious())
				throw new NoSuchElementException();
			if(current != null)
				return current = current.previous;
			else return current = lastChild;
		}

		@Override
		public void remove() { }
	}

	@Override
	public Iterator<ICategoryEntry> iterator() {
		return this.getChildEntryIterator();
	}

	
	@Override
	public void setIDEnabled(boolean idEnabled) {
		//No Effect
	}
	
}
