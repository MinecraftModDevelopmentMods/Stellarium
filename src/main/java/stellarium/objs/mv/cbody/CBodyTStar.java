package stellarium.objs.mv.cbody;

import stellarium.config.IConfigCategory;
import stellarium.lang.CPropLangStrsCBody;
import stellarium.mech.Wavelength;
import stellarium.objs.EnumSObjType;
import stellarium.objs.mv.CMvEntry;
import stellarium.world.CWorldProvider;

public class CBodyTStar implements ICBodyType {

	@Override
	public String getTypeName() {
		return CPropLangStrsCBody.starbody;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void formatConfig(IConfigCategory cfg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeConfig(IConfigCategory cat) {
		// TODO Auto-generated method stub

	}

	@Override
	public CBody provideCBody(CMvEntry e) {
		return new CBodyStar(e);
	}

	@Override
	public void apply(CBody body, IConfigCategory cfg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void save(CBody body, IConfigCategory cfg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void formCBody(CBody body) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setCopy(CBody ref, CBody target) {
		// TODO Auto-generated method stub

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
	public CWorldProvider getCWorldProvider() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public class CBodyStar extends CBody {

		public CBodyStar(CMvEntry e) {
			super(e);
			// TODO Auto-generated constructor stub
		}

		@Override
		public double getRadius() {
			// TODO Auto-generated method stub
			return 0;
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
