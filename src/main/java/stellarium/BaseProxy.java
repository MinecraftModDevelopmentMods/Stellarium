package stellarium;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.relauncher.Side;
import stellarium.config.file.FileCfgManager;
import stellarium.settings.StellarSettings;

public interface BaseProxy {
	
	public void initSided(StellarSettings m);
	
	public void initCfgGui(FileCfgManager fm);
	
	public void setSkyRenderer(WorldProvider provider);
	
	public World getDefWorld();
	
}
