package stellarium.config.gui.entry;

import net.minecraft.client.renderer.Tessellator;
import stellarium.config.element.IPropElement;
import stellarium.config.element.IStringElement;
import stellarium.config.gui.GuiConfigCatEntry;
import stellarium.config.gui.GuiPropertyHandler;
import stellarium.config.gui.GuiStellarConfig;

public class StringContent implements IGuiCfgPropContent {

	public StringContent(IStringElement element) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setup(GuiStellarConfig screen, GuiConfigCatEntry list,
			GuiPropertyHandler property, int xContent) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getContentWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isNull() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setEnabled(boolean enable) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onValueUpdate(IPropElement element) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateCursorCounter() {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseEvent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(char eventChar, int eventKey) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean mousePressed(int index, int mouseX, int mouseY,
			int mouseEvent, int relativeX, int relativeY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void mouseReleased(int index, int mouseX, int mouseY,
			int mouseEvent, int relativeX, int relativeY) {
		// TODO Auto-generated method stub

	}

	@Override
	public void drawEntry(int slotIndex, int x, int y, int listWidth,
			int slotHeight, Tessellator tessellator, int mouseX, int mouseY,
			boolean isSelected) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGuiClosed() {
		// TODO Auto-generated method stub

	}

	@Override
	public void drawToolTip(int mouseX, int mouseY, float partialTicks) {
		// TODO Auto-generated method stub
		
	}

}
