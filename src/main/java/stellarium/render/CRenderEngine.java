package stellarium.render;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Map;

import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;

import com.google.common.collect.Maps;

public class CRenderEngine {
	
	public static final CRenderEngine instance = new CRenderEngine();
	
	public Map<String, SavedTexture> savedTexMap = Maps.newHashMap();
	
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

}
