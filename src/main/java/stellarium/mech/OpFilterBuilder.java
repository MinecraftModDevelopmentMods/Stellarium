package stellarium.mech;

import java.awt.Color;

import com.google.common.collect.ImmutableList;

public class OpFilterBuilder {
	
	private OpFilter filter;
	private ImmutableList.Builder<OpFilter.WaveFilter> waveBuilder = ImmutableList.builder();
	
	public OpFilterBuilder() {
		this.filter = new OpFilter(false);
	}
	
	public OpFilterBuilder(boolean isRGB) {
		this.filter = new OpFilter(isRGB);
		if(filter.isRGB())
			this.addWaveSpec(null, 1.0, null);
	}
	
	/**
	 * Adds Wave specific spec to the filter.
	 * @param wl the wavelength
	 * @param eff the efficiency of filter(1.0 for fully transparent, 0.0 for fully opaque)
	 * @param color the color to display this wave
	 * */
	public void addWaveSpec(Wavelength wl, double eff, Color color) {
		if(!filter.isRGB())
			waveBuilder.add(filter.getWaveSpec(wl, eff, color));
	}
	
	public OpFilter build() {
		filter.filters = waveBuilder.build();
		return this.filter;
	}

}
