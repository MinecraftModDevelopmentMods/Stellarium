package stellarium.stellars;

import stellarium.util.math.Rotate;
import stellarium.util.math.Spmath;
import stellarium.util.math.Vec;

public class Satellite extends SolarObj {
	
	Planet Parplanet;
	
	//Orbital Elements
	double a, e, I, w, Omega, M0;
	double mean_mot;

	//Get Satellite's Ecliptic Position of 
	@Override
	public Vec GetEcRPos(double time) {
		double M=M0+mean_mot*time;
		Vec vec=Spmath.GetOrbVec(a, e, new Rotate('X', -I), new Rotate('Z', -w), new Rotate('Z', -Omega), M);
		return Vec.Add(vec, Parplanet.EcRPos);
	}
	
	//Update Satellite
	public void Update(){
		super.Update();
	}
	
	
	//Initialize
	@Override
	public void Initialize() {
		mean_mot=Parplanet.Mass;
	}
}
