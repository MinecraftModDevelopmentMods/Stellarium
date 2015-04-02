package stellarium.config.core;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.UUID;

import stellarium.config.ICfgArrMListener;
import stellarium.config.IConfigCategory;
import stellarium.config.IStellarConfig;

public class CategoryNode implements ICategoryEntry {

	protected StellarConfiguration cfg;
	
	protected CategoryNode parent;
	protected CategoryNode previous, next;
	protected CategoryNode firstChild, lastChild;
	
	protected StellarConfigCategory theCategory;
	
	private UUID id = null;
	private String name;
	private boolean idEnabled = true;
	
	public CategoryNode(StellarConfiguration cfg, StellarConfigCategory theCategory, CategoryNode parent, CategoryNode previous, CategoryNode next)
	{
		this.cfg = cfg;
		this.theCategory = theCategory;
		this.parent = parent;
		this.previous = previous;
		this.next = next;
		
		if(parent != null)
			this.id = UUID.randomUUID();
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
			if(name.equals(find.getName()))
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
	public boolean canCreateCategory(String name, EnumPosOption option) {
		if(option.isOnSameLevel() && this.isRootEntry())
			return false;
		
		else if(option.isOnSameLevel() && parent.getChildEntry(name) != null)
			return false;
		
		else if(!option.isOnSameLevel() && this.getChildEntry(name) != null)
			return false;
		
		CategoryNode parent = option.isOnSameLevel()? this.parent : this;
		
		return cfg.canMakeCategory(parent, name);
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
		
		if(category == null)
			return null;

		addNewNode(category, option, null);
		
		cfg.postCreated(category);
		
		return category;
	}
	
	@Override
	public boolean canRemoveCategory() {
		if(this.theCategory == null)
			return false;
		
		if(this.isRootEntry())
			return false;
		
		//Removes child categories
		for(ICategoryEntry entry = this.firstChild; entry != null; entry = entry.getNextEntry())
		{
			if(!entry.removeCategory())
				return false;
		}
		
		return true;
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
		}
		
		
		cfg.preRemoved(theCategory);
		
		this.removeNode();
		
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
			
		else if(parent.getChildEntry(name) != null)
			return false;
		
		return true;
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
		
		if(option.isOnSameLevel()) {
			if(this.isRootEntry())
				throw new IllegalArgumentException("Option " + option.name() + " is not available for root entry.");
		
			else if(parent.getChildEntry(name) != null)
				return parent.getChildEntry(name).getCategory();
			
			else if(!parent.isRootEntry() && parent.getCategory().isImmutable())
				return null;
		}
		
		else if(!option.isOnSameLevel()) {
			if(this.getChildEntry(name) != null)
				return this.getChildEntry(name).getCategory();
			
			else if(this.getCategory().isImmutable())
				return null;
		}
				
		
		CategoryNode parent = option.isOnSameLevel()? this.parent : this;
		
		StellarConfigCategory copiedCategory = cfg.newCategory(parent, name);
		
		if(copiedCategory == null)
			return null;
		
		addNewNode(copiedCategory, option, null);
		
		cfg.postCreated(copiedCategory);
		
		copiedCategory.copy(category);
		
		return copiedCategory;
	}
	
	@Override
	public boolean canMigrateCategory(IConfigCategory category,
			EnumPosOption option) {
		if(category.isImmutable())
			return false;
		
		else if(option.isOnSameLevel())
		{
			if(this.isRootEntry())
				return false;
			
			ICategoryEntry entry = parent.getChildEntry(category.getName());
			
			if(entry != null && entry.getCategory() != category)
				return false;
			else if(!parent.isRootEntry() && parent.getCategory().isImmutable())
				return false;
			else {
				ICategoryEntry ent = this;
				while(!ent.isRootEntry()) {
					ent = ent.getParentEntry();
					if(ent.equals(category.getCategoryEntry()))
						return false;
				}
			}
		}
		
		else if(!option.isOnSameLevel()) {
			if(this.getChildEntry(category.getName()) != null || this.getCategory().isImmutable())
				return false;
			
			else {
				ICategoryEntry ent = this;
				while(!ent.isRootEntry()) {
					if(ent.equals(category.getCategoryEntry()))
						return false;
					ent = ent.getParentEntry();
				}
			}
		}
		
		return cfg.canMigrate(option.isOnSameLevel()? this.parent : this, category.getName(), category.getCategoryEntry());
	}

	@Override
	public boolean migrateCategory(IConfigCategory category,
			EnumPosOption option) {
		
		if(category.isImmutable())
			return false;
		
		else if(option.isOnSameLevel())
		{
			if(this.isRootEntry())
				return false;
			
			ICategoryEntry entry = parent.getChildEntry(category.getName());
			
			if(entry != null && entry.getCategory() != category)
				return false;
			else if(!parent.isRootEntry() && parent.getCategory().isImmutable())
				return false;
			else {
				ICategoryEntry ent = this;
				while(!ent.isRootEntry()) {
					ent = ent.getParentEntry();
					if(ent.equals(category.getCategoryEntry()))
						return false;
				}
			}
		}
		
		else if(!option.isOnSameLevel()) {
			if(this.getChildEntry(category.getName()) != null || this.getCategory().isImmutable())
				return false;
			
			else {
				ICategoryEntry ent = this;
				while(!ent.isRootEntry()) {
					if(ent.equals(category.getCategoryEntry()))
						return false;
					ent = ent.getParentEntry();
				}
			}
		}
		
		if(!cfg.canMigrate(option.isOnSameLevel()? this.parent : this, category.getName(), category.getCategoryEntry()))
			return false;
		
		StellarConfigCategory mcategory = (StellarConfigCategory) category;
		
		//Invalidates the entry.
		CategoryNode node = ((CategoryNode)mcategory.getCategoryEntry());
		node.removeNode();
		
		try {
			CategoryNode newNode = addNewNode(mcategory, option, node.id);
			newNode.firstChild = node.firstChild;
			if(newNode.firstChild != null)
				newNode.firstChild.parent = newNode;
			if(newNode.lastChild != null)
				newNode.lastChild.parent = newNode;
			newNode.lastChild = node.lastChild;
		} catch(IllegalArgumentException e) {
			return false;
		}
		
		return true;
	}
	
	
	private CategoryNode addNewNode(StellarConfigCategory category, EnumPosOption option, UUID nid)
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
		
		if(nid != null)
			node.id = nid;
		
		category.setEntry(node);
		return node;
	}
	
	private void removeNode() {
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
				parent.firstChild = this.next;
			} else {
				parent.firstChild = parent.lastChild = null;
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
		if(obj instanceof CategoryNode)
		{
			CategoryNode node = (CategoryNode) obj;
			
			if(this.isRootEntry())
				return node.isRootEntry();
			else if(node.isRootEntry())
				return false;
			
			if(!parent.equals(node.parent))
				return false;
			
			if(!this.idEnabled)
				return this.getName().equals(node.getName());
			else return this.id.equals(node.id);
		}
		return false;
	}
	
	@Override
	public int hashCode()
	{
		if(this.isRootEntry())
			return 0;
		
		if(!this.idEnabled)
			return parent.hashCode() + this.getName().hashCode();
		else return parent.hashCode() + id.hashCode();
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
