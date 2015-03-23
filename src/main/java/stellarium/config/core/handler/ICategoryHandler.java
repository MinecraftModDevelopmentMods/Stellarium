package stellarium.config.core.handler;

import stellarium.config.core.StellarConfigCategory.PropertyRelation;
import stellarium.config.core.StellarConfigProperty;

public interface ICategoryHandler {

	/**called after add*/
	public IPropertyHandler getNewProp(StellarConfigProperty prop);

	/**called before remove*/
	public void onRemoveProp(StellarConfigProperty prop);
	
	/**For Property Relation Cosmetics*/
	public void onPropertyRelationAdded(PropertyRelation pr);
	
	/**For Property Relation Cosmetics*/
	public void onPropertyRelationRemoved(PropertyRelation pr);

}
