package stellarium.config.gui;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import stellarium.config.IConfigCategory;
import stellarium.config.core.EnumPosOption;
import stellarium.config.core.ICategoryEntry;
import stellarium.config.core.StellarConfigCategory;
import stellarium.config.gui.gui.GuiTexturedButton;
import stellarium.lang.CLangStrs;
import stellarium.lang.CPropLangUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import cpw.mods.fml.client.GuiScrollingList;
import cpw.mods.fml.client.config.GuiButtonExt;

public abstract class GuiCfgCatViewBase extends GuiScrollingList implements GuiYesNoCallback {
	
	protected static final int slotHeight = 15;
	protected static final int SPACING = 2;
	
	private static String newCatName = "New Category";
	
	protected GuiStellarConfig parent;
	protected GuiConfigCatList parList;
	
	protected GuiButton btnNew, btnCopy, btnRemove, btnNameChange, btnUp, btnDown;
	
	private int selectedIndex = -1, dragIndex = -2;
	private long dragDuration = 0L;
	
	protected StellarConfigCategory nameChangeCategory = null;
	protected boolean isNameChanging = false;
	
	private boolean canModify = true, warn = false, canDragToChild;
		
	protected Minecraft mc;


	public GuiCfgCatViewBase(Minecraft mc, GuiStellarConfig parent, GuiConfigCatList parList, int width, int offsetX) {
		super(mc, width, parent.height, 23, parent.height - 32, offsetX, slotHeight);
		
		this.parent = parent;
		this.parList = parList;
		
		this.mc = mc;
		
		int topbtn = this.top - ConfigGuiUtil.VER_SIZE - SPACING;
		int bottombtn = this.bottom + SPACING;
		int midx = this.listWidth / 2;
		int halfWidth = (ConfigGuiUtil.VER_SIZE + SPACING)/2;
		int first = midx - halfWidth * 3;
		int second = midx - halfWidth;
		int third = midx + halfWidth;
		
		btnNew = new GuiTexturedButton(0, first, topbtn, ConfigGuiUtil.VER_SIZE, ConfigGuiUtil.VER_SIZE,
				ConfigGuiUtil.ICON_ADD);
		btnCopy = new GuiTexturedButton(1, second, topbtn, ConfigGuiUtil.VER_SIZE, ConfigGuiUtil.VER_SIZE,
				ConfigGuiUtil.ICON_COPY);
		btnRemove = new GuiButtonExt(2, third, topbtn, ConfigGuiUtil.VER_SIZE, ConfigGuiUtil.VER_SIZE,
				ConfigGuiUtil.DELETE_CHAR);
		btnNameChange = new GuiButtonExt(3, first, bottombtn, ConfigGuiUtil.VER_SIZE, ConfigGuiUtil.VER_SIZE,
				ConfigGuiUtil.CHANGE_CHAR);
		btnUp = new GuiButtonExt(3, second, bottombtn, ConfigGuiUtil.VER_SIZE, ConfigGuiUtil.VER_SIZE,
				ConfigGuiUtil.UP_CHAR);
		btnDown = new GuiButtonExt(3, third, bottombtn, ConfigGuiUtil.VER_SIZE, ConfigGuiUtil.VER_SIZE,
				ConfigGuiUtil.DOWN_CHAR);
		
		btnRemove.packedFGColour = 0xff2222;
		btnNameChange.packedFGColour = 0x66ff66;
		
		this.canDragToChild = this.canDragToChild();
		
		if(!parList.modifiable)
		{
			this.canModify = false;
			
			btnNew.visible = false;
			btnCopy.visible = false;
			btnRemove.visible = false;
			btnNameChange.visible = false;
			btnUp.visible = false;
			btnDown.visible = false;
		}
		
		this.warn = parList.warn;
	}
	
	public abstract void onReset();
	public abstract void onCreateCategory(StellarConfigCategory cat);
	public abstract void onRemoveCategory(StellarConfigCategory cat);
	public abstract void onMigrateCategory(StellarConfigCategory cat, ICategoryEntry before);
	public abstract void onNameChange(StellarConfigCategory cat, String before);
	
