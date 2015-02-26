package stellarium.config.gui.gui;

import net.minecraft.client.gui.GuiScreen;

public interface IGuiCfgProvider {
	
	public GuiScreen getCfgGui(GuiScreen parScreen);
	
	public String getUnlocalizedName();

}
