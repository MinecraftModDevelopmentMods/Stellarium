package stellarium.render;

import stellarium.mech.OpFilter;
import stellarium.objs.IStellarObj;
import stellarium.objs.mv.cbody.CBody;

public interface ISObjRenderer {
	
	/**Called directly before rendering to initialize this renderer*/
	public void preRender(IStellarObj obj);
	
	/**Set object to render the Stellar Object by certain Wavelength*/
	public void setWaveRender(IStellarObj obj, double radVsRes, double magnitude, OpFilter.WaveFilter filter);
	
	/**render the object*/
	public void render(CRenderEngine re, IStellarObj obj);
	
}
