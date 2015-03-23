package stellarium.config.core;

import java.util.Iterator;
import java.util.NoSuchElementException;

import stellarium.config.ICfgArrMListener;
import stellarium.config.IConfigCategory;
import stellarium.config.IStellarConfig;

public class CategoryNode implements ICategoryEntry {

	protected StellarConfiguration cfg;
	
	protected CategoryNode parent;
	protected CategoryNode previous, next;
	protected CategoryNode firstChild, lastChild;
	
	protected StellarConfigCategory theCategory;
		
	
	public CategoryNode(StellarConfiguration cfg, StellarConfigCategory theCategory, CategoryNode parent, CategoryNode previous, CategoryNode next)
	{
		this.cfg = cfg;
		this.theCategory = theCategory;
		this.parent = parent;
		this.previous = previous;
		this.next = next;
	}
	
	public CategoryNode(StellarConfiguration cfg, StellarConfigCategory theCategory, CategoryNode parent)
	{
		this(cfg, theCategory, parent, null, null);
	}
	
	
	@Override
	public IStellarConfig getConfig()
	{
		return this.cfg;
	}
	
	@Override
	public IConfigCategory getCategory() {
		if(this.theCategory == null)
			throw new IllegalStateException("Cannot get the Category from Invalidated Entry!");
		
		return this.theCategory;
	}
	
	
	@Override
	public boolean isRootEntry() {
		return parent == null;
	}


	@Override
	public ICategoryEntry getParentEntry() {
		return parent;
	}
	
	@Override
	public boolean hasChildEntry() {
		return this.firstChild != null;
	}
	
	@Override
	public IEntryIterator<ICategoryEntry> getChildEntryIterator() {
		return new ChildIterator(true);
	}

	public IEntryIterator<ICategoryEntry> getChildEntryBackwardIte() {
		return new ChildIterator(false);
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
		
		if(option.isOnSameLevel() && this.isRootEntry())
			throw new IllegalArgumentException("Option " + option.name() + " is not available for root entry.");
		
		else if(option.isOnSameLevel() && parent.getChildEntry(name) != null)
			return parent.getChildEntry(name).getCategory();
		
		else if(!option.isOnSameLevel() && this.getChildEntry(name) != null)
			return this.getChildEntry(name).getCategory();
		
		
		CategoryNode parent = option.isOnSameLevel()? this.parent : this;
		
		StellarConfigCategory category = cfg.newCategory(parent, name);

		addNewNode(category, option);
		
		cfg.postCreated(category);
		
		return category;
	}

	@Override
	public boolean removeCategory() {
		
		if(this.theCategory == null)
			return false;
		
		if(this.isRootEntry())
			return false;
		
		//Removes child categories
		for(ICategoryEntry entry = this.firstChild; entry != null; entry = entry.getNextEntry())
		{
			if(!entry.removeCategory())
				return false;
			
			entry = entry.getNextEntry();
		}
		
		
		cfg.preRemoved(theCategory);
		
		
		boolean hasPrevious = this.hasPreviousEntry();
		boolean hasNext = this.hasNextEntry();
		
		if(hasPrevious) {
			if(hasNext)	{
				previous.next = this.next;
				next.previous = this.previous;
			} else {
				previous.next = null;
				parent.lastChild = this.previous;
			}
		} else {
			if(hasNext) {
				next.previous = null;
				parent.firstChild = this.previous;
			} else {
				parent.firstChild = parent.lastChild = null;
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
		
		else if(parent.getChildEntry(name) != null)
			return false;
		
		return theCategory.setName(name);
	}

	@Override
	public IConfigCategory copyCategory(IConfigCategory category, String name,
			EnumPosOption option) {
		
		if(option.isOnSameLevel() && this.isRootEntry())
			throw new IllegalArgumentException("Option " + option.name() + " is not available for root entry.");
		
		else if(option.isOnSameLevel() && parent.getChildEntry(name) != null)
			return parent.getChildEntry(name).getCategory();
		
		else if(!option.isOnSameLevel() && this.getChildEntry(name) != null)
			return this.getChildEntry(name).getCategory();
		
		
		CategoryNode parent = option.isOnSameLevel()? this.parent : this;
		
		StellarConfigCategory copiedCategory = cfg.newCategory(parent, name);
		
		addNewNode(copiedCategory, option);
		
		cfg.postCreated(copiedCategory);
		
		copiedCategory.copy(category);
		
		return copiedCategory;
	}

	@Override
	public boolean migrateCategory(IConfigCategory category,
			EnumPosOption option) {
		
		if(option.isOnSameLevel() && this.isRootEntry())
			return false;
		
		else if(option.isOnSameLevel() && parent.getChildEntry(category.getName()) != null)
			return false;
		
		else if(!option.isOnSameLevel() && this.getChildEntry(category.getName()) != null)
			return false;
		
		
		StellarConfigCategory mcategory = (StellarConfigCategory) category;
		
		//Invalidates this entry
		((CategoryNode)mcategory.getCategoryEntry()).theCategory = null;
		
		try {
			addNewNode(mcategory, option);
		} catch(Exception e) {
			return false;
		}
		
		return true;
	}
	
	
	private void addNewNode(StellarConfigCategory category, EnumPosOption option)
	{
		CategoryNode node;
		
		switch(option)
		{
		case Previous:
			if(this.hasPreviousEntry()) {
				node = new CategoryNode(this.cfg, category, this.parent, this.previous, this);
				previous.next = node;
				this.previous = node;
			} else {
				node = new CategoryNode(this.cfg, category, this.parent, null, this);
				parent.firstChild = node;
				this.previous = node;
			}
			break;
			
		case Next:
			if(this.hasNextEntry())	{
				node = new CategoryNode(this.cfg, category, this.parent, this, this.next);
				next.previous = node;
				this.next = node;
			} else {
				node = new CategoryNode(this.cfg, category, this.parent, this, null);
				parent.lastChild = node;
				this.next = node;
			}
			break;
			
		case Child:
			
			if(this.hasChildEntry())
			{
				node = new CategoryNode(this.cfg, category, this, this.lastChild, null);
				lastChild.next = node;
				this.lastChild = node;
			} else {
				node = new CategoryNode(this.cfg, category, this);
				this.firstChild = this.lastChild = node;
			}
			break;
			
		default:
			throw new IllegalArgumentException("Invalid Option: " + option);
		}
		
		category.setEntry(node);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof CategoryNode)
		{
			CategoryNode node = (CategoryNode) obj;
			
			if(this.isRootEntry())
				return node.isRootEntry();
			else if(node.isRootEntry())
				return false;
			
			return this.parent.equals(node.parent) &&
					this.getName().equals(node.getName());
		}
		return false;
	}
	
	@Override
	public int hashCode()
	{
		if(this.isRootEntry())
			return 0;
		
		return parent.hashCode() + this.getName().hashCode();
	}

	
	public class ChildIterator implements IEntryIterator<ICategoryEntry> {
		
		protected boolean onFront;
		protected CategoryNode current;
		
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
				return CategoryNode.this.hasChildEntry();
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
				return CategoryNode.this.hasChildEntry();
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
}
