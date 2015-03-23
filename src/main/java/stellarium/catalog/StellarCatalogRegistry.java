package stellarium.catalog;

import java.util.Iterator;
import java.util.List;

import stellarium.objs.mv.StellarMvCatalog;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class StellarCatalogRegistry {
	
	private static StellarCatalogRegistry instance = new StellarCatalogRegistry();
	private List<IStellarCatalogProvider> providers = Lists.newArrayList();
	
	static {
		registerBase();
	}
	
	public static void registerBase() {
		register(new StellarMvCatalog());
	}
	
	public static void register(IStellarCatalogProvider prov) {
		instance.providers.add(prov);
	}
	
	public static ImmutableList<IStellarCatalogProvider> getProvList() {
		return ImmutableList.copyOf(instance.providers);
	}
	
	public static int getProvSize()
	{
		return instance.providers.size();
	}
	
	public static void onLoad()
	{
		for(IStellarCatalogProvider provider : instance.providers)
			provider.load();
	}
	
}
