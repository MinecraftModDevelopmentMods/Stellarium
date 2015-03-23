package stellarium;

import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import stellarium.catalog.StellarCatalogRegistry;
import stellarium.config.file.FileCfgManager;
import stellarium.config.gui.StellarCfgGuiMProvider;
import stellarium.config.gui.StellarConfigGuiProvider;
import stellarium.config.gui.gui.DefCfgGuiProvider;
import stellarium.config.gui.gui.StellarCfgGuiRegistry;
import stellarium.lang.CLangStrs;
import stellarium.settings.StellarSettings;


public class ClientProxy implements BaseProxy {

	@Override
	public void initSided(StellarSettings m) {
		m.side = Side.CLIENT;
		
		FMLCommonHandler.instance().bus().register(new StellarTickHandler(m.side));
	}
	
	@Override
	public void initCfgGui(FileCfgManager fm)
	{
		StellarCfgGuiRegistry.register(new DefCfgGuiProvider(CLangStrs.defaultConfig, Configuration.CATEGORY_GENERAL));
    	StellarCfgGuiRegistry.register(new StellarCfgGuiMProvider(fm));
	}
	
}
