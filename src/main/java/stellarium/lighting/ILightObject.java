package stellarium.lighting;

import sciapi.api.value.euclidian.EVector;
import stellarium.mech.Wavelength;

public interface ILightObject {
	
	/**
	 * gives global position on specific partial time
	 * @param partime partial tick
	 * */
	public EVector getPosition(float partime);
	
	/**
	 * gives the radius of the object
	 * */
	public double getRadius();
	
	/**
	 * adds lighting data to object
	 * @param data the lighting data to add
	 * */
	public void addLightingData(Wavelength wl, ILightingData data);
	
	/**refreshes lighting data of object */
	public void refreshLightingData();

}
