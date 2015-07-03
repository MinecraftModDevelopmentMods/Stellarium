package stellarium.objs.mv.orbit;

import sciapi.api.value.euclidian.ECoord;
import sciapi.api.value.euclidian.EVector;
import stellarium.config.IConfigCategory;
import stellarium.config.IConfigProperty;
import stellarium.config.IStellarConfig;
import stellarium.lang.CPropLangStrs;
import stellarium.lang.CPropLangStrsCBody;
import stellarium.objs.mv.CMvEntry;
import stellarium.util.math.VecMath;

/**
 * Stationary orbit, which is the only root orbit.
 * */
public class OrbTStationary implements IOrbitType {

	@Override
	public String getTypeName() {
		return CPropLangStrsCBody.storb;
	}

	@Override
	public void init() { }

	@Override
	public void formatConfig(IConfigCategory cat) {
		CPropLangStrs.addProperty(cat, "vector3", CPropLangStrsCBody.position, new EVector(0.0, 0.0, 0.0));
	}

	@Override
	public void removeConfig(IConfigCategory cat) {
		cat.removeProperty(CPropLangStrsCBody.position);
	}

	@Override
	public Orbit provideOrbit(CMvEntry e) {
		return new OrbitStationary(e);
	}

	@Override
	public void apply(Orbit orbit, IConfigCategory cfg) {
		IConfigProperty<EVector> propPos = cfg.getProperty(CPropLangStrsCBody.position);
		orbit.position = propPos.getVal();
	}

	@Override
	public void save(Orbit orbit, IConfigCategory cfg) {
		IConfigProperty<EVector> propPos = cfg.getProperty(CPropLangStrsCBody.position);
		
		propPos.simSetEnabled(true);
		propPos.simSetVal(orbit.position);
	}

	@Override
	public void formOrbit(Orbit orb, IStellarConfig cfg) { }

	@Override
	public void setScaled(Orbit ref, Orbit target, double scale) {
		//Does not called
	}

	@Override
	public boolean hasParent() {
		return false;
	}

	@Override
	public void onRemove(Orbit orbit) {
		OrbitStationary orb = (OrbitStationary) orbit;
		orb.defCoord = null;
	}

	public class OrbitStationary extends Orbit
	{
		private ECoord defCoord;
		
		public OrbitStationary(CMvEntry e) {
			super(e);
			
			this.velocity = new EVector(0.0, 0.0, 0.0);
			this.defCoord = VecMath.getDefaultCoord();
		}
		
		@Override
		public void update(double year) { }

		@Override
		public ECoord getOrbCoord(double year) {
			return this.defCoord;
		}

		@Override
		public double getAvgSize() {
			return 0.0;
		}
		
		@Override
		public double getMaxDist() {
			return 0.0;
		}

		@Override
		public IOrbitType getOrbitType() {
			return OrbTStationary.this;
		}
		
	}
}
