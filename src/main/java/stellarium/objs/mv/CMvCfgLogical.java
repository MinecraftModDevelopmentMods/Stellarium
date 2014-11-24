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

public class CMvCfgLogical extends CMvCfgBase implements ICfgArrMListener {

	private StellarMvLogical ins;
	
	public CMvCfgLogical(StellarMvLogical pins)
	{
		super(pins, "Basic Properties",
				"Planetary Mass Unit(Solar Mass)",
				"Revolution Period Unit(Sidereal Year, in Day)",
				"Rotation Period Unit(Sidereal Day, in Tick)",
				"Planetary Distance Unit(Au, in m)");
	}
	
	public void loadConfig(IStellarConfig subConfig) {
		
		//TODO Basic Properties Handling
		
		IConfigCategory props = subConfig.getCategory(bprop);
		
		IConfigProperty<Double> mu = props.getProperty(msun);
		
		ins.Msun = mu.getVal();
		
		for(IConfigCategory cat : subConfig.getAllCategories())
		{
			CMvEntry ent = findEntry(cat);
			
			if(ent == null)
				addEntry(cat.getDisplayName(), findEntry(cat.getParCategory()));
			
			IConfigProperty<Double> pmass = cat.getProperty("Mass");
			ent.setMass(pmass.getVal());
			
			IConfigProperty<IOrbitType> torb = cat.getProperty("Orbit Type");
			
			if(torb.getVal() == null)
			{
				//TODO Exception Handling
				return;
			}
			
			ent.setOrbit(torb.getVal().provideOrbit(ent));
			
			IConfigProperty<ICBodyType> tcb = cat.getProperty("CBody Type");
			
			if(tcb.getVal() == null)
			{
				//TODO Exception Handling
				return;
			}
			
			ent.setCBody(tcb.getVal().provideCBody(ent));
		}
		
		for(IConfigCategory cat : subConfig.getAllCategories())
		{
			CMvEntry ent = findEntry(cat);

			ent.orbit().getOrbitType().apply(ent.orbit(), cat);
			ent.cbody().getCBodyType().apply(ent.cbody(), cat);
		}
	}
	
	public void saveConfig(IStellarConfig subConfig) {
		
		//TODO Basic Properties Handling
		
		for(IConfigCategory cat : subConfig.getAllCategories())
		{
			CMvEntry ent = findEntry(cat);
			
			IMConfigProperty<IOrbitType> typeOrbit = (IMConfigProperty)cat.getProperty("Orbit Type");
			typeOrbit.setVal(ent.orbit().getOrbitType());
			typeOrbit.setEnabled(false);
			
			IMConfigProperty<ICBodyType> typeCBody = (IMConfigProperty)cat.getProperty("CBody Type");
			typeCBody.setVal(ent.cbody().getCBodyType());
			typeCBody.setEnabled(false);
			
			ent.orbit().getOrbitType().formatConfig(cat);
			ent.orbit().getOrbitType().save(ent.orbit(), cat);
			
			ent.cbody().getCBodyType().formatConfig(cat);
			ent.cbody().getCBodyType().save(ent.cbody(), cat);
		}
	}

}
