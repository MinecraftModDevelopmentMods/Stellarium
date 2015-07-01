package stellarium.view;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class StellarViewProvider implements IViewProvider {
	
	@Override
	public ViewPoint provideViewPoint(World world, int x, int y, int z) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IViewer getViewer(Entity entity) {
		EntityViewData data = (EntityViewData) entity.getExtendedProperties(EntityViewData.DATA_ID);
		
		if(data == null) {
			data = new EntityViewData(entity);
			entity.registerExtendedProperties(EntityViewData.DATA_ID, data);
		}
		
		return data;
	}

}
