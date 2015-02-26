package stellarium;

import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import stellarium.catalog.StellarCatalogRegistry;
import stellarium.catalog.gui.GuiCatalogCfgProvider;
import stellarium.config.gui.gui.DefCfgGuiProvider;
import stellarium.config.gui.gui.StellarCfgGuiRegistry;
import stellarium.lang.CLangStrs;
import stellarium.stellars.OldStellarManager;


public class ClientProxy extends BaseProxy {

	@Override
	public void InitSided(OldStellarManager m) {
		m.side = Side.CLIENT;
		
		FMLCommonHandler.instance().bus().register(new StellarTickHandler(m.side));

        StellarCfgGuiRegistry.register(new DefCfgGuiProvider(CLangStrs.defaultConfig, Configuration.CATEGORY_GENERAL));
    	//StellarCfgGuiRegistry.register(new GuiCatalogCfgProvider());
	}
	
}
