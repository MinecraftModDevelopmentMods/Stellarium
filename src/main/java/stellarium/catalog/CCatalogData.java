package stellarium.catalog;

import java.util.Iterator;
import java.util.Map;

import stellarium.config.EnumCategoryType;
import stellarium.config.IConfigFormatter;
import stellarium.config.IConfigurableData;
import stellarium.config.IStellarConfig;
import stellarium.config.core.EnumPosOption;

import com.google.common.collect.Maps;

public class CCatalogData implements ICCatalogDataSet {
	private Map<String, IStellarCatalogData> datamap = Maps.newHashMap();
	
	public CCatalogData()
	{
		for(IStellarCatalogProvider prov : StellarCatalogRegistry.getProvList())
			datamap.put(prov.getCatalogName(), prov.provideCatalogData(false));
	}
	
	@Override
	public void formatConfig(IStellarConfig cfg) {
		cfg.setCategoryType(EnumCategoryType.ConfigList);
		
		for(IStellarCatalogData data : this)
			cfg.getRootEntry().createCategory(data.getProvider().getCatalogName(), EnumPosOption.Child);
	}

	@Override
	public void applyConfig(IStellarConfig config) { }

	@Override
	public void saveConfig(IStellarConfig config) { }

	@Override
	public Iterator<IStellarCatalogData> iterator() {
		return datamap.values().iterator();
	}

	
	@Override
	public IConfigFormatter getSubFormatter(String name) {
		return datamap.get(name);
	}

	@Override
	public IConfigurableData getSubData(String name) {
		return datamap.get(name);
	}
}
