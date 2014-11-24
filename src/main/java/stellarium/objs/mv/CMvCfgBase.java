package stellarium.objs.mv;

import stellarium.config.ICfgArrMListener;
import stellarium.config.IConfigCategory;
import stellarium.config.IConfigProperty;
import stellarium.config.IMConfigProperty;
import stellarium.config.IPropertyRelation;
import stellarium.config.IStellarConfig;
import stellarium.construct.CPropLangRegistry;
import stellarium.construct.CPropLangStrs;
import stellarium.objs.mv.cbody.ICBodyType;
import stellarium.objs.mv.cbody.TypeCBodyRelation;
import stellarium.objs.mv.orbit.IOrbitType;
import stellarium.objs.mv.orbit.TypeOrbitRelation;

public abstract class CMvCfgBase implements ICfgArrMListener {
	
	private StellarMvLogical ins;
	
	protected String bprop;
	protected String msun;
	protected String yr;
	protected String day;
	protected String au;
	
	
	public CMvCfgBase(StellarMvLogical pins, String bp, String ms, String pyr, String pday, String pau)
	{
		ins = pins;
		
		bprop = bp;
		msun = ms;
		yr = pyr;
		day = pday;
		au = pau;
	}
	
	public void formatConfig(IStellarConfig cfg) {
		
		{
			IConfigCategory props = cfg.addCategory(CPropLangStrs.basicprops);

			cfg.markImmutable(props);
			
			props.addProperty("double", CPropLangStrs.msun, 1.0).setExpl(CPropLangStrs.getExpl(CPropLangStrs.msun));
			props.addProperty("double", CPropLangStrs.yr, 365.2560).setExpl(CPropLangStrs.getExpl(CPropLangStrs.yr));
			props.addProperty("double", CPropLangStrs.day, 24000.0).setExpl(CPropLangStrs.getExpl(CPropLangStrs.day));
			props.addProperty("double", CPropLangStrs.au, 1.496e+11).setExpl(CPropLangStrs.getExpl(CPropLangStrs.au));
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
				formatEntryCategory(cfg.getCategory(entry.getName()));
		}
		
		cfg.addAMListener(this);
	}
	
	public void loadConfig(IStellarConfig subConfig) {
		
		//TODO Basic Properties Handling
		
		IConfigCategory props = subConfig.getCategory(CPropLangStrs.basicprops);
		
		IConfigProperty<Double> mu = props.getProperty(CPropLangStrs.msun);
		
		ins.Msun = mu.getVal();
		
		for(IConfigCategory cat : subConfig.getAllCategories())
		{
			CMvEntry ent = findEntry(cat);
			
			if(ent == null)
				addEntry(cat.getDisplayName(), findEntry(cat.getParCategory()));
			
			IConfigProperty<Double> pmass = cat.getProperty(CPropLangStrs.mass);
			ent.setMass(pmass.getVal());
			
			IConfigProperty<IOrbitType> torb = cat.getProperty(CPropLangStrs.orbtype);
			
			if(torb.getVal() == null)
			{
				//TODO Exception Handling
				return;
			}
			
			ent.setOrbit(torb.getVal().provideOrbit(ent));
			
			IConfigProperty<ICBodyType> tcb = cat.getProperty(CPropLangStrs.cbtype);
			
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
			
			IMConfigProperty<IOrbitType> typeOrbit = (IMConfigProperty)cat.getProperty(CPropLangStrs.orbtype);
			typeOrbit.setVal(ent.orbit().getOrbitType());
			typeOrbit.setEnabled(false);
			
			IMConfigProperty<ICBodyType> typeCBody = (IMConfigProperty)cat.getProperty(CPropLangStrs.cbtype);
			typeCBody.setVal(ent.cbody().getCBodyType());
			typeCBody.setEnabled(false);
			
			ent.orbit().getOrbitType().formatConfig(cat);
			ent.orbit().getOrbitType().save(ent.orbit(), cat);
			
			ent.cbody().getCBodyType().formatConfig(cat);
			ent.cbody().getCBodyType().save(ent.cbody(), cat);
		}
	}
	
	public void formatEntryCategory(IConfigCategory cat)
	{
		IConfigProperty name = cat.addProperty("string", CPropLangStrs.name, cat.getDisplayName()).setExpl(CPropLangStrs.getExpl(CPropLangStrs.name));
		
		EntryNameRelation rel = new EntryNameRelation();
		rel.setCategory(cat);
		cat.addPropertyRelation(rel, name);
		
		cat.addProperty("double", CPropLangStrs.mass, 1.0).setExpl(CPropLangStrs.getExpl(CPropLangStrs.mass));
		
		IConfigProperty typeOrbit = cat.addProperty("typeOrbit", CPropLangStrs.orbtype, null).setExpl(CPropLangStrs.getExpl(CPropLangStrs.orbtype));
		cat.addPropertyRelation(new TypeOrbitRelation(cat), typeOrbit);
		
		IConfigProperty typeCBody = cat.addProperty("typeCBody", CPropLangStrs.cbtype, null).setExpl(CPropLangStrs.getExpl(CPropLangStrs.cbtype));
		cat.addPropertyRelation(new TypeCBodyRelation(cat), typeCBody);
	}
	
	public CMvEntry addEntry(String name, CMvEntry par)
	{
		CMvEntry e = ins.newEntry(par, name);
		return e;
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
		ins.removeEntry(findEntry(cat));
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
		IMConfigProperty<String> name = (IMConfigProperty)cat.getProperty(CPropLangStrs.name);
		
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
