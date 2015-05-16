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
	public void preRender(IStellarObj obj) {
		if(obj instanceof CBody)
		{
			CBody ob = (CBody) obj;
			ob.getCBodyType().getCBodyRenderer().preRender(ob);
			
			if(ob.getEntry().hasAdditive())
			{
				CAdditive add = ob.getEntry().additive();
				add.getAdditiveType().getCAdditiveRenderer().preRender(add);
			}
		}
	}

	@Override
	public void setWaveRender(IStellarObj obj, double radVsRes,
			double magnitude, WaveFilter filter) {
		if(obj instanceof CBody)
		{
			CBody ob = (CBody) obj;
			ob.getCBodyType().getCBodyRenderer().setWaveRender(ob, radVsRes, magnitude, filter);
			
			if(ob.getEntry().hasAdditive())
			{
				CAdditive add = ob.getEntry().additive();
				add.getAdditiveType().getCAdditiveRenderer().setWaveRender(add, radVsRes, magnitude, filter);
			}
		}
	}

	@Override
	public void render(CRenderEngine re, IStellarObj obj) {
		if(obj instanceof CBody)
		{
			CBody ob = (CBody) obj;
			ob.getCBodyType().getCBodyRenderer().render(re, ob);
			
			if(ob.getEntry().hasAdditive())
			{
				CAdditive add = ob.getEntry().additive();
				add.getAdditiveType().getCAdditiveRenderer().render(re, add);
			}
		}
	}

}
