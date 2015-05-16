package stellarium.render;

import java.util.List;

import net.minecraft.client.renderer.Tessellator;
import sciapi.api.value.IValRef;
import sciapi.api.value.euclidian.EVector;
import sciapi.api.value.numerics.DDouble;
import sciapi.api.value.numerics.IReal;
import stellarium.catalog.IStellarCatalog;
import stellarium.catalog.IStellarCatalogProvider;
import stellarium.mech.OpFilter.WaveFilter;
import stellarium.objs.IStellarObj;
import stellarium.util.math.SpCoord;
import stellarium.util.math.Spmath;
import stellarium.util.math.VecMath;
import stellarium.view.IScope;
import stellarium.view.ViewPoint;
import stellarium.view.Viewer;
import stellarium.render.CRenderEngine;
import stellarium.sky.ISkySet;

public class RenderLayer {
	
	private final IStellarCatalog catalog;
	private final IStellarCatalogProvider provider;
	
	private int width, height;
	
	private long prevTick;
	private IScope prevScope;
	private SpCoord prevDir;
	
	private boolean rendered = false;
	
	private List<IStellarObj> currentObjs;

	public RenderLayer(IStellarCatalog catalog) {
		this.catalog = catalog;
		this.provider = catalog.getProvider();
		
		this.prevTick = -(catalog.getRUpTick() + 1);
	}
	
	public void updateWidthHeight(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public boolean shouldUpdateRender(long time, IScope scope, SpCoord dir, double fov) {
		boolean timeUpdate = this.prevTick > time || time >= this.prevTick + catalog.getRUpTick();
		boolean sameScope = this.prevScope == scope;
		boolean notRendered = !scope.isFOVCoverSky() && prevDir != null && Spmath.distArc(prevDir, dir) > fov/2;
		
		if(timeUpdate || sameScope || notRendered) {
			this.prevTick = time;
			this.prevScope = scope;
			this.prevDir = dir;
			
			return true;
		} else return false;
	}
	
	public boolean canRender(double magLimit) {
		return provider.getMag() < magLimit;
	}
	
	public void preRender(CRenderEngine re, ViewPoint vp, SpCoord dir, double fov, boolean coverSky) {

		if(coverSky)
			currentObjs = catalog.getList(vp, dir, 90.0);
		else currentObjs = catalog.getList(vp, dir, fov);
		
		this.rendered = false;
		
		re.preSaveRendered(provider.getCatalogName(), 2 * width, 2 * height);

		for(IStellarObj obj : currentObjs)
			StellarRenderingRegistry.getRenderer(obj.getRenderId()).preRender(obj);
	}

	public void renderForWave(CRenderEngine re, Viewer viewer, long time, double partialTicks, WaveFilter wfilter) {
		ISkySet skyset = viewer.getViewPoint().getSkySet();
		IScope scope = viewer.getScope();
		double resolution = Math.min(scope.getResolution(wfilter.wl), skyset.getSeeing(wfilter.wl));
		double mag, bglight;
		double size;
		SpCoord coord = new SpCoord();
		EVector pos;
		IReal dist = new DDouble();

		for(IStellarObj obj : currentObjs) {
			pos = obj.getPos(viewer.getViewPoint(), partialTicks);
			
			dist.set(VecMath.size(pos));
			coord.setWithVec(VecMath.div(dist, pos));
			
			mag = obj.getMag(wfilter.wl) + skyset.getExtinction(wfilter.wl, coord);
			bglight = skyset.getBgLight(wfilter.wl, coord);
			
			size = obj.getRadius(wfilter.wl) / (resolution * dist.asDouble());
			
			if(mag < bglight) {
				mag = Spmath.subMag(mag, bglight);
				
				StellarRenderingRegistry.getRenderer(obj.getRenderId()).setWaveRender(obj, size, mag, wfilter);
			}
		}
		
		this.rendered = true;
	}
	
	public void render(CRenderEngine re) {
		for(IStellarObj obj : currentObjs) {
			StellarRenderingRegistry.getRenderer(obj.getRenderId()).render(re, obj);
		}
		re.postSaveRendered();
	}
	
	public void loadRendered(CRenderEngine re) {
		if(this.rendered)
			re.loadRendered(provider.getCatalogName());
	}
}
