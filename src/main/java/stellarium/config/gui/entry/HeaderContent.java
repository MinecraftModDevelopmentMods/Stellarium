package stellarium.config.gui.entry;

import java.util.List;

import cpw.mods.fml.client.config.HoverChecker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import stellarium.config.element.IPropElement;
import stellarium.config.gui.ConfigGuiUtil;
import stellarium.config.gui.GuiConfigCatEntry;
import stellarium.config.gui.GuiConfigCatHandler.GuiPropertyRelation;
import stellarium.config.gui.GuiPropertyHandler;
import stellarium.config.gui.GuiStellarConfig;
import stellarium.gui.config.GuiTexturedButton;
import stellarium.lang.CLangStrs;
import stellarium.lang.CPropLangUtil;

public class HeaderContent implements IGuiCfgPropContent {

	private static int SPACING = 2;
	
	private GuiStellarConfig screen;
	private GuiPropertyHandler property;
	
	private GuiTexturedButton btnLock;
	private HoverChecker btnLockChecker;
	private boolean locked;
	
	private Minecraft mc;
	
	@Override
	public void setup(GuiStellarConfig screen, GuiConfigCatEntry list,
			GuiPropertyHandler property, int xContent) {
		this.screen = screen;
		this.mc = screen.mc;
		this.property = property;
		this.btnLock = new GuiTexturedButton(0, xContent + SPACING, 0, ConfigGuiUtil.VER_SIZE, ConfigGuiUtil.VER_SIZE, ConfigGuiUtil.ICON_UNLOCK);
		btnLock.setButtonBackgroundEnabled(2, false);
		this.btnLockChecker = new HoverChecker(btnLock, ConfigGuiUtil.TOOLTIP_TIMING);
		
		this.setEnabled(property.getProperty().isEnabled());
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
		
		if(btnLock != null)
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
			property.getProperty().simSetEnabled(this.locked); //locked != enabled.
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
		
		mc.getTextureManager().bindTexture(ConfigGuiUtil.ICON_PROPREL);
		tessellator.startDrawingQuads();
		int px = btnLock.xPosition + btnLock.width + SPACING;
		for(GuiPropertyRelation relation : property.getPropertyRelation()) {
			this.drawPropRel(relation, px, y + ConfigGuiUtil.VER_SIZE / 4, ConfigGuiUtil.VER_SIZE / 2, ConfigGuiUtil.VER_SIZE / 2, tessellator);
			px += (ConfigGuiUtil.VER_SIZE / 2 + SPACING);
		}
		tessellator.draw();
	}
	
	private void drawPropRel(GuiPropertyRelation relation, int x, int y, int width, int height,
			Tessellator tessellator) {
		if(relation.getTooltipChecker() == null)
			relation.setTooltipChecker(new HoverChecker(y, y + height, x, x + width, ConfigGuiUtil.TOOLTIP_TIMING));
		else relation.getTooltipChecker().updateBounds(y, y + height, x, x + width);
		
		tessellator.setColorOpaque_I(relation.getColor());
		tessellator.addVertexWithUV(x, y + height, screen.getZLevel(), 0.0, 1.0);
		tessellator.addVertexWithUV(x + width, y + height, screen.getZLevel(), 1.0, 1.0);
		tessellator.addVertexWithUV(x + width, y, screen.getZLevel(), 1.0, 0.0);
		tessellator.addVertexWithUV(x, y, screen.getZLevel(), 0.0, 0.0);
	}

	@Override
	public void drawToolTip(int mouseX, int mouseY, float partialTicks) {
		if(btnLockChecker.checkHover(mouseX, mouseY)) {
			List<String> strs = screen.getFontRenderer().listFormattedStringToWidth(CPropLangUtil.getLocalizedString(CLangStrs.LockBtnToolTip), 300);
			screen.drawToolTip(strs, mouseX, mouseY);
		}
		
		for(GuiPropertyRelation relation : property.getPropertyRelation()) {
			if(relation.getTooltipChecker() != null && relation.getTooltipChecker().checkHover(mouseX, mouseY))
			{
				List<String> strs = screen.getFontRenderer().listFormattedStringToWidth(relation.getLocalizedTooltipString(), 300);
				screen.drawToolTip(strs, mouseX, mouseY);
			}
		}
	}

	@Override
	public void onGuiClosed() { }

}
