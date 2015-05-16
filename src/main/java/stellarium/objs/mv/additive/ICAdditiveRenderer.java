package stellarium.objs.mv.additive;

import stellarium.mech.OpFilter;
import stellarium.mech.OpFilter.WaveFilter;
import stellarium.objs.IStellarObj;
import stellarium.render.CRenderEngine;

public interface ICAdditiveRenderer {
	
	/**Called directly before rendering to initialize this renderer*/
	public void preRender(CAdditive obj);
	
	/**render the Celestial Additive by certain Wavelength*/
	public void setWaveRender(CAdditive add, double radVsRes, double brightness, WaveFilter filter);

	/**render object*/
	public void render(CRenderEngine re, CAdditive add);

	
}
