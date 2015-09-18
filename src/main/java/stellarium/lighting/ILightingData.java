package stellarium.lighting;

import sciapi.api.value.euclidian.EVector;
import stellarium.view.ViewPoint;

public interface ILightingData {
	
	/**
	 * gives light flux from specific lighting effect.
	 * @param vp the viewpoint
	 * */
	public double getFlux();
	
	/**
	 * gives light intensity from specific origin point.
	 * regards global coordinate system.
	 * @param vp the viewpoint
	 * @param origin the origin point relative to the lighting object
	 * */
	public double getIntensity(EVector origin);
	
}
