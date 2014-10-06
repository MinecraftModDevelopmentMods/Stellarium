package stellarium.stellars.background;

import stellarium.util.math.Vec;

public abstract class StarManager {
	public abstract Star[] GetStarArray(Vec view, double dangle);
}
