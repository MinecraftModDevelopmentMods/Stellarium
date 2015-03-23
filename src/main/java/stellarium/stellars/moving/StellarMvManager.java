package stellarium.stellars.moving;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import stellarium.settings.StellarSettings;
import stellarium.stellars.orbit.OrbitSt;

public class StellarMvManager {
	StellarSettings manager;
	
	@SideOnly(Side.CLIENT)
	public String pointytexloc;
	
	public OrbitSt CSystem;
	
	public StellarMvManager(StellarSettings m){
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
