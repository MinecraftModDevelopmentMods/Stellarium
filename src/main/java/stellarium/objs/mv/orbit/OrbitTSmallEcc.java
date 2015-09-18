package stellarium.objs.mv.orbit;

import sciapi.api.value.IValRef;
import sciapi.api.value.euclidian.ECoord;
import sciapi.api.value.euclidian.EVector;
import stellarium.config.IConfigCategory;
import stellarium.config.IConfigProperty;
import stellarium.config.IMConfigProperty;
import stellarium.config.IPropertyRelation;
import stellarium.config.IStellarConfig;
import stellarium.config.StrMessage;
import stellarium.lang.CPropLangStrs;
import stellarium.lang.CPropLangStrsCBody;
import stellarium.objs.mv.CMvEntry;
import stellarium.objs.mv.StellarMvLogical;
import stellarium.util.UpdateDouble;
import stellarium.util.math.Rotate;
import stellarium.util.math.Spmath;
import stellarium.util.math.VecMath;

/**
 * Elliptic orbit with small eccentricity.
 * */
public class OrbitTSmallEcc implements IOrbitType {
	
	protected static final ECoord defCoord = VecMath.getDefaultCoord();

	@Override
	public String getTypeName() {
		return CPropLangStrsCBody.seccorb;
	}

	@Override
	public void init() { }

	@Override
	public void formatConfig(final IConfigCategory cat, StellarMvLogical mv) {
		//Note: every property should be inserted as degrees.
		//Note: change is per century.		
		IConfigProperty semimajor = CPropLangStrs.addProperty(cat, "udouble", CPropLangStrsCBody.semiMajorAxis, 1.0); //a
		IConfigProperty period = CPropLangStrs.addProperty(cat, "udouble", CPropLangStrsCBody.orbitalPeriod, 1.0); //P
		
		CPropLangStrs.addProperty(cat, "udouble", CPropLangStrsCBody.eccentricity, 0.0); //e
		CPropLangStrs.addProperty(cat, "double", CPropLangStrsCBody.inclination, 0.0); //i
		CPropLangStrs.addProperty(cat, "double", CPropLangStrsCBody.inclinationChange, 0.0);
		CPropLangStrs.addProperty(cat, "double", CPropLangStrsCBody.longitudeAscNode, 0.0); //omega
		CPropLangStrs.addProperty(cat, "double", CPropLangStrsCBody.longitudeAscNodeChange, 0.0);
		
		IConfigProperty isMajor = CPropLangStrs.addProperty(cat, "toggleYesNo", CPropLangStrsCBody.isMajor, true);
		
		IConfigProperty argP = CPropLangStrs.addProperty(cat, "double", CPropLangStrsCBody.argumentPeri, 0.0); //w
		IConfigProperty argPd = CPropLangStrs.addProperty(cat, "double", CPropLangStrsCBody.argumentPeriChange, 0.0);
		IConfigProperty meanA = CPropLangStrs.addProperty(cat, "double", CPropLangStrsCBody.meanAnomaly, 0.0); //M
		
		IConfigProperty longP = CPropLangStrs.addProperty(cat, "double", CPropLangStrsCBody.longitudePeri, 0.0); //wbar
		IConfigProperty longPd = CPropLangStrs.addProperty(cat, "double", CPropLangStrsCBody.longitudePeriChange, 0.0);
		IConfigProperty meanL = CPropLangStrs.addProperty(cat, "double", CPropLangStrsCBody.meanLongitude, 0.0);

		cat.addPropertyRelation(new IPropertyRelation() {
			IMConfigProperty[] props;
			IMConfigProperty<Double> semimajor;
			IMConfigProperty<Double> period;
			IConfigProperty<Double> mass;
			
			@Override
			public void setProps(IMConfigProperty... props) {
				this.props = props;
				this.semimajor = props[0];
				this.period = props[1];
				period.setEnabled(false);
				this.mass = cat.getCategoryEntry().getParentEntry().getCategory().getProperty(CPropLangStrs.mass);
			}

			@Override
			public void onEnable(int i) {
				props[1-i].setEnabled(false);
			}

			@Override
			public void onDisable(int i) {
				props[1-i].setEnabled(true);
			}

			@Override
			public void onValueChange(int i) {				
				if(i == 0) {
					double a = semimajor.getVal();
					period.setVal(Math.sqrt(a*a*a/mass.getVal()));
				} else {
					double p = period.getVal();
					semimajor.setVal(Math.pow(mass.getVal()*p*p, 1.0/3));
				}
			}

			@Override
			public String getRelationToolTip() {
				return CPropLangStrsCBody.sizeVsPeriod;
			}
			
		}, semimajor, period);
		
		cat.addPropertyRelation(new IPropertyRelation() {
			IMConfigProperty<Boolean> handleFlag;
			IMConfigProperty[] positive = new IMConfigProperty[3];
			IMConfigProperty[] negative = new IMConfigProperty[3];
			
			@Override
			public void setProps(IMConfigProperty... props) {
				this.handleFlag = props[0];
				System.arraycopy(props, 1, this.negative, 0, 3);
				System.arraycopy(props, 4, this.positive, 0, 3);
				
				for(IMConfigProperty prop : positive)
					prop.setEnabled(handleFlag.getVal());
				
				for(IMConfigProperty prop : negative)
					prop.setEnabled(!handleFlag.getVal());
			}

			@Override
			public void onEnable(int i) {
				for(IMConfigProperty prop : positive)
					prop.setEnabled(handleFlag.getVal());
				
				for(IMConfigProperty prop : negative)
					prop.setEnabled(!handleFlag.getVal());
			}

			@Override
			public void onDisable(int i) {
				for(IMConfigProperty prop : positive)
					prop.setEnabled(handleFlag.getVal());
				
				for(IMConfigProperty prop : negative)
					prop.setEnabled(!handleFlag.getVal());
			}

			@Override
			public void onValueChange(int i) {
				for(IMConfigProperty prop : positive)
					prop.setEnabled(handleFlag.getVal());
				
				for(IMConfigProperty prop : negative)
					prop.setEnabled(!handleFlag.getVal());
			}

			@Override
			public String getRelationToolTip() {
				return CPropLangStrsCBody.majorExpression;
			}
			
		}, isMajor, argP, argPd, meanA, longP, longPd, meanL);
	}

