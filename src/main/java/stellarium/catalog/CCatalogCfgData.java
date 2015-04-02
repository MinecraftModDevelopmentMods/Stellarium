package stellarium.catalog;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import stellarium.config.EnumCategoryType;
import stellarium.config.ICfgArrMListener;
import stellarium.config.IConfigCategory;
import stellarium.config.IConfigFormatter;
import stellarium.config.IConfigurableData;
import stellarium.config.IStellarConfig;
import stellarium.config.core.EnumPosOption;
import stellarium.config.core.ICategoryEntry;

public class CCatalogCfgData implements ICatalogDataHandler, IConfigurableData, IConfigFormatter, ICfgArrMListener {
	
	public Map<String, CCatalogData> datamap = Maps.newHashMap();
	public boolean isPhysical;
	
	public CCatalogCfgData(boolean isPhysical)
	{
		this.isPhysical = isPhysical;
		
		datamap.put("Default", new CCatalogData(isPhysical));
	}
	
	@Override
	public ICCatalogDataSet getData(String id) {
		return this.getDataRawType(id);
	}
	
	private CCatalogData getDataRawType(String id) {
		if(!datamap.containsKey(id))
			throw new IllegalArgumentException("Wrong ID: " + id);
		return datamap.get(id);
	}

	@Override
	public ICCatalogDataSet getDefaultData() {
		if(datamap.isEmpty())
			throw new IllegalStateException("Empty Catalog Data; Might be connection error");
		return (ICCatalogDataSet) datamap.values().toArray()[0];
	}

	
	@Override
	public void formatConfig(IStellarConfig cfg) {
		cfg.setCategoryType(EnumCategoryType.ConfigList);
		
		cfg.setModifiable(true, true);
		
		cfg.addAMListener(this);
		
		cfg.loadCategories();
		
		for(String name : datamap.keySet())
		{
			cfg.getRootEntry().createCategory(name, EnumPosOption.Child);
		}
		
		//TODO default working.
	}

	@Override
	public void applyConfig(IStellarConfig config) { }

	@Override
	public void saveConfig(IStellarConfig config) { }
	
	
	@Override
	public IConfigFormatter getSubFormatter(String name) {
		return getDataRawType(name);
	}

	@Override
	public IConfigurableData getSubData(String name) {
		return getDataRawType(name);
	}

	
	@Override
	public boolean canCreate(ICategoryEntry parent, String name) {
		if(datamap.containsKey(name))
			return true;
		
		datamap.put(name, new CCatalogData(isPhysical));
		
		return true;
	}
	
	@Override
	public void onRemove(IConfigCategory cat) {
		datamap.remove(cat.getName());
	}

	@Override
	public void onPostCreated(IConfigCategory cat) { }

	@Override
	public boolean canMigrate(ICategoryEntry parent, String name,
			ICategoryEntry before) {
		return true;
	}
	
	@Override
	public void onMigrate(IConfigCategory cat, ICategoryEntry before) { }

	@Override
	public void onNameChange(IConfigCategory cat, String before) {
		if(!datamap.containsKey(before))
			return;
		
		CCatalogData data = datamap.remove(before);
		datamap.put(cat.getName(), data);
	}
	
	
	public class CCatalogData implements ICCatalogDataSet, IConfigurableData, IConfigFormatter {
		
		private boolean isPhysical;
		private Map<String, IStellarCatalogData> datamap = Maps.newHashMap();
		
		public CCatalogData(boolean isPhysical)
		{
			this.isPhysical = isPhysical;
			
			for(IStellarCatalogProvider prov : StellarCatalogRegistry.getProvList())
				datamap.put(prov.getCatalogName(), prov.provideCatalogData(isPhysical));
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

}
