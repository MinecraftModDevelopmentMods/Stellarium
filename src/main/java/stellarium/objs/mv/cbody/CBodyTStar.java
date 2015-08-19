package stellarium.objs.mv.cbody;

import sciapi.api.value.euclidian.EVector;
import sciapi.api.value.util.BOp;
import stellarium.config.IConfigCategory;
import stellarium.config.IConfigProperty;
import stellarium.config.IMConfigProperty;
import stellarium.config.IPropertyRelation;
import stellarium.config.IStellarConfig;
import stellarium.config.StrMessage;
import stellarium.lang.CPropLangStrs;
import stellarium.lang.CPropLangStrsCBody;
import stellarium.lighting.ILightSource;
import stellarium.lighting.ILightingData;
import stellarium.mech.Wavelength;
import stellarium.objs.EnumSObjType;
import stellarium.objs.mv.CMvEntry;
import stellarium.objs.mv.StellarMvLogical;
import stellarium.objs.mv.cbody.renderer.CBodyStarRenderer;
import stellarium.util.math.Spmath;
import stellarium.util.math.VecMath;
import stellarium.view.ViewPoint;
import stellarium.world.CWorldProviderPart;
import stellarium.world.IWorldHandler;

public class CBodyTStar extends CBodyTBase implements ICBodyType {
	
	@Override
	public String getTypeName() {
		return CPropLangStrsCBody.starbody;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void formatConfig(IConfigCategory cat, final StellarMvLogical mv, boolean isMain) {
		super.formatConfig(cat, mv, isMain);
		IConfigProperty radius = CPropLangStrs.addProperty(cat, "udouble", CPropLangStrsCBody.radius, 1.0);
		IConfigProperty luminosity = CPropLangStrs.addProperty(cat, "udouble", CPropLangStrsCBody.luminosity, 1.0);
		IConfigProperty temperature = CPropLangStrs.addProperty(cat, "udouble", CPropLangStrsCBody.temperature, 1.0);
		
		cat.addPropertyRelation(new IPropertyRelation() {

			IMConfigProperty<Double> lum, temp, rad;
			IMConfigProperty[] props;
			
			@Override
			public void setProps(IMConfigProperty... props) {
				this.props = props;

				this.lum = props[0];
				this.temp = props[1];
				this.rad = props[2];
			}

			@Override
			public void onEnable(int i) {
				props[(i+1)%3].setEnabled(false);
			}

			@Override
			public void onDisable(int i) {
				props[(i+1)%3].setEnabled(true);
			}

			@Override
			public void onValueChange(int i) {
				if(i == 0) {
					double l = lum.getVal();
					double d = rad.getVal();
					temp.setVal(mv.Tsun * Math.sqrt(Math.sqrt(l) / d));
				} else {
					double t = temp.getVal() / mv.Tsun;
					double td = t * t * rad.getVal();
					lum.setVal(td * td);
				}
			}

			@Override
			public String getRelationToolTip() {
				return CPropLangStrsCBody.lumTempRelation;
			}
			
		}, luminosity, temperature, radius);
	}

	@Override
	public void removeConfig(IConfigCategory cat) {
		super.removeConfig(cat);
		cat.removeProperty(CPropLangStrsCBody.radius);
		cat.removeProperty(CPropLangStrsCBody.luminosity);
		cat.removeProperty(CPropLangStrsCBody.temperature);
	}

	@Override
	public CBody provideCBody(CMvEntry e) {
		return new CBodyStar(e);
	}

	@Override
	public void apply(CBody body, IConfigCategory cfg) {
		super.apply(body, cfg);
		CBodyStar star = (CBodyStar) body;
		
		IConfigProperty<Double> radius = cfg.getProperty(CPropLangStrsCBody.radius);
		IConfigProperty<Double> luminosity = cfg.getProperty(CPropLangStrsCBody.luminosity);
		IConfigProperty<Double> temperature = cfg.getProperty(CPropLangStrsCBody.temperature);
		
		star.radius = radius.getVal();
		star.luminosity = luminosity.getVal();
		star.temperature = temperature.getVal();
	}

	@Override
	public void save(CBody body, IConfigCategory cfg) {
		super.save(body, cfg);
		CBodyStar star = (CBodyStar) body;

		IConfigProperty<Double> radius = cfg.getProperty(CPropLangStrsCBody.radius);
		IConfigProperty<Double> luminosity = cfg.getProperty(CPropLangStrsCBody.luminosity);
		IConfigProperty<Double> temperature = cfg.getProperty(CPropLangStrsCBody.temperature);
		
		radius.simSetVal(star.radius);
		luminosity.simSetVal(star.luminosity);
		temperature.simSetVal(star.temperature);
	}

	@Override
	public void formCBody(CBody body, IStellarConfig cfg) {
		// TODO Auto-generated method stub
		super.formCBody(body, cfg);
		CBodyStar star = (CBodyStar) body;
		
		if(Double.isInfinite(star.luminosity) || Double.isNaN(star.luminosity))
			cfg.addLoadFailMessage(CPropLangStrsCBody.invalidLuminosity,
					new StrMessage(CPropLangStrs.getExpl(CPropLangStrsCBody.invalidLuminosity)));
		if(Double.isInfinite(star.temperature) || Double.isNaN(star.temperature))
			cfg.addLoadFailMessage(CPropLangStrsCBody.invalidTemperature,
					new StrMessage(CPropLangStrs.getExpl(CPropLangStrsCBody.invalidTemperature)));
		
		body.getEntry().getMain().registerLightSource(star);
	}

	@Override
	public void setCopy(CBody ref, CBody target) {
		super.setCopy(ref, target);
		CBodyStar refStar = (CBodyStar) ref;
		CBodyStar tarStar = (CBodyStar) target;
		tarStar.temperature = refStar.temperature;
		tarStar.luminosity = refStar.luminosity;
	}

	@Override
	public void onRemove(CBody body) {
		// TODO Auto-generated method stub
		CBodyStar star = (CBodyStar) body;
		body.getEntry().getMain().unregisterLightSource(star);
	}

	@Override
	public ICBodyRenderer getCBodyRenderer() {
		return new CBodyStarRenderer();
	}
	
	@Override
	public boolean hasWorld() { return false; }

	@Override
	public IWorldHandler provideWorldHandler() { return null; }
	
	public class CBodyStar extends CBody implements ILightSource {

		private double luminosity, temperature;
		
		public CBodyStar(CMvEntry e) {
			super(e);
			// TODO Auto-generated constructor stub
		}

		@Override
		public double getLuminosity(Wavelength wl) {
			return luminosity * Spmath.getBoltzmannRate(this.temperature, wl.wlen, wl.getWidth());
		}
		
		@Override
		public void update(double day) { }

		@Override
		public String getDescription() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public EnumSObjType getType() {
			return EnumSObjType.Star;
		}

		@Override
		public ICBodyType getCBodyType() {
			return CBodyTStar.this;
		}
	}

}
