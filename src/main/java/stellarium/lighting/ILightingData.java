package stellarium.lighting;

import sciapi.api.value.euclidian.EVector;
import stellarium.view.ViewPoint;

public interface ILightingData {
	
	/**
	 * gives light flux from specific lighting effect on specific viewpoint
	 * @param vp the viewpoint
	 * */
	public double getFlux(ViewPoint vp);
	
	/**
	 * gives light intensity from specific origin point on specific viewpoint.
	 * regards global coordinate system.
	 * @param vp the viewpoint
	 * @param origin the origin point relative to the lighting object
	 * */
	public double getIntensity(ViewPoint vp, EVector origin);
	
}
