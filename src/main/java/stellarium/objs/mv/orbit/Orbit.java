package stellarium.objs.mv.orbit;

import sciapi.api.value.IValRef;
import sciapi.api.value.euclidian.ECoord;
import sciapi.api.value.euclidian.EVector;
import stellarium.objs.mv.CMvEntry;
import stellarium.objs.mv.cbody.ICBodyType;
import stellarium.util.math.Spmath;
import stellarium.util.math.VecMath;

public abstract class Orbit {

	protected CMvEntry entry;
	
	protected EVector position;
	protected EVector velocity;
	
	public Orbit(CMvEntry e)
	{
		entry = e;
	}
	
	public CMvEntry getEntry()
	{
		return entry;
	}
	
	public abstract void update(double year);
	
	public IValRef<EVector> getPosition(double secondPassed)
	{
		return VecMath.add(this.position, VecMath.mult(secondPassed, this.velocity));
	}
	
	public EVector getCurrentVelocity()
	{
		return this.velocity;
	}
	
	/**
	 * Get the coordinate of this orbit in the year.
	 * 
	 * <li>x axis is pointing Prime Meridian.
	 * <li>y axis is pointing east, which is perpendicular to the x, y axis.
	 * <li>z axis is pointing North Pole.
	 * */
	abstract public ECoord getOrbCoord(double year);
	
	/**
	 * Get the average size(radius) of this orbit.
	 * */
	abstract public double getAvgSize();
	
	/**
	 * Get the maximum distance (on the apoapsis) of this orbit.
	 * */
	abstract public double getMaxDist();

	
	public double getAvgPeriod() {
		if(entry.hasParent()) {
			double size = this.getAvgSize();
			return Math.sqrt(size * size * size / entry.getParent().getMass());
		} else return 0.0;
	}
	
	abstract public IOrbitType getOrbitType();
}
