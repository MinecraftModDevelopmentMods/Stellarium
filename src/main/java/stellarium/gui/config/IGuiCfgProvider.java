package stellarium.gui.config;

import net.minecraft.client.gui.GuiScreen;

public interface IGuiCfgProvider {
	
	public GuiScreen getCfgGui(GuiStellarConfigMain parScreen);
	
	public String getUnlocalizedName();

}
