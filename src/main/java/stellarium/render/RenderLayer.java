package stellarium.render;

import java.util.List;

import net.minecraft.client.renderer.Tessellator;
import stellarium.catalog.IStellarCatalog;
import stellarium.catalog.IStellarCatalogProvider;
import stellarium.mech.OpFilter.WaveFilter;
import stellarium.objs.IStellarObj;
import stellarium.util.math.SpCoord;
import stellarium.view.ViewPoint;
import stellarium.render.CRenderEngine;

public class RenderLayer {
	
	private static final int TEX_SIZE = 512;
	private final IStellarCatalog catalog;
	private final IStellarCatalogProvider provider;
	
	private long prevTick;
	private boolean rendered = false;
	
	private List<IStellarObj> currentObjs;

	public RenderLayer(IStellarCatalog catalog) {
		this.catalog = catalog;
		this.provider = catalog.getProvider();
		
		this.prevTick = -(catalog.getRUpTick() + 1);
	}
	
	public boolean shouldUpdateRender(long time) {
		boolean result = (this.prevTick > time || time >= this.prevTick + catalog.getRUpTick());
		this.prevTick = time;
		return result;
	}
	
	public boolean canRender(double magLimit) {
		return provider.getMag() < magLimit;
	}
	
	public void preRender(ViewPoint vp, SpCoord dir, double hfov) {
		// TODO Auto-generated method stub
		currentObjs = catalog.getList(vp, dir, hfov);
		this.rendered = false;
		
		for(IStellarObj obj : currentObjs)
			StellarRenderingRegistry.getRenderer(obj.getRenderId()).preRender(obj);
	}

	public void renderForWave(CRenderEngine re, long time, WaveFilter wfilter) {
		// TODO Auto-generated method stub
		for(IStellarObj obj : currentObjs);
	//		StellarRenderingRegistry.getRenderer(obj.getRenderId()).setWaveRender(obj, radVsRes, brightness, filter);
		
		this.rendered = true;
	}
	
	public void render(CRenderEngine re, long time) {
		// TODO Auto-generated method stub
		
	}
	
	public void loadRendered(CRenderEngine re) {
		if(this.rendered)
			re.loadRendered(provider.getCatalogName());
	}
}
