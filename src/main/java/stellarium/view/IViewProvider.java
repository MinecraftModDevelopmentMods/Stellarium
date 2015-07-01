package stellarium.view;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public interface IViewProvider {
	
	/**
	 * Provides viewpoint from specific location.
	 * @param world the world
	 * @param x coordinate
	 * @param y coordinate
	 * @param z coordinate
	 * @return provided viewpoint for the location
	 * */
	public ViewPoint provideViewPoint(World world, int x, int y, int z);
	
	/**
	 * Gets viewer for specific entity.
	 * @param entity the entity
	 * @return viewer for the entity
	 * */
	public IViewer getViewer(Entity entity);
}
