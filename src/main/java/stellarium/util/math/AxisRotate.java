package stellarium.util.math;

import sciapi.api.value.IValRef;
import sciapi.api.value.euclidian.CrossUtil;
import sciapi.api.value.euclidian.ERotate;
import sciapi.api.value.euclidian.EVectorSet;
import sciapi.api.value.euclidian.IEVector;
import sciapi.api.value.numerics.IReal;
import sciapi.api.value.util.VOp;

public class AxisRotate extends ERotate {

	public AxisRotate(IEVector axis, double angle) {
		super(axis, axis, true);
		
		this.a = (IEVector) axis.getParentSet().getNew();
		this.b = (IEVector) axis.getParentSet().getNew();
		
		try{
			IValRef v = CrossUtil.cross(axis, (IEVector)EVectorSet.ins(3).units[0]);
			// Some fix needed in SciAPI
			if(((IReal)VOp.size(v)).asDouble() == 0.0)
				throw new ArithmeticException();
			a.set(VOp.normalize(v));
		} catch(ArithmeticException e)
		{
			a.set(VOp.normalize(CrossUtil.cross(axis, (IEVector)EVectorSet.ins(3).units[1])));
		}
		
		b.set(VOp.normalize(CrossUtil.cross(axis, a)));
		
		this.setAngle(angle);
	}

}
