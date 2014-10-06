package stellarium.stellars;

import stellarium.util.math.Rotate;
import stellarium.util.math.Spmath;
import stellarium.util.math.Transforms;
import stellarium.util.math.Vec;

public class Moon extends Satellite {
	
	public double Radius;
	
	//Additional Orbital Elements for Moon
	double a0, e0, I0, w0, Omega0, M0_0;
	double wd, Omegad;

	double brightness;
	
	//Moon's Ecliptic Position Vector from Ground
	Vec EcRPosG;
	
	
	//Moon's Pole(Ecliptic Coord)
	Vec Pole;
	
	//Moon's Prime Meridian at first
	Vec PrMer0;
	
	//Moon's East from Prime Meridian
	Vec East;

	
	public void Initialize(){
		Pole=new Vec(0.0, 0.0, 1.0);
		Rotate RI=new Rotate('X', -Spmath.Radians(I0));
		Rotate ROm=new Rotate('Z', -Spmath.Radians(Omega0));
		Pole=ROm.Rot(RI.Rot(Pole));
		PrMer0=Vec.Mul(this.GetEcRPosE(0.0),-1.0);
		PrMer0=Vec.Div(PrMer0, PrMer0.Size());
		East=Vec.Cross(Pole, PrMer0);
	}
	
	//Get Ecliptic Position Vector from Earth
	public Vec GetEcRPosE(double time){
		double day=time/24000.0;
		double yr=day/365.25;
		UpdateOrbE(yr);
		double M=M0+mean_mot*yr;
		return Spmath.GetOrbVec(a, e, new Rotate('X', -Spmath.Radians(I)), new Rotate('Z', -Spmath.Radians(w)), new Rotate('Z', -Spmath.Radians(Omega)), M);
	}
	
	//Update Orbital Elements in time
	public void UpdateOrbE(double yr){
		a=a0;
		e=e0;
		I=I0;
		w=w0+wd*yr;
		Omega=Omega0+Omegad*yr;
		M0=M0_0;
		mean_mot=360.0*Math.sqrt(Parplanet.Mass/a)/a;
	}

	
	//Update Moon(Use After Earth is Updated)
	public void Update(){
		double time=Transforms.time;
		
		EcRPosE=this.GetEcRPosE(time);
		EcRPos=Vec.Add(Parplanet.GetEcRPos(time),EcRPosE);
		EcRPosG=Vec.Sub(EcRPosE,Transforms.Zen);
		
		AppPos=GetAtmPos();
		/*App_Mag=Mag+ExtinctionRefraction.Airmass(AppPos.z, true)*ExtinctionRefraction.ext_coeff_V;*/
		this.UpdateMagnitude();
		this.UpdateBrightness();
	}
	
	//Update magnitude
	public void UpdateMagnitude(){
		double dist=this.EcRPosG.Size();
		double distS=this.EcRPos.Size();
		double distE=StellarManager.Earth.EcRPos.Size();
		double LvsSun=this.Radius*this.Radius*this.GetPhase()*distE*distE*Albedo*1.4/(dist*dist*distS*distS);
		this.Mag=-26.74-2.5*Math.log10(LvsSun);
	}
	
	public Vec GetPosition(){
		Vec pvec=Transforms.ZTEctoNEc.Rot(EcRPosG);
		pvec=Transforms.EctoEq.Rot(pvec);
		pvec=Transforms.NEqtoREq.Rot(pvec);
		pvec=Transforms.REqtoHor.Rot(pvec);
		return pvec;
	}
	
	//Ecliptic Position of Moon's Local Region from Moon Center (Update Needed)
	public Vec PosLocalM(double longitude, double lattitude, double time){
		time/=24000;
		time/=365.25;
		float longp=(float)Spmath.Radians(longitude+mean_mot*time);
		float lat=(float)Spmath.Radians(lattitude);
		Vec Pl=Vec.Add(Vec.Add(Vec.Mul(Pole, Spmath.sind(lat)), Vec.Mul(PrMer0, Spmath.cosd(lat)*Spmath.cosd(longp))), Vec.Mul(East,Spmath.cosd(lat)*Spmath.sind(longp)));
		return Vec.Mul(Pl, Radius);
	}
	
	//Ecliptic Position of Moon's Local Region from Ground (Update Needed)
	//Parameter: PosLocalM Result
	public Vec PosLocalG(Vec p){
		return Vec.Add(EcRPosG, p);
	}
	
	//Brightness for Parts (After Magnitude is Updated)
	public void UpdateBrightness(){
		brightness=(Math.pow(10.0, (-16-Mag)/2.5));
		brightness/=this.GetPhase();
		double Angsize=Radius/EcRPosG.Size();
		brightness/=(Angsize*Angsize*3e+3);
	}
	
	
	//Illumination of Moon's Local Region (Update Needed)
	//Parameter: PosLocalM Result
	public double Illumination(Vec p){
		return (-Vec.Dot(EcRPos, p)/EcRPos.Size()/p.Size())*brightness;
	}
	
	//Phase of the Moon(Update Needed)
	public double GetPhase(){
		return (1-Vec.Dot(EcRPos, EcRPosG)/EcRPos.Size()/EcRPosG.Size())/2;
	}
	
	//Time phase for moon
	public double Phase_Time(){
		double k=Math.signum(Vec.Dot(Vec.Cross(EcRPosG, EcRPos),Pole))*GetPhase();
		if(k<0) k=k+2;
		return k/2;
	}
}
