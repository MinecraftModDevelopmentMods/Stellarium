package stellarium.render;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Map;

import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;

import sciapi.api.value.euclidian.ECoord;
import stellarium.util.math.SpCoord;

import com.google.common.collect.Maps;

public class CRenderEngine {
	
	public static final CRenderEngine instance = new CRenderEngine();
	
	public Tessellator tessellator = Tessellator.instance;
	private Map<String, SavedTexture> savedTexMap = Maps.newHashMap();
	
	/**
	 * Saves rendering to specific frame buffer.
	 * */
	public void preSaveRendered(String id, int width, int height) {
		SavedTexture texture = savedTexMap.get(id);
		
		if(texture == null) {
			texture = new SavedTexture(width, height);
			texture.loadTexture();
		}
		
		EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, texture.getGlFramebufferId());
		GL11.glPushAttrib(GL11.GL_VIEWPORT_BIT);
		GL11.glViewport(0, 0, width, height);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
	}
	
	/**
	 * call after rendering to frame buffer.
	 * */
	public void postSaveRendered() {
		EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, 0);
		GL11.glPopAttrib();
	}
	
	/**
	 * Binds the texture from the frame buffer.
	 * */
	public void loadRendered(String id) {
		SavedTexture texture = savedTexMap.get(id);

		if(texture != null)
		{
	        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getGlTextureId());
		} else {
			throw new IllegalArgumentException("Texture with id [" + id + "] does not exist.");
		}
	}
	
	/**Renders whole textured rectangle in specific depth*/
	public void renderTexturedRect(double x1, double y1, double x2, double y2, double depth) {
		tessellator.addVertexWithUV(depth, x1, y2, 0.0, 1.0);
		tessellator.addVertexWithUV(depth, x2, y2, 1.0, 1.0);
		tessellator.addVertexWithUV(depth, x2, y1, 1.0, 0.0);
		tessellator.addVertexWithUV(depth, x1, y1, 0.0, 0.0);
		tessellator.draw();
	}

	/**Points spherical position with x axis*/
	public void pointSpCoord(SpCoord coord) {
		GL11.glRotated(coord.x, 0.0, 0.0, 1.0);
		GL11.glRotated(coord.y, 0.0, 1.0, 0.0);
	}

	/**Reverse pointing spherical position with x axis*/
	public void reverseSpCoord(SpCoord coord) {
		GL11.glRotated(coord.y, 0.0, -1.0, 0.0);
		GL11.glRotated(coord.x, 0.0, 0.0, -1.0);
	}

	/**Converts from a coordinate to default coordinate*/
	public void convertFrom(ECoord sysCoord) {
		// TODO Auto-generated method stub
		
	}
	
	/**Converts to a coordinate from default coordinate*/
	public void convertTo(ECoord sysCoord) {
		// TODO Auto-generated method stub
		
	}


}
