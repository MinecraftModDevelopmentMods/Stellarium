package stellarium.config.core;

import java.util.Iterator;
import java.util.UUID;

import stellarium.config.IConfigCategory;
import stellarium.config.IStellarConfig;

public class CategoryListEntry implements ICategoryEntry {
	
	protected StellarConfigCategory theCategory;
	
	protected CategoryListRoot root;
	protected CategoryListEntry previous, next;

	private UUID id;
	private String name;
	private boolean idEnabled = true;

	public CategoryListEntry(StellarConfigCategory category, CategoryListRoot root, CategoryListEntry previous, CategoryListEntry next) {
		this.theCategory = category;
		this.root = root;
		this.previous = previous;
		this.next = next;
		
		this.id = UUID.randomUUID();
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
	public boolean canCreateCategory(String name, EnumPosOption option) {
		if(!option.isOnSameLevel())
			return false;
		
		else if(option.isOnSameLevel() && root.getChildEntry(name) != null)
			return false;
		
		return root.config.canMakeCategory(this.root, name);
	}

	@Override
	public IConfigCategory createCategory(String name, EnumPosOption option) {
		if(!option.isOnSameLevel())
			throw new IllegalArgumentException("Option " + option.name() + " is not available for list entry.");
		
		else if(option.isOnSameLevel() && root.getChildEntry(name) != null)
			return root.getChildEntry(name).getCategory();
		
		
		StellarConfigCategory category = root.config.newCategory(this.root, name);
		
		if(category == null)
			return null;

		addNewEntry(category, option, null);
		
		root.config.postCreated(category);
		
		return category;
	}

	@Override
	public boolean canRemoveCategory() {
		if(this.theCategory == null)
			return false;
		return true;
	}
	
	@Override
	public boolean removeCategory() {
		
		if(this.theCategory == null)
			return false;
		
		root.config.preRemoved(theCategory);
		
		this.removeEntry();
		
		return true;
	}

	@Override
	public String getName() {
		if(this.theCategory == null)
			return this.name;
		
		return theCategory.getName();
	}
	
	@Override
	public boolean canChangeName(String name) {
		if(this.theCategory == null)
			return false;
		
		else if(root.getChildEntry(name) != null)
			return false;
		
		return true;
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
		
		else if(category.isImmutable())
			return null;
		
		StellarConfigCategory copiedCategory = root.config.copyCategory(this.root, name, category);
		
		if(copiedCategory == null)
			return null;
		
		addNewEntry(copiedCategory, option, null);
		
		root.config.postCopied(copiedCategory, category);
				
		return copiedCategory;
	}
	
	@Override
	public boolean canMigrateCategory(IConfigCategory category,
			EnumPosOption option) {
		if(category.isImmutable())
			return false;
		
		if(!option.isOnSameLevel())
			return false;
		
		else if(option.isOnSameLevel())
		{
			ICategoryEntry entry = root.getChildEntry(category.getName());
			if(entry != null && entry.getCategory() != category)
				return false;
		}
		
		return root.config.canMigrate(root, category.getName(), category.getCategoryEntry());
	}

	@Override
	public boolean migrateCategory(IConfigCategory category,
			EnumPosOption option) {
		
		if(category.isImmutable())
			return false;
		
		if(!option.isOnSameLevel())
			return false;
		
		else if(option.isOnSameLevel())
		{
			ICategoryEntry entry = root.getChildEntry(category.getName());
			if(entry != null && entry.getCategory() != category)
				return false;
		}
		
		if(!root.config.canMigrate(root, category.getName(), category.getCategoryEntry()))
			return false;
		
		StellarConfigCategory mcategory = (StellarConfigCategory) category;
		
		CategoryListEntry bentry = ((CategoryListEntry)mcategory.getCategoryEntry());
		
		//Invalidates this entry
		bentry.removeEntry();
		
		try {
			addNewEntry(mcategory, option, bentry.id);
		} catch(Exception e) {
			return false;
		}
		
		return true;
	}
	
	private void addNewEntry(StellarConfigCategory category, EnumPosOption option, UUID nid) {
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
		
		if(nid != null)
			list.id = nid;
		category.setEntry(list);
	}
	
	protected void removeEntry() {
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
				root.firstChild = this.next;
			} else {
				root.firstChild = root.lastChild = null;
			}
		}
		
		this.name = theCategory.name;
		this.theCategory = null;
	}
	
	@Override
	public void setIDEnabled(boolean idEnabled) {
		this.idEnabled = idEnabled;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof CategoryListEntry)
		{			
			if(!this.idEnabled)
				return this.getName().equals(((CategoryListEntry) obj).getName());
			else return id.equals(((CategoryListEntry) obj).id);
		}
		return false;
	}
	
	@Override
	public int hashCode()
	{
		if(!this.idEnabled)
			return this.getName().hashCode();
		else return id.hashCode();
	}
	
	
	@Override
	public Iterator<ICategoryEntry> iterator() {
		return null;
	}

}
