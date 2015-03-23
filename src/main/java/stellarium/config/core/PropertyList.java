package stellarium.config.core;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import com.google.common.collect.Lists;

import stellarium.config.IConfigCategory;
import stellarium.config.IStellarConfig;
import stellarium.config.core.CategoryListRoot.ChildIterator;

public class PropertyList implements Iterable<StellarConfigProperty> {

	protected StellarConfigCategory category;
	
	protected PropertyListEntry first, last;
	protected List<EntryIterator> ites = Lists.newArrayList();
	
	public PropertyList(StellarConfigCategory category) {
		this.category = category;
	}
	
	public IConfigCategory getCategory() {
		return this.category;
	}
	
	public boolean isEmpty() {
		return first == null;
	}
	
	
	public PropertyListEntry getFirstEntry() {
		return first;
	}
	
	public PropertyListEntry getLastEntry() {
		return last;
	}
	
	public EntryIterator forwardIterator() {
		EntryIterator ite = new EntryIterator(true);
		ites.add(ite);
		return ite;
	}
	
	public EntryIterator backwardIterator() {
		EntryIterator ite = new EntryIterator(true);
		ites.add(ite);
		return ite;
	}

	public PropertyListEntry getEntry(String name) {
		
		for(PropertyListEntry find = this.first; find != null; find = find.getNext())
		{
			if(name.equals(find.getProperty().getName()))
				return find;
		}
		
		return null;
	}
	
	public PropertyListEntry getEntry(StellarConfigProperty property) {
		return getEntry(property.getName());
	}
	
	public EntryIterator getEntryIterator(String name) {
		EntryIterator ite = new EntryIterator(getEntry(name));
		ites.add(ite);
		return ite;
	}
	
	public EntryIterator getEntryIterator(StellarConfigProperty property) {
		return getEntryIterator(property.getName());
	}
	
	public void add(StellarConfigProperty e) {
		if(last != null)
			last.add(e);
		else first = last = new PropertyListEntry(e, null, null);
	}
	
	public void remove(String name) {
		getEntry(name).remove();
	}
	
	public void remove(StellarConfigProperty el) {
		getEntry(el).remove();
	}
	
	public class PropertyListEntry {
		
		protected StellarConfigProperty property;
		
		protected PropertyListEntry previous, next;

		public PropertyListEntry(StellarConfigProperty property, PropertyListEntry previous, PropertyListEntry next) {
			this.property = property;
			this.previous = previous;
			this.next = next;
		}

		
		public StellarConfigProperty getProperty() {
			return property;
		}

		public boolean hasNext() {
			return this.next != null;
		}

		public PropertyListEntry getNext() {
			return this.next;
		}

		public boolean hasPrevious() {
			return this.previous != null;
		}

		public PropertyListEntry getPrevious() {
			return this.previous;
		}


		public void remove() {
			if(this.hasPrevious())
				previous.next = this.next;
			else first = this.next;
			
			if(this.hasNext())
				next.previous = this.previous;
			else last = this.previous;
			
			for(EntryIterator ite : ites)
				ite.onSync(this);
		}

		public void set(StellarConfigProperty e) {
			property = e;
		}

		public void add(StellarConfigProperty e) {
			PropertyListEntry entry = new PropertyListEntry(e, this, next);
			
			if(this.hasNext())
				next.previous = entry;
			else last = entry;
			
			this.next = entry;
		}
	}
	
	public class EntryIterator implements IEntryIterator<StellarConfigProperty> {
		
		protected boolean onFront;
		protected PropertyListEntry current;
		
		public EntryIterator(boolean front)
		{
			this.onFront = front;
			current = null;
		}

		public EntryIterator(PropertyListEntry entry) {
			current = entry;
			if(entry == null)
				this.onFront = false;
		}

		@Override
		public boolean hasNext() {
			if(current != null)
				return current.hasNext();
			else if(onFront)
				return !PropertyList.this.isEmpty();
			else return false;
		}

		@Override
		public StellarConfigProperty next() {
			if(!hasNext())
				throw new NoSuchElementException();
			if(current != null)
				current = current.next;
			else current = first;
			
			return current.property;
		}

		@Override
		public boolean hasPrevious() {
			if(current != null)
				return current.hasPrevious();
			else if(!onFront)
				return !PropertyList.this.isEmpty();
			else return false;
		}

		@Override
		public StellarConfigProperty previous() {
			if(!hasPrevious())
				throw new NoSuchElementException();
			if(current != null)
				current = current.previous;
			else current = last;
			
			return current.property;
		}
		
		public StellarConfigProperty get() {
			if(current != null)
				return current.property;
			else return null;
		}

		/**
		 * Sets this element.
		 * */
		public void set(StellarConfigProperty e) {
			if(current != null)
				current.set(e);
			else throw new IllegalStateException("Invalid Iterator Position to set");
		}

		/**
		 * Adds to the next element.
		 * */
		public void add(StellarConfigProperty e) {
			if(current != null)
				current.add(e);
			else PropertyList.this.add(e);
		}

		@Override
		public void remove() {
			if(current != null)
				current.remove();
		}
		
		
		public void onSync(PropertyListEntry theEntry) {
			if(current == null)
				return;
			
			if(theEntry == current)
			{
				if(current.hasPrevious())
					current = current.previous;
				else if(current.hasNext())
					current = current.next;
				else {
					current = null;
					this.onFront = false;
				}
			}
		}
	}

	@Override
	public Iterator<StellarConfigProperty> iterator() {
		return this.forwardIterator();
	}
}
