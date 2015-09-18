package stellarium.lighting;

import java.util.List;

import sciapi.api.value.euclidian.EVector;
import stellarium.util.math.Spmath;
import stellarium.util.math.VecMath;
import stellarium.view.ViewPoint;

public class LightingDataReflected implements ILightingData {
	
	private static final double SPHERE = 4 * Math.PI * Math.PI;
	private double intensity;
	private EVector pos;
	private EVector posFromSrc;
	private List<LightingShade> shades;

	@Override
	public double getFlux() {
		double phaseAngle = Math.PI - VecMath.getAngle(this.posFromSrc, this.pos);
		double shadeFactor = 1.0;
		for(LightingShade shade : shades)
			shadeFactor -= shade.getTotalShadedAmount(this.pos);
		shadeFactor = Math.max(0.0, shadeFactor);
		return this.intensity * shadeFactor * SPHERE * phaseAngle;
	}

	@Override
	public double getIntensity(EVector origin) {
		if(Spmath.getD(VecMath.dot(this.posFromSrc, origin)) > 0.0)
			return 0.0;
		
		double shadeFactor = 1.0;
		for(LightingShade shade : shades)
			shadeFactor -= shade.getShadedAmount(origin);
		shadeFactor = Math.max(0.0, shadeFactor);
		
		return this.intensity * shadeFactor;
	}

}
