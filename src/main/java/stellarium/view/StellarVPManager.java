package stellarium.view;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

import com.google.common.collect.Lists;

public class StellarVPManager implements IViewProvider {
	
	public List<IViewProvider> providers = Lists.newArrayList();
	
	/**
	 * Registers specific viewpoint provider.
	 * @param provider the provider to register
	 * @param onHead when it is <code>true</code>, register the provider on first priority
	 * */
	public void registerViewPointProvider(IViewProvider provider, boolean onHead) {
		if(onHead)
			providers.add(0, provider);
		else providers.add(provider);
	}

	
	@Override
	public ViewPoint provideViewPoint(World world, int x, int y, int z) {
		for(IViewProvider provider : providers) {
			ViewPoint vp = provider.provideViewPoint(world, x, y, z);
			if(vp != null)
				return vp;
		}
		
		return null;
	}

	@Override
	public IViewer getViewer(Entity entity) {
		for(IViewProvider provider : providers) {
			IViewer viewer = provider.getViewer(entity);
			if(viewer != null)
				return viewer;
		}
		
		return null;
	}
}
