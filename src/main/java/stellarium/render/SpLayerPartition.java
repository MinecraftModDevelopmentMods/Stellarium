package stellarium.render;

import java.util.List;

import com.google.common.collect.Lists;

import sciapi.api.value.euclidian.ECoord;
import stellarium.catalog.IStellarCatalog;
import stellarium.util.math.SpCoord;

public class SpLayerPartition implements ILayerPartition {

	public static final int FRAGMENT_VERTICAL = 9;
	public static final int FRAGMENT_SIZE = 90 / FRAGMENT_VERTICAL;
	private List<RenderLayerPart> partList = Lists.newArrayList();
	
	public SpLayerPartition(IStellarCatalog catalog) {
		int cnt = 0;
		RenderLayerPart curPart;
		
		for(int i = - FRAGMENT_VERTICAL; i < FRAGMENT_VERTICAL; i++) {
			for(int j = 0; j < 4 * FRAGMENT_VERTICAL; j++) {
				partList.add(curPart = new RenderLayerPart(catalog, cnt++));
				curPart.updateView(new SpCoord(i * FRAGMENT_SIZE, j * FRAGMENT_SIZE), FRAGMENT_SIZE, 1.0);
			}
		}
	}

	@Override
	public void updateView(SpCoord dir, double fov, double pxScale) {
		for(RenderLayerPart part : partList) {
			part.updateView(null, FRAGMENT_SIZE, pxScale);
		}
	}

	@Override
	public Iterable<RenderLayerPart> getLayers() {
		return this.getLayers();
	}

}
