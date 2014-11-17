package stellarium.objs.mv;

import stellarium.config.ICfgArrMListener;
import stellarium.config.IConfigCategory;
import stellarium.config.IConfigProperty;
import stellarium.config.IMConfigProperty;
import stellarium.config.IPropertyRelation;
import stellarium.config.IStellarConfig;

public class CMvCfgManager implements ICfgArrMListener {

	private StellarMv ins;
	
	public CMvCfgManager(StellarMv mvc)
	{
		ins = mvc;
	}
	
	public void formatConfig(IStellarConfig cfg) {
		IConfigCategory props = cfg.addCategory("Basic Properties");
		
		cfg.markImmutable(props);
		
		props.addProperty("double", "Planetary Mass Unit(Solar Mass)", 1.0);
		props.addProperty("double", "Revolution Period Unit(Year)", 1.0);
		props.addProperty("double", "Rotation Period Unit(Day)", 1.0);
		props.addProperty("double", "Planetary Distance Unit(Au)", 1.0);
		//props.addProperty("double", "Stellar Distance Unit(pc)", 1.0);
		
		if(ins.root == null)
		{
			
			
			cfg.addCategory("Base Orbit");
			formatEntryCategory(cfg.getCategory("Base Orbit"));
		}
		
		cfg.addAMListener(this);
	}
	
	public void formatEntryCategory(IConfigCategory cat)
	{
		IConfigProperty name = cat.addProperty("string", "Name", cat.getDisplayName());
		
		EntryNameRelation rel = new EntryNameRelation();
		cat.addPropertyRelation(rel, name);
		
		cat.addProperty("double", "Mass", 1.0);
		
		cat.addProperty("typeOrbit", "Orbit Type", null);
		
		cat.addProperty("typeCBody", "CBody Type", null);
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
	
	@Override
	public void onRemove(IConfigCategory cat) {
		removeEntry(ins.findEntry(cat.getDisplayName()));
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onChangeParent(IConfigCategory cat, IConfigCategory from,
			IConfigCategory to) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onChangeOrder(IConfigCategory cat, int before, int after) { }

	@Override
	public void onDispNameChange(IConfigCategory cat, String before) {
		IMConfigProperty<String> name = (IMConfigProperty)cat.getProperty("Name");
		
		//This will call EntryNameRelation.onValueChange(0) if name is not same
		name.setVal(cat.getDisplayName());
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
