package stellarium;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import stellarium.stellars.OldStellarManager;

public class ServerProxy extends BaseProxy {

	@Override
	public void InitSided(OldStellarManager m) {
		m.side = Side.CLIENT;
		
		FMLCommonHandler.instance().bus().register(new StellarTickHandler(m.side));

	}


}
