package stellarium.detector;

import java.util.Iterator;
import java.util.List;

import net.minecraft.client.Minecraft;

import com.google.common.collect.Lists;

import cpw.mods.fml.relauncher.Side;
import stellarium.StellarManager;
import stellarium.catalog.CCatalogManager;
import stellarium.catalog.IStellarCatalog;
import stellarium.mech.OpFilter;
import stellarium.render.RenderLayer;
import stellarium.sky.ISkySet;
import stellarium.util.math.Spmath;
import stellarium.view.EnumEyeCCD;
import stellarium.view.IScope;
import stellarium.view.IViewer;
import stellarium.view.ViewPoint;
import stellarium.render.CRenderEngine;

public class DetectorEye implements IDetector {
	
	private static double DEF_EYELIMIT = 6.0;
	private List<RenderLayer> layers = Lists.newArrayList();
	private int width, height;
	private double detectorDefFov;
	private double pixelScale;
	
	public void setup() {
		CCatalogManager manager = StellarManager.getManager(Side.CLIENT).getCatalogManager();
		Iterator<IStellarCatalog> ite = manager.getItetoRender();
		
		while(ite.hasNext()) {
			IStellarCatalog catalog = ite.next();
			layers.add(new RenderLayer(catalog));
		}
	}

	public void render(IViewer viewer, long time, float partialTicks, Minecraft mc) {
		CRenderEngine re = CRenderEngine.instance;
		IScope scope = viewer.getScope();
		ISkySet skyset = viewer.getViewPoint().getSkySet();
		double eyeLimit = DEF_EYELIMIT + Spmath.lumRatioToMag(scope.getLGP());
		double fov = this.detectorDefFov / scope.getMP();
		
		re.convertFrom(viewer.getViewCoord());
		
		for(RenderLayer layer : layers) {
			if(layer.shouldUpdateRender(time, scope, viewer.getViewPos(), fov))
				layer.renderLayer(re, viewer, time, partialTicks, eyeLimit, fov, this.pixelScale);
			
			layer.loadRendered(re);
		}
	}
	
	@Override
	public EnumEyeCCD type() {
		return EnumEyeCCD.Eye;
	}

}
