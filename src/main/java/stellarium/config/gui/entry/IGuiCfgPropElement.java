package stellarium.config.gui.entry;

import java.util.List;

import stellarium.config.core.StellarConfigProperty;
import stellarium.config.element.IPropElement;
import stellarium.config.gui.GuiConfigCatEntry;
import stellarium.config.gui.GuiPropertyHandler;
import stellarium.config.gui.GuiStellarConfig;
import cpw.mods.fml.client.config.HoverChecker;
import net.minecraft.client.gui.GuiListExtended;

public interface IGuiCfgPropElement extends GuiListExtended.IGuiListEntry {
	
	public void setup(GuiStellarConfig screen, GuiConfigCatEntry list, GuiPropertyHandler property,
			int xLabel, int xContent);
	
	public String getLabel();
	
	public boolean hasNoContent();
	public boolean isSubContent();
		
	public void setEnabled(boolean enable);
	public void onValueUpdate(IPropElement element);
	
    public void updateCursorCounter();
    
	public void mouseClicked(int x, int y, int mouseevent);
    public void keyTyped(char eventChar, int eventKey);

	public void drawToolTip(int mouseX, int mouseY, float partialTicks);
    
	public void onGuiClosed();
    
}
