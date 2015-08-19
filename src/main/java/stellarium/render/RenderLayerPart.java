package stellarium.render;

import java.util.List;
import java.util.concurrent.Callable;

import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.opengl.GL11;

import sciapi.api.value.euclidian.ECoord;
import sciapi.api.value.euclidian.EVector;
import sciapi.api.value.numerics.DDouble;
import sciapi.api.value.numerics.IReal;
import stellarium.catalog.IStellarCatalog;
import stellarium.catalog.IStellarCatalogProvider;
import stellarium.mech.OpFilter;
import stellarium.mech.OpFilter.WaveFilter;
import stellarium.mech.Wavelength;
import stellarium.objs.IStellarObj;
import stellarium.sky.ISkySet;
import stellarium.util.math.SpCoord;
import stellarium.util.math.Spmath;
import stellarium.util.math.VecMath;
import stellarium.view.IScope;
import stellarium.view.IViewer;
import stellarium.view.ViewPoint;

/**
 * Partial render layer class for sky covering scope.
 * */
public class RenderLayerPart {
	private final IStellarCatalog catalog;
	private final IStellarCatalogProvider provider;
	
	private final int index;
	private SpCoord center;
	private double range;
	
	private int pxRadius;
	
	private boolean rendered = false;
	
	private List<IStellarObj> currentObjs;

	public RenderLayerPart(IStellarCatalog catalog, int index) {
		this.catalog = catalog;
		this.provider = catalog.getProvider();
		this.index = index;
	}
	
	public void updateView(SpCoord center, double range, double px) {
		if(center != null)
			this.center = center;
		this.range = range;
		this.pxRadius = (int)(px * range);
	}
	
	@Override
	public String toString() {
		return provider.getCatalogName() + "_" + this.index;
	}
	
	public void preRender(CRenderEngine re, ViewPoint vp) {
		this.currentObjs = catalog.getList(vp, this.center, this.range);
		this.rendered = false;
	}

	public void renderForWave(final CRenderEngine re, IViewer viewer, double resolution, double partialTicks, final WaveFilter wfilter) {
		
		IRenderable renderable = new IRenderable() {
			@Override
			public Wavelength getWavelength() {
				return wfilter.wl;
			}

			@Override
			public void render(IStellarObj obj, double size, double mag) {
				StellarRenderingRegistry.getRenderer(obj.getRenderId()).renderForWave(re, obj, size, mag, wfilter);
			}
		};
		
		this.render(re, viewer, resolution, partialTicks, renderable);
	}
	
	public void renderForRGB(final CRenderEngine re, IViewer viewer, double resolution, float partialTicks, final OpFilter filter) {
		IRenderable renderable = new IRenderable() {
			@Override
			public Wavelength getWavelength() {
				return Wavelength.visible;
			}

			@Override
			public void render(IStellarObj obj, double size, double mag) {
				StellarRenderingRegistry.getRenderer(obj.getRenderId()).renderRGB(re, obj, size, mag, filter);
			}
		};
		
		this.render(re, viewer, resolution, partialTicks, renderable);
	}
	
	public void render(CRenderEngine re, IViewer viewer, double resolution, double partialTicks, IRenderable renderable) {
		ISkySet skyset = viewer.getViewPoint().getSkySet();
		IScope scope = viewer.getScope();
		Wavelength wl = renderable.getWavelength();
		double mag, bglight;
		double size;
		double invres = 1.0 / resolution;
		SpCoord coord = new SpCoord();
		EVector pos;
		IReal dist = new DDouble();
		
		re.preSaveRendered(this.toString(), 2 * this.pxRadius, 2 * this.pxRadius);
		re.reverseSpCoord(this.center);
		
		for(IStellarObj obj : this.currentObjs) {
			pos = obj.getPos(viewer.getViewPoint(), partialTicks);
			
			dist.set(VecMath.size(pos));
			coord.setWithVec(VecMath.div(dist, pos));
			
			mag = obj.getMag(viewer.getViewPoint(), partialTicks, wl) + skyset.getExtinction(wl, coord);
			bglight = skyset.getBgLight(wl, coord);
			
			size = obj.getRadius(wl) / (resolution * dist.asDouble());
			
			if(mag < bglight) {
				mag = Spmath.subMag(mag, bglight);
				
				GL11.glPushMatrix();
				re.pointSpCoord(coord);
				GL11.glTranslated(invres, 0.0, 0.0);
				renderable.render(obj, size, mag);
				GL11.glPopMatrix();
			}
		}
		
		re.postSaveRendered();
		this.rendered = true;
	}
	
	public void loadRendered(CRenderEngine re) {
		if(this.rendered)
		{
			re.loadRendered(this.toString());
			re.pointSpCoord(this.center);
			re.renderTexturedRect(-this.range, -this.range, this.range, this.range, 1.0);
		}
	}
	
	private interface IRenderable {
		public Wavelength getWavelength();
		public void render(IStellarObj obj, double size, double mag);
	}
}
