package stellarium.stellars.background;

import stellarium.util.math.Vec;

public class BrStarManager extends StarManager{
	Star[] bgstars;

	@Override
	public Star[] GetStarArray(Vec view, double dangle) {
		return bgstars;
	}

}
