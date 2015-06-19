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
import stellarium.util.math.Spmath;
import stellarium.util.math.VecMath;

public class EVectorHandler implements IConfigPropHandler<EVector> {

	@Override
	public void onConstruct(IMConfigProperty<EVector> prop) {
		prop.addElement(CPropLangStrs.size, EnumPropElement.Double);
		prop.addElement(CPropLangStrs.yaw, EnumPropElement.Double);
		prop.addElement(CPropLangStrs.pitch, EnumPropElement.Double);
	}

	@Override
	public EVector getValue(IMConfigProperty<EVector> prop) {
		IDoubleElement size = prop.getElement(CPropLangStrs.size);
		IDoubleElement yaw = prop.getElement(CPropLangStrs.yaw);
		IDoubleElement pitch = prop.getElement(CPropLangStrs.pitch);
		
		SpCoord coord = new SpCoord(yaw.getValue(), pitch.getValue());
		
		EVector val = new EVector(3).set(VecMath.mult(size.getValue(), coord.getVec()));
		return val;
	}
	
	@Override
	public void onSetVal(IMConfigProperty<EVector> prop, EVector val) {
		IDoubleElement size = prop.getElement(CPropLangStrs.size);
		IDoubleElement yaw = prop.getElement(CPropLangStrs.yaw);
		IDoubleElement pitch = prop.getElement(CPropLangStrs.pitch);
		
		IValRef<IReal> sizer = VecMath.size(val);
		double sized = sizer.getVal().asDouble();
		SpCoord coord = new SpCoord();
		coord.setWithVec(VecMath.div(sizer, val));
		
		size.setValue(sized);
		yaw.setValue(coord.x);
		pitch.setValue(coord.y);
	}

}
