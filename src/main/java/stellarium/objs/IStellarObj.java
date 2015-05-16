package stellarium.objs;

import sciapi.api.value.euclidian.EVector;
import stellarium.catalog.EnumCatalogType;
import stellarium.mech.Wavelength;
import stellarium.util.math.SpCoord;
import stellarium.view.ViewPoint;

public interface IStellarObj {
	
	/**gives the name of this object*/
	public String getName();
	
	/**gives the relative position of this object. (As Ecliptic Coordinate)*/
	public EVector getPos(ViewPoint vp, double partime);

	/**
	 * gives the radius for certain wavelength.
	 * @param wl the wavelength in which being observed.
	 * */
	public double getRadius(Wavelength wl);
	
	/**
	 * gives the apparent magnitude for certain wavelength.
	 * @param wl the wavelength in which being observed.
	 * */
	public double getMag(Wavelength wl);
	
	/**gives the render id for this object*/
	public int getRenderId();
	
	/**gives the description for this object*/
	public String getDescription();
	
	/**gives the type of this object*/
	public EnumSObjType getType();
}
