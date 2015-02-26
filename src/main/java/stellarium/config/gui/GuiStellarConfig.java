package stellarium.config.gui;

import org.lwjgl.input.Keyboard;

import stellarium.config.ILoadSaveHandler;
import cpw.mods.fml.client.config.GuiButtonExt;
import cpw.mods.fml.client.config.GuiConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class GuiStellarConfig extends GuiScreen {
	
	private GuiScreen parentScreen;
	private String title;
	private GuiConfigHandler handler;
	private boolean needsRefresh;
	
	public GuiStellarConfig(GuiScreen parentScreen, GuiConfigHandler handler, String title)
	{
		this.mc = Minecraft.getMinecraft();
        this.parentScreen = parentScreen;
        this.title = title;
        this.handler = handler;
	}
	
    @Override
    public void initGui()
    {
    	handler.onFormat();
    	handler.onSave();
    	
    	if(this.needsRefresh)
    	{
    		this.needsRefresh = false;
    	}
    	
        Keyboard.enableRepeatEvents(true);
        
        int textWidth = Math.max(mc.fontRenderer.getStringWidth(I18n.format("gui.done")) + 60, 200);
        int buttonWidthHalf = textWidth / 2;
        this.buttonList.add(new GuiButtonExt(2000, this.width / 2 - buttonWidthHalf, this.height - 29, textWidth, 20, I18n.format("gui.done")));
    }
    
    @Override
    public void onGuiClosed()
    {
        if (this.parentScreen instanceof GuiStellarConfig)
        {
        	GuiStellarConfig parentGuiConfig = (GuiStellarConfig) this.parentScreen;
            parentGuiConfig.needsRefresh = true;
            parentGuiConfig.initGui();
        }
        else Keyboard.enableRepeatEvents(false);
    	
    }
    
    @Override
    protected void actionPerformed(GuiButton button)
    {
    	if (button.id == 2000)
    	{
    		handler.onApply();
    		
    		if(handler.hasLoadFailMessages())
    		{
    			StringBuilder builder = new StringBuilder();
    			
    			for(String str : handler.getLoadFailMessages())
    				builder.append(String.format("- %s\n", str));
    			
    			String built = builder.toString();
    			
        		// TODO Exception Handling
    			
    			return;
    		}
    		
    		this.mc.displayGuiScreen(this.parentScreen);
    	}
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
    @Override
    protected void keyTyped(char eventChar, int eventKey)
    {
        if (eventKey == Keyboard.KEY_ESCAPE)
            this.mc.displayGuiScreen(parentScreen);
    }

    /**
     * Called from the main game loop to update the screen.
     */
    @Override
    public void updateScreen()
    {
        super.updateScreen();
    }

    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, this.title, this.width / 2, 8, 16777215);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
