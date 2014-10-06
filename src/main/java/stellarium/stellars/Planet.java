package stellarium.stellars;

import java.util.ArrayList;

import stellarium.util.math.Rotate;
import stellarium.util.math.Spmath;
import stellarium.util.math.Transforms;
import stellarium.util.math.Vec;

public class Planet extends SolarObj{
	
	Planet(){
		b=c=s=f=0.0;
	}
	
	//Orbital Elements of Planet
	double a0, e0, I0, L0, wbar0, Omega0;
	double ad, ed, Id, Ld, wbard, Omegad;
	double b, c, s, f;
	
	//Radius of Planet
	public double Radius;
	
	//Planet's Pole(Ecliptic Coord)
	Vec Pole;
	
	//Planet's Prime Meridian at first
	Vec PrMer0;
	
	//Planet's East from Prime Meridian
	Vec East;
	
	//Rotating angular velocity
	double Rot;
	
	//Mass of Planet
	double Mass;
	
	//Albedo of Planet
	double Albedo;
	
	//Satellites
	ArrayList<Satellite> satellites=new ArrayList(1);
	
	//Planet name
	char name[];

	@Override
	//Calculate Planet's Ecliptic Vector from Sun
	public Vec GetEcRPos(double time) {
		double day=time/24000.0;
		double cen=day/36525.0;
		double a=a0+ad*cen,
				e=e0+ed*cen,
				I=I0+Id*cen,
				L=L0+Ld*cen,
				wbar=wbar0+wbard*cen,
				Omega=Omega0+Omegad*cen;
		double w=wbar-Omega;
		double M=L-wbar+b*cen*cen+c*Spmath.cosd(f*cen)+s*Spmath.sind(f*cen);
		return Spmath.GetOrbVec(a, e, new Rotate('X', -Spmath.Radians(I)), new Rotate('Z', -Spmath.Radians(w)), new Rotate('Z', -Spmath.Radians(Omega)), M);
	}
	
	//Ecliptic Position of Planet's Local Region from Moon Center (Update Needed)
	public Vec PosLocalP(double longitude, double lattitude, double time){
		double longp=Spmath.Radians(longitude+Rot*time);
		double lat=Spmath.Radians(lattitude);
		Vec Pl=Vec.Add(Vec.Add(Vec.Mul(Pole, Math.sin(lat)), Vec.Mul(PrMer0, Math.cos(lat)*Math.cos(longp))), Vec.Mul(East,Math.cos(lat)*Math.sin(longp)));
		return Vec.Mul(Pl, Radius);
	}
	
	//Ecliptic Position of Planet's Local Region from Earth (Update Needed)
	public Vec PosLocalE(double longitude, double lattitude, double time){
		return Vec.Add(EcRPosE, PosLocalP(longitude, lattitude, time));
	}
	
	//Update magnitude
	public void UpdateMagnitude(){
		double dist=this.EcRPosE.Size();
		double distS=this.EcRPos.Size();
		double distE=StellarManager.Earth.EcRPos.Size();
		double LvsSun=this.Radius*this.Radius*this.GetPhase()*distE*distE*Albedo*1.4/(dist*dist*distS*distS);
		this.Mag=-26.74-2.5*Math.log10(LvsSun);
	}
	


	//Update Planet
	@Override
	public void Update() {
		EcRPos=GetEcRPos(Transforms.time);
		EcRPosE=Vec.Sub(this.EcRPos, StellarManager.Earth.EcRPos);
		
		for(int i=0; i<satellites.size(); i++)
			satellites.get(i).Update();
		
		this.UpdateMagnitude();
		
		AppPos=GetAtmPos();
		App_Mag=Mag+ExtinctionRefraction.Airmass(AppPos.z, true)*ExtinctionRefraction.ext_coeff_V;
	}
	
	public void AddSatellite(Satellite sat){
		sat.Parplanet=this;
		satellites.add(sat);
	}


	@Override
	public void Initialize() {
//		East=Vec.Cross(Pole, PrMer0);
		for(int i=0; i<satellites.size(); i++)
			satellites.get(i).Initialize();
	}

}
