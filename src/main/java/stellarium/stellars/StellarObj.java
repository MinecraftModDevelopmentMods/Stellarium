package stellarium.stellars;

import stellarium.util.math.Vec;

public abstract class StellarObj {
	
	//Object's Ecliptic Position(Mainly from Sun)
	public Vec EcRPos;
	
	//Object's Apparent Position
	public Vec AppPos;
	
	//Magnitude of Object(Except Atmosphere)
	public double Mag;
	
	//Object's Apparent Magnitude
	public double App_Mag;
	
	//Initialize the Object
	abstract public void Initialize();
	
	//Update the Object
	public void Update(){
		AppPos=GetAtmPos();
		App_Mag=Mag+ExtinctionRefraction.Airmass(AppPos.z, true)*ExtinctionRefraction.ext_coeff_V;
	}
	
	//Get Vector of Object from Earth
	abstract public Vec GetPosition();
	
	public Vec GetAtmPos(){
		Vec p=GetPosition();
		return ExtinctionRefraction.Refraction(p, true);
	}
}
