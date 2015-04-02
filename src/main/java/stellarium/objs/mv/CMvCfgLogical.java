package stellarium.objs.mv;

import net.minecraft.client.resources.I18n;
import stellarium.config.ICfgArrMListener;
import stellarium.config.IConfigCategory;
import stellarium.config.IConfigProperty;
import stellarium.config.IMConfigProperty;
import stellarium.config.IPropertyRelation;
import stellarium.config.IStellarConfig;
import stellarium.config.StrMessage;
import stellarium.lang.CPropLangRegistry;
import stellarium.lang.CPropLangStrs;
import stellarium.objs.mv.cbody.ICBodyType;
import stellarium.objs.mv.cbody.TypeCBodyRelation;
import stellarium.objs.mv.orbit.IOrbitType;
import stellarium.objs.mv.orbit.TypeOrbitRelation;

public class CMvCfgLogical extends CMvCfgBase implements ICfgArrMListener {

	private StellarMvLogical ins;
	
	public CMvCfgLogical(StellarMvLogical pins)
	{
		super(pins);
	}

	@Override
	public boolean handleOrbitMissing(CMvEntry ent, IConfigCategory cat) {
		cat.getConfig().addLoadFailMessage(CPropLangStrs.orbmissing,
				new StrMessage(CPropLangStrs.getExpl(CPropLangStrs.orbmissing), cat.getName()));
		return true;
	}

	@Override
	public boolean handleCBodyMissing(CMvEntry ent, IConfigCategory cat) {
		cat.getConfig().addLoadFailMessage(CPropLangStrs.cbmissing,
				new StrMessage(CPropLangStrs.getExpl(CPropLangStrs.cbmissing), cat.getName()));
		return true;
	}
	
	@Override
	public boolean handleOrbitNotLocked(CMvEntry ent, IConfigCategory cat) {
		cat.getConfig().addLoadFailMessage(CPropLangStrs.orbNotLocked,
				new StrMessage(CPropLangStrs.getExpl(CPropLangStrs.orbNotLocked), cat.getName()));
		return true;
	}

	@Override
	public boolean handleCBodyNotLocked(CMvEntry ent, IConfigCategory cat) {
		cat.getConfig().addLoadFailMessage(CPropLangStrs.cbNotLocked,
				new StrMessage(CPropLangStrs.getExpl(CPropLangStrs.cbNotLocked), cat.getName()));
		return true;
	}

	@Override
	public void postLoad(IStellarConfig subConfig) { }
	
}
