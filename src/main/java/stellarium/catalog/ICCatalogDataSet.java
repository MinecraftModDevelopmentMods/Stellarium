package stellarium.catalog;

import stellarium.config.IConfigFormatter;
import stellarium.config.IConfigurableData;

import com.google.common.collect.ImmutableList;

public interface ICCatalogDataSet extends Iterable<IStellarCatalogData>,
		IConfigurableData, IConfigFormatter {
	
}
