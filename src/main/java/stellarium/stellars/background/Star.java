package stellarium.stellars.background;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

import stellarium.stellars.ExtinctionRefraction;
import stellarium.stellars.StellarObj;
import stellarium.util.math.Transforms;
import stellarium.util.math.Vec;

public abstract class Star extends StellarObj{
	
	//Star Values
	byte star_value[];
	
	//Is It variable or not?
	boolean Isvariable;
	
	//B-V Value
	public double B_V;
	
	//Apparant B-V
	public double App_B_V;

	
	public String Name;
	
	public Vec Pos;
	
	/*
	 * Get star's position
	 * time is 'tick' unit
	 * world is false in Overworld, and true in Ender
	*/
	public Vec GetPosition(){
		Vec pvec=Transforms.ZTEctoNEc.Rot(EcRPos);
		pvec=Transforms.EctoEq.Rot(pvec);
		pvec=Transforms.NEqtoREq.Rot(pvec);
		pvec=Transforms.REqtoHor.Rot(pvec);
		return pvec;
	}

	@Override
	public void Update() {
		AppPos=GetAtmPos();
		double Airmass=ExtinctionRefraction.Airmass(AppPos, true);
    	App_Mag=Mag+Airmass*ExtinctionRefraction.ext_coeff_V;
    	App_B_V=B_V+Airmass*ExtinctionRefraction.ext_coeff_B_V;
	}

	@Override
	abstract public void Initialize();
	
}