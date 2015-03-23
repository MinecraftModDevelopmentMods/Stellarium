package stellarium.config.gui.entry;

import java.util.List;

import cpw.mods.fml.client.config.HoverChecker;
import net.minecraft.client.renderer.Tessellator;
import stellarium.config.element.IPropElement;
import stellarium.config.gui.ConfigGuiUtil;
import stellarium.config.gui.GuiConfigCatEntry;
import stellarium.config.gui.GuiPropertyHandler;
import stellarium.config.gui.GuiStellarConfig;
import stellarium.lang.CLangStrs;
import stellarium.lang.CPropLangUtil;

public class HeaderContent implements IGuiCfgPropContent {

	private static int VER_SIZE = 18;
	private static int SPACING = 2;
	private static int TOOLTIP_TIMING = 400;
	
	private GuiStellarConfig screen;
	private GuiPropertyHandler property;
	
	private GuiTexturedButton btnLock;
	private HoverChecker btnLockChecker;
	private boolean locked;
	
	@Override
	public void setup(GuiStellarConfig screen, GuiConfigCatEntry list,
			GuiPropertyHandler property, int xContent) {
		this.screen = screen;
		this.property = property;
		this.btnLock = new GuiTexturedButton(0, xContent + SPACING, 0, VER_SIZE, VER_SIZE, ConfigGuiUtil.ICON_UNLOCK);
		btnLock.setButtonBackgroundEnabled(2, false);
		this.btnLockChecker = new HoverChecker(btnLock, TOOLTIP_TIMING);
	}

	@Override
	public int getContentWidth() {
		return 0;
	}

	@Override
	public boolean isNull() {
		return false;
	}

	@Override
	public void setEnabled(boolean enable) {
		this.locked = !enable;
		btnLock.resLocation = this.locked? ConfigGuiUtil.ICON_LOCK : ConfigGuiUtil.ICON_UNLOCK;
	}

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
			int mouseEvent, int relativeX, int relativeY) {
		if(btnLock.mousePressed(screen.mc, mouseX, mouseY))
		{
			property.getProperty().setEnabled(this.locked); //locked != enabled.
			return true;
		}
		return false;
	}

	@Override
	public void mouseReleased(int index, int mouseX, int mouseY,
			int mouseEvent, int relativeX, int relativeY) {
		btnLock.mouseReleased(mouseX, mouseY);
	}

	@Override
	public void drawEntry(int slotIndex, int x, int y, int listWidth,
			int slotHeight, Tessellator tessellator, int mouseX, int mouseY,
			boolean isSelected) {
		btnLock.yPosition = y;
		btnLock.drawButton(screen.mc, mouseX, mouseY);
	}
	
	@Override
	public void drawToolTip(int mouseX, int mouseY, float partialTicks) {
		if(btnLockChecker.checkHover(mouseX, mouseY)) {
			List<String> strs = screen.getFontRenderer().listFormattedStringToWidth(CPropLangUtil.getLocalizedString(CLangStrs.LockBtnToolTip), 300);
			screen.drawToolTip(strs, mouseX, mouseY);
		}
	}

	@Override
	public void onGuiClosed() { }

}
