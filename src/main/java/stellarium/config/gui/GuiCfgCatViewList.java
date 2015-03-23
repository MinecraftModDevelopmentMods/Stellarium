package stellarium.config.gui;

import java.util.List;

import com.google.common.collect.Lists;

import stellarium.config.IConfigCategory;
import stellarium.config.core.ICategoryEntry;
import stellarium.config.core.StellarConfigCategory;
import stellarium.lang.CPropLangUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;

public class GuiCfgCatViewList extends GuiCfgCatViewBase {
	
	public List<StellarConfigCategory> categoryList = Lists.newArrayList();
	
	public GuiCfgCatViewList(Minecraft mc, GuiStellarConfig parent,
			GuiConfigCatList parList,  int width, int offsetX) {
		super(mc, parent, parList, width, offsetX);
	}
	
	@Override
	public void onReset() {
		categoryList.clear();
		
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
	protected int getSize() {
		return categoryList.size();
	}

	@Override
	protected void drawSlot(int listIndex, int var2, int var3, int var4,
			Tessellator var5) {
		StellarConfigCategory category = categoryList.get(listIndex);
		String locname = CPropLangUtil.getLocalizedFromID(category.getName());
		
		int color = category.isImmutable()? 0xcccccc: 0xffffff;
		
        parent.getFontRenderer().drawString(parent.getFontRenderer().trimStringToWidth(locname, listWidth), this.left + 3, var3 + 1, color);
	}

	@Override
	protected StellarConfigCategory getCategory(int index) {
		return categoryList.get(index);
	}

}
