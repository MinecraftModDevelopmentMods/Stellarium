package stellarium.view;

import java.util.HashMap;
import java.util.Map;

import sciapi.api.value.IValRef;
import sciapi.api.value.euclidian.CrossUtil;
import sciapi.api.value.euclidian.IEVector;
import stellarium.stellars.local.LocalCValue;
import stellarium.stellars.orbit.Orbit;
import stellarium.util.DVec;
import stellarium.util.math.VecMath;
import cpw.mods.fml.relauncher.*;

public class VPClient extends ViewPoint {
	
	public DVec DZen, DNorth, DEast;
	
	public void Setpartial(double part){
		//this.Setpartial(part, manager.mvmanager.CSystem);
		
		Zen.set(DZen.Get(part));
		North.set(DNorth.Get(part));
		East.set(DEast.Get(part));
	}
	
	
	protected void UpdateCoordPos(){
		if(HostCBody!=null){
			DZen.set(HostCBody.GetZenDir(lat, lon));
			DEast.set((IValRef)CrossUtil.cross((IEVector)HostCBody.Pol, (IEVector)Zen));
			DNorth.set((IValRef)CrossUtil.cross((IEVector)Zen, (IEVector)East));
			
			HeightAU=Heightkm/manager.AU;
			EcRPos.set(VecMath.add(HostCBody.theOrbit.Pos, VecMath.mult(HostCBody.Radius+this.HeightAU, Zen)));
		}
		else{
		}
	}
}
