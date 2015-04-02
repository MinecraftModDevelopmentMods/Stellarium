package stellarium.config.gui.entry;

import java.util.List;

import cpw.mods.fml.client.config.HoverChecker;
import net.minecraft.client.renderer.Tessellator;
import stellarium.config.element.IPropElement;
import stellarium.config.gui.ConfigGuiUtil;
import stellarium.config.gui.GuiConfigCatEntry;
import stellarium.config.gui.GuiPropertyHandler;
import stellarium.config.gui.GuiStellarConfig;
import stellarium.lang.CPropLangUtil;

public class GuiCfgPropLabel {
	
	private static final int SPACING = 3;
	
	protected GuiStellarConfig screen;
	protected GuiPropertyHandler property;
	
	private int xLabel, maxLabelWidth;
	private String label;
	
	private HoverChecker tooltipChecker;
	
	public GuiCfgPropLabel(String labelId) {
		this.label = CPropLangUtil.getLocalizedFromID(labelId);
	}

	public void setup(GuiStellarConfig screen, GuiConfigCatEntry list,
			GuiPropertyHandler property, int xLabel, int xContent) {
		this.screen = screen;
		this.property = property;
		
		this.xLabel = xLabel;
		this.maxLabelWidth = xContent - xLabel - SPACING;
	}

	public String getLabel() {
		return label;
	}

	public void setEnabled(boolean enable) { }

	public void mouseClicked(int mouseX, int mouseY, int mouseEvent) { }

	public void keyTyped(char eventChar, int eventKey) { }
	
	public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight,
			Tessellator tessellator, int mouseX, int mouseY, boolean isSelected) {
		
		String labelPrint = this.label;
		
        int strWidth = screen.getFontRenderer().getStringWidth(labelPrint);
        int ellipsisWidth = screen.getFontRenderer().getStringWidth("...");
        
        if (strWidth > this.maxLabelWidth - 6 && strWidth > ellipsisWidth)
        	labelPrint = screen.getFontRenderer().trimStringToWidth(labelPrint, this.maxLabelWidth - 6 - ellipsisWidth).trim() + "...";
        
        screen.drawCenteredString(screen.getFontRenderer(), labelPrint, this.xLabel + this.maxLabelWidth / 2, y + SPACING, 0xffffff);

        if (this.tooltipChecker == null)
            this.tooltipChecker = new HoverChecker(y, y + slotHeight, this.xLabel, this.xLabel + this.maxLabelWidth, ConfigGuiUtil.TOOLTIP_TIMING);
        else this.tooltipChecker.updateBounds(y, y + slotHeight, this.xLabel, this.xLabel + this.maxLabelWidth);
	}

	public boolean mousePressed(int index, int mouseX, int mouseY, int mouseEvent,
			int relativeX, int relativeY) { return false; }

	public void mouseReleased(int index, int mouseX, int mouseY, int mouseEvent,
			int relativeX, int relativeY) { }

	public void drawToolTip(int mouseX, int mouseY, float partialTicks) {
		if(this.tooltipChecker != null && tooltipChecker.checkHover(mouseX, mouseY))
		{
			String toolTip = CPropLangUtil.getLocalizedString(property.getExpl());
			List<String> stringList = screen.getFontRenderer().listFormattedStringToWidth(toolTip, 300);
			screen.drawToolTip(stringList, mouseX, mouseY);
		}
	}
	
	public void onGuiClosed() { }
	
}
