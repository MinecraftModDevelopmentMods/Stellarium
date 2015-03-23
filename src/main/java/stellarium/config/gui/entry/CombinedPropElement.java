package stellarium.config.gui.entry;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import stellarium.config.element.IPropElement;
import stellarium.config.gui.ConfigGuiUtil;
import stellarium.config.gui.GuiConfigCatEntry;
import stellarium.config.gui.GuiPropertyHandler;
import stellarium.config.gui.GuiStellarConfig;
import stellarium.lang.CPropLangUtil;
import cpw.mods.fml.client.config.GuiButtonExt;
import cpw.mods.fml.client.config.HoverChecker;

public class CombinedPropElement implements IGuiCfgPropElement {

	private static int VER_SIZE= 18;
	private static int SPACING = 2;
	private static int TOOLTIP_TIMING = 400;
	
	private GuiStellarConfig screen;
	private GuiPropertyHandler property;
	
	private GuiCfgPropLabel label;
	private IGuiCfgPropContent content;
	private IGuiCfgPropContent headerContent;
	
	private boolean isSubContent;
	
	private int xLabel;
	
	public CombinedPropElement(String label, IGuiCfgPropContent content, boolean isHeader) {
		this.label = new GuiCfgPropLabel(label);
		this.content = content;
		this.headerContent = (isHeader? new HeaderContent() : new NullContent());
		this.isSubContent = !isHeader;
	}

	@Override
	public void setup(GuiStellarConfig screen, GuiConfigCatEntry list,
			GuiPropertyHandler property, int xLabel, int xContent) {
		this.screen = screen;
		this.property = property;
		this.xLabel = xLabel;
		
		label.setup(screen, list, property, xLabel, xContent);
		content.setup(screen, list, property, xContent);
		headerContent.setup(screen, list, property, xContent);
	}

	@Override
	public String getLabel() {
		return label.getLabel();
	}
	
	@Override
	public boolean hasNoContent() {
		return content.isNull();
	}

	@Override
	public boolean isSubContent() {
		return this.isSubContent;
	}

	@Override
	public void setEnabled(boolean enable) {
		label.setEnabled(enable);
		content.setEnabled(enable);
		headerContent.setEnabled(enable);
	}

	@Override
	public void onValueUpdate(IPropElement element) {
		content.onValueUpdate(element);
		headerContent.onValueUpdate(element);
	}

	@Override
	public void updateCursorCounter() {
		content.updateCursorCounter();
		headerContent.updateCursorCounter();
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseEvent) {
		label.mouseClicked(mouseX, mouseY, mouseEvent);
		content.mouseClicked(mouseX, mouseY, mouseEvent);
		headerContent.mouseClicked(mouseX, mouseY, mouseEvent);
	}

	@Override
	public void keyTyped(char eventChar, int eventKey) {
		label.keyTyped(eventChar, eventKey);
		content.keyTyped(eventChar, eventKey);
		headerContent.keyTyped(eventChar, eventKey);
	}
	
	@Override
	public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight,
			Tessellator tessellator, int mouseX, int mouseY, boolean isSelected) {
		label.drawEntry(slotIndex, x, y, listWidth, slotHeight, tessellator,
				mouseX, mouseY, isSelected);
		content.drawEntry(slotIndex, x, y, listWidth, slotHeight, tessellator,
				mouseX, mouseY, isSelected);
		headerContent.drawEntry(slotIndex, x, y, listWidth, slotHeight, tessellator,
				mouseX, mouseY, isSelected);
	}
	
	@Override
	public void drawToolTip(int mouseX, int mouseY, float partialTicks) {

		label.drawToolTip(mouseX, mouseY, partialTicks);
		headerContent.drawToolTip(mouseX, mouseY, partialTicks);
		content.drawToolTip(mouseX, mouseY, partialTicks);
	}

	@Override
	public boolean mousePressed(int index, int mouseX, int mouseY, int mouseEvent,
			int relativeX, int relativeY) {
		if(label.mousePressed(index, mouseX, mouseY, mouseEvent, relativeX, relativeY))
			return true;
		if(content.mousePressed(index, mouseX, mouseY, mouseEvent, relativeX, relativeY))
			return true;
		return headerContent.mousePressed(index, mouseX, mouseY, mouseEvent, relativeX, relativeY);
	}

	@Override
	public void mouseReleased(int index, int mouseX, int mouseY, int mouseEvent,
			int relativeX, int relativeY) {
		label.mouseReleased(index, mouseX, mouseY, mouseEvent, relativeX, relativeY);
		content.mouseReleased(index, mouseX, mouseY, mouseEvent, relativeX, relativeY);
		headerContent.mouseReleased(index, mouseX, mouseY, mouseEvent, relativeX, relativeY);
	}

	@Override
	public void onGuiClosed() {
		label.onGuiClosed();
		content.onGuiClosed();
	}

}
