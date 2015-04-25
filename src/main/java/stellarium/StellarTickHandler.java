package stellarium;

import java.util.EnumSet;

import stellarium.settings.StellarSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.relauncher.Side;

public class StellarTickHandler {
	
	private Side side;
	
	public StellarTickHandler(Side pside)
	{
		this.side = pside;
	}

	@SubscribeEvent
	public void tickStart(TickEvent.WorldTickEvent e) {
		if(e.phase == Phase.START && side == Side.SERVER){
			World world = e.world;
			StellarSettings.Update(world.getWorldTime(), world.provider.isSurfaceWorld());
		}
	}
	
	@SubscribeEvent
	public void tickStart(TickEvent.ClientTickEvent e) {
		if(e.phase == Phase.START){
			World world = Stellarium.proxy.getDefWorld();
			
			if(world != null)
				StellarSettings.Update(world.getWorldTime(), world.provider.isSurfaceWorld());
		}
	}

}
