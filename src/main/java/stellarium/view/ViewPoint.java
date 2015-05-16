package stellarium.view;

import java.util.HashMap;
import java.util.Map;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import scala.collection.immutable.Map.Map1;
import sciapi.api.value.IValRef;
import sciapi.api.value.euclidian.CrossUtil;
import sciapi.api.value.euclidian.EVector;
import sciapi.api.value.euclidian.IEVector;
import stellarium.settings.StellarSettings;
import stellarium.sky.ISkySet;
import stellarium.stellars.cbody.*;
import stellarium.stellars.local.*;
import stellarium.stellars.orbit.*;
import stellarium.util.math.*;


public class ViewPoint {
	
	//Fixed in the Host Body
	boolean IsFixedVp=false;
	
	//Hosting Celestial Body
	public CBody HostCBody;
	
	//Gravitational Influence Orbit
	public Orbit OrbInf;

	//Lattitude/Longitude (Degrees)
	public double lat, lon;
	
	//Height from Surface of the Host Planet (km, AU)
	public double Heightkm, HeightAU;
	
	//Horizontal Coordinate - Zenith, North, East (Unit Vector)
	public EVector Zen = new EVector(3), North = new EVector(3), East = new EVector(3);
	
	//EcRPos of Viewpoint
	public EVector EcRPos = new EVector(3);

	
	//The StellarManager
	public StellarSettings manager;
	
	private ISkySet skyset;
	
	public ISkySet getSkySet()
	{
		return skyset;
	}
	
	public void InitFixedVp(CBody hostcbody, StellarSettings m){
		manager=m;
		
		IsFixedVp=true;
		HostCBody=hostcbody;
		
		//SetLCV(manager.mvmanager.CSystem);
	}
	
	public void InitVp(StellarSettings m){
		manager=m;
		//SetLCV(manager.mvmanager.CSystem);
	}

	protected void UpdateCoordPos(){
		if(HostCBody!=null){
			Zen.set(HostCBody.GetZenDir(lat, lon));
			East.set((IValRef)CrossUtil.cross((IEVector)HostCBody.Pol, (IEVector)Zen));
			North.set((IValRef)CrossUtil.cross((IEVector)Zen, (IEVector)East));
			
			HeightAU=Heightkm/manager.AU;
			EcRPos.set(VecMath.add(HostCBody.theOrbit.Pos, VecMath.mult(HostCBody.Radius+this.HeightAU, Zen)));
		}
		else{
		}
	}
	
	protected void UpdateHost() {
		//Leaving Host
		if(!HostCBody.IsHosting(this.HeightAU)){
			HostCBody=null;
			return;
		}
		
		//Entering Host
		if(HostCBody==null){
			HostCBody=FindHostCBody(true, OrbInf);
		}
	}
	
	public CBody FindHostCBody(boolean checksat, Orbit orb){
		return HostCBody;
	/*	if(!orb.IsVirtual){
			CBody theBody=orb.theBody;
			LocalCValue lcv=mapotol.get(OrbInf);
			if(theBody.IsHosting(lcv.Dist-theBody.Radius)){
				return theBody;
			}
		}
		
		if(checksat){
			CBody body;
			for(int i=0; i<orb.SatOrbit.size(); i++){
				body=FindHostCBody(false, orb.SatOrbit.get(i));
				if(body!=null) return body;
			}
		}
		return null;*/
	}
	
	protected void UpdateInf() {
		//Leaving Gravitational Influence Sphere
		/*if(this.OrbInf instanceof OrbitMv){
			OrbitMv orb=(OrbitMv)OrbInf;
			if(orb.Hill_Radius < mapotol.get(orb).Dist){
				this.OrbInf=OrbInf.ParOrbit;
				return;
			}
		}
		
		//Entering Gravitational Influence Sphere
		OrbitMv orb;
		for(int i=0; i<OrbInf.SatOrbit.size(); i++){
			orb=(OrbitMv) OrbInf.SatOrbit.get(i);
			if(mapotol.get(orb).Dist <= orb.Hill_Radius)
			{
				this.OrbInf=orb;
				return;
			}
		}*/
	}

	public EVector getEcRPos() {
		// TODO Auto-generated method stub
		return null;
	}

	
/*	public Vec GetLocPos(Vec TarEcRPos){
		Vec tar=Vec.Sub(TarEcRPos, EcRPos);
		if(HostCBody!=null) return HorCoord.Transform(TarEcRPos);
		else return tar;
	}*/
}