	@Override
	public void removeConfig(IConfigCategory cat) {
		cat.removeProperty(CPropLangStrsCBody.semiMajorAxis);
		cat.removeProperty(CPropLangStrsCBody.orbitalPeriod);
		cat.removeProperty(CPropLangStrsCBody.eccentricity);
		cat.removeProperty(CPropLangStrsCBody.inclination);
		cat.removeProperty(CPropLangStrsCBody.inclinationChange);
		cat.removeProperty(CPropLangStrsCBody.longitudeAscNode);
		cat.removeProperty(CPropLangStrsCBody.longitudeAscNodeChange);
		cat.removeProperty(CPropLangStrsCBody.isMajor);
		cat.removeProperty(CPropLangStrsCBody.argumentPeri);
		cat.removeProperty(CPropLangStrsCBody.argumentPeriChange);
		cat.removeProperty(CPropLangStrsCBody.meanAnomaly);
	}

	@Override
	public Orbit provideOrbit(CMvEntry e) {
		return new OrbitSmallEcc(e);
	}

	@Override
	public void apply(Orbit orbit, IConfigCategory cat) {
		OrbitSmallEcc orb = (OrbitSmallEcc) orbit;
		
		IConfigProperty<Double> aP = cat.getProperty(CPropLangStrsCBody.semiMajorAxis);
		IConfigProperty<Double> eP = cat.getProperty(CPropLangStrsCBody.eccentricity);
		IConfigProperty<Double> iP = cat.getProperty(CPropLangStrsCBody.inclination);
		IConfigProperty<Double> idP = cat.getProperty(CPropLangStrsCBody.inclinationChange);
		IConfigProperty<Double> omP = cat.getProperty(CPropLangStrsCBody.longitudeAscNode);
		IConfigProperty<Double> omdP = cat.getProperty(CPropLangStrsCBody.longitudeAscNodeChange);

		orb.a = aP.getVal();
		orb.e = eP.getVal();
		orb.i = new UpdateDouble(Spmath.Radians(iP.getVal()), Spmath.Radians(idP.getVal()) / 100.0);
		orb.Om = new UpdateDouble(Spmath.Radians(omP.getVal()), Spmath.Radians(omdP.getVal()) / 100.0);
		
		IConfigProperty<Boolean> majorP = cat.getProperty(CPropLangStrsCBody.isMajor);
		
		double parMass = orbit.getEntry().getParent().getMass();
		double md = (2 * Math.PI) * parMass / (Math.sqrt(orb.a)*orb.a);
		
		orb.isMajor = majorP.getVal();
		if(orb.isMajor) {
			IConfigProperty<Double> wbarP = cat.getProperty(CPropLangStrsCBody.longitudePeri);
			IConfigProperty<Double> wbardP = cat.getProperty(CPropLangStrsCBody.longitudePeriChange);
			IConfigProperty<Double> lP = cat.getProperty(CPropLangStrsCBody.meanLongitude);
			
			orb.w = new UpdateDouble(Spmath.Radians(wbarP.getVal()-omP.getVal()), Spmath.Radians(wbardP.getVal()-omdP.getVal()) / 100.0);
			orb.M = new UpdateDouble(Spmath.Radians(lP.getVal()-wbarP.getVal()), (md-Spmath.Radians(wbardP.getVal())) / 100.0);
		} else {
			IConfigProperty<Double> wP = cat.getProperty(CPropLangStrsCBody.argumentPeri);
			IConfigProperty<Double> wdP = cat.getProperty(CPropLangStrsCBody.argumentPeriChange);
			IConfigProperty<Double> mP = cat.getProperty(CPropLangStrsCBody.meanAnomaly);
			
			orb.w = new UpdateDouble(Spmath.Radians(wP.getVal()), Spmath.Radians(wdP.getVal()) / 100.0);
			orb.M = new UpdateDouble(Spmath.Radians(mP.getVal()), md / 100.0);
		}
		
		orb.hillRadius = orb.a * (1-orb.e) * Math.pow(orb.getEntry().getMass()/(3.0*parMass), 1.0/3.0);
	}

