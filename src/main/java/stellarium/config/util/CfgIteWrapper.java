package stellarium.config.util;

import java.util.Iterator;

import stellarium.config.IConfigCategory;
import stellarium.config.IStellarConfig;

public class CfgIteWrapper implements Iterable<IConfigCategory>
{
	
	IStellarConfig cfg;
	
	public CfgIteWrapper(IStellarConfig pcfg)
	{
		cfg = pcfg;
	}

	@Override
	public Iterator<IConfigCategory> iterator() {
		return new CfgCategoryIterator(cfg);
	}
	
}
