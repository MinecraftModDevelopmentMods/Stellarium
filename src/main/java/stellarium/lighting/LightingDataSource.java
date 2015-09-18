package stellarium.lighting;

import java.util.List;

import sciapi.api.value.euclidian.EVector;
import stellarium.util.math.Spmath;
import stellarium.util.math.VecMath;
import stellarium.view.ViewPoint;

public class LightingDataSource implements ILightingData {
	
	private static final double RATE_SR = 0.25 / Math.PI / Math.PI;
	private final double flux;
	private EVector pos;
	private List<LightingShadeVP> shades;
	
	public LightingDataSource(double flux) {
		this.flux = flux;
	}

	@Override
	public double getFlux() {
		double shadeFactor = 1.0;
		for(LightingShadeVP shade : shades)
			shadeFactor -= shade.getTotalShadedAmount(this.pos);
		shadeFactor = Math.max(0.0, shadeFactor);
		return this.flux * shadeFactor;
	}

	@Override
	public double getIntensity(EVector origin) {
		double shadeFactor = 1.0;
		for(LightingShadeVP shade : shades)
			shadeFactor += shade.getTotalShadedAmount(this.pos);
		shadeFactor = Math.max(0.0, shadeFactor);
		return this.flux * RATE_SR * shadeFactor;
	}

}
