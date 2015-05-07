package stellarium.view;

import stellarium.mech.OpFilter;
import stellarium.util.math.SpCoord;

/**
 * Representation of specific entity or tile entity as 'viewer'.
 * */
public class Viewer {
	private ViewPoint viewPoint;
	private IScope scope;
	private OpFilter filter;
	private SpCoord viewPos;
	
	public ViewPoint getViewPoint() {
		return this.viewPoint;
	}
	
	public IScope getScope() {
		return this.scope;
	}
	
	public OpFilter getFilter() {
		return this.filter;
	}

	public SpCoord getPos() {
		return viewPos;
	}
}
