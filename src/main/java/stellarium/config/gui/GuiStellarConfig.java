package stellarium.config.gui;

import java.util.List;

import org.lwjgl.input.Keyboard;

import com.google.common.collect.Lists;

import cpw.mods.fml.client.config.GuiButtonExt;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
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
import stellarium.config.gui.gui.GuiDetailedMessage;
import stellarium.lang.CPropLangUtil;

public class GuiStellarConfig extends GuiScreen implements IConfigHandler {

	private GuiScreen parentScreen;
	protected String title;
	
	private GuiConfigBase handler;
	
	protected StellarConfiguration config;
	
	protected boolean needsRefresh = true, modifiable, warn;
	
	public GuiStellarConfig(GuiScreen parScreen, StellarConfiguration config,
			String title) {
		this.mc = Minecraft.getMinecraft();
		this.parentScreen = parScreen;
		this.config = config;
        this.title = title;
	}

	
	@Override
	public void setCategoryType(EnumCategoryType t) {
		handler = ConfigGuiUtil.getCfgHandler(parentScreen, this, config, title, t);
		handler.setCategoryType(t);
	}

	@Override
	public void setModifiable(boolean modif, boolean warn) {
		handler.setModifiable(modif, warn);
	}
	
	public void setTitle(String title)
	{
		this.title = title;
		handler.setTitle(title);
	}
	
	public FontRenderer getFontRenderer() {
		return mc.fontRenderer;
	}
	
	public double getZLevel() {
		return this.zLevel;
	}
	
    @Override
    public void initGui()
    {
    	handler.initGui(this.needsRefresh);
    	this.needsRefresh = false;
    }
    
    @Override
    public void onGuiClosed()
    {
    	handler.onGuiClosed();

    	if (this.parentScreen instanceof GuiStellarConfig)
        {
    		GuiStellarConfig parentGuiConfig = (GuiStellarConfig) this.parentScreen;
        	parentGuiConfig.needsRefresh = true;
            parentGuiConfig.initGui();
        }
        else Keyboard.enableRepeatEvents(false);
    }
    

	public void addToButtonList(GuiButton guiButton) {
		buttonList.add(guiButton);
	}
    
    @Override
    protected void actionPerformed(GuiButton button)
    {
    	handler.actionPerformed(button);
    }

    /**
     * Called when the mouse is clicked.
     */
    @Override
    protected void mouseClicked(int x, int y, int mouseEvent)
    {
    	handler.mouseClicked(x, y, mouseEvent);
    }

    /**
     * Called when the mouse is moved or a mouse button is released.  Signature: (mouseX, mouseY, which) which==-1 is
     * mouseMove, which==0 or which==1 is mouseUp
     */
    @Override
    protected void mouseMovedOrUp(int x, int y, int mouseEvent)
    {
    	handler.mouseMovedOrUp(x, y, mouseEvent);
	}
    
	public void mouseClickMove(int mouseX, int mouseY, int lastButtonClicked, long timeSinceMouseClick)
	{
		handler.mouseClickMove(mouseX, mouseY, lastButtonClicked, timeSinceMouseClick);
	}

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    @Override
    protected void keyTyped(char eventChar, int eventKey)
    {
        handler.keyTyped(eventChar, eventKey);
    }

    /**
     * Called from the main game loop to update the screen.
     */
    @Override
    public void updateScreen()
    {
        handler.updateScreen();
    }

    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        handler.drawScreen(mouseX, mouseY, partialTicks);
    }
    

	@Override
	public ICategoryHandler getNewCat(StellarConfigCategory cat) {
		return handler.getNewCat(cat);
	}

	@Override
	public IConfigHandler getNewSubCfg(StellarConfiguration subConfig) {
		return handler.getNewSubCfg(subConfig);
	}

	@Override
	public void onPostCreated(StellarConfigCategory cat) {
		handler.onPostCreated(cat);
	}

	@Override
	public void onRemove(StellarConfigCategory cat) {
		handler.onRemove(cat);
	}

	@Override
	public void onMigrate(StellarConfigCategory cat, ICategoryEntry before) {
		handler.onMigrate(cat, before);
	}

	@Override
	public boolean isValidNameChange(StellarConfigCategory cat, String postName) {
		return handler.isValidNameChange(cat, postName);
	}

	@Override
	public void onNameChange(StellarConfigCategory cat, String before) {
		handler.onNameChange(cat, before);
	}

	@Override
	public void onMarkImmutable(StellarConfigCategory cat) {
		handler.onMarkImmutable(cat);
	}

	@Override
	public void loadCategories(StellarConfiguration config) {
		//Not Used.
	}

	@Override
	public void addLoadFailMessage(String title, ICfgMessage msg) {
		handler.addLoadFailMessage(title, msg);
	}

	@Override
	public void onSave(StellarConfiguration config) {
		handler.onSave(config);
	}


	public void mouseClickedSuper(int x, int y, int mouseEvent) {
		super.mouseClicked(x, y, mouseEvent);
	}


	public void mouseMovedOrUpSuper(int x, int y, int mouseEvent) {
		super.mouseMovedOrUp(x, y, mouseEvent);
	}


	public void updateScreenSuper() {
		super.updateScreen();
	}


	public void drawScreenSuper(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	public void drawToolTip(List stringList, int x, int y)
    {
        this.func_146283_a(stringList, x, y);
    }

}
