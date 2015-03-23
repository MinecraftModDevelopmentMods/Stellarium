package stellarium.config.gui.entry;

import net.minecraft.client.renderer.Tessellator;
import stellarium.config.element.IPropElement;
import stellarium.config.gui.GuiConfigCatEntry;
import stellarium.config.gui.GuiPropertyHandler;
import stellarium.config.gui.GuiStellarConfig;
import cpw.mods.fml.client.config.HoverChecker;

public interface IGuiCfgPropContent {

	public void setup(GuiStellarConfig screen, GuiConfigCatEntry list, GuiPropertyHandler property, int xContent);
	
	public int getContentWidth();
	
	public boolean isNull();
		
	public void setEnabled(boolean enable);
	public void onValueUpdate(IPropElement element);
	
    public void updateCursorCounter();
    
	public void mouseClicked(int mouseX, int mouseY, int mouseEvent);
    public void keyTyped(char eventChar, int eventKey);
    
	public boolean mousePressed(int index, int mouseX, int mouseY, int mouseEvent,
			int relativeX, int relativeY);
	public void mouseReleased(int index, int mouseX, int mouseY, int mouseEvent,
			int relativeX, int relativeY);
	
	public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight,
			Tessellator tessellator, int mouseX, int mouseY, boolean isSelected);
	
	public void drawToolTip(int mouseX, int mouseY, float partialTicks);
	
	public void onGuiClosed();
	
}
