package stellarium.config.proptype;

import stellarium.config.IConfigPropHandler;
import stellarium.config.IMConfigProperty;
import stellarium.config.element.EnumPropElement;
import stellarium.config.element.IEnumElement;
import stellarium.lang.CPropLangStrs;
import stellarium.objs.mv.CMvTypeRegistry;

public class ToggleYesNoPropHandler implements IConfigPropHandler<Boolean> {

	@Override
	public void onConstruct(IMConfigProperty<Boolean> prop) {
		prop.addElement(prop.getName(), EnumPropElement.Enum);
		
		IEnumElement pee = prop.getElement(prop.getName());
		pee.setValRange(CPropLangStrs.yes, CPropLangStrs.no);
	}

	@Override
	public Boolean getValue(IMConfigProperty<Boolean> prop) {
		IEnumElement pee = prop.getElement(prop.getName());
		return pee.getIndex() == 0;
	}

	@Override
	public void onSetVal(IMConfigProperty<Boolean> prop, Boolean val) {
		IEnumElement pee = prop.getElement(prop.getName());
		pee.setValue(val? 0 : 1);
	}

}
