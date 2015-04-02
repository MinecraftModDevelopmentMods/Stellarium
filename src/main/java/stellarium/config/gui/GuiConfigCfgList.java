package stellarium.config.gui;

import java.util.Iterator;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;

import cpw.mods.fml.client.config.GuiButtonExt;
import stellarium.config.EnumCategoryType;
import stellarium.config.ICfgMessage;
import stellarium.config.IConfigCategory;
import stellarium.config.core.EnumPosOption;
import stellarium.config.core.ICategoryEntry;
import stellarium.config.core.StellarConfigCategory;
import stellarium.config.core.StellarConfiguration;
import stellarium.config.core.handler.ICategoryHandler;
import stellarium.config.core.handler.IConfigHandler;
import stellarium.config.gui.gui.GuiTexturedButton;
import stellarium.lang.CLangStrs;
import stellarium.lang.CPropLangUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiListExtended.IGuiListEntry;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;

public class GuiConfigCfgList extends GuiConfigBase {
	
	protected CfgBtnList cfgList;
	
	protected boolean modifiable = false;
	protected boolean warn = false;
	
	public GuiConfigCfgList(GuiScreen parentScreen, GuiStellarConfig guiConfig, StellarConfiguration config, String title)
	{
		super(parentScreen, guiConfig, config, title);
		cfgList = new CfgBtnList(guiConfig, mc);
	}
	
    @Override
    public void initGui(boolean needsRefresh)
    {    	
        Keyboard.enableRepeatEvents(true);
        
        int textWidth = Math.max(mc.fontRenderer.getStringWidth(I18n.format("gui.done")) + 60, 200);
        int buttonWidthHalf = textWidth / 2;
        guiConfig.addToButtonList(new GuiButtonExt(2000, guiConfig.width / 2 - buttonWidthHalf, guiConfig.height - 29, textWidth, 20, I18n.format("gui.done")));
        
        cfgList.initGui();
    }
    
    @Override
    public void onGuiClosed() {
		super.onGuiClosed();
    }
    
    @Override
    protected void actionPerformed(GuiButton button)
    {
    	if (button.id == 2000)
    	{
    		config.onApply();
    		
    		this.close();
    	}
    }

    /**
     * Called when the mouse is clicked.
     */
    @Override
    protected void mouseClicked(int x, int y, int mouseEvent)
    {
    	if(mouseEvent != 0 || !cfgList.func_148179_a(x, y, mouseEvent))
    		guiConfig.mouseClickedSuper(x, y, mouseEvent);
    }

    /**
     * Called when the mouse is moved or a mouse button is released.  Signature: (mouseX, mouseY, which) which==-1 is
     * mouseMove, which==0 or which==1 is mouseUp
     */
    @Override
    protected void mouseMovedOrUp(int x, int y, int mouseEvent)
    {
    	if(mouseEvent != 0 || !cfgList.func_148181_b(x, y, mouseEvent));
    		guiConfig.mouseMovedOrUpSuper(x, y, mouseEvent);
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    @Override
    protected void keyTyped(char eventChar, int eventKey)
    {
        if (eventKey == Keyboard.KEY_ESCAPE)
    		this.close();
        else cfgList.onKeyPressed(eventChar, eventKey);
    }

    /**
     * Called from the main game loop to update the screen.
     */
    @Override
    public void updateScreen()
    {
    	cfgList.onUpdate();
    	guiConfig.updateScreenSuper();
    }

    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
    	guiConfig.drawDefaultBackground();
        cfgList.drawScreen(mouseX, mouseY, partialTicks);
        guiConfig.drawCenteredString(this.fontRendererObj, this.title, guiConfig.width / 2, 8, 16777215);

        guiConfig.drawScreenSuper(mouseX, mouseY, partialTicks);
    }
   
	public class CfgBtnList extends GuiListExtended {
		
		protected static final int slotHeight = 20;
		protected GuiStellarConfig screen;
		
		protected List<CfgBtnEntry> entrycfglist = Lists.newArrayList();

		public CfgBtnList(GuiStellarConfig screen, Minecraft mc) {
			super(mc, screen.width, screen.height, 23, screen.height - 32, slotHeight);
			this.screen = screen;
		}

		protected void initGui() {
	        this.width = screen.width;
	        this.height = screen.height;
	        this.top = 23;
	        this.bottom = screen.height - 32;
	        this.left = 0;
	        this.right = screen.width;
		}
		
		protected void onUpdate() {
			for(CfgBtnEntry entry : entrycfglist)
				entry.update();
		}
		
		protected void onKeyPressed(char typed, int id)
		{
			for(CfgBtnEntry entry : entrycfglist)
				entry.keyPressed(typed, id);
		}
		
