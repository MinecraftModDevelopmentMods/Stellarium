package stellarium.objs.mv;

import stellarium.config.ICfgArrMListener;
import stellarium.config.IConfigCategory;
import stellarium.config.IConfigProperty;
import stellarium.config.IMConfigProperty;
import stellarium.config.IPropertyRelation;
import stellarium.config.IStellarConfig;
import stellarium.objs.mv.CMvCfgLogical.EntryNameRelation;
import stellarium.objs.mv.cbody.ICBodyType;
import stellarium.objs.mv.cbody.TypeCBodyRelation;
import stellarium.objs.mv.orbit.IOrbitType;
import stellarium.objs.mv.orbit.TypeOrbitRelation;

public class CMvCfgPhysical implements ICfgArrMListener {

	private StellarMv ins;
	
	public CMvCfgPhysical(StellarMv pins)
	{
		ins = pins;
	}
	
	public void formatConfig(IStellarConfig cfg) {
		
		{
			IConfigCategory props = cfg.addCategory("Basic Properties");

			cfg.markImmutable(props);
			
			props.addProperty("double", "Planetary Mass Unit(Solar Mass)", 1.0);
			props.addProperty("double", "Revolution Period Unit(Sidereal Year, in Day)", 365.2564);
			props.addProperty("double", "Rotation Period Unit(Sidereal Day, in Tick)", 23934.5);
			props.addProperty("double", "Planetary Distance Unit(Au, in m)", 1.496e+11);
			//props.addProperty("double", "Stellar Distance Unit(pc)", 1.0);
		}
		
		
		if(ins.root == null)
		{
			cfg.addCategory("Base Orbit");
			addEntry("Base Orbit", null);
			formatEntryCategory(cfg.getCategory("Base Orbit"));
		}
		else
		{
			for(CMvEntry entry : ins)
			{
				//TODO Virtual Orbit Processing
				formatEntryCategory(cfg.getCategory(entry.getName()));
			}
		}
		
		cfg.addAMListener(this);
	}
	
	public void loadConfig(IStellarConfig subConfig) {
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
			
			IConfigProperty<ICBodyType> tcb = cat.getProperty("Orbit Type");
			
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

			ent.orbit().getOrbitType().populate(ent.orbit(), cat);
			ent.cbody().getCBodyType().populate(ent.cbody(), cat);
		}
	}
	
	public void saveConfig(IStellarConfig subConfig) {
		for(IConfigCategory cat : subConfig.getAllCategories())
		{
			CMvEntry ent = findEntry(cat);
			
			if(ent.isVirtual())
				continue;
			
			IMConfigProperty<IOrbitType> typeOrbit = (IMConfigProperty)cat.getProperty("Orbit Type");
			typeOrbit.setVal(ent.orbit().getOrbitType());
			
			IMConfigProperty<ICBodyType> typeCBody = (IMConfigProperty)cat.getProperty("CBody Type");
			typeCBody.setVal(ent.cbody().getCBodyType());
			
			ent.orbit().getOrbitType().save(ent.orbit(), cat);
			ent.cbody().getCBodyType().save(ent.cbody(), cat);
		}
	}
	
	public void formatEntryCategory(IConfigCategory cat)
	{
		IConfigProperty name = cat.addProperty("string", "Name", cat.getDisplayName());
		
		EntryNameRelation rel = new EntryNameRelation();
		rel.setCategory(cat);
		cat.addPropertyRelation(rel, name);
		
		cat.addProperty("double", "Mass", 1.0);
		
		IConfigProperty typeOrbit = cat.addProperty("typeOrbit", "Orbit Type", null);
		cat.addPropertyRelation(new TypeOrbitRelation(cat), typeOrbit);
		
		IConfigProperty typeCBody = cat.addProperty("typeCBody", "CBody Type", null);
		cat.addPropertyRelation(new TypeCBodyRelation(cat), typeCBody);
	}
	
	public CMvEntry addEntry(String name, CMvEntry par)
	{
		CMvEntry e = ins.newEntry(par, name);
		return e;
	}
	
	public void removeEntry(CMvEntry mv)
	{
		mv.additive().getAdditiveType().onRemove(mv.additive());
		mv.cbody().getCBodyType().onRemove(mv.cbody());
		mv.orbit().getOrbitType().onRemove(mv.orbit());
		
		mv.getParent().removeSatellite(mv);
		ins.bodies.remove(mv.cbody());
	}
	
	@Override
	public void onNew(IConfigCategory cat) {
		formatEntryCategory(cat);
		
		CMvEntry par = findEntry(cat.getParCategory());
		CMvEntry added = addEntry(cat.getDisplayName(), par);
	}
	
	public void onRenew(IConfigCategory cat) {		
		CMvEntry par = findEntry(cat.getParCategory());
		CMvEntry added = addEntry(cat.getDisplayName(), par);
	}
	
	@Override
	public void onRemove(IConfigCategory cat) {
		removeEntry(findEntry(cat));
	}

	@Override
	public void onChangeParent(IConfigCategory cat, IConfigCategory from,
			IConfigCategory to) {
		onRemove(cat);
		onRenew(cat);
	}

	@Override
	public void onChangeOrder(IConfigCategory cat, int before, int after) { }

	@Override
	public void onDispNameChange(IConfigCategory cat, String before) {
		IMConfigProperty<String> name = (IMConfigProperty)cat.getProperty("Name");
		
		//This will call EntryNameRelation.onValueChange(0) if name is not same
		name.setVal(cat.getDisplayName());
		
		onRemove(cat);
		onRenew(cat);
	}
	
	
	public class EntryNameRelation implements IPropertyRelation {

		IMConfigProperty<String> name;
		IConfigCategory cat;
		
		@Override
		public void setProps(IMConfigProperty... props) {
			name = props[0];
		}
		
		public void setCategory(IConfigCategory pcat)
		{
			cat = pcat;
		}

		@Override
		public void onEnable(int i) { }
		@Override
		public void onDisable(int i) { }

		@Override
		public void onValueChange(int i) {
			if(i == 0)
			{
				//This will call StellarMv.onDispNameChange()
				cat.setDisplayName(name.getVal());
			}
		}

	}
	
	public CMvEntry findEntry(IConfigCategory cat)
	{
		return ins.findEntry(cat.getDisplayName());
	}

}
