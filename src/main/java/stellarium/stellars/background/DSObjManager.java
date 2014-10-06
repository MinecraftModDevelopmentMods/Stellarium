package stellarium.stellars.background;

import stellarium.util.math.Vec;

public abstract class DSObjManager {
	public abstract DSObj[] GetDsObjArray(Vec view, double dangle);
}
