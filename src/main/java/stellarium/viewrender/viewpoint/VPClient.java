package stellarium.viewrender.viewpoint;

import java.util.HashMap;
import java.util.Map;

import stellarium.stellars.local.LocalCValue;
import stellarium.stellars.orbit.Orbit;
import stellarium.util.DVec;
import stellarium.util.math.Coord;
import stellarium.util.math.Vec;
import cpw.mods.fml.relauncher.*;


@SideOnly(Side.CLIENT)
public class VPClient extends ViewPoint {
	
	public Map<Orbit, LocalCValue> mapotoln=new HashMap<Orbit, LocalCValue>();
	public Map<LocalCValue, Orbit> mapltoon=new HashMap<LocalCValue, Orbit>();
	
	public DVec DZen, DNorth, DEast;
	
	protected void SetLCV(Orbit orb){
		mapotol.put(orb, new LocalCValue());
		mapotoln.put(orb, new LocalCValue());
		for(int i=0; i<orb.SatOrbit.size(); i++)
			SetLCV(orb.SatOrbit.get(i));
	}
	
	public void Setpartial(double part){
		this.Setpartial(part, manager.mvmanager.CSystem);
		
		Zen = DZen.Get(part);
		North = DNorth.Get(part);
		East = DEast.Get(part);
	}
	
	protected void Setpartial(double part, Orbit orb){
		LocalCValue lcv=mapotol.get(orb);
		LocalCValue lcvn=mapotoln.get(orb);
		lcv.Get(part, lcvn);
		for(int i=0; i<orb.SatOrbit.size(); i++)
			Setpartial(part, orb.SatOrbit.get(i));
	}
	
	
	protected void UpdateCoordPos(){
		if(HostCBody!=null){
			DZen.Set(HostCBody.GetZenDir(lat, lon));
			DEast.Set(Vec.Cross(HostCBody.Pol, Zen));
			DNorth.Set(Vec.Cross(Zen, East));
			
			HeightAU=Heightkm/manager.AU;
			EcRPos=Vec.Add(HostCBody.theOrbit.Pos, Vec.Mul(Zen, HostCBody.Radius+this.HeightAU));
		}
		else{
		}
	}
}
