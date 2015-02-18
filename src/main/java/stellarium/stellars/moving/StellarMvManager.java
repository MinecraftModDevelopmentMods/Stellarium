package stellarium.stellars.moving;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import stellarium.stellars.OldStellarManager;
import stellarium.stellars.orbit.OrbitSt;

public class StellarMvManager {
	OldStellarManager manager;
	
	@SideOnly(Side.CLIENT)
	public String pointytexloc;
	
	public OrbitSt CSystem;
	
	public StellarMvManager(OldStellarManager m){
		manager = m;
	}
	
	public void OpenWorld(OrbitSt consys){
		CSystem = consys;
		CSystem.manager = this.manager;
		if(!CSystem.IsVirtual)
			CSystem.theBody.manager = this.manager;
	}
	
	public void CloseWorld(){
		CSystem = null;
	}
}
