package stellarium.lighting;

import sciapi.api.value.IValRef;
import sciapi.api.value.euclidian.EProjection;
import sciapi.api.value.euclidian.EVector;
import sciapi.api.value.util.BOp;
import stellarium.util.math.Spmath;
import stellarium.util.math.VecMath;

public class LightingShade {
	/* 
	 * Center position relative to the center of shaded body.
	 * Perpendicular to the vector to the Source.
	 */
	private EVector centerPos = new EVector(3);
	
	//Effective Depth for Penumbra.
	private double effDepth_Penumbra;
	//Effective Depth for Umbra/Antumbra.
	private double effDepth_Antumbra;
	
	//Size of Umbra(positive), or Antumbra(negative)
	private double size_Umbra;
	//Size of Penumbra
	private double size_Penumbra;
	private EVector lightSrc = new EVector(3), shader = new EVector(3);
	private double lightSrcRadius, shaderRadius, shadedRadius;
	private double factor;
	
	public LightingShade(EVector shaderPos, EVector shadedPos, double lightSrcRadius, double shaderRadius, double shadedRadius, double shaderDistFromSrc, double srcDist) {
		lightSrc.set(BOp.minus(shadedPos));
		shader.set(VecMath.sub(shaderPos, shadedPos));
		this.lightSrcRadius = lightSrcRadius;
		this.shaderRadius = shaderRadius;
		this.shadedRadius = shadedRadius;
		
		double rate = srcDist / shaderDistFromSrc;
		this.size_Umbra = shaderRadius * rate - lightSrcRadius * (rate - 1.0);
		this.size_Penumbra = shaderRadius * rate + lightSrcRadius * (rate - 1.0);
		
		if(this.size_Umbra < 0.0) {
			this.effDepth_Antumbra = (shaderRadius * shaderRadius) / (lightSrcRadius * lightSrcRadius);
		} else this.effDepth_Antumbra = 1.0;
		
		this.effDepth_Penumbra = this.effDepth_Antumbra * 0.5;
		
		this.factor = Spmath.getD(VecMath.dot(VecMath.normalize(shaderPos), VecMath.normalize(shadedPos)));
		
		EVector normSrc = new EVector(3).set(VecMath.normalize(this.lightSrc));
		centerPos.set(VecMath.sub(this.shader, VecMath.mult(VecMath.dot(normSrc, this.shader), normSrc)));
		centerPos.set(VecMath.projectionToPlane(new EVector(3).set(VecMath.normalize(this.lightSrc)), this.centerPos));
	}
	
	public double getTotalShadedAmount(EVector pos) {
		// TODO Intersection Area Calculations
		return 0.0;
	}

	public double getShadedAmount(EVector origin) {
		EVector srcVec = new EVector(3).set(VecMath.sub(this.lightSrc, origin));
		EVector shaderVec = new EVector(3).set(VecMath.sub(this.shader, origin));
		double angle = VecMath.getAngle(srcVec, shaderVec);
		double srcRad = this.lightSrcRadius / Spmath.getD(VecMath.size(srcVec));
		double shaderRad = this.shaderRadius / Spmath.getD(VecMath.size(shaderVec));
		
		return Spmath.getAreaOfIntersection(srcRad, shaderRad, angle) / (Math.PI * shaderRad * shaderRad);
	}
	

}
