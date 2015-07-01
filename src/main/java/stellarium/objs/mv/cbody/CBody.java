package stellarium.objs.mv.cbody;

import net.minecraftforge.common.config.Configuration;
import sciapi.api.value.IValRef;
import sciapi.api.value.euclidian.CrossUtil;
import sciapi.api.value.euclidian.ECoord;
import sciapi.api.value.euclidian.EVector;
import sciapi.api.value.euclidian.IEVector;
import sciapi.api.value.util.BOp;
import sciapi.api.value.util.VOp;
import stellarium.catalog.EnumCatalogType;
import stellarium.mech.Wavelength;
import stellarium.objs.EnumSObjType;
import stellarium.objs.IStellarObj;
import stellarium.objs.mv.CMvEntry;
import stellarium.stellars.orbit.OrbitMv;
import stellarium.stellars.orbit.OrbitSt;
import stellarium.util.math.AxisRotate;
import stellarium.util.math.SpCoord;
import stellarium.util.math.SpCoordf;
import stellarium.util.math.VecMath;
import stellarium.view.ViewPoint;

public abstract class CBody implements IStellarObj {

	protected CMvEntry entry;
	
	protected double w_prec, w_rot;
	protected ECoord initialCoord;
	protected boolean isTidalLocked;
	
	public CBody(CMvEntry e)
	{
		this.entry = e;
	}
	
	public CMvEntry getEntry()
	{
		return this.entry;
	}
	
	@Override
	public String getName() {
		return entry.getName();
	}

	@Override
	public EVector getPos(ViewPoint vp, double partime) {
		EVector ret = new EVector(3);
		ret.set(BOp.sub(entry.orbit().getPosition(partime), vp.EcRPos));
		return ret;
	}
	
	public abstract void update(double day);
	
	/**
	 * Get the coordinate of this celestial body on the day.
	 * 
	 * <li>x axis is pointing Prime Meridian.
	 * <li>y axis is pointing east, which is perpendicular to the x, y axis.
	 * <li>z axis is pointing North Pole.
	 * */
	public ECoord getCoord(double day)
	{
		double tyr = day / entry.getMain().yr;
		ECoord orbCoord = entry.orbit().getOrbCoord(tyr);

		if(this.isTidalLocked) {
			return getRotated(orbCoord, day);
		} else {
			ECoord precCoord = VecMath.rotateCoord(this.initialCoord, orbCoord.getCoord(2), tyr * this.w_prec);
			return getRotated(precCoord, day);
		}
	}
	
	private ECoord getRotated(ECoord coord, double day) {
		return VecMath.rotateCoordZ(coord, day * this.w_rot);
	}

	@Override
	public double getRadius(Wavelength wl) {
		return getRadius();
	}

	@Override
	public int getRenderId() {
		return entry.getMain().renderId;
	}

	abstract public double getRadius();
	
	abstract public ICBodyType getCBodyType();

}