	@Override
	public void save(Orbit orbit, IConfigCategory cat) {
		OrbitSmallEcc orb = (OrbitSmallEcc) orbit;

		IConfigProperty<Double> aP = cat.getProperty(CPropLangStrsCBody.semiMajorAxis);
		IConfigProperty<Double> eP = cat.getProperty(CPropLangStrsCBody.eccentricity);
		IConfigProperty<Double> iP = cat.getProperty(CPropLangStrsCBody.inclination);
		IConfigProperty<Double> idP = cat.getProperty(CPropLangStrsCBody.inclinationChange);
		IConfigProperty<Double> omP = cat.getProperty(CPropLangStrsCBody.longitudeAscNode);
		IConfigProperty<Double> omdP = cat.getProperty(CPropLangStrsCBody.longitudeAscNodeChange);
		
		aP.simSetEnabled(true);
		aP.simSetVal(orb.a);
		
		eP.simSetEnabled(true);
		eP.simSetVal(orb.e);
		
		iP.simSetEnabled(true);
		iP.simSetVal(Spmath.Degrees(orb.i.getValue(0.0)));
		
		idP.simSetEnabled(true);
		idP.simSetVal(Spmath.Degrees(100.0 * orb.i.getDifference()));
		
		omP.simSetEnabled(true);
		omP.simSetVal(Spmath.Degrees(orb.Om.getValue(0.0)));

		omdP.simSetEnabled(true);
		omdP.simSetVal(100.0 * orb.Om.getDifference());
		
		
		IConfigProperty<Boolean> majorP = cat.getProperty(CPropLangStrsCBody.isMajor);
		
		majorP.simSetEnabled(true);
		majorP.simSetVal(orb.isMajor);
		
		if(orb.isMajor) {
			IConfigProperty<Double> wbarP = cat.getProperty(CPropLangStrsCBody.longitudePeri);
			IConfigProperty<Double> wbardP = cat.getProperty(CPropLangStrsCBody.longitudePeriChange);
			IConfigProperty<Double> lP = cat.getProperty(CPropLangStrsCBody.meanLongitude);
			
			wbarP.simSetEnabled(true);
			wbarP.simSetVal(Spmath.Degrees(orb.w.getValue(0.0) + orb.Om.getValue(0.0)));
			
			wbardP.simSetEnabled(true);
			wbardP.simSetVal(Spmath.Degrees(100.0 * (orb.w.getDifference() + orb.Om.getDifference())));
			
			lP.simSetEnabled(true);
			lP.simSetVal(Spmath.Degrees(orb.M.getValue(0.0)) + wbarP.getVal());
		} else {
			IConfigProperty<Double> wP = cat.getProperty(CPropLangStrsCBody.argumentPeri);
			IConfigProperty<Double> wdP = cat.getProperty(CPropLangStrsCBody.argumentPeriChange);
			IConfigProperty<Double> mP = cat.getProperty(CPropLangStrsCBody.meanAnomaly);
			
			wP.simSetEnabled(true);
			wP.simSetVal(orb.w.getValue(0.0));
			
			wdP.simSetEnabled(true);
			wdP.simSetVal(100.0 * orb.w.getDifference());
			
			mP.simSetEnabled(true);
			mP.simSetVal(orb.M.getValue(0.0));
		}
	}