		public void addCfgEntry(StellarConfigCategory category)
		{
			entrycfglist.add(new CfgBtnEntry(this, category));
		}
		
		public CfgBtnEntry getCfgEntry(ICategoryEntry entry)
		{
			for(CfgBtnEntry bentry : entrycfglist)
			{
				if(bentry.category.getCategoryEntry().equals(entry))
					return bentry;
			}
			
			return null;
		}
		
		public void removeCfgEntry(ICategoryEntry configEntry) {
			Iterator<CfgBtnEntry> entite = entrycfglist.iterator();
			
			while(entite.hasNext())
			{
				if(entite.next().category.getCategoryEntry().equals(configEntry))
					entite.remove();
			}
		}

		@Override
		public IGuiListEntry getListEntry(int i) {
			return entrycfglist.get(i);
		}

		@Override
		protected int getSize() {
			return entrycfglist.size();
		}
		
	}

	public class CfgBtnEntry implements IGuiListEntry, GuiYesNoCallback {
		
		protected final GuiButton button, btnChange, btnCopy, btnDelete;
		protected final CfgBtnList parlist;
		protected final IConfigCategory category;
		protected final StellarConfiguration subConfig;
		protected final GuiStellarConfig subCfgGui;
        protected final GuiTextField textFieldValue;
        protected boolean isImmutable = false;
        
		protected boolean changingname;
		
		public CfgBtnEntry(CfgBtnList parlist, IConfigCategory category)
		{
			this.parlist = parlist;
			this.category = category;
			
			this.button = new GuiButtonExt(0, parlist.left + parlist.width * 1/3 - 20, 0, parlist.width / 3, 18,
					CPropLangUtil.getLocalizedFromID(category.getName()));
			this.btnChange = new GuiButtonExt(1, parlist.right - parlist.width * 1/3 - 20, 0, 18, 18,
					ConfigGuiUtil.CHANGE_CHAR);
			this.btnCopy = new GuiTexturedButton(2, parlist.right - parlist.width, 0, 18, 18,
					ConfigGuiUtil.ICON_COPY);
			this.btnDelete = new GuiButtonExt(3, parlist.right - parlist.width * 1/3 + 20, 0, 18, 18,
					ConfigGuiUtil.DELETE_CHAR);
			btnChange.packedFGColour = 0xffff99;
			btnCopy.packedFGColour = 0x00ffff;
			btnDelete.packedFGColour = 0xff2222;

			this.textFieldValue = new GuiTextField(fontRendererObj, button.xPosition, 0, button.width, 18);
			textFieldValue.setVisible(false);

			this.subConfig = config.getSubConfigRaw(category);
			this.subCfgGui = (GuiStellarConfig) subConfig.getHandler();

			if(!modifiable)
				markImmutable();
		}
		
		public void markImmutable() {
			this.isImmutable = true;
			btnChange.visible = false;
			btnCopy.visible = false;
			btnDelete.visible = false;
		}
		
		public void changeName(String name) {
			button.displayString = CPropLangUtil.getLocalizedFromID(name);
			subCfgGui.setTitle(CPropLangUtil.getLocalizedFromID(name));
		}
		
		public void update() {
			textFieldValue.updateCursorCounter();
		}
		
		public void keyPressed(char typed, int id)
		{
			textFieldValue.textboxKeyTyped(typed, id);
		}

		@Override
		public void drawEntry(int slotIndex, int x,
				int y, int listWidth, int slotHeight,
				Tessellator tessellator, int mouseX, int mouseY,
				boolean isSelected) {
			button.xPosition = parlist.left + parlist.width * 1/3 - 30;
			button.yPosition = y;
			button.width = parlist.width / 3 + (this.isImmutable? 60 : 0);
			button.drawButton(mc, mouseX, mouseY);
			
			btnChange.xPosition = parlist.right - parlist.width * 1/3 - 30;
			btnChange.yPosition = y;
			btnChange.drawButton(mc, mouseX, mouseY);
			
			btnCopy.xPosition = parlist.right - parlist.width * 1/3 - 10;
			btnCopy.yPosition = y;
			btnCopy.drawButton(mc, mouseX, mouseY);
			
			btnDelete.xPosition = parlist.right - parlist.width * 1/3 + 10;
			btnDelete.yPosition = y;
			btnDelete.drawButton(mc, mouseX, mouseY);

			textFieldValue.xPosition = button.xPosition;
			textFieldValue.yPosition = y;
			textFieldValue.width = button.width;
			textFieldValue.drawTextBox();
		}

