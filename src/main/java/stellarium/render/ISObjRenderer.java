package stellarium.render;

import stellarium.mech.OpFilter;
import stellarium.objs.IStellarObj;
import stellarium.objs.mv.cbody.CBody;

public interface ISObjRenderer {
	
	/**Called directly before rendering to initialize this renderer*/
	public void preRender(IStellarObj obj);
	
	/**Set object to render the Stellar Object by certain Wavelength*/
	public void setWaveRender(IStellarObj obj, double radVsRes, double brightness, OpFilter.WaveFilter filter);
	
	/**render object regardless of wavelength (for RGB mode)*/
	public void render(IStellarObj obj, double radVsRes, double brightness);
	
}
