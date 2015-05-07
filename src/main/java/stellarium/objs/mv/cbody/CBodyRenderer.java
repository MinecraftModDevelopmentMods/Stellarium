package stellarium.objs.mv.cbody;

import stellarium.mech.OpFilter;
import stellarium.mech.OpFilter.WaveFilter;
import stellarium.mech.Wavelength;
import stellarium.objs.IStellarObj;
import stellarium.objs.mv.additive.CAdditive;
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
			double brightness, WaveFilter filter) {
		if(obj instanceof CBody)
		{
			CBody ob = (CBody) obj;
			ob.getCBodyType().getCBodyRenderer().setWaveRender(ob, radVsRes, brightness, filter);
			
			if(ob.getEntry().hasAdditive())
			{
				CAdditive add = ob.getEntry().additive();
				add.getAdditiveType().getCAdditiveRenderer().setWaveRender(add, radVsRes, brightness, filter);
			}
		}
	}

	@Override
	public void render(IStellarObj obj, double radVsRes, double brightness) {
		if(obj instanceof CBody)
		{
			CBody ob = (CBody) obj;
			ob.getCBodyType().getCBodyRenderer().render(ob, radVsRes, brightness);
			
			if(ob.getEntry().hasAdditive())
			{
				CAdditive add = ob.getEntry().additive();
				add.getAdditiveType().getCAdditiveRenderer().render(add, radVsRes, brightness);
			}
		}
	}

}
