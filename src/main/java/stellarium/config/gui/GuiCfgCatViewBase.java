package stellarium.config.gui;

import org.lwjgl.opengl.GL11;

import stellarium.config.core.ICategoryEntry;
import stellarium.config.core.StellarConfigCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;
import cpw.mods.fml.client.GuiScrollingList;

public abstract class GuiCfgCatViewBase extends GuiScrollingList {
	
	protected static final int slotHeight = 15;
	protected static final int SPACING = 2;
	
	protected GuiStellarConfig parent;
	protected GuiConfigCatList parList;
	
	protected Minecraft mc;

	public GuiCfgCatViewBase(Minecraft mc, GuiStellarConfig parent, GuiConfigCatList parList, int width, int offsetX) {
		super(mc, width, parent.height, 23, parent.height - 32, offsetX, slotHeight);
		
		this.parent = parent;
		this.parList = parList;
		
		this.mc = mc;
	}
	
	public abstract void onReset();
	public abstract void onCreateCategory(StellarConfigCategory cat);
	public abstract void onRemoveCategory(StellarConfigCategory cat);
	public abstract void onMigrateCategory(StellarConfigCategory cat, ICategoryEntry before);
	
	
	@Override
	protected void elementClicked(int index, boolean doubleClick) {
		if(isSelected(index))
			parList.onSelectChange(null);
		else parList.onSelectChange(this.getCategory(index));
	}
	
	@Override
	protected boolean isSelected(int index) {
		StellarConfigCategory category = this.getCategory(index);
		StellarConfigCategory selectedCategory = parList.selectedCategory;
		
		if(category == null)
			return false;
		if(selectedCategory == null)
			return false;
		
		return category.getCategoryEntry().equals(selectedCategory.getCategoryEntry());
	}
	
	protected abstract StellarConfigCategory getCategory(int index);

	@Override
	protected int getContentHeight()
    {
		return this.getSize() * this.slotHeight + 1;
    }
	
	@Override
	protected void drawBackground() {
		if(this.getContentHeight() - (this.bottom - this.top - 4) <= 0)
		{
	        int scrollBarXStart = this.left + this.listWidth;
	        int scrollBarXEnd = scrollBarXStart + 6;
			
	        GL11.glEnable(GL11.GL_BLEND);
	        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	        GL11.glDisable(GL11.GL_ALPHA_TEST);
	        GL11.glShadeModel(GL11.GL_SMOOTH);
	        GL11.glDisable(GL11.GL_TEXTURE_2D);
	        
			Tessellator tessellator = Tessellator.instance;
			tessellator.startDrawingQuads();
			tessellator.setColorRGBA_I(0, 255);
			tessellator.addVertexWithUV((double)scrollBarXStart, (double)this.bottom, 0.0D, 0.0D, 1.0D);
			tessellator.addVertexWithUV((double)scrollBarXEnd, (double)this.bottom, 0.0D, 1.0D, 1.0D);
			tessellator.addVertexWithUV((double)scrollBarXEnd, (double)this.top, 0.0D, 1.0D, 0.0D);
			tessellator.addVertexWithUV((double)scrollBarXStart, (double)this.top, 0.0D, 0.0D, 0.0D);
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setColorRGBA_I(8421504, 255);
			tessellator.addVertexWithUV((double)scrollBarXStart, (double)this.bottom, 0.0D, 0.0D, 1.0D);
			tessellator.addVertexWithUV((double)scrollBarXEnd, (double)this.bottom, 0.0D, 1.0D, 1.0D);
			tessellator.addVertexWithUV((double)scrollBarXEnd, (double)this.top, 0.0D, 1.0D, 0.0D);
			tessellator.addVertexWithUV((double)scrollBarXStart, (double)this.top, 0.0D, 0.0D, 0.0D);
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setColorRGBA_I(12632256, 255);
			tessellator.addVertexWithUV((double)scrollBarXStart, (double)(this.bottom - 1), 0.0D, 0.0D, 1.0D);
			tessellator.addVertexWithUV((double)(scrollBarXEnd - 1), (double)(this.bottom - 1), 0.0D, 1.0D, 1.0D);
			tessellator.addVertexWithUV((double)(scrollBarXEnd - 1), (double)this.top, 0.0D, 1.0D, 0.0D);
			tessellator.addVertexWithUV((double)scrollBarXStart, (double)this.top, 0.0D, 0.0D, 0.0D);
			tessellator.draw();
			
	        GL11.glEnable(GL11.GL_TEXTURE_2D);
	        GL11.glShadeModel(GL11.GL_FLAT);
	        GL11.glEnable(GL11.GL_ALPHA_TEST);
	        GL11.glDisable(GL11.GL_BLEND);
		}
	}
}
