package stellarium.objs.mv.cbody;

import java.util.List;
import java.util.Set;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Sets;

import net.minecraftforge.common.config.Configuration;
import sciapi.api.value.IValRef;
import sciapi.api.value.euclidian.CrossUtil;
import sciapi.api.value.euclidian.ECoord;
import sciapi.api.value.euclidian.EVector;
import sciapi.api.value.euclidian.IEVector;
import sciapi.api.value.util.BOp;
import sciapi.api.value.util.VOp;
import stellarium.catalog.EnumCatalogType;
import stellarium.lighting.ILightObject;
import stellarium.lighting.ILightingData;
import stellarium.mech.Wavelength;
import stellarium.objs.EnumSObjType;
import stellarium.objs.IStellarObj;
import stellarium.objs.mv.CMvEntry;
import stellarium.util.math.AxisRotate;
import stellarium.util.math.SpCoord;
import stellarium.util.math.SpCoordf;
import stellarium.util.math.Spmath;
import stellarium.util.math.VecMath;
import stellarium.view.ViewPoint;
import stellarium.world.IWorldHandler;

public abstract class CBody implements IStellarObj, ILightObject {

	protected CMvEntry entry;
	
	protected double radius;
	protected double w_prec, w_rot;
	protected ECoord initialCoord;
	protected boolean isTidalLocked;
	
	protected Multimap<Wavelength, ILightingData> lightData = ArrayListMultimap.create();
	
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
	public EVector getPosition(float partime) {
		EVector ret = new EVector(3);
		ret.set(entry.orbit().getPosition(partime));
		return ret;
	}

	@Override
	public EVector getPos(ViewPoint vp, double partime) {
		EVector ret = new EVector(3);
		ret.set(BOp.sub(entry.orbit().getPosition(partime), vp.EcRPos));
		return ret;
	}
	
	@Override
	public double getMag(ViewPoint vp, double partime, Wavelength wl) {
		double flux = 0.0;

		for(ILightingData data : lightData.get(wl))
			flux += data.getFlux();
		
		return Spmath.LumToMag(flux);
	}
	
	@Override
	public void addLightingData(Wavelength wl, ILightingData data) {
		lightData.put(wl, data);
	}

	@Override
	public void refreshLightingData() {
		lightData.clear();
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
		return this.radius;
	}
	
	@Override
	public double getMaxRadius() {
		return this.radius;
	}


	@Override
	public int getRenderId() {
		return entry.getMain().renderId;
	}
	
	/** @return The type of this celestial body. */
	abstract public ICBodyType getCBodyType();

}
