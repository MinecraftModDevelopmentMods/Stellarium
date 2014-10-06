package stellarium.viewrender.render;

import org.lwjgl.opengl.GL11;

import stellarium.stellars.Color;
import stellarium.util.math.Vec;

public class RFuzzy extends RCShape {
	public void SetColor(Color c){
		color=c;
	}
	
	Color color;

	@Override
	public void render() {
		CRenderEngine cre = CRenderEngine.instance;
		cre.bindTexture("star.png");
		cre.useShader("fuzzy");
		cre.setValue("lum", Lum * cre.con);
		cre.setValue("color", color);
		cre.setValue("size", Size);
		
	   	GL11.glBegin(GL11.GL_POINT);
	   	GL11.glVertex3d(Pos.x, Pos.y, Pos.z); 
		GL11.glEnd();
	}

}
