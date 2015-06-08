package stellarium.detector;

import stellarium.Stellarium;
import stellarium.view.IScope;
import stellarium.view.ViewPoint;
import stellarium.view.Viewer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraftforge.client.IRenderHandler;

public class SkyRendererForEye extends IRenderHandler {

	private DetectorEye eye = new DetectorEye();
	
	@Override
	public void render(float partialTicks, WorldClient world, Minecraft mc) {
		// TODO Auto-generated method stub
		Viewer viewer = Stellarium.instance.getVPManager().getViewer(mc.renderViewEntity);
		eye.render(viewer, world.getWorldTime(), partialTicks, mc);
	}
}
