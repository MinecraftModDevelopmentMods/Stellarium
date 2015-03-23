package stellarium.objs.mv;

import stellarium.config.ICfgArrMListener;
import stellarium.config.IConfigCategory;
import stellarium.config.IConfigProperty;
import stellarium.config.IMConfigProperty;
import stellarium.config.IPropertyRelation;
import stellarium.config.IStellarConfig;
import stellarium.config.StrMessage;
import stellarium.config.util.CConfigUtil;
import stellarium.lang.CPropLangRegistry;
import stellarium.lang.CPropLangStrs;
import stellarium.objs.mv.cbody.ICBodyType;
import stellarium.objs.mv.cbody.TypeCBodyRelation;
import stellarium.objs.mv.orbit.IOrbitType;
import stellarium.objs.mv.orbit.TypeOrbitRelation;

public class CMvCfgPhysical extends CMvCfgBase implements ICfgArrMListener {

	private StellarMv ins;
	
	public CMvCfgPhysical(StellarMv pins)
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
		ent.setCBody(null);
		return false;
	}

	@Override
	public void postLoad(IStellarConfig subConfig) {
		for(IConfigCategory cat : CConfigUtil.getCfgIteWrapper(subConfig))
		{
			if(cat.isImmutable())
				continue;
			
			CMvEntry ent = findEntry(cat.getCategoryEntry());

			ent.orbit().getOrbitType().formOrbit(ent.orbit());
			if(!ent.isVirtual())
				ent.cbody().getCBodyType().formCBody(ent.cbody());
		}
	}
	
}
