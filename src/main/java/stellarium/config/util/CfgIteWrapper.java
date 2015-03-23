package stellarium.config.util;

import java.util.Iterator;

import stellarium.config.IConfigCategory;
import stellarium.config.IStellarConfig;
import stellarium.config.core.ICategoryEntry;

public class CfgIteWrapper implements Iterable<IConfigCategory>
{
	
	ICategoryEntry rentry;
	
	public CfgIteWrapper(IStellarConfig cfg)
	{
		rentry = cfg.getRootEntry();
	}
	
	public CfgIteWrapper(ICategoryEntry rentry)
	{
		this.rentry = rentry;
	}

	@Override
	public Iterator<IConfigCategory> iterator() {
		return new CfgCategoryIterator(rentry);
	}
	
}
