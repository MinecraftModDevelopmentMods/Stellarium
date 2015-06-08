package stellarium.render;

import java.util.Collections;
import java.util.Set;

import com.google.common.collect.Iterables;

import sciapi.api.value.euclidian.ECoord;
import stellarium.catalog.IStellarCatalog;
import stellarium.util.math.SpCoord;

public class SingleLayerPartition implements ILayerPartition {
	
	private RenderLayerPart singlePart;
	private Set<RenderLayerPart> setPart;

	public SingleLayerPartition(IStellarCatalog catalog) {
		this.singlePart = new RenderLayerPart(catalog, 0);
		this.setPart = Collections.singleton(singlePart);
	}

	@Override
	public void updateView(SpCoord dir, double fov, double pxScale) {
		singlePart.updateView(dir, fov, pxScale);
	}

	@Override
	public Iterable<RenderLayerPart> getLayers() {
		return this.setPart;
	}

}
