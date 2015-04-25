package stellarium.catalog;

import java.util.List;

import stellarium.config.IConfigFormatter;
import stellarium.config.IConfigurableData;

public interface ICatalogDataHandler extends IConfigurableData, IConfigFormatter {
	
	/**Catalog Data for specific ID.*/
	public ICCatalogDataSet getData(String id);
	
	/**the default Catalog Data.*/
	public ICCatalogDataSet getDefaultData();
	
}
