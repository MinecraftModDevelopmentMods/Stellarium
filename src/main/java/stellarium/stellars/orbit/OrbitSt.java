package stellarium.stellars.orbit;

import java.util.ArrayList;

import stellarium.settings.StellarSettings;
import stellarium.stellars.cbody.CBody;
import stellarium.util.math.*;

public class OrbitSt extends Orbit {
	
	public static final String PPos= "P";

	
	public void RegisterOrbit(){
	}

/*
	protected CProperty PrPos;
	protected double Di = 0.0, Lo = 0.0, La = 0.0;

	@Override
	public void PreConstruct(){
		super.PreConstruct();
		PrPos = conreader.addProperty(PPos, 3);
		PrPos.addModeforRead(virbody, "E");
	}
	
	@Override
	public void PostConstruct(){
		super.PostConstruct();
		
		String[] SPos = conreader.ReadValue(PrPos);
		
		Di = Spmath.StrtoD(SPos[0]);
		Lo = Spmath.StrtoD(SPos[1]);
		La = Spmath.StrtoD(SPos[2]);
		
		Pos.set(VecMath.mult(Di, new SpCoord(Lo, La).getVec()));
	}
	
	
	
	@SideOnly(Side.SERVER)
	@Override
	public void Update(double yr) {
		super.Update(yr);
	}
*/
}
