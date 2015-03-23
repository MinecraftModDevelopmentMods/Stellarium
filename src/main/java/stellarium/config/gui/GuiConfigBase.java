package stellarium.config.gui;

import org.lwjgl.input.Keyboard;

import stellarium.config.EnumCategoryType;
import stellarium.config.ICfgMessage;
import stellarium.config.core.ICategoryEntry;
import stellarium.config.core.StellarConfigCategory;
import stellarium.config.core.StellarConfiguration;
import stellarium.config.core.handler.ICategoryHandler;
import stellarium.config.core.handler.IConfigHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public abstract class GuiConfigBase extends GuiScreen implements IConfigHandler {
	
	protected GuiScreen parentScreen;
	protected String title;
	
	protected StellarConfiguration config;
	protected GuiStellarConfig guiConfig;
	
	public GuiConfigBase(GuiScreen parentScreen, GuiStellarConfig guiConfig, StellarConfiguration config, String title)
	{
        this.parentScreen = parentScreen;
        this.guiConfig = guiConfig;
		this.mc = guiConfig.mc;
        this.title = title;
        this.config = config;
        
		this.fontRendererObj = guiConfig.getFontRenderer();
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	public abstract void initGui(boolean needsRefresh);
	
    protected abstract void actionPerformed(GuiButton button);
    
    public void close()
    {
		ConfigGuiUtil.onCfgGuiClosed(parentScreen, config);
        this.mc.displayGuiScreen(parentScreen);
    }

    /**
     * Called when the mouse is clicked.
     */
    @Override
    protected void mouseClicked(int x, int y, int mouseEvent)
    {
    	super.mouseClicked(x, y, mouseEvent);
    }

    /**
     * Called when the mouse is moved or a mouse button is released.  Signature: (mouseX, mouseY, which) which==-1 is
     * mouseMove, which==0 or which==1 is mouseUp
     */
    @Override
    protected void mouseMovedOrUp(int x, int y, int mouseEvent)
    {
    	super.mouseMovedOrUp(x, y, mouseEvent);
	}
    
    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected abstract void keyTyped(char eventChar, int eventKey);

}
