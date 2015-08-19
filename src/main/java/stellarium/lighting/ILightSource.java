package stellarium.lighting;

import sciapi.api.value.euclidian.EVector;
import stellarium.mech.Wavelength;
import stellarium.view.ViewPoint;

public interface ILightSource extends ILightObject {
	
	/**
	 * calculates luminosity on specific wave band
	 * @param wl the wavelength
	 * */
	public double getLuminosity(Wavelength wl);

}
