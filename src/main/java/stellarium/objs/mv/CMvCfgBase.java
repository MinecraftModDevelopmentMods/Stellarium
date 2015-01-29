package stellarium.objs.mv;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Stack;

import com.google.common.collect.Lists;

import stellarium.config.ICfgArrMListener;
import stellarium.config.IConfigCategory;
import stellarium.config.IConfigProperty;
import stellarium.config.IMConfigProperty;
import stellarium.config.IPropertyRelation;
import stellarium.config.IStellarConfig;
import stellarium.config.util.CfgIteWrapper;
import stellarium.construct.CPropLangRegistry;
import stellarium.construct.CPropLangStrs;
import stellarium.objs.mv.cbody.ICBodyType;
import stellarium.objs.mv.cbody.TypeCBodyRelation;
import stellarium.objs.mv.orbit.IOrbitType;
import stellarium.objs.mv.orbit.TypeOrbitRelation;

public abstract class CMvCfgBase implements ICfgArrMListener {
	
	private StellarMvLogical ins;	
	
	public CMvCfgBase(StellarMvLogical pins)
	{
		ins = pins;
	}
	
	public void formatConfig(IStellarConfig cfg)
	{	
		{
			IConfigCategory props = cfg.addCategory(CPropLangStrs.basicprops);

			cfg.markImmutable(props);
			
			CPropLangStrs.addProperty(props, "udouble", CPropLangStrs.msun, 1.0);
			CPropLangStrs.addProperty(props, "udouble", CPropLangStrs.yr, 365.2564);
			CPropLangStrs.addProperty(props, "udouble", CPropLangStrs.day, 24000.0);
			CPropLangStrs.addProperty(props, "udouble", CPropLangStrs.au, 1.496e+11);
			//props.addProperty("udouble", "Stellar Distance Unit(pc)", 1.0);
		}
		
		cfg.addAMListener(this);
		
		cfg.loadCategories();
		
		for(IConfigCategory cat : getCfgIteWrapper(cfg))
		{
			if(cfg.isImmutable(cat))
				continue;
			if(findEntry(cat) == null)
				onNew(cat);
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
				IConfigCategory encat, tmp;
				
				if(entry.hasParent())
				{
					encat = findCategory(cfg, entry.getParent());
					
					if((tmp = findSubCategory(cfg, encat, entry.getName())) != null)
						encat = tmp;
					else encat = cfg.addSubCategory(encat, entry.getName());
				}
				else
				{
					if((tmp = findCategory(cfg, entry.getName())) != null)
						encat = tmp;
					else encat = cfg.addCategory(entry.getName());
				}
				
				formatEntryCategory(encat);
			}
		}
	}
	
	public void loadConfig(IStellarConfig subConfig) {
		
		IConfigCategory props = subConfig.getCategory(CPropLangStrs.basicprops);
		
		IConfigProperty<Double> mu = props.getProperty(CPropLangStrs.msun);
		IConfigProperty<Double> yr = props.getProperty(CPropLangStrs.yr);
		IConfigProperty<Double> day = props.getProperty(CPropLangStrs.day);
		IConfigProperty<Double> au = props.getProperty(CPropLangStrs.au);

		ins.Msun = mu.getVal();
		ins.yr = yr.getVal();
		ins.day = day.getVal();
		ins.Au = au.getVal();
		
		for(IConfigCategory cat : getCfgIteWrapper(subConfig))
		{
			if(subConfig.isImmutable(cat))
				continue;
			
			CMvEntry ent = findEntry(cat);
			
			if(ent == null)
				addEntry(cat.getDisplayName(), findEntry(cat.getParCategory()));
			
			IConfigProperty<Double> pmass = cat.getProperty(CPropLangStrs.mass);
			ent.setMass(pmass.getVal());
			
			IConfigProperty<IOrbitType> torb = cat.getProperty(CPropLangStrs.orbtype);
			
			if(cat.getParCategory() != null && torb.getVal() == null)
			{
				if(handleOrbitMissing(ent, cat))
					return;
			}
			else ent.setOrbit(torb.getVal().provideOrbit(ent));
			
			IConfigProperty<ICBodyType> tcb = cat.getProperty(CPropLangStrs.cbtype);
			
			if(tcb.getVal() == null)
			{
				if(handleCBodyMissing(ent, cat))
					return;
			}
			else ent.setCBody(tcb.getVal().provideCBody(ent));
		}
		
		for(IConfigCategory cat : getCfgIteWrapper(subConfig))
		{
			if(subConfig.isImmutable(cat))
				continue;
			
			CMvEntry ent = findEntry(cat);

			if(ent.orbit() != null)
				ent.orbit().getOrbitType().apply(ent.orbit(), cat);
			
			if(!ent.isVirtual())
				ent.cbody().getCBodyType().apply(ent.cbody(), cat);
		}
		
		postLoad(subConfig);
	}
	
	/**@return <code>false</code> to continue loading.*/
	public abstract boolean handleOrbitMissing(CMvEntry ent, IConfigCategory cat);
	
	/**@return <code>false</code> to continue loading.*/
	public abstract boolean handleCBodyMissing(CMvEntry ent, IConfigCategory cat);
	
	public abstract void postLoad(IStellarConfig subConfig);
	
	
	public void saveConfig(IStellarConfig subConfig) {
		
		IConfigCategory props = subConfig.getCategory(CPropLangStrs.basicprops);
		
		IConfigProperty<Double> mu = props.getProperty(CPropLangStrs.msun);
		IConfigProperty<Double> yr = props.getProperty(CPropLangStrs.yr);
		IConfigProperty<Double> day = props.getProperty(CPropLangStrs.day);
		IConfigProperty<Double> au = props.getProperty(CPropLangStrs.au);

		mu.simSetVal(ins.Msun);
		yr.simSetVal(ins.yr);
		day.simSetVal(ins.yr);
		au.simSetVal(ins.Au);
		
		for(IConfigCategory cat : getCfgIteWrapper(subConfig))
		{
			if(subConfig.isImmutable(cat))
				continue;
			
			CMvEntry ent = findEntry(cat);
			
			IConfigProperty<IOrbitType> typeOrbit = cat.getProperty(CPropLangStrs.orbtype);
			
			if(typeOrbit != null && ent.orbit() != null)
			{
				typeOrbit.simSetEnabled(true);
				typeOrbit.simSetVal(ent.orbit().getOrbitType());
				typeOrbit.simSetEnabled(false);
				
				ent.orbit().getOrbitType().formatConfig(cat);
				ent.orbit().getOrbitType().save(ent.orbit(), cat);
			} else {
				typeOrbit.simSetEnabled(true);
				typeOrbit.simSetVal(null);
			}
			
			
			IConfigProperty<ICBodyType> typeCBody = cat.getProperty(CPropLangStrs.cbtype);
			
			if(!ent.isVirtual())
			{
				typeCBody.simSetEnabled(true);
				typeCBody.simSetVal(ent.cbody().getCBodyType());
				typeCBody.simSetEnabled(false);

				ent.cbody().getCBodyType().formatConfig(cat);
				ent.cbody().getCBodyType().save(ent.cbody(), cat);
			} else {
				typeCBody.simSetEnabled(true);
				typeCBody.simSetVal(null);
			}
		}
	}
	
	public void formatEntryCategory(IConfigCategory cat)
	{
		IConfigProperty name = CPropLangStrs.addProperty(cat, "string", CPropLangStrs.name, cat.getDisplayName());
		
		EntryNameRelation rel = new EntryNameRelation();
		rel.setCategory(cat);
		cat.addPropertyRelation(rel, name);
		
		CPropLangStrs.addProperty(cat, "udouble", CPropLangStrs.mass, 1.0);
		
		if(cat.getParCategory() != null)
		{
			IConfigProperty typeOrbit = CPropLangStrs.addProperty(cat, "typeOrbit", CPropLangStrs.orbtype, null);
			cat.addPropertyRelation(new TypeOrbitRelation(cat), typeOrbit);
		}
		
		IConfigProperty typeCBody = CPropLangStrs.addProperty(cat, "typeCBody", CPropLangStrs.cbtype, null);
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
		IConfigProperty<String> name = cat.getProperty(CPropLangStrs.name);
		
		//This will call EntryNameRelation.onValueChange(0) if name is not same
		name.simSetVal(cat.getDisplayName());
		
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
	
	public CfgIteWrapper getCfgIteWrapper(IStellarConfig cfg)
	{
		return new CfgIteWrapper(cfg);
	}
	
	
	public CMvEntry findEntry(IConfigCategory cat)
	{
		return ins.findEntry(cat.getDisplayName());
	}
	
	public IConfigCategory findCategory(IStellarConfig cfg, CMvEntry ent)
	{
		if(ent.hasParent())
			return findSubCategory(cfg, findCategory(cfg, ent.getParent()), ent.getName());
		return findCategory(cfg, ent.getName());
	}
	
	public IConfigCategory findCategory(IStellarConfig cfg, String dispname)
	{
		for(IConfigCategory sub : cfg.getAllCategories())
			if(sub.getDisplayName().equals(dispname))
				return sub;
		
		return null;
	}
	
	public IConfigCategory findSubCategory(IStellarConfig cfg, IConfigCategory cat, String dispname)
	{
		for(IConfigCategory sub : cfg.getAllSubCategories(cat))
			if(sub.getDisplayName().equals(dispname))
				return sub;
		
		return null;
	}

	
}
