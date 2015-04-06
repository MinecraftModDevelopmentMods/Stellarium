package stellarium;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import stellarium.config.file.FileCfgManager;
import stellarium.settings.StellarSettings;

public class ServerProxy implements BaseProxy {

	@Override
	public void initSided(StellarSettings m) {
		m.side = Side.SERVER;
		
		FMLCommonHandler.instance().bus().register(new StellarTickHandler(m.side));
	}

	@Override
	public void initCfgGui(FileCfgManager fm) { }


}
