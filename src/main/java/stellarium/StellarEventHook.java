package stellarium;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.world.WorldEvent;

public class StellarEventHook {

	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load e)
	{
		if(e.world.provider.isSurfaceWorld())
		{
			e.world.provider.setSkyRenderer(new DrawSky());
		}
	}
	
	/*@SubscribeEvent
	public void onRenderPlayer(RenderPlayerEvent.Pre event)
	{
		System.out.print("C");
	}*/
	
}
