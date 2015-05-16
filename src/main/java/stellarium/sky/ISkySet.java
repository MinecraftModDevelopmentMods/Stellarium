package stellarium.sky;

import stellarium.mech.Wavelength;
import stellarium.util.math.SpCoord;

public interface ISkySet {
	
	/**Seeing of this sky settings for specific wavelength*/
	public double getSeeing(Wavelength wl);

	/**Extinction magnitude of this sky settings for specific wavelength & specific position*/
	public double getExtinction(Wavelength wl, SpCoord pos);
	
	/**Magnitude of background light (pollution) of this sky settings
	 * for specific wavelength & specific position*/
	public double getBgLight(Wavelength wl, SpCoord pos);
}
