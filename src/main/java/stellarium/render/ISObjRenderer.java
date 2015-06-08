package stellarium.render;

import stellarium.mech.OpFilter;
import stellarium.objs.IStellarObj;
import stellarium.objs.mv.cbody.CBody;

public interface ISObjRenderer<SObj extends IStellarObj> {
	
	/**
	 * render the stellar object by certain wavelength.<p>
	 * When this stellar object is rendered,
	 * <ul>
	 * <li>x axis is perpendicular to celestial sphere.
	 * <li>y axis is on parallel of latitude.
	 * <li>z axis is pointing north.
	 * </ul>
	 * @param re Celestial Rendering Engine
	 * @param obj the Celestial Object to render
	 * @param radVsRes radius of the object divided by resolution
	 * @param magnitude the magnitude of this object
	 * @param filter the wave filter
	 * */
	public void renderForWave(CRenderEngine re, SObj obj, double radVsRes, double magnitude, OpFilter.WaveFilter filter);
	
	/**
	 * render the stellar object for RGB filter.<p>
	 * When this stellar object is rendered,
	 * <ul>
	 * <li>x axis is perpendicular to celestial sphere.
	 * <li>y axis is on parallel of latitude.
	 * <li>z axis is pointing north.
	 * </ul>
	 * @param re Celestial Rendering Engine
	 * @param obj the Celestial Object to render
	 * @param radVsRes radius of the object divided by resolution
	 * @param magnitude the magnitude of this object
	 * @param filter the RGB filter
	 * */
	public void renderRGB(CRenderEngine re, SObj obj, double radVsRes, double magnitude, OpFilter filter);
	
}
