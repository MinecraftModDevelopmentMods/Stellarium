package stellarium.objs.mv.orbit;

import stellarium.config.IConfigCategory;
import stellarium.config.IConfigProperty;
import stellarium.config.IMConfigProperty;
import stellarium.config.IPropertyRelation;
import stellarium.lang.CPropLangStrs;
import stellarium.objs.mv.StellarMvLogical;

public class TypeOrbitRelation implements IPropertyRelation {

	IConfigCategory cat;
	StellarMvLogical mv;
	
	IMConfigProperty<IOrbitType> prop;
	
	public TypeOrbitRelation(IConfigCategory pcat, StellarMvLogical ins)
	{
		cat = pcat;
		this.mv = ins;
	}

	@Override
	public void setProps(IMConfigProperty... props) {
		prop = props[0];
	}

	@Override
	public void onEnable(int i) {
		if(prop.getVal() != null)
			prop.getVal().removeConfig(cat);
	}

	@Override
	public void onDisable(int i) {
		if(prop.getVal() != null)
		{
			IConfigProperty prep = cat.setPropAddEntry(prop);
			prop.getVal().formatConfig(cat, mv);
			cat.setPropAddEntry(prep);
		}
	}

	@Override
	public void onValueChange(int i) {
		//Does Nothing!
	}

	@Override
	public String getRelationToolTip() {
		return CPropLangStrs.orbTypeRelation;
	}

}
