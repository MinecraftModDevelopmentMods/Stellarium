package stellarium.catalog;

import java.util.Comparator;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

import stellarium.config.EnumCategoryType;
import stellarium.config.IConfigCategory;
import stellarium.config.IStellarConfig;
import stellarium.render.StellarRenderingRegistry;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;

import cpw.mods.fml.relauncher.Side;

public class StellarCatalogRegistry {
	
	private static EnumMap<Side, StellarCatalogRegistry> instance = Maps.newEnumMap(Side.class);
	
	public static StellarCatalogRegistry instance(Side side)
	{
		if(instance.get(side) == null)
			instance.put(side, new StellarCatalogRegistry());
		return instance.get(side);
	}
	
	private List<IStellarCatalogProvider> providers = Lists.newArrayList();
	
	public void register(IStellarCatalogProvider prov)
	{
		providers.add(prov);
	}
	
}
