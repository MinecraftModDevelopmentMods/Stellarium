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

public class CCatalogCfgData implements ICatalogDataHandler, ICfgArrMListener {
	
	private Map<String, CCatalogData> dataMap = Maps.newHashMap();
	
	public CCatalogCfgData()
	{
		dataMap.put("Default", new CCatalogData());
	}
	
	@Override
	public ICCatalogDataSet getData(String id) {
		return this.getDataRawType(id);
	}
	
	private CCatalogData getDataRawType(String id) {
		if(!dataMap.containsKey(id))
			throw new IllegalArgumentException("Wrong ID: " + id);
		return dataMap.get(id);
	}

	@Override
	public ICCatalogDataSet getDefaultData() {
		if(dataMap.isEmpty())
			throw new IllegalStateException("Empty Catalog Data; Might be connection error");
		for(String id : dataMap.keySet())
		{
			if(!id.equals("Default"))
				return dataMap.get(id);
		}
		return dataMap.get("Default");
	}

	
	@Override
	public void formatConfig(IStellarConfig cfg) {
		cfg.setCategoryType(EnumCategoryType.ConfigList);
		
		cfg.setModifiable(true, true);
		
		cfg.addAMListener(this);
		
		cfg.loadCategories();
		
		for(String name : dataMap.keySet())
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
		if(dataMap.containsKey(name))
			return true;
		
		dataMap.put(name, new CCatalogData());
		
		return true;
	}
	
	@Override
	public void onRemove(IConfigCategory cat) {
		dataMap.remove(cat.getName());
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
		if(!dataMap.containsKey(before))
			return;
		
		CCatalogData data = dataMap.remove(before);
		dataMap.put(cat.getName(), data);
	}

}
