package stellarium.config.gui.gui;

import java.util.Iterator;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class GuiDetailedMessage extends GuiScreen {
	
    protected GuiScreen parentScreen;
    protected String title;
    protected List<String> main;
    protected String doneButtonText;
    private int field_146353_s;

    public GuiDetailedMessage(GuiScreen parentscreen, String title, List<String> main)
    {
        this.parentScreen = parentscreen;
        this.title = title;
        this.main = main;
        this.doneButtonText = I18n.format("gui.done");
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        this.buttonList.add(new GuiButton(0, this.width / 2 - 105, this.height / 6 + 100, this.doneButtonText));
    }

    protected void actionPerformed(GuiButton button)
    {
    	this.parentScreen.initGui();
    	mc.displayGuiScreen(this.parentScreen);
    }
    
    protected void keyTyped(char p_73869_1_, int p_73869_2_) { }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, this.title, this.width / 2, 60, 0xffffff);
        
        float f = 0.9f;
        int centerx = (int) ((this.width / 2) / f);
        int centery = (int) (80 / f);
        
        GL11.glPushMatrix();
        GL11.glScaled(f, f, f);
        
        int i;
        int numcontent = Math.min(main.size(), 4);
        centery += (3 - numcontent) * fontRendererObj.FONT_HEIGHT;
        
        for(i = 0; i < Math.min(main.size(), 3); i++)
        {
        	this.drawLog(centerx, centery, main.get(i));
        	centery += 2 * fontRendererObj.FONT_HEIGHT;
        }
        
        if(main.size() > 3)
        	this.drawCenteredString(this.fontRendererObj, (main.size()-3) + " More...", (int)(this.width/(2*f)),
        			(int)(80/f) + 2 * 3 * fontRendererObj.FONT_HEIGHT, 0xdddddd);
        
        GL11.glPopMatrix();
        
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    public void drawLog(int centerx, int centery, String str)
    {    	
    	String[] strs = str.split("\\[");
    	String first = strs[0];
    	strs = strs[1].split("\\]");
    	String second = strs[0];
    	second = "[" + second + "]";
    	String third =  strs[1];
    	
    	int hwidth = this.fontRendererObj.getStringWidth(str) / 2;
    	int firstwidth = this.fontRendererObj.getStringWidth(first);
    	int secondwidth = this.fontRendererObj.getStringWidth(second);
    	
    	centerx -= hwidth;
    	this.drawString(this.fontRendererObj, first, centerx, centery, 0xcccccc);
    	
    	centerx += firstwidth;
    	this.drawString(this.fontRendererObj, second, centerx, centery, 0xeeeeee);
    	
    	centerx += secondwidth;
    	this.drawString(this.fontRendererObj, third, centerx, centery, 0xcccccc);
    }
}
