package stellarium.objs.mv.additive;

import stellarium.mech.OpFilter;
import stellarium.objs.IStellarObj;

public interface ICAdditiveRenderer {

	/**Renders the Celestial Additive by certain Wavelength*/
	public void render(CAdditive obj, double radVsRes, double brightness, OpFilter filter);
	
}
