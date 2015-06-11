package stellarium.objs.mv.orbit;

import stellarium.config.IConfigCategory;
import stellarium.objs.mv.CMvEntry;

public class OrbitTBase implements IOrbitType {

	@Override
	public String getTypeName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void formatConfig(IConfigCategory cat) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeConfig(IConfigCategory cat) {
		// TODO Auto-generated method stub

	}

	@Override
	public Orbit provideOrbit(CMvEntry e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void apply(Orbit orbit, IConfigCategory cfg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void save(Orbit orbit, IConfigCategory cfg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void formOrbit(Orbit orb) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setScaled(Orbit ref, Orbit target, double scale) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean hasParent() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onRemove(Orbit orbit) {
		// TODO Auto-generated method stub

	}

}
