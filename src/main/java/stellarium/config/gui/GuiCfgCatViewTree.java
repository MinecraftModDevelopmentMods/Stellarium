package stellarium.config.gui;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.lwjgl.input.Mouse;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ibm.icu.util.ULocale.Category;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.Tessellator;
import stellarium.config.IConfigCategory;
import stellarium.config.IStellarConfig;
import stellarium.config.core.ICategoryEntry;
import stellarium.config.core.StellarConfigCategory;
import stellarium.config.util.CConfigUtil;
import stellarium.lang.CPropLangUtil;

public class GuiCfgCatViewTree extends GuiCfgCatViewBase implements Iterable<GuiCfgCatViewTree.GuiCategoryEntry> {

	private static final int CHILD_SPACING = 10;
	private static final int BTN_SIZE = 8;
	
	protected Map<ICategoryEntry, GuiCategoryEntry> catEntMap = Maps.newHashMap();
	
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
	protected StellarConfigCategory getCategory(int index) {
		return getCatEntry(index).category;
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
	protected void drawSlot(int listIndex, int var2, int var3, int var4, Tessellator tessellator) {
		getCatEntry(listIndex).drawSlot(var2, var3, var4, tessellator);
	}
	
	protected class GuiCategoryEntry {
		
		public final StellarConfigCategory category;
		public int spacing;
		public boolean isOpened = false;
		public GuiButton button;
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
		
		public void drawSlot(int var2, int var3, int var4, Tessellator tessellator) {
			button.displayString = this.isOpened? ConfigGuiUtil.CONTRACT_CHAR : ConfigGuiUtil.EXPAND_CHAR;
			button.yPosition = var3;
			button.drawButton(mc, mouseX, mouseY);
	        
			if(Mouse.isButtonDown(0)) {
				long curTime = System.currentTimeMillis();
				if(curTime - this.lastClickedTime >= 200L)
				{
					if(button.mousePressed(mc, mouseX, mouseY))
						this.isOpened = !this.isOpened;
				}
				
				this.lastClickedTime = curTime;
			}
			
			String locname = CPropLangUtil.getLocalizedFromID(category.getName());
			int color = category.isImmutable()? 0xcccccc: 0xffffff;

	        parent.getFontRenderer().drawString(parent.getFontRenderer().trimStringToWidth(locname, listWidth),
	        		GuiCfgCatViewTree.this.left + this.spacing, var3 + 1, color);
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
			else if(now.hasNextEntry())
				return true;
			
			return false;
		}

		@Override
		public GuiCategoryEntry next() {
			if(now.hasChildEntry() && ((nowEntry == null) || nowEntry.isOpened))
			{
				now = now.getFirstChildEntry();
				return nowEntry = catEntMap.get(now.getCategory().getCategoryEntry());
			}
			else if(now.hasNextEntry())
			{
				now = now.getNextEntry();
				return nowEntry = catEntMap.get(now.getCategory().getCategoryEntry());
			}
			else throw new NoSuchElementException();
		}

		@Override
		public void remove() { }
	}

	@Override
	public Iterator<GuiCategoryEntry> iterator() {
		return new GuiEntryIterator(parent.config);
	}

}
