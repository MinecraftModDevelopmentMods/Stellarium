package stellarium.render;

import java.util.Map;

import stellarium.mech.OpFilter.WaveFilter;
import stellarium.objs.IStellarObj;

import com.google.common.collect.Maps;

public class StellarRenderingRegistry {
	
	private static final int RENDERER_MAX = 256;
	
	private static StellarRenderingRegistry instance = new StellarRenderingRegistry();
	
	public static StellarRenderingRegistry instance()
	{
		return instance;
	}
	
    private ISObjRenderer[] sobjRenderers = new ISObjRenderer[RENDERER_MAX];
	private int nextId;
	
	public static void registerRenderer(int id, ISObjRenderer renderer){
		if(id < 0 || id >= RENDERER_MAX)
			throw new IllegalArgumentException(
					"Invalid Renderer ID: " + id + "\n"
					+ "Cannot accept id with < 0 or > " + RENDERER_MAX);
		
		instance().sobjRenderers[id] = renderer;
	}
	
	public static int nextRenderId()
	{
		return instance().nextId;
	}
	
	
	public static ISObjRenderer getRenderer(int id)
	{
		if(instance().sobjRenderers == null)
			throw new IllegalArgumentException("There is no Renderer with ID: " + id);
		
		return instance().sobjRenderers[id];
	}

}
