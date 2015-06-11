package stellarium.objs.mv.cbody;

import stellarium.config.IConfigCategory;
import stellarium.config.IConfigProperty;
import stellarium.config.IMConfigProperty;
import stellarium.config.IPropertyRelation;
import stellarium.lang.CPropLangStrs;
import stellarium.lang.CPropLangStrsCBody;
import stellarium.objs.mv.CMvEntry;
import stellarium.util.math.VecMath;
import stellarium.world.CWorldProvider;

public abstract class CBodyTBase implements ICBodyType {

	@Override
	public void formatConfig(IConfigCategory cfg, boolean isMain) {
		// TODO Auto-generated method stub
		
		IConfigProperty tidalLock = null;
		
		if(!isMain)
		{
			 tidalLock = CPropLangStrs.addProperty(cfg, "toggleYesNo", CPropLangStrsCBody.tidalLocked, false);
		}
		
		IConfigProperty periodRot = CPropLangStrs.addProperty(cfg, "udouble", CPropLangStrsCBody.periodRotation, 1.0);
		
		IConfigProperty hasPrec = CPropLangStrs.addProperty(cfg, "toggleYesNo", CPropLangStrsCBody.hasPrecession, false);
		IConfigProperty periodPrec = CPropLangStrs.addProperty(cfg, "udouble", CPropLangStrsCBody.periodPrecession, 26000.0);
		
		cfg.addPropertyRelation(new IPropertyRelation() {
			IMConfigProperty<Boolean> hasPrec;
			IMConfigProperty periodPrec;
			
			@Override
			public void setProps(IMConfigProperty... props) {
				this.hasPrec = props[0];
				this.periodPrec = props[1];
			}

			@Override
			public void onEnable(int i) {
				if(i == 1)
					periodPrec.setEnabled(hasPrec.getVal());
			}

			@Override
			public void onDisable(int i) { }

			@Override
			public void onValueChange(int i) {
				periodPrec.setEnabled(hasPrec.getVal());
			}

			@Override
			public String getRelationToolTip() {
				return "";
			}
			
		}, hasPrec, periodPrec);
		
		if(!isMain) {
			cfg.addPropertyRelation(new IPropertyRelation() {
				
				IMConfigProperty<Boolean> check;
				IMConfigProperty[] props;
				
				@Override
				public void setProps(IMConfigProperty... props) {
					this.check = props[0];
					this.props = new IMConfigProperty[props.length - 1];
					System.arraycopy(props, 1, this.props, 0, props.length - 1);
				}

				@Override
				public void onEnable(int i) {
					if(check.getVal())
						for(IMConfigProperty prop : props)
							prop.setEnabled(false);
				}

				@Override
				public void onDisable(int i) { }

				@Override
				public void onValueChange(int i) {
					if(i == 0 && check.getVal())
						for(IMConfigProperty prop : props)
							prop.setEnabled(false);
				}

				@Override
				public String getRelationToolTip() {
					return "";
				}
				
			}, tidalLock, periodRot, hasPrec, periodPrec);
		}
	}

	@Override
	public void removeConfig(IConfigCategory cat) {
		// TODO Auto-generated method stub
		cat.removeProperty(CPropLangStrsCBody.tidalLocked);
		cat.removeProperty(CPropLangStrsCBody.periodRotation);
		cat.removeProperty(CPropLangStrsCBody.hasPrecession);
		cat.removeProperty(CPropLangStrsCBody.periodPrecession);
	}

	@Override
	public void apply(CBody body, IConfigCategory cfg) {
		// TODO Auto-generated method stub
		body.isTidalLocked = (boolean) cfg.getProperty(CPropLangStrsCBody.tidalLocked).getVal();
		boolean hasPrec = (boolean) cfg.getProperty(CPropLangStrsCBody.hasPrecession).getVal();
		
		if(!body.isTidalLocked) {
			body.w_rot = 2 * Math.PI / (double) cfg.getProperty(CPropLangStrsCBody.periodRotation).getVal();
			
			if(hasPrec)
				body.w_prec = 2 * Math.PI / (double) cfg.getProperty(CPropLangStrsCBody.periodPrecession).getVal();
			else body.w_prec = 0.0;
		} else {
			body.w_rot = 2 * Math.PI / body.getEntry().orbit().getAvgPeriod();
		}
	}

	@Override
	public void save(CBody body, IConfigCategory cfg) {
		// TODO Auto-generated method stub
		cfg.getProperty(CPropLangStrsCBody.tidalLocked).simSetVal(body.isTidalLocked);
		
		if(!body.isTidalLocked) {
			cfg.getProperty(CPropLangStrsCBody.periodRotation).simSetEnabled(true);
			cfg.getProperty(CPropLangStrsCBody.periodRotation).simSetVal(2 * Math.PI / body.w_rot);
			
			cfg.getProperty(CPropLangStrsCBody.hasPrecession).simSetEnabled(true);
			
			if(body.w_prec != 0.0) {
				cfg.getProperty(CPropLangStrsCBody.hasPrecession).simSetVal(true);
				
				cfg.getProperty(CPropLangStrsCBody.periodPrecession).simSetEnabled(true);
				cfg.getProperty(CPropLangStrsCBody.periodPrecession).simSetVal(2 * Math.PI / body.w_prec);
			} else {
				cfg.getProperty(CPropLangStrsCBody.hasPrecession).simSetVal(false);
			}
		}
	}

	@Override
	public void formCBody(CBody body) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCopy(CBody ref, CBody target) {
		// TODO Auto-generated method stub
		target.initialCoord = VecMath.copyCoord(ref.initialCoord);
		target.isTidalLocked = ref.isTidalLocked;
		target.w_prec = ref.w_prec;
		target.w_rot = ref.w_rot;
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

}
