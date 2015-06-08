package stellarium;

import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import stellarium.config.file.FileCfgManager;
import stellarium.settings.StellarSettings;

public interface BaseProxy {
	
	public void initSided(StellarSettings m);
	
	public void initCfgGui(FileCfgManager fm);
	
	public void setSkyRenderer(WorldProvider provider);
	
	public World getDefWorld();
	
}
