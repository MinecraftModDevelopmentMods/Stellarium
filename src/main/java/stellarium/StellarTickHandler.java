package stellarium;

import java.util.EnumSet;

import stellarium.stellars.StellarManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;

public class StellarTickHandler {

	@SubscribeEvent
	public void tickStart(TickEvent.WorldTickEvent e) {
		if(e.phase == Phase.START){
			World world = e.world;
			StellarManager.Update(world.getWorldTime(), world.provider.isSurfaceWorld());
		}
	}

}
