package stellarium.stellars;

import stellarium.util.math.Transforms;
import stellarium.util.math.Vec;

public class Sun extends StellarObj{
	
	//Mass of Sun
	public double Mass;
	
	//Radius of Sun
	public double Radius;

	//Update Sun
	@Override
	public void Update() {
		super.Update();
	}

	//Get Direction Vector of Sun from Earth
	@Override
	public Vec GetPosition() {
		Vec pvec=Transforms.ZTEctoNEc.Rot(Vec.Mul(StellarManager.Earth.EcRPos, -1.0));
		pvec=Transforms.EctoEq.Rot(pvec);
		pvec=Transforms.NEqtoREq.Rot(pvec);
		pvec=Transforms.REqtoHor.Rot(pvec);
		return pvec;
	}

	@Override
	public void Initialize() {
		
	}

}