	@Override
	protected void elementClicked(int index, boolean doubleClick) {
		if(isSelected(index))
		{
			parList.onSelectChange(null);
			this.selectedIndex = -1;
		} else {
			parList.onSelectChange(this.getCategory(index));
			this.selectedIndex = index;
		}
		
		this.dragIndex = index;
		
		this.onChangeSelectedCategory(parList.selectedCategory);
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
	protected int getContentHeight() {
		return this.getSize() * this.slotHeight + 1;
    }
	
	
	private void onChangeSelectedCategory(StellarConfigCategory selectedCategory) {
		if(selectedCategory == null) {
			btnNew.enabled = true;
			btnCopy.enabled = false;
			btnRemove.enabled = false;
			btnUp.enabled = false;
			btnDown.enabled = false;
		} else {
			btnNew.enabled = !selectedCategory.isImmutable();
			btnCopy.enabled = !selectedCategory.isImmutable();
			btnRemove.enabled = !selectedCategory.isImmutable();
			btnUp.enabled = selectedCategory.getCategoryEntry().hasPreviousEntry() && !selectedCategory.isImmutable();
			btnDown.enabled = selectedCategory.getCategoryEntry().hasNextEntry() && !selectedCategory.isImmutable();
		}
		
		if(this.isNameChanging)
			if(this.nameChangeCategory == null)
				btnNameChange.enabled = false;
			else btnNameChange.enabled = !nameChangeCategory.isImmutable();
		else if(selectedCategory == null)
			btnNameChange.enabled = false;
		else btnNameChange.enabled = !selectedCategory.isImmutable();
	}
	
    public void drawScreen(int mouseX, int mouseY, float partialTick) {
    	super.drawScreen(mouseX, mouseY, partialTick);
    	this.drawAdditional(mouseX, mouseY, partialTick);
    }
    
    protected void drawAdditional(int mouseX, int mouseY, float partialTick) {
		btnNew.drawButton(mc, mouseX, mouseY);
		btnCopy.drawButton(mc, mouseX, mouseY);
		btnRemove.drawButton(mc, mouseX, mouseY);
		btnNameChange.drawButton(mc, mouseX, mouseY);
		btnUp.drawButton(mc, mouseX, mouseY);
		btnDown.drawButton(mc, mouseX, mouseY);
    }
    
	public abstract void updateCursorCounter();
	public abstract void keyPressed(char typed, int id);

	public void mousePressed(int x, int y, int mouseEvent) {
		StellarConfigCategory selectedCategory = parList.selectedCategory;
		boolean flag;
		
		if(btnNew.mousePressed(mc, x, y)) {
			if(selectedCategory == null)
				flag = parList.config.getRootEntry().canCreateCategory(newCatName, EnumPosOption.Child);
			else flag = selectedCategory.getCategoryEntry().canCreateCategory(newCatName, EnumPosOption.Child);
			
			if(flag) this.handleWarn(0, CLangStrs.newCategory, CLangStrs.newCategorySub);
		} else if(btnCopy.mousePressed(mc, x, y)) {
			if(selectedCategory == null)
				return;
			String copiedCatName = CPropLangUtil.getLocalizedFromID(selectedCategory.getName()) + "_Copy";
			flag = selectedCategory.getCategoryEntry().canCreateCategory(copiedCatName, EnumPosOption.Next);
			
			if(flag)
				this.handleWarn(1, CLangStrs.copyCategory, CLangStrs.copyCategorySub);			
		} else if(btnRemove.mousePressed(mc, x, y)) {
			if(selectedCategory == null)
				return;
			
			if(selectedCategory.getCategoryEntry().canRemoveCategory())
				this.handleWarn(2, CLangStrs.removeCategory, CLangStrs.removeCategorySub);			
		} else if(btnNameChange.mousePressed(mc, x, y)) {
			if(this.isNameChanging)
			{
				if(this.nameChangeCategory == null)
					 return;
				if(nameChangeCategory.getCategoryEntry().canChangeName(this.getCurrentChangedName(nameChangeCategory)))
					this.handleWarn(3, CLangStrs.nameChange, CLangStrs.nameChangeSub);
				else {
					this.isNameChanging = false;
					btnNameChange.displayString = ConfigGuiUtil.CHANGE_CHAR;
				}
			}
			else if(selectedCategory != null)
				this.setNameChange(selectedCategory);
		} else if(btnUp.mousePressed(mc, x, y)) {
			if(selectedCategory == null)
				return;
			if(!selectedCategory.getCategoryEntry().hasPreviousEntry())
				return;
			if(selectedCategory.getCategoryEntry().getPreviousEntry().canMigrateCategory(selectedCategory, EnumPosOption.Previous))
				this.handleWarn(4, CLangStrs.migrateCategory, CLangStrs.migrateCategorySub);
		} else if(btnDown.mousePressed(mc, x, y)) {
			if(selectedCategory == null)
				return;
			if(!selectedCategory.getCategoryEntry().hasNextEntry())
				return;
			if(selectedCategory.getCategoryEntry().getNextEntry().canMigrateCategory(selectedCategory, EnumPosOption.Next))
				this.handleWarn(5, CLangStrs.migrateCategory, CLangStrs.migrateCategorySub);
		}
	}
	
	private void handleWarn(int id, String text1, String text2) {
		if(this.warn) {
			GuiYesNo msg = new GuiYesNo(this, I18n.format(text1), I18n.format(text2), id);
			mc.displayGuiScreen(msg);
		} else this.confirmClickTask(true, id);
	}
	
	@Override
	public void confirmClicked(boolean check, int id) {
		this.confirmClickTask(check, id);
    	
    	parent.initGui();
		mc.displayGuiScreen(parent);
	}
	
	private void confirmClickTask(boolean check, int id) {
		if(!check)
			return;
		
		StellarConfigCategory selectedCategory = parList.selectedCategory;
		
		switch(id)
		{
		case 0:
			IConfigCategory cat;
			
			if(selectedCategory == null)
				cat = parList.config.getRootEntry().createCategory(newCatName, EnumPosOption.Child);
			else cat = selectedCategory.getCategoryEntry().createCategory(newCatName, EnumPosOption.Child);
			
			if(cat != null)
			{
				selectedCategory = (StellarConfigCategory) cat;
				this.onChangeSelectedCategory(selectedCategory);
				this.setNameChange(selectedCategory);
			}
			break;
		case 1:
			String copiedCatName = CPropLangUtil.getLocalizedFromID(selectedCategory.getName()) + "_Copy";
			selectedCategory.getCategoryEntry().copyCategory(selectedCategory, copiedCatName, EnumPosOption.Next);
			this.onChangeSelectedCategory(selectedCategory);
			break;
		case 2:
			selectedCategory.getCategoryEntry().removeCategory();
			break;
		case 3:
			this.isNameChanging = false;
			btnNameChange.displayString = ConfigGuiUtil.CHANGE_CHAR;
				
			if(this.nameChangeCategory != null)
				nameChangeCategory.getCategoryEntry().changeName(this.getCurrentChangedName(nameChangeCategory));
			break;
		case 4:
			selectedCategory.getCategoryEntry().getPreviousEntry().migrateCategory(selectedCategory, EnumPosOption.Previous);
			this.onChangeSelectedCategory(selectedCategory);
			break;
		case 5:
			selectedCategory.getCategoryEntry().getNextEntry().migrateCategory(selectedCategory, EnumPosOption.Next);
			this.onChangeSelectedCategory(selectedCategory);
			break;
		}
	}

	
	/**Sets the category as changing name.*/
	protected void setNameChange(StellarConfigCategory category) {
		this.nameChangeCategory  = category;
		this.isNameChanging = true;
		btnNameChange.displayString = ConfigGuiUtil.CHECK_CHAR;
	}
	
	/**gets current changed name and finishes changing mode.*/
	protected abstract String getCurrentChangedName(StellarConfigCategory category);

	public void mouseMovedOrUp(int x, int y, int mouseEvent) {		
		if(this.canModify && this.dragIndex >= 0)
		{
			int scrollBarXStart = this.left + this.listWidth - 6;
			int boxLeft = this.left;
			int boxRight = scrollBarXStart-1;
		    
			if(x >= boxLeft && x <= boxRight && y >= this.top && y <= this.bottom)
			{
				if(this.dragDuration < 1000L)
					return;
				this.dragDuration = 0;
				
				if(!this.canDragToChild) {
					int index = this.getElementOn(x, y - this.slotHeight / 2);
					this.setElementDrawDrag(-1, -2, false);

					if(this.dragIndex != index && !(this.dragIndex == 0 && index == -1))
						this.onDragnDropPre(this.dragIndex, index, false);
				} else {
					int index1 = this.getElementOn(x, y - this.slotHeight / 2);
					int index2 = this.getElementOn(x, y);

					this.setElementDrawDrag(-1, -2, false);
					
					if(this.dragIndex != index1 && !(this.dragIndex == 0 && index1 == -1))
						this.onDragnDropPre(this.dragIndex, index1, index1 == index2);
				}
			}
			
			this.dragIndex = -1;
		}
		
		btnNew.mouseReleased(x, y);
		btnCopy.mouseReleased(x, y);
		btnRemove.mouseReleased(x, y);
		btnNameChange.mouseReleased(x, y);
		btnUp.mouseReleased(x, y);
		btnDown.mouseReleased(x, y);
	}
	
	public void mouseClickMove(int mouseX, int mouseY, int lastButtonClicked, long timeSinceMouseClick) {
		if(this.canModify && this.dragIndex >= 0)
		{
			int scrollBarXStart = this.left + this.listWidth - 6;
			int boxLeft = this.left;
			int boxRight = scrollBarXStart-1;
		    
			if(mouseX >= boxLeft && mouseX <= boxRight && mouseY >= this.top && mouseY <= this.bottom)
			{
				this.dragDuration = timeSinceMouseClick;
				if(this.dragDuration < 1000L)
					return;
				
				if(!this.canDragToChild) {
					int index = this.getElementOn(mouseX, mouseY - this.slotHeight / 2);
					this.setElementDrawDrag(this.dragIndex, index, false);
				} else {
					int index1 = this.getElementOn(mouseX, mouseY - this.slotHeight / 2);
					int index2 = this.getElementOn(mouseX, mouseY);
					this.setElementDrawDrag(this.dragIndex, index1, index1 == index2);
				}
			}
		}
	}
	
	private int getElementOn(int x, int y) {
		return this.func_27256_c(x, y);
	}
	
	protected abstract void setElementDrawDrag(int selectedIndex, int index1, boolean b);
	
	protected abstract boolean canDragToChild();
	
	private void onDragnDropPre(final int preIndex, final int entryIndex, final boolean migrateToChild) {
		if(!GuiCfgCatViewBase.this.canDragnDrop(preIndex, entryIndex, migrateToChild))
			return;
		
		if(this.warn) {
			GuiYesNoCallback callback = new GuiYesNoCallback() {
				@Override
				public void confirmClicked(boolean check, int id) {
					if(check)
						GuiCfgCatViewBase.this.onDragnDrop(preIndex, entryIndex, migrateToChild);
			    	
					parent.initGui();
					mc.displayGuiScreen(parent);
				}
			};
			GuiYesNo msg = new GuiYesNo(callback, I18n.format(CLangStrs.migrateCategory), I18n.format(CLangStrs.migrateCategorySub), 0);
			mc.displayGuiScreen(msg);
		} else this.onDragnDrop(preIndex, entryIndex, migrateToChild);
	}
	
	/**
	 * @param preIndex the index of the element being migrated
	 * @param entryIndex either the previous or parent index of migration position.
	 * @param migrateToChild flag determining if it is migrating to the child.
	 * */
	protected abstract boolean canDragnDrop(int preIndex, int entryIndex, boolean migrateToChild);

	/**
	 * @param preIndex the index of the element being migrated
	 * @param entryIndex either the previous or parent index of migration position.
	 * @param migrateToChild flag determining if it is migrating to the child.
	 * */
	protected abstract void onDragnDrop(int preIndex, int entryIndex, boolean migrateToChild);
	
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
