package stellarium.catalog;

import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import stellarium.config.IConfigAdditionalData;

public class CCatalogAdditionalData implements IConfigAdditionalData {

	private String selected = "";
	
	public CCatalogAdditionalData() { }
	
	public CCatalogAdditionalData(String selected) {
		this.selected = selected;
	}

	public String getContext() {
		return this.selected;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.selected = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, this.selected);
	}

}
