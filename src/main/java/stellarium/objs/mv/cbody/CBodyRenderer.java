package stellarium.objs.mv.cbody;

import stellarium.mech.OpFilter;
import stellarium.mech.OpFilter.WaveFilter;
import stellarium.mech.Wavelength;
import stellarium.objs.IStellarObj;
import stellarium.objs.mv.additive.CAdditive;
import stellarium.render.CRenderEngine;
import stellarium.render.ISObjRenderer;

public class CBodyRenderer implements ISObjRenderer {

	@Override
	public void renderForWave(CRenderEngine re, IStellarObj obj, double radVsRes, double magnitude, WaveFilter filter) {
		if(obj instanceof CBody)
		{
			CBody ob = (CBody) obj;
			ob.getCBodyType().getCBodyRenderer().renderForWave(re, ob, radVsRes, magnitude, filter);
			
			if(ob.getEntry().hasAdditive())
			{
				CAdditive add = ob.getEntry().additive();
				add.getAdditiveType().getCAdditiveRenderer().renderForWave(re, add, radVsRes, magnitude, filter);
			}
		}
	}

	@Override
	public void renderRGB(CRenderEngine re, IStellarObj obj, double radVsRes, double magnitude, OpFilter filter) {
		if(obj instanceof CBody)
		{
			CBody ob = (CBody) obj;
			ob.getCBodyType().getCBodyRenderer().renderRGB(re, ob, radVsRes, magnitude, filter);
			
			if(ob.getEntry().hasAdditive())
			{
				CAdditive add = ob.getEntry().additive();
				add.getAdditiveType().getCAdditiveRenderer().renderRGB(re, add, radVsRes, magnitude, filter);
			}
		}
	}

}
