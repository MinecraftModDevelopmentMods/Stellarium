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

public class CCatalogCfgData implements ICatalogDataHandler, IConfigurableData, IConfigFormatter, ICfgArrMListener {
	
	public CCatalogData def;
	public Map<String, CCatalogData> datamap = Maps.newHashMap();
	public boolean isPhysical;
	
	public CCatalogCfgData(boolean isPhysical)
	{
		this.isPhysical = isPhysical;
		def = new CCatalogData(isPhysical);
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
		
		cfg.loadCategories();
		
		//TODO default working.
		
		cfg.addAMListener(this);
	}

	@Override
	public void applyConfig(IStellarConfig config) { }

	@Override
	public void saveConfig(IStellarConfig config) { }
	
	
	@Override
	public IConfigFormatter getSubFormatter(IConfigCategory cat) {
		return getDataRawType(cat.getDisplayName());
	}

	@Override
	public IConfigurableData getSubData(IConfigCategory cat) {
		return getDataRawType(cat.getDisplayName());
	}

	
	@Override
	public void onNew(IConfigCategory cat) {
		if(datamap.containsKey(cat.getDisplayName()))
			return;
		
		datamap.put(cat.getDisplayName(), new CCatalogData(isPhysical));		
	}

	@Override
	public void onRemove(IConfigCategory cat) {
		datamap.remove(cat.getDisplayName());
	}

	@Override
	public void onChangeParent(IConfigCategory cat, IConfigCategory from,
			IConfigCategory to) {
		//Cannot Be Called.
	}

	@Override
	public void onChangeOrder(IConfigCategory cat, int before, int after) {
		//Not Needed.
	}

	@Override
	public void onDispNameChange(IConfigCategory cat, String before) {
		CCatalogData data = datamap.remove(before);
		datamap.put(cat.getDisplayName(), data);
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
				cfg.addCategory(data.getProvider().getCatalogName());
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
		public IConfigFormatter getSubFormatter(IConfigCategory cat) {
			return datamap.get(cat.getID());
		}

		@Override
		public IConfigurableData getSubData(IConfigCategory cat) {
			return datamap.get(cat.getID());
		}
	}

}
