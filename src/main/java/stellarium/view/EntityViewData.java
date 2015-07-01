package stellarium.view;

import sciapi.api.value.euclidian.ECoord;
import stellarium.mech.OpFilter;
import stellarium.util.math.SpCoord;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class EntityViewData implements IViewer, IExtendedEntityProperties {
	
	public static String DATA_ID = "c_viewdata";
	
	private Entity entity;
	
	public EntityViewData(Entity entity) {
		this.entity = entity;
	}

	@Override
	public void init(Entity entity, World world) { }

	@Override
	public void saveNBTData(NBTTagCompound compound) {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		// TODO Auto-generated method stub

	}

	@Override
	public ViewPoint getViewPoint() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IScope getScope() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OpFilter getFilter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ECoord getViewCoord() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SpCoord getViewPos() {
		// TODO Auto-generated method stub
		return null;
	}

}
