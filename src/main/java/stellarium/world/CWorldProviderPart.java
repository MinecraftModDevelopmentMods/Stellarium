package stellarium.world;

import net.minecraft.util.Vec3;
import net.minecraft.world.WorldProvider;

public class CWorldProviderPart extends WorldProvider implements IWorldDomain<Vec3, IWorldPos> {

	@Override
	public boolean isInDomain(IWorldPos pos) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Vec3 getLocalPos(IWorldPos pos) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IWorldPos getGlobalPos(Vec3 pos) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDimensionName() {
		// TODO Auto-generated method stub
		return null;
	}

}
