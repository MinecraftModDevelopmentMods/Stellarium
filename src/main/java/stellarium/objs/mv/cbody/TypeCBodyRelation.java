package stellarium.objs.mv.cbody;

import stellarium.config.IConfigCategory;
import stellarium.config.IConfigProperty;
import stellarium.config.IMConfigProperty;
import stellarium.config.IPropertyRelation;
import stellarium.lang.CPropLangStrs;
import stellarium.objs.mv.StellarMvLogical;

public class TypeCBodyRelation implements IPropertyRelation {
	
	IConfigCategory cat;
	StellarMvLogical mv;
	
	IMConfigProperty<ICBodyType> prop;
	
	public TypeCBodyRelation(IConfigCategory pcat, StellarMvLogical ins)
	{
		cat = pcat;
		mv = ins;
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
			prop.getVal().formatConfig(cat, mv, cat.getCategoryEntry().getParentEntry().isRootEntry());
			cat.setPropAddEntry(prep);
		}
	}

	@Override
	public void onValueChange(int i) {
		//Does Nothing!
	}

	@Override
	public String getRelationToolTip() {
		return CPropLangStrs.cbTypeRelation;
	}

}
