package stellarium.catalog;

import java.util.List;

public interface ICatalogDataHandler {
	
	/**Catalog Data for specific ID.*/
	public ICCatalogDataSet getData(String id);
	
	/**the default Catalog Data.*/
	public ICCatalogDataSet getDefaultData();
	
}
