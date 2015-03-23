package stellarium.config.util;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import stellarium.config.IConfigCategory;
import stellarium.config.IStellarConfig;
import stellarium.config.core.ICategoryEntry;


/**
 * DFS Category Iterator for Configuration.
 * NOTE: Not removable with this iterator!
 * */
public class CfgCategoryIterator implements Iterator<IConfigCategory> {

	private ICategoryEntry now;
		
	public CfgCategoryIterator(IStellarConfig cfg)
	{
		this(cfg.getRootEntry());
	}
	
	public CfgCategoryIterator(ICategoryEntry rentry)
	{
		this.now = rentry;
	}
	
	@Override
	public boolean hasNext() {
		
		if(now.hasChildEntry())
			return true;
		else if(now.hasNextEntry())
			return true;
		else return false;
	}

	@Override
	public IConfigCategory next() {
		
		if(now.hasChildEntry())
		{
			now = now.getFirstChildEntry();
			return now.getCategory();
		}
		else if(now.hasNextEntry())
		{
			now = now.getNextEntry();
			return now.getCategory();
		}
		else throw new NoSuchElementException();
	}

	@Override
	public void remove() {
		//Not Removable via this Iterator.
	}
		
}