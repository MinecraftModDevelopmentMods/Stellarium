package stellarium.render;

import sciapi.api.value.euclidian.ECoord;
import stellarium.util.math.SpCoord;

public interface ILayerPartition {
	
	/**
	 * Updates viewing conditions.
	 * */
	public void updateView(SpCoord dir, double fov, double pxScale);
	
	/**
	 * Gives Iterable of partitioned layers.
	 * */
	public Iterable<RenderLayerPart> getLayers();

}
