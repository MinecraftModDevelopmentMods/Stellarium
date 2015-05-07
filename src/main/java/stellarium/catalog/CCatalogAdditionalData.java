package stellarium.catalog;

import net.minecraft.nbt.NBTTagCompound;
import stellarium.config.IConfigAdditionalData;

public class CCatalogAdditionalData implements IConfigAdditionalData {

	private static String TAG = "CATALOG_SET";
	private String selected = "";
	
	public CCatalogAdditionalData() { }
	
	public CCatalogAdditionalData(String selected) {
		this.selected = selected;
	}

	public String getContext() {
		return this.selected;
	}

	@Override
	public void fromNBT(NBTTagCompound comp) {
		selected = comp.getString(TAG);
	}

	@Override
	public void toNBT(NBTTagCompound comp) {
		comp.setString(TAG, this.selected);
	}

}
