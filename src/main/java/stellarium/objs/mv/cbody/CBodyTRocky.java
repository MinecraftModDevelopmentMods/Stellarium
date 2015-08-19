package stellarium.objs.mv.cbody;

import stellarium.config.IConfigCategory;
import stellarium.config.IStellarConfig;
import stellarium.lang.CPropLangStrsCBody;
import stellarium.mech.Wavelength;
import stellarium.objs.EnumSObjType;
import stellarium.objs.mv.CMvEntry;
import stellarium.objs.mv.StellarMvLogical;
import stellarium.view.ViewPoint;
import stellarium.world.IWorldHandler;

public class CBodyTRocky extends CBodyTBase implements ICBodyType {

	@Override
	public String getTypeName() {
		return CPropLangStrsCBody.rockbody;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void formatConfig(IConfigCategory cat, StellarMvLogical mv,
			boolean isMain) {
		// TODO Auto-generated method stub
		super.formatConfig(cat, mv, isMain);
	}

	@Override
	public void removeConfig(IConfigCategory cat) {
		// TODO Auto-generated method stub
		super.removeConfig(cat);
	}

	@Override
	public CBody provideCBody(CMvEntry e) {
		return new CBodyRocky(e);
	}

	@Override
	public void apply(CBody body, IConfigCategory cat) {
		// TODO Auto-generated method stub
		super.apply(body, cat);
	}

	@Override
	public void save(CBody body, IConfigCategory cat) {
		// TODO Auto-generated method stub
		super.apply(body, cat);
	}

	@Override
	public void formCBody(CBody body, IStellarConfig cfg) {
		// TODO Auto-generated method stub
		super.formCBody(body, cfg);
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
	public boolean hasWorld() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IWorldHandler provideWorldHandler() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public class CBodyRocky extends CBody {

		public CBodyRocky(CMvEntry e) {
			super(e);
			// TODO Auto-generated constructor stub
		}

		@Override
		public String getDescription() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public EnumSObjType getType() {
			return EnumSObjType.Planet;
		}

		@Override
		public void update(double day) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public ICBodyType getCBodyType() {
			return CBodyTRocky.this;
		}
		
	}

}
