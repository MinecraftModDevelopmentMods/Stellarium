package stellarium.view;

import sciapi.api.value.euclidian.ECoord;
import stellarium.mech.OpFilter;
import stellarium.util.math.SpCoord;

/**
 * Representation of specific entity or tile entity as 'viewer'.
 * */
public interface IViewer {
	public ViewPoint getViewPoint();
	public IScope getScope();
	public OpFilter getFilter();
	public ECoord getViewCoord();
	public SpCoord getViewPos();
}
