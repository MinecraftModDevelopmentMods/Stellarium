package stellarium.objs.mv.orbit;

import stellarium.config.IStellarConfig;
import stellarium.objs.mv.CMvEntry;

public interface IOrbitType {
	
	/**gives name of this type*/
	public String getTypeName();
	
	/**forms configuration for this type*/
	public void formatConfig(IStellarConfig cfg);
	
	/**provides Orbit from the entry*/
	public Orbit provideOrbit(CMvEntry e);
	
	/**populates the body with configuration*/
	public void populate(Orbit orbit, IStellarConfig cfg);
	
	/**sets the target orbit to be scaled orbit of reference orbit*/
	public void setScaled(Orbit ref, Orbit target, double scale);
	
	/**@return true iff.the orbit with this type has parent orbit.*/
	public boolean hasParent();
	
	/**do tasks needed for remove*/
	public void onRemove(Orbit orbit);

}
