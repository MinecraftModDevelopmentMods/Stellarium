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
import stellarium.view.ViewPoint;
import stellarium.view.Viewer;
import stellarium.render.CRenderEngine;

public class DetectorEye implements IDetector {
	
	private static double DEF_EYELIMIT = 6.0;
	private static double DEF_FOV = 70.0;
	private List<RenderLayer> layers = Lists.newArrayList();
	
	public void setup() {
		CCatalogManager manager = StellarManager.getManager(Side.CLIENT).getCatalogManager();
		Iterator<IStellarCatalog> ite = manager.getItetoRender();
		
		while(ite.hasNext()) {
			IStellarCatalog catalog = ite.next();
			layers.add(new RenderLayer(catalog));
		}
	}

	public void render(Viewer viewer, long time, float partialTicks, Minecraft mc) {
		// TODO Auto-generated method stub
		CRenderEngine re = CRenderEngine.instance;
		OpFilter filter = viewer.getFilter();
		IScope scope = viewer.getScope();
		ISkySet skyset = viewer.getViewPoint().getSkySet();
		double eyeLimit = DEF_EYELIMIT + Spmath.lumRatioToMag(scope.getLGP());
		double fov = DEF_FOV / scope.getMP();
		
		for(RenderLayer layer : layers) {
			if(layer.shouldUpdateRender(time, scope, viewer.getPos(), fov)) {
				layer.preRender(re, viewer.getViewPoint(), viewer.getPos(), fov, scope.isFOVCoverSky());
				
				for(OpFilter.WaveFilter wfilter : filter.getFilterList()) {
					double magLimit = Math.min(eyeLimit, skyset.getBgLight(wfilter.wl, viewer.getPos()));
					
					if(!layer.canRender(magLimit))
						continue;
					
					layer.renderForWave(re, viewer, time, partialTicks, wfilter);
				}
				
				layer.render(re);
			}
			
			layer.loadRendered(re);
		}
	}
	
	@Override
	public EnumEyeCCD type() {
		return EnumEyeCCD.Eye;
	}

}
