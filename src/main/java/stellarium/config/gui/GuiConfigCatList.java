package stellarium.config.gui;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.input.Keyboard;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cpw.mods.fml.client.config.GuiButtonExt;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import stellarium.config.EnumCategoryType;
import stellarium.config.ICfgMessage;
import stellarium.config.core.ICategoryEntry;
import stellarium.config.core.StellarConfigCategory;
import stellarium.config.core.StellarConfiguration;
import stellarium.config.core.handler.ICategoryHandler;
import stellarium.config.core.handler.IConfigHandler;
import stellarium.config.gui.GuiCfgCatViewTree.GuiCategoryEntry;
import stellarium.config.gui.gui.GuiDetailedMessage;
import stellarium.lang.CPropLangUtil;

public class GuiConfigCatList extends GuiConfigBase {
	
	private final int viewX = 10;
	private int offsetX;
	private final int spaceX = 5;
	
	protected GuiCfgCatViewBase viewList;
	protected GuiConfigCatEntry cfgList;
	
	protected StellarConfigCategory selectedCategory = null;

	protected boolean modifiable, warn;
	
	protected HashMap<ICategoryEntry, GuiConfigCatHandler> handlerMap = Maps.newLinkedHashMap();
	
	protected List<String> loadFails = Lists.newArrayList();
	private EnumCategoryType cattype;

	public GuiConfigCatList(GuiScreen parentScreen, GuiStellarConfig guiConfig, StellarConfiguration config, String title) {
		super(parentScreen, guiConfig, config, title);
	}

	@Override
	public void setCategoryType(EnumCategoryType t) {
		this.cattype = t;
	}

	@Override
	public void setModifiable(boolean modif, boolean warn) {
		this.modifiable = modif;
		this.warn = warn;
	}
	
	
	@Override
	public void initGui(boolean needsRefresh) {
		
		this.offsetX = guiConfig.width * 2 / 7;
		
    	this.cfgList = new GuiConfigCatEntry(this.guiConfig, mc, offsetX);
    	
    	this.viewList = ConfigGuiUtil.getCfgCatView(this.guiConfig, this, mc, offsetX - viewX - spaceX, viewX, cattype);
		this.viewList.onReset();
		
		this.selectedCategory = null;
    	
        Keyboard.enableRepeatEvents(true);
        
        int textWidth = Math.max(mc.fontRenderer.getStringWidth(I18n.format("gui.done")) + 60, 200);
        int buttonWidthHalf = textWidth / 2;
        guiConfig.addToButtonList(new GuiButtonExt(2000, guiConfig.width / 2 - buttonWidthHalf, guiConfig.height - 29, textWidth, 20, I18n.format("gui.done")));
        
        cfgList.initGui();
	}

	@Override
	public void onGuiClosed() {
		cfgList.onGuiClosed();
		super.onGuiClosed();
	}
	
	public void onSelectChange(StellarConfigCategory newSelected) {
		GuiConfigCatHandler handler;
		
		handler = (newSelected != null)? handlerMap.get(newSelected.getCategoryEntry()) : null;
		
		cfgList.setCategory(newSelected, handler);
		this.selectedCategory = newSelected;
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.id == 2000)
    	{
    		config.onApply();
    		
    		if(!loadFails.isEmpty())
    		{
    			List<String> strs = Lists.newArrayList();
    			
    			for(String str : this.loadFails)
    			{
    				strs.add(String.format("- %s", str));
    			}
    				
    			this.mc.displayGuiScreen(new GuiDetailedMessage(this.guiConfig, "Invalid Configuration", strs));
    			
    			loadFails.clear();
    			
    			return;
    		}
    		
    		this.close();
    	}
	}

	@Override
	protected void mouseClicked(int x, int y, int mouseEvent) {
    	if(mouseEvent != 0 || !cfgList.func_148179_a(x, y, mouseEvent))
    	{
    		viewList.mousePressed(x, y, mouseEvent);
            cfgList.mouseClicked(x, y, mouseEvent);
    		guiConfig.mouseClickedSuper(x, y, mouseEvent);
    	}
	}

	@Override
	protected void mouseMovedOrUp(int x, int y, int mouseEvent) {
		viewList.mouseMovedOrUp(x, y, mouseEvent);
    	if(mouseEvent != 0 || !cfgList.func_148181_b(x, y, mouseEvent))
    		guiConfig.mouseMovedOrUpSuper(x, y, mouseEvent);
	}
	
	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int lastButtonClicked, long timeSinceMouseClick) {
		viewList.mouseClickMove(mouseX, mouseY, lastButtonClicked, timeSinceMouseClick);
	}

	@Override
	protected void keyTyped(char eventChar, int eventKey) {
        if (eventKey == Keyboard.KEY_ESCAPE)
            this.close();
        else {
        	viewList.keyPressed(eventChar, eventKey);
        	cfgList.onKeyPressed(eventChar, eventKey);
        }
	}

	@Override
	public void updateScreen() {
		cfgList.updateScreen();
		viewList.updateCursorCounter();
		guiConfig.updateScreenSuper();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		guiConfig.drawDefaultBackground();
        cfgList.drawScreen(mouseX, mouseY, partialTicks);
		viewList.drawScreen(mouseX, mouseY, partialTicks);
        guiConfig.drawCenteredString(this.fontRendererObj, guiConfig.title, guiConfig.width / 2, 8, 16777215);
        
        guiConfig.drawScreenSuper(mouseX, mouseY, partialTicks);
        
        cfgList.drawToolTip(mouseX, mouseY, partialTicks);
	}


	@Override
	public ICategoryHandler getNewCat(StellarConfigCategory cat) {
		GuiConfigCatHandler catHandler = new GuiConfigCatHandler(this, cat);
		handlerMap.put(cat.getCategoryEntry(), catHandler);
		return catHandler;
	}

	@Override
	public IConfigHandler getNewSubCfg(StellarConfiguration subConfig) {
		//Should not be called.
		return null;
	}

	@Override
	public void onPostCreated(StellarConfigCategory cat) {
		if(viewList != null)
			viewList.onCreateCategory(cat);
	}

	@Override
	public void onRemove(StellarConfigCategory cat) {		
		if(this.selectedCategory != null && cat.getCategoryEntry().equals(selectedCategory.getCategoryEntry()))
			this.onSelectChange(null);
		
		if(viewList != null)
			viewList.onRemoveCategory(cat);
		
		handlerMap.remove(cat.getCategoryEntry());
	}

	@Override
	public void onMigrate(StellarConfigCategory cat, ICategoryEntry before) {		
		if(this.selectedCategory != null && selectedCategory.getCategoryEntry().equals(before))
			this.onSelectChange(cat);
		
		if(viewList != null)
			viewList.onMigrateCategory(cat, before);
		
		handlerMap.put(cat.getCategoryEntry(), handlerMap.remove(before));
	}

	@Override
	public boolean isValidNameChange(StellarConfigCategory cat, String postName) {
		return true;
	}

	@Override
	public void onNameChange(StellarConfigCategory cat, String before) {
		viewList.onNameChange(cat, before);
	}

	@Override
	public void onMarkImmutable(StellarConfigCategory cat) { }

	@Override
	public void loadCategories(StellarConfiguration config) { }

	@Override
	public void addLoadFailMessage(String title, ICfgMessage msg) {
		loadFails.add(String.format("[%s]: %s", CPropLangUtil.getLocalizedFromID(title),
				CPropLangUtil.getLocalizedMessage(msg)));
	}

	@Override
	public void onSave(StellarConfiguration config) { }
}
