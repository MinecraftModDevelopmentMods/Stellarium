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
		return def;
	}

	
	@Override
	public void formatConfig(IStellarConfig cfg) {
		cfg.setCategoryType(EnumCategoryType.ConfigList);

		cfg.setModifiable(true, true);
		
		cfg.loadCategories();
		
		for(IConfigCategory cat : cfg.getAllCategories())
		{
			CCatalogData newData = new CCatalogData(isPhysical);
			datamap.put(cat.getDisplayName(), newData);
			
			newData.formatConfig(cfg.getSubConfig(cat));
		}
		
		cfg.addAMListener(this);
	}

	@Override
	public void applyConfig(IStellarConfig config) {
		for(IConfigCategory cat : config.getAllCategories())
			getDataRawType(cat.getDisplayName()).applyConfig(config.getSubConfig(cat));
	}

	@Override
	public void saveConfig(IStellarConfig config) {
		for(IConfigCategory cat : config.getAllCategories())
			getDataRawType(cat.getDisplayName()).saveConfig(config.getSubConfig(cat));
	}

	
	@Override
	public void onNew(IConfigCategory cat) {
		CCatalogData newData = new CCatalogData(isPhysical);
		datamap.put(cat.getDisplayName(), newData);
		
		newData.formatConfig(cat.getConfig().getSubConfig(cat));
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
		private List<IStellarCatalogData> datalist = Lists.newArrayList();
		
		public CCatalogData(boolean isPhysical)
		{
			this.isPhysical = isPhysical;
			
			for(IStellarCatalogProvider prov : StellarCatalogRegistry.getProvList())
				datalist.add(prov.provideCatalogData(isPhysical));
		}

		@Override
		public void formatConfig(IStellarConfig cfg) {
			cfg.setCategoryType(EnumCategoryType.ConfigList);
			
			for(IStellarCatalogData data : this)
			{
				IConfigCategory datacat = cfg.addCategory(data.getProvider().getCatalogName());
				data.formatConfig(cfg.getSubConfig(datacat));
			}
		}

		@Override
		public void applyConfig(IStellarConfig config) {
			for(IStellarCatalogData data : this)
			{
				IConfigCategory datacat = config.getCategory(data.getProvider().getCatalogName());
				data.applyConfig(config.getSubConfig(datacat));
			}
		}

		@Override
		public void saveConfig(IStellarConfig config) {
			for(IStellarCatalogData data : this)
			{
				IConfigCategory datacat = config.getCategory(data.getProvider().getCatalogName());
				data.saveConfig(config.getSubConfig(datacat));
			}
		}

		@Override
		public Iterator<IStellarCatalogData> iterator() {
			return datalist.iterator();
		}
	}

}
