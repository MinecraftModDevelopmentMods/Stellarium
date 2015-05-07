package stellarium;

import stellarium.config.core.ConfigDataPhysicalManager;import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.world.WorldEvent;

public class StellarEventHook {

	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load e) {
		if(e.world.provider.dimensionId == 0 || e.world.provider.dimensionId == -1)
		{
			Stellarium.proxy.setSkyRenderer(e.world.provider);
		}
	}
	
	@SubscribeEvent
	public void onWorldSave(WorldEvent.Save e) {
		ConfigDataPhysicalManager.getManager(Side.SERVER).saveData(DimensionManager.getCurrentSaveRootDirectory());
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onFOVUpdate(FOVUpdateEvent e) {
		
	}
	
}
