package stellarium.config.gui.gui;

import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.config.GuiButtonExt;
import cpw.mods.fml.client.config.GuiCheckBox;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.config.GuiMessageDialog;
import cpw.mods.fml.client.config.GuiUnicodeGlyphButton;
import cpw.mods.fml.client.config.HoverChecker;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import cpw.mods.fml.client.event.ConfigChangedEvent.PostConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.Event.Result;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ChatComponentText;

public class GuiStellarConfigMain extends GuiScreen {
	
	private GuiScreen parentScreen;
	protected String title = "Stellarium";

	public GuiStellarConfigMain(GuiScreen parentScreen)
	{
		this.mc = Minecraft.getMinecraft();
        this.parentScreen = parentScreen;
	}
	
	public GuiStellarConfigMain(GuiScreen parentScreen, String customtitle)
	{
		this.mc = Minecraft.getMinecraft();
        this.parentScreen = parentScreen;
        this.title = customtitle;
	}
	
    @Override
    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        
        int textWidth = Math.max(mc.fontRenderer.getStringWidth(I18n.format("gui.done")) + 60, 200);
        int buttonWidthHalf = textWidth / 2;
        int buttonHeight, buttonHeightHalf;
        this.buttonList.add(new GuiButtonExt(2000, this.width / 2 - buttonWidthHalf, this.height - 29, textWidth, 20, I18n.format("gui.done")));
        
        int i = 2000, j = 0;
        int size = StellarCfgGuiRegistry.getProvList().size();
        
        buttonHeight =  20;
        buttonHeightHalf = buttonHeight / 2;
        int heightIntervalHalf = 12;
        
        for(IGuiCfgProvider prov : StellarCfgGuiRegistry.getProvList())
        {
        	i++;
            textWidth = 160;
            buttonWidthHalf = textWidth / 2;

        	this.buttonList.add(new GuiButtonExt(i, this.width / 2 - buttonWidthHalf, this.height / 2 - buttonHeightHalf + heightIntervalHalf * (2 * j - size),
        			textWidth, buttonHeight, I18n.format(prov.getUnlocalizedName())));
        	j++;
        }
    }
    
    @Override
    public void onGuiClosed()
    {
    	Keyboard.enableRepeatEvents(false);
    }
    
    @Override
    protected void actionPerformed(GuiButton button)
    {
    	if (button.id == 2000)
    		this.mc.displayGuiScreen(this.parentScreen);
        
    	int i = button.id - 2000 - 1;
    	
    	if(i >= 0 && i < StellarCfgGuiRegistry.getProvList().size())
    		this.mc.displayGuiScreen(StellarCfgGuiRegistry.getProvList().get(i).getCfgGui(this));
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
        this.drawBackground(0, this.width, 23, this.height - 33, 2105376);
        this.drawCenteredString(this.fontRendererObj, this.title , this.width / 2, 8, 16777215);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    public void drawBackground(int left, int right, int top, int bottom, int color)
    {
    	Tessellator tessellator = Tessellator.instance;
    	this.mc.getTextureManager().bindTexture(Gui.optionsBackground);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float f1 = 32.0F;
        tessellator.startDrawingQuads();
        tessellator.setColorOpaque_I(color);
        tessellator.addVertexWithUV((double)left, (double)bottom, 0.0D, (double)((float)left / f1), (double)((float)bottom / f1));
        tessellator.addVertexWithUV((double)right, (double)bottom, 0.0D, (double)((float)right / f1), (double)((float)bottom / f1));
        tessellator.addVertexWithUV((double)right, (double)top, 0.0D, (double)((float)right / f1), (double)((float)top / f1));
        tessellator.addVertexWithUV((double)left, (double)top, 0.0D, (double)((float)left / f1), (double)((float)top / f1));
        tessellator.draw();
        
        
        int b0 = 5;
        
        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(770, 771, 0, 1);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_I(0, 0);
        tessellator.addVertexWithUV((double)left, (double)(top + b0), 0.0D, 0.0D, 1.0D);
        tessellator.addVertexWithUV((double)right, (double)(top + b0), 0.0D, 1.0D, 1.0D);
        tessellator.setColorRGBA_I(0, 255);
        tessellator.addVertexWithUV((double)right, (double)top, 0.0D, 1.0D, 0.0D);
        tessellator.addVertexWithUV((double)left, (double)top, 0.0D, 0.0D, 0.0D);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_I(0, 255);
        tessellator.addVertexWithUV((double)left, (double)bottom, 0.0D, 0.0D, 1.0D);
        tessellator.addVertexWithUV((double)right, (double)bottom, 0.0D, 1.0D, 1.0D);
        tessellator.setColorRGBA_I(0, 0);
        tessellator.addVertexWithUV((double)right, (double)(bottom - b0), 0.0D, 1.0D, 0.0D);
        tessellator.addVertexWithUV((double)left, (double)(bottom - b0), 0.0D, 0.0D, 0.0D);
        tessellator.draw();
        
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_BLEND);
    }

}
