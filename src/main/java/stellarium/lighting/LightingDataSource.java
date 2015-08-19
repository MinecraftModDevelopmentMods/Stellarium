package stellarium.lighting;

import sciapi.api.value.euclidian.EVector;
import stellarium.util.math.Spmath;
import stellarium.util.math.VecMath;
import stellarium.view.ViewPoint;

public class LightingDataSource implements ILightingData {
	
	private static double RATE_SR = 0.25 / Math.PI / Math.PI;
	private final double luminosity;
	private final EVector pos;
	
	public LightingDataSource(double lum, EVector pos) {
		this.luminosity = lum;
		this.pos = pos;
	}

	@Override
	public double getFlux(ViewPoint vp) {
		double dist2 = Spmath.getD(VecMath.size2(VecMath.sub(pos, vp.getEcRPos())));
		return luminosity / dist2;
	}

	@Override
	public double getIntensity(ViewPoint vp, EVector origin) {
		double dist2 = Spmath.getD(VecMath.size2(VecMath.sub(pos, vp.getEcRPos())));
		return luminosity * RATE_SR / dist2;
	}

}
