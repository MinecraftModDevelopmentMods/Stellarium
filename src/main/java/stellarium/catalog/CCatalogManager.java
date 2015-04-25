package stellarium.catalog;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

import com.google.common.collect.Lists;

import stellarium.StellarManager;
import stellarium.config.*;

public class CCatalogManager {
	
	private StellarManager parent;
	
	protected CCatalogCfgDataPhysical catalogData;
	
	private List<IStellarCatalog> catalog = Lists.newArrayList();
	
	private List<IStellarCatalog> findlist = Lists.newArrayList();
	private PriorityQueue<IStellarCatalog> finds = new PriorityQueue(
			StellarCatalogRegistry.getProvSize(), new Comparator<IStellarCatalog>() {

				@Override
				public int compare(IStellarCatalog arg0, IStellarCatalog arg1) {
					if(arg0.getProvider().prioritySearch() > arg1.getProvider().prioritySearch())
						return 1;
					else return -1;
				}
				
			});
	
	private List<IStellarCatalog> renderlist = Lists.newArrayList();
	private PriorityQueue<IStellarCatalog> renders = new PriorityQueue(
			StellarCatalogRegistry.getProvSize(), new Comparator<IStellarCatalog>() {

				@Override
				public int compare(IStellarCatalog arg0, IStellarCatalog arg1) {
					if(arg0.getProvider().priorityRender() > arg1.getProvider().priorityRender())
						return 1;
					else return -1;
				}
				
			});
		
	public CCatalogManager(StellarManager par) {
		parent = par;
	}
	
	protected void addCatalog(IStellarCatalog cat)
	{
		catalog.add(cat);
		finds.add(cat);
		renders.add(cat);
	}
	
	public void setupCatalogs()
	{
		for(IStellarCatalogData data : this.catalogData)
		{
			IStellarCatalog cat = data.getProvider().provideCatalog(parent, data);
			
			if(!cat.isDisabled())
				addCatalog(cat);
		}
		
		while(!finds.isEmpty())
			findlist.add(finds.poll());
		
		while(!renders.isEmpty())
			renderlist.add(renders.poll());
	}
	
	
	public Iterator<IStellarCatalog> getItetoFind()
	{
		return findlist.iterator();
	}
	
	public Iterator<IStellarCatalog> getItetoRender()
	{
		return renderlist.iterator();
	}

	
	public IConfigFormatter createPhysicalFormatter(
			ICatalogDataHandler formatter, CCatalogAdditionalData addData) {
		ICCatalogDataSet dataset;
		if("".equals(addData.getContext()))
			dataset = formatter.getDefaultData();
		else dataset = formatter.getData(addData.getContext());
		
		return this.catalogData = new CCatalogCfgDataPhysical(dataset);
	}

	public IConfigurableData createPhysicalData(ICatalogDataHandler data,
			CCatalogAdditionalData addData) {
		return this.catalogData;
	}

	public IConfigFormatter createPhysicalFormatter() {
		return this.catalogData = new CCatalogCfgDataPhysical();
	}

	public IConfigurableData createPhysicalData() {
		return this.catalogData;
	}
}
