package stellarium.catalog;

import java.util.Map;

import com.google.common.collect.Maps;

import stellarium.config.IConfigFormatter;
import stellarium.config.IConfigurableData;
import stellarium.config.IStellarConfig;

public class CCatalogCfgDataPhysical extends CCatalogData {
	
	private Map<String, IStellarCatalogData> datamap = Maps.newHashMap();
	
	public CCatalogCfgDataPhysical(ICCatalogDataSet dataset) {		
		for(IStellarCatalogData data : dataset)
		{
			IStellarCatalogData phdata = data.getProvider().providePhysicalData(data);
			datamap.put(data.getProvider().getCatalogName(), phdata);
		}
	}

	public CCatalogCfgDataPhysical() {
		for(IStellarCatalogProvider provider : StellarCatalogRegistry.getProvList())
		{
			IStellarCatalogData phdata = provider.provideCatalogData(true);
			datamap.put(provider.getCatalogName(), phdata);
		}
	}

}
