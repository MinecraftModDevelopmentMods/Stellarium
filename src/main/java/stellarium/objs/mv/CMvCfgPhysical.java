package stellarium.objs.mv;

import stellarium.config.ICfgArrMListener;
import stellarium.config.IConfigCategory;
import stellarium.config.IConfigProperty;
import stellarium.config.IMConfigProperty;
import stellarium.config.IPropertyRelation;
import stellarium.config.IStellarConfig;
import stellarium.construct.CPropLangRegistry;
import stellarium.objs.mv.cbody.ICBodyType;
import stellarium.objs.mv.cbody.TypeCBodyRelation;
import stellarium.objs.mv.orbit.IOrbitType;
import stellarium.objs.mv.orbit.TypeOrbitRelation;

public class CMvCfgPhysical extends CMvCfgBase implements ICfgArrMListener {

	private StellarMv ins;
	
	public CMvCfgPhysical(StellarMv pins)
	{
		super(pins, "BRProps", "Msun", "syr", "sday", "Au");
	}
	
	public void loadConfig(IStellarConfig subConfig) {
		
		//TODO Basic Properties Handling
		
		for(IConfigCategory cat : subConfig.getAllCategories())
		{
			CMvEntry ent = findEntry(cat);
			
			if(ent == null)
				addEntry(cat.getDisplayName(), findEntry(cat.getParCategory()));
			
			IConfigProperty<Double> pmass = cat.getProperty("Mass");
			ent.setMass(pmass.getVal());
			
			IConfigProperty<IOrbitType> torb = cat.getProperty("OrbT");
			
			if(torb.getVal() == null)
			{
				//TODO Exception Handling
				return;
			}
			
			ent.setOrbit(torb.getVal().provideOrbit(ent));
			
			IConfigProperty<ICBodyType> tcb = cat.getProperty("CBT");
			
			if(tcb.getVal() == null) ent.setCBody(null);
			else ent.setCBody(tcb.getVal().provideCBody(ent));
		}
		
		for(IConfigCategory cat : subConfig.getAllCategories())
		{
			CMvEntry ent = findEntry(cat);

			ent.orbit().getOrbitType().apply(ent.orbit(), cat);
			if(!ent.isVirtual())
				ent.cbody().getCBodyType().apply(ent.cbody(), cat);
		}
		
		for(IConfigCategory cat : subConfig.getAllCategories())
		{
			CMvEntry ent = findEntry(cat);

			ent.orbit().getOrbitType().formOrbit();
			if(!ent.isVirtual())
				ent.cbody().getCBodyType().formCBody();
		}
	}
	
	public void saveConfig(IStellarConfig subConfig) {
		
		//TODO Basic Properties Handling
		subConfig.getCategory("BRProps");
		
		for(IConfigCategory cat : subConfig.getAllCategories())
		{
			CMvEntry ent = findEntry(cat);
			
			IMConfigProperty<IOrbitType> typeOrbit = (IMConfigProperty)cat.getProperty("Orbit Type");
			typeOrbit.setVal(ent.orbit().getOrbitType());
			typeOrbit.setEnabled(false);
			
			ent.orbit().getOrbitType().formatConfig(cat);
			ent.orbit().getOrbitType().save(ent.orbit(), cat);
			
			IMConfigProperty<ICBodyType> typeCBody = (IMConfigProperty)cat.getProperty("CBody Type");
			
			if(!ent.isVirtual())
			{
				typeCBody.setVal(ent.cbody().getCBodyType());
				typeCBody.setEnabled(false);
				
				ent.cbody().getCBodyType().formatConfig(cat);
				ent.cbody().getCBodyType().save(ent.cbody(), cat);
				
			} else {
				typeCBody.setVal(null);
			}
		}
	}
	
}
