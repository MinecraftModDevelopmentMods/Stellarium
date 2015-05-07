package stellarium.mech;

import java.awt.Color;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class OpFilter {

	private final boolean isRGB;
	protected ImmutableList<WaveFilter> filters;
	
	public OpFilter(boolean isRGB) {
		this.isRGB = isRGB;
	}

	/**
	 * <code>true</code> for RGB Filter, which is same as eye.
	 * */
	public boolean isRGB() {
		return this.isRGB;
	}
	
	/**Immutable list of filter spec for each wavelengths*/
	public ImmutableList<WaveFilter> getFilterList() {
		return this.filters;
	}
	
	protected WaveFilter getWaveSpec(Wavelength wl, double eff, Color color) {
		return new WaveFilter(wl, eff, color);
	}
	
	public class WaveFilter {
		
		public WaveFilter(Wavelength wl, double eff, Color color) {
			this.wl = wl;
			this.filter_eff = eff;
			this.color = color;
		}
		
		/**
		 * <code>true</code> for RGB Filter, which is same as eye.
		 * */
		public final boolean isRGB = OpFilter.this.isRGB;
		
		/**Filtering Wavelength*/
		public final Wavelength wl;
		
		/**Filtering Efficiency (1.0 for fully transparent, 0.0 for fully opaque)*/
		public final double filter_eff;
	
		/**Output Color*/
		public final Color color;
	}
}
