package stellarium.stellars;

import stellarium.util.math.Transforms;
import stellarium.util.math.Vec;

public class Earth extends Planet {
	
	final double MvsE=0.0121505;
	
	//Get Ecliptic Position vector from Sun
	
	public Vec GetEcRPos(double time) {
		Vec EM=super.GetEcRPos(time);
		this.satellites.get(0).EcRPosE=((Moon)this.satellites.get(0)).GetEcRPosE(time);
		return Vec.Sub(EM, Vec.Mul(this.satellites.get(0).EcRPosE, MvsE));
	}
	
	//Update Earth(Have to be first)
	public void Update(){
		EcRPos=GetEcRPos(Transforms.time);
		this.satellites.get(0).Update();
	}
	
}
