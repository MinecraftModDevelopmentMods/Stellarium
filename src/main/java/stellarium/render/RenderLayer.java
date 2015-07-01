package stellarium.render;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.client.renderer.Tessellator;
import sciapi.api.value.IValRef;
import sciapi.api.value.euclidian.EVector;
import sciapi.api.value.numerics.DDouble;
import sciapi.api.value.numerics.IReal;
import stellarium.catalog.IStellarCatalog;
import stellarium.catalog.IStellarCatalogProvider;
import stellarium.mech.OpFilter;
import stellarium.mech.Wavelength;
import stellarium.mech.OpFilter.WaveFilter;
import stellarium.objs.IStellarObj;
import stellarium.util.math.SpCoord;
import stellarium.util.math.Spmath;
import stellarium.util.math.VecMath;
import stellarium.view.IScope;
import stellarium.view.IViewer;
import stellarium.view.ViewPoint;
import stellarium.render.CRenderEngine;
import stellarium.sky.ISkySet;

public class RenderLayer {
	
	private final IStellarCatalog catalog;
	private final IStellarCatalogProvider provider;
	
	private int width, height;
	
	private long prevTick;
	private IScope prevScope;
	private SpCoord prevDir;
	private boolean isCoverSky;
	
	private final SingleLayerPartition basePartition;
	private final SpLayerPartition spPartition;
		
	private List<IStellarObj> currentObjs;

	public RenderLayer(IStellarCatalog catalog) {
		this.catalog = catalog;
		this.provider = catalog.getProvider();
		
		this.prevTick = -(catalog.getRUpTick() + 1);
		
		this.basePartition = new SingleLayerPartition(catalog);
		this.spPartition = new SpLayerPartition(catalog);
	}
	
	public ILayerPartition getCurrentPartition() {
		if(this.isCoverSky)
			return spPartition;
		return basePartition;
	}

	public boolean shouldUpdateRender(long time, IScope scope, SpCoord dir, double fov) {
		boolean timeUpdate = this.prevTick > time || time >= this.prevTick + catalog.getRUpTick();
		boolean sameScope = this.prevScope == scope;
		boolean notRendered = !scope.isFOVCoverSky() && prevDir != null && Spmath.distArc(prevDir, dir) > fov/2;
		
		if(timeUpdate || sameScope || notRendered) {
			this.prevTick = time;
			this.prevScope = scope;
			this.prevDir = dir;
			this.isCoverSky = scope.isFOVCoverSky();
						
			return true;
		} else return false;
	}
	
	public boolean canRender(double magLimit) {
		return provider.getMag() < magLimit;
	}
	
	public void renderLayer(CRenderEngine re, IViewer viewer, long time, float partialTicks, double eyeLimit, double fov, double pxScale) {
		OpFilter filter = viewer.getFilter();
		ViewPoint vp = viewer.getViewPoint();
		ISkySet skyset = vp.getSkySet();
		
		this.getCurrentPartition().updateView(viewer.getViewPos(), fov, pxScale);
		
		for(RenderLayerPart layer : this.getCurrentPartition().getLayers())
			layer.preRender(re, vp);
		
		if(!filter.isRGB()) {
			for(OpFilter.WaveFilter wfilter : filter.getFilterList()) {
				double magLimit = Math.min(eyeLimit, skyset.getBgLight(wfilter.wl, viewer.getViewPos()));
			
				if(!this.canRender(magLimit))
					continue;
			
				this.renderForWave(re, viewer, time, partialTicks, wfilter);
			}
		} else {
			this.renderForRGB(re, viewer, time, partialTicks, filter);
		}
	}

	private void renderForWave(CRenderEngine re, IViewer viewer, long time, double partialTicks, WaveFilter wfilter) {
		ISkySet skyset = viewer.getViewPoint().getSkySet();
		IScope scope = viewer.getScope();
		double resolution = Math.min(scope.getResolution(wfilter.wl), skyset.getSeeing(wfilter.wl));
			
		for(RenderLayerPart layer : this.getCurrentPartition().getLayers())
			layer.renderForWave(re, viewer, resolution, time, partialTicks, wfilter);
	}
	
	private void renderForRGB(CRenderEngine re, IViewer viewer, long time, float partialTicks, OpFilter filter) {
		ISkySet skyset = viewer.getViewPoint().getSkySet();
		IScope scope = viewer.getScope();
		double resolution = Math.min(scope.getResolution(Wavelength.visible), skyset.getSeeing(Wavelength.visible));
		
		for(RenderLayerPart layer : this.getCurrentPartition().getLayers())
			layer.renderForRGB(re, viewer, resolution, time, partialTicks, filter);
	}
	
	public void loadRendered(CRenderEngine re) {
		for(RenderLayerPart layer : this.getCurrentPartition().getLayers())
			layer.loadRendered(re);
	}
}
