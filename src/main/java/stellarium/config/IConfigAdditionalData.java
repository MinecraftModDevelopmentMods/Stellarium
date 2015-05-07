package stellarium.config;

import net.minecraft.nbt.NBTTagCompound;

public interface IConfigAdditionalData {

	/**
	 * Reads data from nbt compound.
	 * @param comp the nbt compound to read
	 * */
	public void fromNBT(NBTTagCompound comp);

	/**
	 * Writes data to nbt compound.
	 * @param buf the nbt compound to write
	 * */
	public void toNBT(NBTTagCompound comp);

}
