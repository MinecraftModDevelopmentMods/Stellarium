package stellarium.config.gui.entry;

import net.minecraft.client.renderer.Tessellator;
import stellarium.config.element.IPropElement;
import stellarium.config.gui.GuiConfigCatEntry;
import stellarium.config.gui.GuiPropertyHandler;
import stellarium.config.gui.GuiStellarConfig;

public class NullContent implements IGuiCfgPropContent {

	@Override
	public void setup(GuiStellarConfig screen, GuiConfigCatEntry list,
			GuiPropertyHandler property, int xContent) { }

	@Override
	public int getContentWidth() {
		return 0;
	}

	@Override
	public boolean isNull() {
		return true;
	}

	@Override
	public void setEnabled(boolean enable) { }

	@Override
	public void onValueUpdate(IPropElement element) { }

	@Override
	public void updateCursorCounter() { }

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseEvent) { }

	@Override
	public void keyTyped(char eventChar, int eventKey) { }

	@Override
	public boolean mousePressed(int index, int mouseX, int mouseY,
			int mouseEvent, int relativeX, int relativeY) { return false; }

	@Override
	public void mouseReleased(int index, int mouseX, int mouseY,
			int mouseEvent, int relativeX, int relativeY) { }

	@Override
	public void drawEntry(int slotIndex, int x, int y, int listWidth,
			int slotHeight, Tessellator tessellator, int mouseX, int mouseY,
			boolean isSelected) { }

	@Override
	public void onGuiClosed() { }

	@Override
	public void drawToolTip(int mouseX, int mouseY, float partialTicks) {
		// TODO Auto-generated method stub
		
	}

}
