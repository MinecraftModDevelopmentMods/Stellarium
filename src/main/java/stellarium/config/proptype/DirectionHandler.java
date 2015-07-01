package stellarium.config.proptype;

import sciapi.api.value.IValRef;
import sciapi.api.value.euclidian.EVector;
import sciapi.api.value.numerics.IReal;
import stellarium.config.IConfigPropHandler;
import stellarium.config.IMConfigProperty;
import stellarium.config.element.EnumPropElement;
import stellarium.config.element.IDoubleElement;
import stellarium.lang.CPropLangStrs;
import stellarium.util.math.SpCoord;
import stellarium.util.math.VecMath;

public class DirectionHandler implements IConfigPropHandler<EVector> {

	@Override
	public void onConstruct(IMConfigProperty<EVector> prop) {
		prop.addElement(CPropLangStrs.yaw, EnumPropElement.Double);
		prop.addElement(CPropLangStrs.pitch, EnumPropElement.Double);
	}

	@Override
	public EVector getValue(IMConfigProperty<EVector> prop) {
		IDoubleElement yaw = prop.getElement(CPropLangStrs.yaw);
		IDoubleElement pitch = prop.getElement(CPropLangStrs.pitch);
		
		SpCoord coord = new SpCoord(yaw.getValue(), pitch.getValue());
		EVector val = new EVector(3).set(coord.getVec());
		return val;
	}
	
	@Override
	public void onSetVal(IMConfigProperty<EVector> prop, EVector val) {
		IDoubleElement yaw = prop.getElement(CPropLangStrs.yaw);
		IDoubleElement pitch = prop.getElement(CPropLangStrs.pitch);
		
		SpCoord coord = new SpCoord();
		coord.setWithVec(val);
		
		yaw.setValue(coord.x);
		pitch.setValue(coord.y);
	}

}
