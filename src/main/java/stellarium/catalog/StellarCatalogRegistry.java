package stellarium.catalog;

import java.util.Iterator;
import java.util.List;

import stellarium.objs.mv.StellarMvCatalog;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class StellarCatalogRegistry {
	
	private static StellarCatalogRegistry instance = new StellarCatalogRegistry();
	private List<IStellarCatalogProvider> providers = Lists.newArrayList();
	
	public static void registerBase()
	{
		register(new StellarMvCatalog());
	}
	
	public static void register(IStellarCatalogProvider prov)
	{
		instance.providers.add(prov);
	}
	
	public static ImmutableList<IStellarCatalogProvider> getProvList()
	{
		return ImmutableList.copyOf(instance.providers);
	}
	
}
