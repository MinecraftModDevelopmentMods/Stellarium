package stellarium.util.math;

import stellarium.stellars.StellarManager;

public class Transforms {
	
	//Axial tilt
	public static final double e=0.4090926;
	//Precession
	public static final double Prec=0.0;
	//Real Day
	public static final double Rot=0.000262516155;
	//Latitude on Overworld
	public static final double Lat=0.6544985;
	//Latitude on Ender
	public static final double Lat2=-0.9162979;
	

	public static double time;
	
	
	//Set Transforms' time and world (stime:tick)
	public static void Update(double stime, boolean IsOverWorld){
		time=stime;
		
		ZTEctoNEc=new Rotate('Z',-Prec*time);
		NEctoZTEc=new Rotate('Z', Prec*time);
		NEqtoREq=new Rotate('Z', -Rot*time);
		REqtoNEq=new Rotate('Z', Rot*time);
		if(IsOverWorld){
			REqtoHor=new Rotate('X', Lat-Math.PI*0.5);
			HortoREq=new Rotate('X', Math.PI*0.5-Lat);
		}
		else{
			REqtoHor=new Rotate('X', Lat2-Math.PI*0.5);
			HortoREq=new Rotate('X', Math.PI*0.5-Lat2);
		}
		
		ZTEctoNEcf=new Rotatef('Z',(float) (-Prec*time));
		NEctoZTEcf=new Rotatef('Z', (float) (Prec*time));
		NEqtoREqf=new Rotatef('Z', (float) (-Rot*time));
		REqtoNEqf=new Rotatef('Z', (float) (Rot*time));
		if(IsOverWorld){
			REqtoHorf=new Rotatef('X', (float) (Lat-Math.PI*0.5));
			HortoREqf=new Rotatef('X', (float) (Math.PI*0.5-Lat));
		}
		else{
			REqtoHorf=new Rotatef('X', (float) (Lat2-Math.PI*0.5));
			HortoREqf=new Rotatef('X', (float) (Math.PI*0.5-Lat2));
		}
		
		ZenD=new Vec(0.0,0.0,1.0);
		ZenD=Transforms.HortoREq.Rot(ZenD);
		ZenD=Transforms.REqtoNEq.Rot(ZenD);
		ZenD=Transforms.EqtoEc.Rot(ZenD);
		ZenD=Transforms.NEctoZTEc.Rot(ZenD);
		Zen=Vec.Mul(ZenD, StellarManager.Earth.Radius);
	}
	
	
	//Direction of Zenith
	public static Vec ZenD;
	
	//Vector from Earth center to Ground
	public static Vec Zen;
	
	
	//Equatorial to Ecliptic
	public static final Rotate EqtoEc=new Rotate('X',-e); 
	
	//Ecliptic to Equatorial
	public static final Rotate EctoEq=new Rotate('X',e); 
	
	
	//Zero Time Ecliptic to Now Ecliptic
	public static Rotate ZTEctoNEc;

	//Now Ecliptic to Zero Time Ecliptic
	public static Rotate NEctoZTEc;


	//Now Equatorial to Rotating Equatorial
	public static Rotate NEqtoREq;
	
	//Rotating Equatorial to Now Equatorial
	public static Rotate REqtoNEq;

	
	//Rotating Equatorial to Horizontal
	public static Rotate REqtoHor;
	
	//Horizontal to Rotating Equatorial
	public static Rotate HortoREq;
	
	//Equatorial to Ecliptic
	public static final Rotatef EqtoEcf=new Rotatef('X',(float) -e); 
	
	//Ecliptic to Equatorial
	public static final Rotatef EctoEqf=new Rotatef('X',(float) e); 
	
	
	//Zero Time Ecliptic to Now Ecliptic
	public static Rotatef ZTEctoNEcf;

	//Now Ecliptic to Zero Time Ecliptic
	public static Rotatef NEctoZTEcf;


	//Now Equatorial to Rotating Equatorial
	public static Rotatef NEqtoREqf;
	
	//Rotating Equatorial to Now Equatorial
	public static Rotatef REqtoNEqf;

	
	//Rotating Equatorial to Horizontal
	public static Rotatef REqtoHorf;
	
	//Horizontal to Rotating Equatorial
	public static Rotatef HortoREqf;
	
	
	//Get Vector from SpCoord
	public static final Vec GetVec(SpCoord eqc){
		return new Vec(
				Spmath.cosd(eqc.y)*Spmath.cosd(eqc.x),
				Spmath.cosd(eqc.y)*Spmath.sind(eqc.x),
				Spmath.sind(eqc.y)
				);
	}
	
	public static final Vecf GetVec(SpCoordf eqc){
		return new Vecf(
				( (Spmath.cosd(eqc.y)*Spmath.cosd(eqc.x))),
				( (Spmath.cosd(eqc.y)*Spmath.sind(eqc.x))),
				( Spmath.sind(eqc.y))
				);
	}
	
	//Get Horizontal Sphere Coordinate from Direction Vector
	public static final SpCoord GetHorCoord(Vec vec){
		return new SpCoord(
				Spmath.Degrees(Math.atan2(vec.y, vec.x)),
				Spmath.Degrees(Math.asin(vec.z))
				);
	}
	
	public static final SpCoordf GetHorCoord(Vecf vec){
		return new SpCoordf(
				(float)(Spmath.Degrees(Math.atan2(vec.y, vec.x))),
				(float)(Spmath.Degrees(Math.asin(vec.z)))
				);
	}
}
