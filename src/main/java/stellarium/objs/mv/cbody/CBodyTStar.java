package stellarium.objs.mv.cbody;

import stellarium.config.IConfigCategory;
import stellarium.config.IConfigProperty;
import stellarium.config.IStellarConfig;
import stellarium.lang.CPropLangStrs;
import stellarium.lang.CPropLangStrsCBody;
import stellarium.mech.Wavelength;
import stellarium.objs.EnumSObjType;
import stellarium.objs.mv.CMvEntry;
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
	public void formatConfig(IConfigCategory cat, boolean isMain) {
		super.formatConfig(cat, isMain);
		IConfigProperty luminosity = CPropLangStrs.addProperty(cat, "udouble", CPropLangStrsCBody.periodRotation, 1.0);
		IConfigProperty temperature = CPropLangStrs.addProperty(cat, "udouble", CPropLangStrsCBody.periodRotation, 1.0);
	}

	@Override
	public void removeConfig(IConfigCategory cat) {
		super.removeConfig(cat);
	}

	@Override
	public CBody provideCBody(CMvEntry e) {
		return new CBodyStar(e);
	}

	@Override
	public void apply(CBody body, IConfigCategory cfg) {
		super.apply(body, cfg);
	}

	@Override
	public void save(CBody body, IConfigCategory cfg) {
		super.save(body, cfg);
	}

	@Override
	public void formCBody(CBody body, IStellarConfig cfg) {
		// TODO Auto-generated method stub
		
		super.formCBody(body, cfg);
		
		body.getEntry().getMain().registerLightSource(body);
	}

	@Override
	public void setCopy(CBody ref, CBody target) {
		// TODO Auto-generated method stub
		super.setCopy(ref, target);
	}

	@Override
	public void onRemove(CBody body) {
		// TODO Auto-generated method stub

	}

	@Override
	public ICBodyRenderer getCBodyRenderer() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean hasWorld() { return false; }

	@Override
	public IWorldHandler provideWorldHandler() { return null; }
	
	public class CBodyStar extends CBody {

		private double luminosity, temperature;
		
		public CBodyStar(CMvEntry e) {
			super(e);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public double getMag(Wavelength wl) {
			// TODO Auto-generated method stub
			return 0;
		}
		
		@Override
		public void update(double day) {
			// TODO Auto-generated method stub
			
		}

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
