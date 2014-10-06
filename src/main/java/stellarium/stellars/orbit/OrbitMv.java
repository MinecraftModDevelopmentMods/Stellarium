package stellarium.stellars.orbit;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import stellarium.initials.CCertificateHelper;
import stellarium.stellars.cbody.CBody;
import stellarium.util.UpDouble;
import stellarium.util.math.*;

public abstract class OrbitMv extends Orbit {
	
	public UpDouble i=new UpDouble(), Om=new UpDouble();
	
	public Vec Pol;
	
	public double Hill_Radius;
	
	public void PreCertificate(){
		super.PreCertificate();
		HillRadius();
	}
	
	public void Certificate(){
		CCertificateHelper cch = null;
		super.Certificate();
		if((!ParOrbit.IsVirtual) && this.Mass < ParOrbit.Mass * cch.SatMassLimit)
			cch.Unstable("Mass of "+this.Name+" Is too big compared to the parent "+ParOrbit.Name+"!");
	}
	
	@SideOnly(Side.SERVER)
	@Override
	public void Update(double yr) {
		UpdateOrbitalElements(yr);
		UpdateEcRPos();
		ParEcRPos();
		UpdatePole();
		super.Update(yr);
	}

	@SideOnly(Side.SERVER)
	protected abstract void UpdateOrbitalElements(double yr);
	
	@SideOnly(Side.SERVER)
	protected abstract void UpdateEcRPos();
	
	public abstract double GetAvgRot();
	
	@SideOnly(Side.SERVER)
	protected void ParEcRPos(){
		Pos=Vec.Add(Pos, ParOrbit.Pos);
	}
	
	@SideOnly(Side.SERVER)
	protected void UpdatePole(){
		Pol=new Vec(0.0, 0.0, 1.0);
		Rotate RI=new Rotate('X', -Spmath.Radians(i.val0));
		Rotate ROm=new Rotate('Z', -Spmath.Radians(Om.val0));
		Pol=ROm.Rot(RI.Rot(Pol));
	}
	
	
	@SideOnly(Side.SERVER)
	abstract protected void HillRadius();
}
