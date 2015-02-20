package stellarium.config.gui;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.config.GuiButtonExt;
import cpw.mods.fml.client.config.GuiCheckBox;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.config.GuiUnicodeGlyphButton;
import cpw.mods.fml.client.config.HoverChecker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class StellarConfigGui extends GuiScreen {
	
	private GuiScreen parentScreen;

	public StellarConfigGui(GuiScreen parentScreen)
	{
		this.mc = Minecraft.getMinecraft();
        this.parentScreen = parentScreen;
	}
	
    @Override
    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);

        
        
    }

}
