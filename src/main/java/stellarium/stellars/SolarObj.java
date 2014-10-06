package stellarium.stellars;

import stellarium.util.math.Transforms;
import stellarium.util.math.Vec;

public abstract class SolarObj extends StellarObj {
	
	//Object's Ecliptic Position Vector from Earth's center
	Vec EcRPosE;
	
	//Albedo
	public double Albedo;

	public double Radius;
	
	//Position from Sun
	abstract public Vec GetEcRPos(double time);
	
	//Direction from Earth (Use this after both Earth and Object is Updated) (Do not use this on Earth)
	public Vec GetEcDir(){
		return Vec.Div(EcRPosE, EcRPosE.Size());
	}

	//Get Direction Vector of Object
	public Vec GetPosition(){
		Vec pvec=Transforms.ZTEctoNEc.Rot(EcRPosE);
		pvec=Transforms.EctoEq.Rot(pvec);
		pvec=Transforms.NEqtoREq.Rot(pvec);
		pvec=Transforms.REqtoHor.Rot(pvec);
		return pvec;
	}
	
	//Update SolarObj
	@Override
	public void Update(){
		EcRPos=GetEcRPos(Transforms.time);
		EcRPosE=Vec.Sub(this.EcRPos, StellarManager.Earth.EcRPos);
		super.Update();
		
		this.UpdateMagnitude();
	}
	
	//Update magnitude
	public void UpdateMagnitude(){
		double dist=this.EcRPosE.Size();
		double distS=this.EcRPos.Size();
		double distE=StellarManager.Earth.EcRPos.Size();
		double LvsSun=this.Radius*this.Radius*this.GetPhase()*distE*distE*Albedo*1.4/(dist*dist*distS*distS);
		this.Mag=-26.74-2.5*Math.log10(LvsSun);
	}
	
	//Phase of the Object(Update Needed)
	public double GetPhase(){
		return (1+Vec.Dot(EcRPos, EcRPosE)/EcRPos.Size()/EcRPosE.Size())/2;
	}

	@Override
	public void Initialize(){
		
	}
}
