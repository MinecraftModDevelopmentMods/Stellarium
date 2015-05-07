package stellarium.objs.mv.additive;

import stellarium.mech.OpFilter;
import stellarium.mech.OpFilter.WaveFilter;
import stellarium.objs.IStellarObj;

public interface ICAdditiveRenderer {
	
	/**Called directly before rendering to initialize this renderer*/
	public void preRender(CAdditive obj);
	
	/**render the Celestial Additive by certain Wavelength*/
	public void setWaveRender(CAdditive add, double radVsRes, double brightness, WaveFilter filter);

	/**render object regardless of wavelength, for RGB mode*/
	public void render(CAdditive obj, double radVsRes, double brightness);

	
}
