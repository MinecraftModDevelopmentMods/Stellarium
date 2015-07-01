package stellarium.objs.mv.cbody;

import stellarium.config.IConfigCategory;
import stellarium.config.IStellarConfig;
import stellarium.objs.mv.CMvEntry;
import stellarium.objs.mv.orbit.Orbit;
import stellarium.render.ISObjRenderer;
import stellarium.world.CWorldProviderPart;

public interface ICBodyType {
	
	/**gives name of this type*/
	public String getTypeName();
	
	/**initialization for this type*/
	public void init();
	
	
	/**forms configuration for this type
	 * @param cfg the configuration category to format
	 * @param isMain check value which is <code>true</code> iff. this category for main star.*/
	public void formatConfig(IConfigCategory cfg, boolean isMain);
	
	/**remove properties from this type*/
	public void removeConfig(IConfigCategory cat);
	
	/**provides CBody from the entry*/
	public CBody provideCBody(CMvEntry e);
	
	/**applies the configuration to the body*/
	public void apply(CBody body, IConfigCategory cfg);
	
	/**saves the body as configuration*/
	public void save(CBody body, IConfigCategory cfg);
	
	/**checks the current settings and forms the celestial body*/
	public void formCBody(CBody body, IStellarConfig cfg);
	
	
	/**Copies the CBody.*/
	public void setCopy(CBody ref, CBody target);
	
	/**
	 * Process tasks needed for remove.
	 * Warning: this can also be done on logical side
	 * */
	public void onRemove(CBody body);
	
	/**gives CBody Renderer for this type*/
	public ICBodyRenderer getCBodyRenderer();
	
	/**gives WorldProvider for this type of CBody*/
	public CWorldProviderPart getCWorldProvider();
	
}
