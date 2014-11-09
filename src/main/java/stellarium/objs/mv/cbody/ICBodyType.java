package stellarium.objs.mv.cbody;

import stellarium.config.IStellarConfig;
import stellarium.objs.mv.CMvEntry;
import stellarium.render.ISObjRenderer;
import stellarium.world.CWorldProvider;

public interface ICBodyType {
	
	/**gives name of this type*/
	public String getTypeName();
	
	/**forms configuration*/
	public void formatConfig(IStellarConfig cfg);
	
	/**provides CBody from the entry*/
	public CBody provideCBody(CMvEntry e);
	
	/**populates the body with configuration*/
	public void populate(CBody body, IStellarConfig cfg);
	
	/**do tasks needed for remove*/
	public void onRemove(CBody body);
	
	/**gives CBody Renderer for this type*/
	public ICBodyRenderer getCBodyRenderer();
	
	/**gives WorldProvider for this type of CBody*/
	public CWorldProvider getCWorldProvider();
	
}