	@Override
	public void formOrbit(Orbit orb, IStellarConfig cfg) {
		OrbitSmallEcc orbit = (OrbitSmallEcc) orb;
		if(orbit.e > 0.5)
			cfg.addLoadFailMessage(CPropLangStrsCBody.eccOutOfRange,
					new StrMessage(CPropLangStrs.getExpl(CPropLangStrsCBody.eccOutOfRange)));
		
		for(CMvEntry child : orb.getEntry().getSatelliteList()) {
			if(child.orbit().getMaxDist() > orbit.getInfluenceSize())
				cfg.addLoadFailMessage(CPropLangStrsCBody.bodyEscaped,
					new StrMessage(CPropLangStrs.getExpl(CPropLangStrsCBody.bodyEscaped)));
		}
	}

	@Override
	public void setScaled(Orbit ref, Orbit target, double scale) {
		OrbitSmallEcc re = (OrbitSmallEcc) ref;
		OrbitSmallEcc tar = (OrbitSmallEcc) target;
		
		tar.a = re.a * scale;
		tar.e = re.e;
		tar.i = re.i.clone();
		tar.w = re.w.clone();
		tar.Om = re.Om.clone();
		tar.M = re.M.clone();
	}

	@Override
	public boolean hasParent() {
		return true;
	}

	@Override
	public void onRemove(Orbit orbit) { }

	public class OrbitSmallEcc extends Orbit
	{		
		protected double a, e;
		
		// These are saved as Radians.
		protected UpdateDouble w;
		protected UpdateDouble i;
		protected UpdateDouble Om;
		protected UpdateDouble M;
		
		protected boolean isMajor;
		
		protected double hillRadius;
		
		public OrbitSmallEcc(CMvEntry e) {
			super(e);
			
			this.position = new EVector(3);
			this.velocity = new EVector(3);
		}
		
		@Override
		public void update(double year) {
			Orbit parent = getEntry().getParent().orbit();
			
			this.updateOrbitalElements(year);
			
			parent.getPosition(0.0);
			position.set(VecMath.add(this.calculateRelativePos(), parent.getPosition(0.0)));
			
			parent.getCurrentVelocity();
			velocity.set(VecMath.add(this.calculateVelocity(), parent.getCurrentVelocity()));
		}
		
		protected void updateOrbitalElements(double year) {
			i.update(year);
			w.update(year);
			Om.update(year);
			M.update(year);
		}
		
		protected IValRef<EVector> calculateRelativePos(){
			return Spmath.getOrbVec(a, e, new Rotate('X').setRAngle(-i.getValue()), new Rotate('Z').setRAngle(-w.getValue()), new Rotate('Z').setRAngle(-Om.getValue()), M.getValue());
		}
		
		protected IValRef<EVector> calculateVelocity() {
			return null;
		}

		@Override
		public ECoord getOrbCoord(double year) {
			ECoord coord = VecMath.rotateCoordX(defCoord, Spmath.Radians(i.getValue(year)));
			return VecMath.rotateCoordZ(coord, Spmath.Radians(Om.getValue(year)));
		}

		@Override
		public double getAvgSize() {
			return this.a;
		}
		
		@Override
		public double getMaxDist() {
			return this.a * (1 + this.e);
		}
		
		@Override
		public double getInfluenceSize() {
			return this.hillRadius;
		}

		@Override
		public IOrbitType getOrbitType() {
			return OrbitTSmallEcc.this;
		}
	}
}
