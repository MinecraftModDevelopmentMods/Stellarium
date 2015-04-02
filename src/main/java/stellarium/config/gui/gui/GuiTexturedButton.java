package stellarium.config.gui.gui;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.config.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

public class GuiTexturedButton extends GuiButton {

    public ResourceLocation resLocation;
	private boolean btnbgEnabled = true;
	private int nState = 1;
    
	public GuiTexturedButton(int id, int left, int top,
			int width, int height, ResourceLocation resLocation) {
		super(id, left, top, width, height, "");
		this.resLocation = resLocation;
	}
	
	/**
	 * Sets if Button Background is enabled.
	 * if it is disabled, it will use the resLocation as button image.
	 * */
	public void setButtonBackgroundEnabled(int nState, boolean btnbgEnabled)
	{
		this.btnbgEnabled = btnbgEnabled;
		this.nState = nState;
	}
	
	@Override
    public void drawButton(Minecraft p_146112_1_, int p_146112_2_, int p_146112_3_)
    {
        if (this.visible)
        {
        	Tessellator tessellator = Tessellator.instance;
            this.field_146123_n = p_146112_2_ >= this.xPosition && p_146112_3_ >= this.yPosition && p_146112_2_ < this.xPosition + this.width && p_146112_3_ < this.yPosition + this.height;
            int k = this.getHoverState(this.field_146123_n);
            int texWidth = this.width, texHeight = this.height;
            int xPos = this.xPosition, yPos = this.yPosition;
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            
            if(this.btnbgEnabled) {
                GuiUtils.drawContinuousTexturedBox(buttonTextures, this.xPosition, this.yPosition, 0, 46 + k * 20, this.width, this.height, 200, 20, 2, 3, 2, 2, this.zLevel);
            	
            	texWidth = this.width - 8;
                texHeight = this.height - 8;
                xPos = this.xPosition + 4;
                yPos = this.yPosition + 4;
            }
            this.mouseDragged(p_146112_1_, p_146112_2_, p_146112_3_);
            
            k = this.btnbgEnabled? 0 : k;
            int l = 14737632;

            if (packedFGColour != 0)
            {
                l = packedFGColour;
            }
            else if (!this.enabled)
            {
                l = 10526880;
            }
            else if (this.field_146123_n)
            {
                l = 16777120;
            }

            tessellator.setColorOpaque_I(l);
            
            p_146112_1_.getTextureManager().bindTexture(resLocation);

            this.drawTexturedRect(xPos, yPos, texWidth, texHeight, k-1, this.nState);
        }
    }
	
    public void drawTexturedRect(int xPos, int yPos, int width, int height, int index, int size)
    {    	
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((double)(xPos + 0), (double)(yPos + height), (double)this.zLevel, 0.0d, (double)((float)(index + 1) / size));
        tessellator.addVertexWithUV((double)(xPos + width), (double)(yPos + height), (double)this.zLevel, 1.0d, (double)((float)(index + 1) / size));
        tessellator.addVertexWithUV((double)(xPos + width), (double)(yPos + 0), (double)this.zLevel, 1.0d, (double)((float)(index + 0) / size));
        tessellator.addVertexWithUV((double)(xPos + 0), (double)(yPos + 0), (double)this.zLevel, 0.0d, (double)((float)(index + 0) / size));
        tessellator.draw();
    }
	
}
