package stellarium.catalog;

import stellarium.config.IConfigFormatter;
import stellarium.config.IConfigurableData;

public interface IStellarCatalogData extends IConfigFormatter, IConfigurableData {

	public IStellarCatalogProvider getProvider();
}
