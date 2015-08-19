package stellarium.objs.mv.cbody;

import stellarium.config.IConfigCategory;
import stellarium.config.IStellarConfig;
import stellarium.objs.mv.CMvEntry;
import stellarium.objs.mv.StellarMvLogical;
import stellarium.objs.mv.orbit.Orbit;
import stellarium.render.ISObjRenderer;
import stellarium.world.CWorldProviderPart;
import stellarium.world.IWorldHandler;

public interface ICBodyType {
	
	/**gives name of this type*/
	public String getTypeName();
	
	/**initialization for this type*/
	public void init();
	
	
	/**forms configuration for this type
	 * @param cfg the configuration category to format
	 * @param mv 
	 * @param isMain check value which is <code>true</code> iff. this category for main star.*/
	public void formatConfig(IConfigCategory cat, StellarMvLogical mv, boolean isMain);
	
	/**remove properties from this type*/
	public void removeConfig(IConfigCategory cat);
	
	/**provides CBody from the entry*/
	public CBody provideCBody(CMvEntry e);
	
	/**applies the configuration to the body*/
	public void apply(CBody body, IConfigCategory cat);
	
	/**saves the body as configuration*/
	public void save(CBody body, IConfigCategory cat);
	
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
	
	/**check for existence of world in this type*/
	public boolean hasWorld();
	
	/**
	 * gives World Handler for this type of CBody
	 * will return <code>null</code> if this type does not contain world.
	 * */
	public IWorldHandler provideWorldHandler();
	
}
