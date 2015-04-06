package stellarium.config.gui;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ibm.icu.util.ULocale.Category;

import cpw.mods.fml.client.config.GuiButtonExt;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import stellarium.config.IConfigCategory;
import stellarium.config.IStellarConfig;
import stellarium.config.core.EnumPosOption;
import stellarium.config.core.ICategoryEntry;
import stellarium.config.core.StellarConfigCategory;
import stellarium.config.util.CConfigUtil;
import stellarium.gui.config.GuiTexturedButton;
import stellarium.lang.CLangStrs;
import stellarium.lang.CPropLangUtil;

public class GuiCfgCatViewTree extends GuiCfgCatViewBase implements Iterable<GuiCfgCatViewTree.GuiCategoryEntry> {

	private static final int CHILD_SPACING = 10;
	private static final int SPACING = 2;
	private static final int BTN_SIZE = 8;
		
	protected Map<ICategoryEntry, GuiCategoryEntry> catEntMap = Maps.newHashMap();
	private int drawDragIndex = -2;
	private boolean drawMigrateToChild;
	private String changingName;
	
	public GuiCfgCatViewTree(Minecraft mc, GuiStellarConfig parent,
			GuiConfigCatList parList, int width, int offsetX) {
		super(mc, parent, parList, width, offsetX);
	}

	@Override
	public void onReset() {
		catEntMap.clear();
		
		for(IConfigCategory category : CConfigUtil.getCfgIteWrapper(parent.config))
			catEntMap.put(category.getCategoryEntry(), new GuiCategoryEntry((StellarConfigCategory) category));
	}
	
	@Override
	public void onCreateCategory(StellarConfigCategory cat) {
		catEntMap.put(cat.getCategoryEntry(), new GuiCategoryEntry(cat));
	}

	@Override
	public void onRemoveCategory(StellarConfigCategory cat) {
		catEntMap.remove(cat.getCategoryEntry());
	}

	@Override
	public void onMigrateCategory(StellarConfigCategory cat,
			ICategoryEntry before) {
		GuiCategoryEntry entry = catEntMap.remove(before);
		catEntMap.put(cat.getCategoryEntry(), entry);
		entry.resetSpacing();
	}

	@Override
	public void onNameChange(StellarConfigCategory cat, String before) { }
	
	@Override
	protected void setNameChange(StellarConfigCategory category) {
		super.setNameChange(category);
		catEntMap.get(category.getCategoryEntry()).setNameChange();
	}
	
	@Override
	protected String getCurrentChangedName(StellarConfigCategory category) {
		return catEntMap.get(category.getCategoryEntry()).getChangedName();
	}

	@Override
	protected StellarConfigCategory getCategory(int index) {
		GuiCategoryEntry entry = this.getCatEntry(index);
		return entry != null? getCatEntry(index).category : null;
	}
	
	private GuiCategoryEntry getCatEntry(int index) {
		int cnt = 0;
		
		for(GuiCategoryEntry entry : this)
		{
			if(cnt == index)
				return entry;
			cnt++;
		}
		
		return null;
	}

	@Override
	protected int getSize() {
		int size = 0;
		
		for(GuiCategoryEntry entry : this)
			size++;
		
		return size;
	}
	
	@Override
	public void keyPressed(char typed, int id) {
		for(GuiCategoryEntry entry : this)
			entry.keyPressed(typed, id);
	}
	
	@Override
	public void updateCursorCounter() {
		for(GuiCategoryEntry entry : this)
			entry.updateCursorCounter();
	}
	
	public void mousePressed(int x, int y, int mouseEvent) {
		super.mousePressed(x, y, mouseEvent);
		for(GuiCategoryEntry entry : this)
			entry.mousePressed(x, y, mouseEvent);
	}

	@Override
	protected void setElementDrawDrag(int preIndex, int entryIndex, boolean migrateToChild) {
		this.drawDragIndex = entryIndex;
		this.drawMigrateToChild = migrateToChild;
	}
	
	@Override
	protected void drawSlot(int listIndex, int var2, int var3, int var4, Tessellator tessellator) {
		GuiCategoryEntry entry = getCatEntry(listIndex);
		if(entry == null)
			return;
		
		entry.drawSlot(listIndex, var2, var3, var4, tessellator);
		
		if(!this.drawMigrateToChild)
			this.drawDrag(listIndex, var3, tessellator);
		else this.drawDragOn(listIndex, var3, tessellator);
	}
	
	private void drawDrag(int listIndex, int yPos, Tessellator tessellator) {
		int left = this.left;
		int right = this.left + this.listWidth;
		int top;
		int bottom;
		if(drawDragIndex == -1 && listIndex == 0)
		{
			top = yPos - 3;
			bottom = yPos - 2;
		} else if(drawDragIndex == listIndex) {
			top = yPos + this.slotHeight - 3;
			bottom = yPos + this.slotHeight - 2;
		} else return;
		
		parent.drawRect(left, top, right, bottom, 0xffffffff);
	}
	
	private void drawDragOn(int listIndex, int yPos, Tessellator tessellator) {
		int left = this.left + 1;
		int right = this.left + this.listWidth - 1;
		int top = yPos - 2;
		int bottom = yPos + this.slotHeight - 1;
		if(drawDragIndex != listIndex)
			return;
		
		parent.drawRect(left, top, right, bottom, 0x33ffff22);
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
		
		EnumPosOption option = migrateToChild? EnumPosOption.Child : EnumPosOption.Next;
		this.getCategory(entryIndex).getCategoryEntry().migrateCategory(theCategory, option);
	}
	
