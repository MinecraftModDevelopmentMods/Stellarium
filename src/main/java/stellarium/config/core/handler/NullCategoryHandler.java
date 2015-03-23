package stellarium.config.core.handler;

import stellarium.config.core.StellarConfigCategory.PropertyRelation;
import stellarium.config.core.StellarConfigProperty;

public class NullCategoryHandler implements ICategoryHandler {

	@Override
	public IPropertyHandler getNewProp(StellarConfigProperty prop) {
		return null;
	}

	@Override
	public void onRemoveProp(StellarConfigProperty prop) { }

	@Override
	public void onPropertyRelationAdded(PropertyRelation pr) { }

	@Override
	public void onPropertyRelationRemoved(PropertyRelation pr) { }

}
