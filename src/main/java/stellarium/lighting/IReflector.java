package stellarium.lighting;

import stellarium.mech.Wavelength;

public interface IReflector extends ILightObject {
	
	/**
	 * gives albedo of a refelctor.
	 * */
	public void getAlbedo(Wavelength wl);
	
}
