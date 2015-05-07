package stellarium;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import stellarium.catalog.StellarCatalogRegistry;
import stellarium.config.file.FileCfgManager;
import stellarium.config.gui.StellarCfgGuiMProvider;
import stellarium.config.gui.StellarConfigGuiProvider;
import stellarium.gui.config.DefCfgGuiProvider;
import stellarium.gui.config.StellarCfgGuiRegistry;
import stellarium.lang.CLangStrs;
import stellarium.settings.StellarSettings;

public class ClientProxy implements BaseProxy {

	@Override
	public void initSided(StellarSettings m) {
		m.side = Side.CLIENT;
	}
	
	@Override
	public void initCfgGui(FileCfgManager fm)
	{
		StellarCfgGuiRegistry.register(new DefCfgGuiProvider(CLangStrs.defaultConfig, Configuration.CATEGORY_GENERAL));
    	StellarCfgGuiRegistry.register(new StellarCfgGuiMProvider(fm));
	}

	@Override
	public void setSkyRenderer(WorldProvider provider) {
		provider.setSkyRenderer(new DrawSky());
	}

	@Override
	public World getDefWorld() {
		return Minecraft.getMinecraft().theWorld;
	}
	
}
