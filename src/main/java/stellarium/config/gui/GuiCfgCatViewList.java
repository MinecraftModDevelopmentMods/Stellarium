package stellarium.config.gui;

import java.util.List;

import org.lwjgl.input.Mouse;

import com.google.common.collect.Lists;

import cpw.mods.fml.client.config.GuiButtonExt;
import stellarium.config.IConfigCategory;
import stellarium.config.core.EnumPosOption;
import stellarium.config.core.ICategoryEntry;
import stellarium.config.core.StellarConfigCategory;
import stellarium.lang.CPropLangUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.Tessellator;

public class GuiCfgCatViewList extends GuiCfgCatViewBase {
	
	private List<StellarConfigCategory> categoryList = Lists.newArrayList();
	private int drawDragIndex = -2;
	
	private boolean isChanging = false;
    private final GuiTextField textFieldValue;
	
	public GuiCfgCatViewList(Minecraft mc, GuiStellarConfig parent,
			GuiConfigCatList parList,  int width, int offsetX) {
		super(mc, parent, parList, width, offsetX);
		
		this.textFieldValue = new GuiTextField(mc.fontRenderer, 0, 0, 0, 0);
	}
	
	@Override
	public void onReset() {
		categoryList.clear();
		
		int index = 0;
		for(ICategoryEntry entry : parent.config.getRootEntry())
			categoryList.add((StellarConfigCategory) entry.getCategory());
	}
	
	@Override
	public void onCreateCategory(StellarConfigCategory cat) {
		this.onReset();
	}

	@Override
	public void onRemoveCategory(StellarConfigCategory cat) {
		this.onReset();
	}

	@Override
	public void onMigrateCategory(StellarConfigCategory cat,
			ICategoryEntry before) {
		this.onReset();
	}
	
	@Override
	public void onNameChange(StellarConfigCategory cat, String before) { }
	

	@Override
	protected void setNameChange(StellarConfigCategory category) {
		super.setNameChange(category);
		String changingName = CPropLangUtil.getLocalizedFromID(category.getName());
		textFieldValue.setText(changingName);
	}
	
	@Override
	protected String getCurrentChangedName(StellarConfigCategory category) {
		return CPropLangUtil.getLocalizedFromID(textFieldValue.getText());
	}
	

	@Override
	protected int getSize() {
		return categoryList.size();
	}
	
	@Override
	public void keyPressed(char typed, int id) {
		textFieldValue.textboxKeyTyped(typed, id);
	}
	
	@Override
	public void updateCursorCounter() {
		textFieldValue.updateCursorCounter();
	}
	
	public void mousePressed(int x, int y, int mouseEvent) {
		super.mousePressed(x, y, mouseEvent);
		textFieldValue.mouseClicked(x, y, mouseEvent);
	}

	@Override
	protected void drawSlot(int listIndex, int var2, int var3, int var4,
			Tessellator var5) {		
		StellarConfigCategory category = this.getCategory(listIndex);
		if(category == null)
			return;
		if(this.isNameChanging && category.equals(nameChangeCategory))
		{
			textFieldValue.xPosition = this.left + 2;
			textFieldValue.yPosition = var3 - 1;
			textFieldValue.width = this.listWidth - 2;
			textFieldValue.height = this.slotHeight;
			
			textFieldValue.drawTextBox();
		} else {
			String locname = CPropLangUtil.getLocalizedFromID(category.getName());
		
			int color = category.isImmutable()? 0xcccccc: 0xffffff;
		
			parent.getFontRenderer().drawString(parent.getFontRenderer().trimStringToWidth(locname, listWidth), this.left + 3, var3 + 1, color);
        
			this.drawDrag(listIndex, var3, var5);
		}
	}
	
	private void drawDrag(int listIndex, int yPos, Tessellator tessellator) {
		int left = this.left;
		int right = this.left + this.listWidth;
		int top;
		int bottom;
		if(drawDragIndex == -1 && listIndex == 0)
		{
			top = yPos - 1;
			bottom = yPos;
		} else if(drawDragIndex == listIndex) {
			top = yPos + this.slotHeight - 1;
			bottom = yPos + this.slotHeight;
		} else return;
		
		parent.drawRect(left, top, right, bottom, 0xffffffff);
	}

	@Override
	protected void setElementDrawDrag(int preIndex, int entryIndex, boolean migrateToChild) {
		this.drawDragIndex = entryIndex;
	}
	
	@Override
	protected boolean canDragnDrop(int preIndex, int entryIndex,
			boolean migrateToChild) {
		StellarConfigCategory theCategory = this.getCategory(preIndex);

		if(entryIndex == -1)
		{
			if(this.getCategory(0) != null)
				return this.getCategory(0).getCategoryEntry().canMigrateCategory(theCategory, EnumPosOption.Previous);
			return false;
		}
		
		return this.getCategory(entryIndex).getCategoryEntry().canMigrateCategory(theCategory, EnumPosOption.Next);
	}
	
	@Override
	protected void onDragnDrop(int preIndex, int entryIndex, boolean migrateToChild) {		
		StellarConfigCategory theCategory = this.getCategory(preIndex);

		if(entryIndex == -1)
		{
			if(this.getCategory(0) != null)
				this.getCategory(0).getCategoryEntry().migrateCategory(theCategory, EnumPosOption.Previous);
			return;
		}
		
		this.getCategory(entryIndex).getCategoryEntry().migrateCategory(theCategory, EnumPosOption.Next);
	}
	
	@Override
	protected boolean canDragToChild() {
		return false;
	}

	@Override
	protected StellarConfigCategory getCategory(int index) {
		if(index >= categoryList.size())
			return null;
		return categoryList.get(index);
	}

}
