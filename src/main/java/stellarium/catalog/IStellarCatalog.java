package stellarium.catalog;

import java.util.List;

import sciapi.api.value.IValRef;
import sciapi.api.value.euclidian.EVector;
import stellarium.config.IConfigurableData;
import stellarium.config.IStellarConfig;
import stellarium.mech.OpFilter;
import stellarium.objs.IStellarObj;
import stellarium.render.CRenderEngine;
import stellarium.util.math.SpCoord;
import stellarium.view.ViewPoint;

public interface IStellarCatalog extends IConfigurableData {
	
	/**gives the provider for this catalog.*/
	public IStellarCatalogProvider getProvider();
	
	
	/**return true to disable this catalog. called while starting world.*/
	public boolean isDisabled();
	
	/**
	 * gives render update tick duration for this catalog.
	 * */
	public int getRUpTick();
	
	/**
	 * called by TickHandler to update this catalog.
	 * */
	public void update(long worldTime);
	
	/**
	 * gives the list of Stellar Objects in the catalog within certain (circular) range.
	 * 
	 * @param vp ViewPoint viewing the objects. Can be null for client
	 * @param dir the center of the range
	 * @param hfov the radius of the range
	 * @return the list of Stellar Objects within the range
	 * */
	public <T extends IStellarObj> List<T> getList(ViewPoint vp, SpCoord dir, double hfov);

	/**
	 * called before rendering to preset filter, refresh lighting, etc.
	 * 
	 * @param re the rendering engine
	 * @param vp the viewpoint viewing the objects
	 * @param filter filter for rendering
	 * @param partime partial tick when renders the catalog. 
	 * */
	public void onPreRender(CRenderEngine re, ViewPoint vp, OpFilter filter, float partime);

}