		@Override
		public boolean mousePressed(int index, int x, int y, int mouseEvent,
				int relativeX, int relativeY) {
			if(button.mousePressed(mc, x, y))
			{
				mc.displayGuiScreen(this.subCfgGui);
				return true;
			}
			else if(btnChange.mousePressed(mc, x, y))
			{
				this.changingname = !this.changingname;
				button.visible = !this.changingname;
				textFieldValue.setVisible(this.changingname);
				
				if(this.changingname)
				{
					btnChange.displayString = ConfigGuiUtil.CHECK_CHAR;
					btnChange.packedFGColour = 0x33ff33;

					textFieldValue.setText(CPropLangUtil.getLocalizedFromID(category.getName()));
				}
				else {
					btnChange.displayString = ConfigGuiUtil.CHANGE_CHAR;
					btnChange.packedFGColour = 0xffff99;
					
					if(!category.getName().equals(textFieldValue.getText()))
					{
						if(warn) {
							GuiYesNo msg = new GuiYesNo(this, I18n.format(CLangStrs.nameChange), I18n.format(CLangStrs.nameChangeSub), 0);
							mc.displayGuiScreen(msg);
						} else this.confirmClickTask(true, 0);
					}
				}
				
				return true;
			}
			else if(btnCopy.mousePressed(mc, x, y))
			{
				if(warn) {
					GuiYesNo msg = new GuiYesNo(this, I18n.format(CLangStrs.copyCategory), I18n.format(CLangStrs.copyCategorySub), 1);
					mc.displayGuiScreen(msg);
				} else this.confirmClickTask(true, 1);
				
				return true;
			}
			else if(btnDelete.mousePressed(mc, x, y))
			{
				if(warn) {
					GuiYesNo msg = new GuiYesNo(this, I18n.format(CLangStrs.removeCategory), I18n.format(CLangStrs.removeCategorySub), 2);
					mc.displayGuiScreen(msg);
				} else this.confirmClickTask(true, 2);
				
				return true;
			}
			else {
				textFieldValue.mouseClicked(x, y, mouseEvent);
				
				return false;
			}
		}

		@Override
		public void mouseReleased(int index, int x, int y, int mouseEvent,
				int relativeX, int relativeY) {
			button.mouseReleased(x, y);
			btnChange.mouseReleased(x, y);
			btnCopy.mouseReleased(x, y);
			btnDelete.mouseReleased(x, y);
		}

		@Override
		public void confirmClicked(boolean check, int id) {
			this.confirmClickTask(check, id);
			
			guiConfig.initGui();
			mc.displayGuiScreen(guiConfig);
		}
		
		private void confirmClickTask(boolean check, int id) {
			if(check)
			{
				if(id == 0)
				{
					category.getCategoryEntry().changeName(textFieldValue.getText());
				}
				else if(id == 1)
				{
					category.getCategoryEntry().copyCategory(category, CPropLangUtil.getLocalizedFromID(category.getName()) + "_Copy",
							EnumPosOption.Next);
				}
				else if(id == 2)
				{
					category.getCategoryEntry().removeCategory();
				}
			}
		}
	}
   

	@Override
	public void setCategoryType(EnumCategoryType t) { }

	@Override
	public void setModifiable(boolean modif, boolean warn) {
		this.modifiable = modif;
		this.warn = warn;
	}

	@Override
	public ICategoryHandler getNewCat(StellarConfigCategory cat) {
		return null;
	}

	@Override
	public IConfigHandler getNewSubCfg(StellarConfiguration subConfig) {
		return ConfigGuiUtil.getCfgGui(guiConfig, subConfig,
				CPropLangUtil.getLocalizedFromID(subConfig.title));
	}

	@Override
	public void onPostCreated(StellarConfigCategory cat) {
		cfgList.addCfgEntry(cat);
	}

	@Override
	public void onRemove(StellarConfigCategory cat) {
		cfgList.removeCfgEntry(cat.getCategoryEntry());
	}

	@Override
	public void onMigrate(StellarConfigCategory cat, ICategoryEntry before) {
		// No change required, since migration is impossible for config list.
	}

	@Override
	public boolean isValidNameChange(StellarConfigCategory cat, String postName) {
		return true;
	}

	@Override
	public void onNameChange(StellarConfigCategory cat, String before) {
		cfgList.getCfgEntry(cat.getCategoryEntry()).changeName(cat.getName());
	}

	@Override
	public void onMarkImmutable(StellarConfigCategory cat) {
		cfgList.getCfgEntry(cat.getCategoryEntry()).markImmutable();
	}

	@Override
	public void loadCategories(StellarConfiguration config) { }

	@Override
	public void addLoadFailMessage(String title, ICfgMessage msg) { }

	@Override
	public void onSave(StellarConfiguration config) { }
	
}
