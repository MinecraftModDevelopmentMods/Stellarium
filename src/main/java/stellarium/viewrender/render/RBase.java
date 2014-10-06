package stellarium.viewrender.render;

import stellarium.util.math.*;

public abstract class RBase {
	
	public StellarRenders sr;
	public Vec Pos;
	public double Lum;
	
	public RBase SetPos(Vec pos){
		Pos=pos;
		return this;
	}
	
	public RBase SetLum(double lum){
		Lum=lum;
		return this;
	}
	
	public abstract void render();
}
