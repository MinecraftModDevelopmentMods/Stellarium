package stellarium.objs.mv.cbody;

import stellarium.config.IConfigCategory;
import stellarium.config.IStellarConfig;
import stellarium.objs.mv.CMvEntry;
import stellarium.objs.mv.orbit.Orbit;
import stellarium.render.ISObjRenderer;
import stellarium.world.CWorldProvider;

public interface ICBodyType {
	
	/**gives name of this type*/
	public String getTypeName();
	
	/**forms configuration for this type*/
	public void formatConfig(IConfigCategory cfg);
	
	/**remove properties from this type*/
	public void removeConfig(IConfigCategory cat);
	
	/**provides CBody from the entry*/
	public CBody provideCBody(CMvEntry e);
	
	/**populates the body with configuration*/
	public void populate(CBody body, IConfigCategory cfg);
	
	/**saves the body as configuration*/
	public void save(CBody body, IConfigCategory cfg);
	
	/**do tasks needed for remove*/
	public void onRemove(CBody body);
	
	/**gives CBody Renderer for this type*/
	public ICBodyRenderer getCBodyRenderer();
	
	/**gives WorldProvider for this type of CBody*/
	public CWorldProvider getCWorldProvider();
	
}
