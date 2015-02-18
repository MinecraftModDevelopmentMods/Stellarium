package stellarium.catalog;

import java.util.List;

public interface ICatalogDataHandler {
	
	/**Catalog Data for specific ID.*/
	public List<IStellarCatalogData> getData(String id);
	
	/**the first Catalog Data.*/
	public List<IStellarCatalogData> getData();
}
