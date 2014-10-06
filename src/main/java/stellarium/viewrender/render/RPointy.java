package stellarium.viewrender.render;

import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.opengl.GL11;

import stellarium.stellars.Color;
import stellarium.util.math.Spmath;
import stellarium.util.math.Vec;

public class RPointy extends RBase {
	
	public Color color;
	private Tessellator tessellator = Tessellator.instance;

	public RPointy SetColor(Color c){
		color=c;
		return this;
	}

	@Override
	public void render() {
		CRenderEngine cre = CRenderEngine.instance;
		cre.bindTexture("star.png");
		cre.useShader("pointy");
		cre.setValue("lum", Lum * cre.con);
		cre.setValue("color", color);
		     
		GL11.glPointSize((float) Spmath.Radians(cre.res));
	   	GL11.glBegin(GL11.GL_POINT);
	   	GL11.glVertex3d(Pos.x, Pos.y, Pos.z); 
		GL11.glEnd();
		
	}

}
