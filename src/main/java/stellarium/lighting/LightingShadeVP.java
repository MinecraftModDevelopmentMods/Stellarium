package stellarium.lighting;

import sciapi.api.value.IValRef;
import sciapi.api.value.euclidian.EVector;
import sciapi.api.value.util.BOp;
import stellarium.util.math.Spmath;
import stellarium.util.math.VecMath;

public class LightingShadeVP {
	
	private EVector shaderPos;
	private double lightSrcRadius, shaderRadius;
	
	public LightingShadeVP(EVector shaderPos, double lightSrcRadius, double shaderRadius) {
		this.shaderPos = shaderPos;
		this.lightSrcRadius = lightSrcRadius;
		this.shaderRadius = shaderRadius;
	}
	
	public double getTotalShadedAmount(EVector pos) {
		EVector shaderVec = new EVector(3).set(VecMath.add(this.shaderPos, pos));
		double angle = VecMath.getAngle(pos, shaderVec);
		double srcRad = this.lightSrcRadius / Spmath.getD(VecMath.size(pos));
		double shaderRad = this.shaderRadius / Spmath.getD(VecMath.size(shaderVec));
		
		return Spmath.getAreaOfIntersection(srcRad, shaderRad, angle) / (Math.PI * shaderRad * shaderRad);
	}

	public double getShadedAmount(EVector origin, EVector pos) {
		EVector relPosNormal = new EVector(3).set(VecMath.normalize(VecMath.add(pos, origin)));
		EVector relShaderPos = new EVector(3).set(VecMath.sub(this.shaderPos, origin));
		double dist = Spmath.getD(VecMath.size(VecMath.projectionToPlane(relPosNormal, relShaderPos)));
		return dist < this.shaderRadius? 1.0 : 0.0;
	}

}
