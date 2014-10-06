package stellarium.lighting;

import stellarium.util.math.Vec;

public class LightSource {
	public Vec Pos;
	public double Lum;
	public double Size;
	
	public LightSource(Vec pos, double lum, double size){
		Pos = pos;
		Lum = lum;
		Size = size;
	}
	
	public static LightSource instance;
	
	
	public double GetFlux(double Dist){
		return Lum/(Dist*Dist);
	}
	
	public double GetFlux(Vec gPos){
		return Lum/ Vec.Sub(gPos, Pos).Size2() ;
	}
}
