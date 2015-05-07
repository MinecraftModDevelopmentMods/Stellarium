package stellarium;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import stellarium.config.file.FileCfgManager;
import stellarium.settings.StellarSettings;

public class ServerProxy implements BaseProxy {

	@Override
	public void initSided(StellarSettings m) {
		m.side = Side.SERVER;		
	}

	@Override
	public void initCfgGui(FileCfgManager fm) { }

	@Override
	public void setSkyRenderer(WorldProvider provider) { }

	@Override
	public World getDefWorld() {
		return null;
	}

}