	@Override
	protected boolean canDragnDrop(int preIndex, int entryIndex, boolean migrateToChild) {
		StellarConfigCategory theCategory = this.getCategory(preIndex);
		
		if(entryIndex == -1)
		{
			if(this.getCategory(0) != null)
				return this.getCategory(0).getCategoryEntry().canMigrateCategory(theCategory, EnumPosOption.Previous);
		}
		
		EnumPosOption option = migrateToChild? EnumPosOption.Child : EnumPosOption.Next;
		return this.getCategory(entryIndex).getCategoryEntry().canMigrateCategory(theCategory, option);
	}
	
	@Override
	protected boolean canDragToChild() {
		return true;
	}
	
	protected class GuiCategoryEntry {
		
		private final StellarConfigCategory category;
		private int spacing;
		private int textColor = 0xffffff;
		protected boolean isOpened = false;
		private GuiButton button;
		private GuiTextField textFieldValue;
	    private boolean isNameChanging = false;
		private long lastClickedTime = System.currentTimeMillis();
		
		public GuiCategoryEntry(StellarConfigCategory category) {
			this.category = category;
			
			this.resetSpacing();
		}

		public void resetSpacing() {
			ICategoryEntry parEntry = category.getCategoryEntry().getParentEntry();			

			if(parEntry.isRootEntry())
				this.spacing = 3;
			else this.spacing = catEntMap.get(parEntry).spacing + CHILD_SPACING;
			
			button = new GuiButton(0, spacing, 0, BTN_SIZE, BTN_SIZE,
					ConfigGuiUtil.EXPAND_CHAR);
			textFieldValue = new GuiTextField(mc.fontRenderer, spacing + BTN_SIZE, 0,
					GuiCfgCatViewTree.this.listWidth - spacing - BTN_SIZE - SPACING, slotHeight - SPACING);
		}
		
		@Override
		public boolean equals(Object o) {
			if(o != null)
			{
				if(o instanceof StellarConfigCategory)
					return ((StellarConfigCategory) o).getCategoryEntry().equals(category.getCategoryEntry());
				else if(o instanceof GuiCategoryEntry)
					return o.equals(category);
			}
			return false;
		}
		
		public void updateCursorCounter() {
			textFieldValue.updateCursorCounter();
		}

		public void keyPressed(char typed, int id) {
			textFieldValue.textboxKeyTyped(typed, id);
		}
		
		public void mousePressed(int x, int y, int mouseEvent) {
			textFieldValue.mouseClicked(x, y, mouseEvent);
		}
		
		public void drawSlot(int listIndex, int var2, int var3, int var4, Tessellator tessellator) {
			button.displayString = this.isOpened? ConfigGuiUtil.CONTRACT_CHAR : ConfigGuiUtil.EXPAND_CHAR;
			button.yPosition = var3 - 1;
			button.drawButton(mc, mouseX, mouseY);
	        
			if(Mouse.isButtonDown(0)) {
				long curTime = System.currentTimeMillis();
				if(curTime - this.lastClickedTime >= 200L)
				{
					if(button.mousePressed(mc, mouseX, mouseY))
					{
						elementClicked(listIndex, false);
						this.isOpened = !this.isOpened;
					}
				}
				
				this.lastClickedTime = curTime;
			}
			
			if(this.isNameChanging) {
				textFieldValue.yPosition = var3;
				textFieldValue.drawTextBox();
			} else {
				String locname = CPropLangUtil.getLocalizedFromID(category.getName());
				int color = category.isImmutable()? 0xcccccc: 0xffffff;
				
				parent.getFontRenderer().drawString(parent.getFontRenderer().trimStringToWidth(locname, listWidth),
	        		GuiCfgCatViewTree.this.left + this.spacing, var3 + 1, color);
			}
		}
		
		public String getChangedName() {
			this.isNameChanging = false;
			return textFieldValue.getText();
		}
		
		public void setNameChange() {
			this.isNameChanging = true;
			textFieldValue.setText(CPropLangUtil.getLocalizedFromID(category.getName()));
		}
	}
	
	protected class GuiEntryIterator implements Iterator<GuiCategoryEntry> {
		
		private ICategoryEntry now;
		private GuiCategoryEntry nowEntry;
		
		public GuiEntryIterator(IStellarConfig config) {
			now = config.getRootEntry();
		}
		
		@Override
		public boolean hasNext() {
			if(now.hasChildEntry())
			{
				if(nowEntry == null)
					return true;
				else if(nowEntry.isOpened)
					return true;
			}
			
			ICategoryEntry entry = now;
			
			while(!entry.isRootEntry())
			{
				if(entry.hasNextEntry())
					return true;
				entry = entry.getParentEntry();
			}
			
			return false;
		}

		@Override
		public GuiCategoryEntry next() {
			if(now.hasChildEntry() && ((nowEntry == null) || nowEntry.isOpened))
			{
				now = now.getFirstChildEntry();
				return nowEntry = catEntMap.get(now);
			}
			
			ICategoryEntry entry = now;
			
			while(!entry.isRootEntry())
			{
				if(entry.hasNextEntry())
				{
					now = entry.getNextEntry();
					return nowEntry = catEntMap.get(now);
				}
				entry = entry.getParentEntry();
			}
			
			throw new NoSuchElementException();
		}

		@Override
		public void remove() { }
	}

	@Override
	public Iterator<GuiCategoryEntry> iterator() {
		return new GuiEntryIterator(parent.config);
	}

}
