package stellarium.objs.mv.cbody;

import stellarium.mech.OpFilter;

public interface ICBodyRenderer {
	
	/**Called directly before rendering to initialize this renderer*/
	public void preRender(CBody obj);
	
	/**render the Celestial Body by certain Wavelength*/
	public void setWaveRender(CBody obj, double radVsRes, double brightness, OpFilter.WaveFilter filter);
	
	/**render object regardless of wavelength, for RGB mode*/
	public void render(CBody obj, double radVsRes, double brightness);
	
}
