package stellarium.config.util;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Stack;

import stellarium.config.IConfigCategory;
import stellarium.config.IStellarConfig;


public class CfgCategoryIterator implements Iterator<IConfigCategory> {

	IStellarConfig cfg;
	IConfigCategory now = null;
		
	Stack<ListIterator> ites = new Stack();
		
	public CfgCategoryIterator(IStellarConfig pcfg)
	{
		cfg = pcfg;
	}
		
	public boolean hasNextRec()
	{
		if(ites.isEmpty())
			return false;
						
		ListIterator ite = ites.pop();
		boolean hn = ite.hasNext() || hasNextRec();
		ites.push(ite);
			
		return hn;
	}
	
	@Override
	public boolean hasNext() {
			
		if(ites.isEmpty())
			return true;
		
		if(now != null && !cfg.getAllSubCategories(now).isEmpty())
			return true;
			
		return hasNextRec();
			
	}

	@Override
	public IConfigCategory next() {
		
		if(ites.isEmpty())
		{
			ListIterator<IConfigCategory> rt = cfg.getAllCategories().listIterator();
			ites.push(rt);
			return now = rt.next();
		}
			
		if(!cfg.getAllSubCategories(now).isEmpty())
		{
			ListIterator<IConfigCategory> ite = cfg.getAllSubCategories(now).listIterator();
			ites.push(ite);
			return now = ite.next();
		}
			
		while(!ites.isEmpty())
		{
			ListIterator<IConfigCategory> ite = ites.pop();
			if(ite.hasNext())
			{
				ites.push(ite);
				return now = ite.next();
			}
		}
			
		return null;
	}

	@Override
	public void remove() {
		//Not Removable via this Iterator.
	}
		
}