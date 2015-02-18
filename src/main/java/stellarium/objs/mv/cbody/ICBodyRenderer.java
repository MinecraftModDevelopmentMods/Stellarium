package stellarium.objs.mv.cbody;

import stellarium.mech.OpFilter;

public interface ICBodyRenderer {

	/**Renders the Celestial Body by certain Wavelength*/
	public void render(CBody obj, double radVsRes, double brightness, OpFilter filter);
	
}
