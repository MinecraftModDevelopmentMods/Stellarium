package stellarium.lighting;

import java.util.List;

import sciapi.api.value.euclidian.EVector;
import stellarium.view.ViewPoint;

public class LightingDataReflected implements ILightingData {
	
	private double intensity;
	private EVector pos;
	private EVector refDir;
	private List<LightingShade> shades;
	
	@Override
	public double getFlux(ViewPoint vp) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getIntensity(ViewPoint vp, EVector origin) {
		// TODO Auto-generated method stub
		return 0;
	}

}
