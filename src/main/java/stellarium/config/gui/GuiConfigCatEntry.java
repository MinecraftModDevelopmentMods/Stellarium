package stellarium.config.gui;

import java.util.List;

import stellarium.config.core.StellarConfigCategory;
import stellarium.config.core.StellarConfigCategory.PropertyRelation;
import stellarium.config.core.StellarConfigProperty;
import stellarium.config.core.handler.ICategoryHandler;
import stellarium.config.core.handler.IPropertyHandler;
import stellarium.config.gui.entry.IGuiCfgPropElement;

import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.renderer.Tessellator;

public class GuiConfigCatEntry extends GuiListExtended {
	
	protected GuiStellarConfig screen;
	protected int offsetX;
	protected static final int slotHeight = 20;
	private static final int SPACING = 6;
	private static final int MAX_LABEL_WIDTH = 200;
	
	protected int labelX;
	protected int sublabelX;
	
	protected StellarConfigCategory category;
	private GuiConfigCatHandler catHandler;
	
	public GuiConfigCatEntry(GuiStellarConfig parent, Minecraft mc, int offsetX) {
		super(mc, parent.width, parent.height, 23, parent.height - 32, slotHeight);
		this.screen = parent;
		this.offsetX = offsetX;
		
		this.setShowSelectionBox(false);
		
		labelX = offsetX + 10;
		sublabelX = labelX + 10;
		
        this.setHasListHeader(true, 20);
	}
	
	@Override
	public int getListWidth() {
		return this.width - SPACING;
	}
	
	@Override
    protected int getScrollBarX() {
        return this.width - SPACING;
    }
	
	public int getLabelX() {
		return this.labelX;
	}
	
	public int getSubLabelX() {
		return this.sublabelX;
	}
	
	public int getContentX() {
		
		if(catHandler == null)
			return 0;
		
		int maxLabelWidth = 0;
		
		for(IGuiCfgPropElement element : this.catHandler)
		{
			if(!element.isSubContent())
				maxLabelWidth = Math.max(maxLabelWidth, screen.getFontRenderer().getStringWidth(element.getLabel()));
		}
		
		maxLabelWidth = Math.min(maxLabelWidth, MAX_LABEL_WIDTH);
		
		return this.labelX + SPACING + maxLabelWidth + 6;
	}
	
	public int getSubContentX() {
		
		if(catHandler == null)
			return 0;
		
		int maxLabelWidth = 0;
		
		for(IGuiCfgPropElement element : this.catHandler)
		{
			if(element.isSubContent())
				maxLabelWidth = Math.max(maxLabelWidth, screen.getFontRenderer().getStringWidth(element.getLabel()));
		}
		
		maxLabelWidth = Math.min(maxLabelWidth, MAX_LABEL_WIDTH);
		
		return this.labelX + SPACING + maxLabelWidth;
	}
	
	public void setCategory(StellarConfigCategory category, GuiConfigCatHandler catHandler)
	{
		this.category = category;
		this.catHandler = catHandler;
		
		this.initGui();
	}
	
	public void initGui() {
        this.width = screen.width;
        this.height = screen.height;
        this.top = 23;
        this.bottom = screen.height - 32;
        this.left = 0;
        this.right = screen.width;

        if(catHandler != null)
        	catHandler.onSetup(this);
        
	}
	
	public void onGuiClosed() {
		if(catHandler == null)
			return;
		
		for(IGuiCfgPropElement element : this.catHandler)
			element.onGuiClosed();
	}
	

	public void mouseClicked(int x, int y, int mouseEvent) {
		if(catHandler == null)
			return;
		
		for(IGuiCfgPropElement element : this.catHandler)
			element.mouseClicked(x, y, mouseEvent);
	}
	
	public void onKeyPressed(char eventChar, int eventKey) {
		if(catHandler == null)
			return;
		
		for(IGuiCfgPropElement element : this.catHandler)
			element.keyTyped(eventChar, eventKey);
	}
	
	public void updateScreen() {
		if(catHandler == null)
			return;
		
		for(IGuiCfgPropElement element : this.catHandler)
			element.updateCursorCounter();
	}

	
	@Override
	public IGuiListEntry getListEntry(int index) {
		if(catHandler == null)
			return null;
		
		return catHandler.getElement(index);
	}

	@Override
	protected int getSize() {
		if(catHandler == null)
			return 0;
		
		return catHandler.getElementSize();
	}

	public void drawToolTip(int mouseX, int mouseY, float partialTicks) {
		if(catHandler == null)
			return;
		
		for(IGuiCfgPropElement element : this.catHandler)
			element.drawToolTip(mouseX, mouseY, partialTicks);
	}
	
}
