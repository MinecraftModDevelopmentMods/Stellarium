package stellarium.config.gui.entry;

import cpw.mods.fml.client.config.GuiButtonExt;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.Tessellator;
import stellarium.config.core.StellarConfigProperty;
import stellarium.config.element.IEnumElement;
import stellarium.config.element.IPropElement;
import stellarium.config.gui.ConfigGuiUtil;
import stellarium.config.gui.GuiConfigCatEntry;
import stellarium.config.gui.GuiPropertyHandler;
import stellarium.config.gui.GuiStellarConfig;
import stellarium.lang.CPropLangUtil;

public class EnumContent implements IGuiCfgPropContent {

	private final IEnumElement theElement;
    private final StellarConfigProperty property;
    
    private GuiButtonExt buttonChange;
    private boolean isEnabled;
    
    private Minecraft mc;
    private int scrWidth;
	
	public EnumContent(StellarConfigProperty property, IEnumElement element) {
		this.theElement = element;
		this.property = property;
		this.isEnabled = property.isEnabled();
	}

	@Override
	public void setup(GuiStellarConfig screen, GuiConfigCatEntry list,
			GuiPropertyHandler property, int xContent) {
		this.mc = screen.mc;
		this.scrWidth = screen.width;
		
		String localized = CPropLangUtil.getLocalizedFromID(theElement.getValue());
		this.buttonChange = new GuiButtonExt(0, xContent, 0, this.getContentWidth(), ConfigGuiUtil.VER_SIZE, localized);
	}

	@Override
	public int getContentWidth() {
		return ConfigGuiUtil.getContentWidth(scrWidth);
	}

	@Override
	public boolean isNull() {
		return false;
	}

	@Override
	public void setEnabled(boolean enable) {
		this.isEnabled = enable;
	}

	@Override
	public void onValueUpdate(IPropElement element) {
		buttonChange.displayString = CPropLangUtil.getLocalizedFromID(theElement.getValue());
	}

	@Override
	public void updateCursorCounter() {	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseEvent) { }

	@Override
	public void keyTyped(char eventChar, int eventKey) { }

	@Override
	public boolean mousePressed(int index, int mouseX, int mouseY,
			int mouseEvent, int relativeX, int relativeY) {
		if(this.isEnabled && buttonChange.mousePressed(mc, mouseX, mouseY)) {
			theElement.setValue(theElement.getIndex() + 1);
			property.updateValue();
			this.onValueUpdate(theElement);
			return true;
		}
		return false;
	}

	@Override
	public void mouseReleased(int index, int mouseX, int mouseY,
			int mouseEvent, int relativeX, int relativeY) {
		buttonChange.mouseReleased(mouseX, mouseY);
	}

	@Override
	public void drawEntry(int slotIndex, int x, int y, int listWidth,
			int slotHeight, Tessellator tessellator, int mouseX, int mouseY,
			boolean isSelected) {
		buttonChange.enabled = this.isEnabled;
		buttonChange.yPosition = y;
		buttonChange.drawButton(mc, mouseX, mouseY);
	}

	@Override
	public void onGuiClosed() { }

	@Override
	public void drawToolTip(int mouseX, int mouseY, float partialTicks) { }

}
