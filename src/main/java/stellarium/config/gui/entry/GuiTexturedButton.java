package stellarium.config.gui.entry;

import org.lwjgl.opengl.GL11;

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
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            
            if(this.btnbgEnabled) {
            	p_146112_1_.getTextureManager().bindTexture(buttonTextures);
            	this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 46 + k * 20, this.width / 2, this.height);
            	this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2, 46 + k * 20, this.width / 2, this.height);
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
            this.drawTexturedRect(this.xPosition, this.yPosition, this.width, this.height, k-1, this.nState);
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
