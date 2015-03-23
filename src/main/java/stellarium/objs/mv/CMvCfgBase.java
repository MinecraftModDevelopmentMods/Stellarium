package stellarium.objs.mv;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Stack;

import com.google.common.collect.Lists;

import stellarium.config.EnumCategoryType;
import stellarium.config.ICfgArrMListener;
import stellarium.config.IConfigCategory;
import stellarium.config.IConfigProperty;
import stellarium.config.IMConfigProperty;
import stellarium.config.IPropertyRelation;
import stellarium.config.IStellarConfig;
import stellarium.config.core.EnumPosOption;
import stellarium.config.core.ICategoryEntry;
import stellarium.config.util.CConfigUtil;
import stellarium.config.util.CfgIteWrapper;
import stellarium.config.util.ICfgTreeWalker;
import stellarium.lang.CPropLangRegistry;
import stellarium.lang.CPropLangStrs;
import stellarium.lang.CPropLangStrsCBody;
import stellarium.objs.mv.cbody.ICBodyType;
import stellarium.objs.mv.cbody.TypeCBodyRelation;
import stellarium.objs.mv.orbit.IOrbitType;
import stellarium.objs.mv.orbit.TypeOrbitRelation;

public abstract class CMvCfgBase implements ICfgArrMListener {
	
	private StellarMvLogical ins;	
	
	public CMvCfgBase(StellarMvLogical pins)
	{
		this.ins = pins;
	}
	
	public void formatConfig(IStellarConfig cfg)
	{	
		cfg.setCategoryType(EnumCategoryType.Tree);
		
		{
			IConfigCategory props = cfg.getRootEntry().createCategory(CPropLangStrs.basicprops, EnumPosOption.Child);
			
			props.markImmutable();
			
			CPropLangStrs.addProperty(props, "udouble", CPropLangStrs.msun, 1.0);
			CPropLangStrs.addProperty(props, "udouble", CPropLangStrs.yr, 365.2564);
			CPropLangStrs.addProperty(props, "udouble", CPropLangStrs.day, 24000.0);
			CPropLangStrs.addProperty(props, "udouble", CPropLangStrs.au, 1.496e+11);
			//props.addProperty("udouble", "Stellar Distance Unit(pc)", 1.0);
		}
		
		cfg.addAMListener(this);
		
		cfg.loadCategories();
		
		if(ins.root == null)
		{
			CMvEntry ent = addEntry("Main Star", null);
			IConfigCategory cat = cfg.getRootEntry().createCategory("Main Star", EnumPosOption.Child);
			
			IConfigProperty<ICBodyType> typeCBody = cat.getProperty(CPropLangStrs.cbtype);

			typeCBody.simSetEnabled(true);
			typeCBody.simSetVal(CMvTypeRegistry.instance().getCBodyType(CPropLangStrsCBody.starbody));
			typeCBody.simSetEnabled(false);
		}
		else
		{
			for(CMvEntry entry : ins)
			{
				ICategoryEntry parent;
				ICategoryEntry child;
				
				if(entry.hasParent())
				{
					parent = findCategory(cfg, entry.getParent());
					child = parent.getChildEntry(entry.getName());
					
					if(child == null)
						parent.createCategory(entry.getName(), EnumPosOption.Child);
				}
				else
				{
					if((child = findCategory(cfg, entry)) == null)
						cfg.getRootEntry().createCategory(entry.getName(), EnumPosOption.Child);
				}
			}
		}
	}
	
	public void loadConfig(IStellarConfig subConfig) {
		
		IConfigCategory props = subConfig.getRootEntry().getChildEntry(CPropLangStrs.basicprops).getCategory();
		
		IConfigProperty<Double> mu = props.getProperty(CPropLangStrs.msun);
		IConfigProperty<Double> yr = props.getProperty(CPropLangStrs.yr);
		IConfigProperty<Double> day = props.getProperty(CPropLangStrs.day);
		IConfigProperty<Double> au = props.getProperty(CPropLangStrs.au);
		
		ins.Msun = mu.getVal();
		ins.yr = yr.getVal();
		ins.day = day.getVal();
		ins.Au = au.getVal();
		
		CConfigUtil.walkConfigTree(subConfig, new LoadWalker());
		
		postLoad(subConfig);
	}
	
	public class LoadWalker implements ICfgTreeWalker<CMvEntry> {

		@Override
		public CMvEntry getRepresentation(ICategoryEntry entry, CMvEntry parent) {
			
			if(entry.getCategory().isImmutable())
				return null;
			
			CMvEntry ent = CMvCfgBase.this.findEntry(parent, entry.getName());
			if(ent == null)
				ent = addEntry(entry.getName(), parent);
			
			return ent;
		}
		
		@Override
		public WalkState onPreWalk(ICategoryEntry entry, CMvEntry ent) {
			
			if(entry.getCategory().isImmutable())
				return WalkState.Pass;

			IConfigCategory cat = entry.getCategory();
			
			IConfigProperty<Double> pmass = cat.getProperty(CPropLangStrs.mass);
			ent.setMass(pmass.getVal());
			
			IConfigProperty<IOrbitType> torb = cat.getProperty(CPropLangStrs.orbtype);
			
			if(cat.getCategoryEntry().getParentEntry().isRootEntry())
			{
				ent.setOrbit(CMvTypeRegistry.instance().getOrbType(CPropLangStrsCBody.storb)
						.provideOrbit(ent));
			}
			else if(torb.getVal() == null)
			{
				if(handleOrbitMissing(ent, cat))
					return WalkState.Terminate;
			}
			else ent.setOrbit(torb.getVal().provideOrbit(ent));
			
			IConfigProperty<ICBodyType> tcb = cat.getProperty(CPropLangStrs.cbtype);
			
			if(tcb.getVal() == null)
			{
				if(handleCBodyMissing(ent, cat))
					return WalkState.Terminate;
			}
			else ent.setCBody(tcb.getVal().provideCBody(ent));
			
			return WalkState.Normal;
		}

		@Override
		public void onPostWalk(ICategoryEntry entry, CMvEntry ent) {

			IConfigCategory cat = entry.getCategory();
			
			if(ent.orbit() != null)
				ent.orbit().getOrbitType().apply(ent.orbit(), cat);
			
			if(!ent.isVirtual())
				ent.cbody().getCBodyType().apply(ent.cbody(), cat);
		}
	}
	
	/**@return <code>false</code> to continue loading.*/
	public abstract boolean handleOrbitMissing(CMvEntry ent, IConfigCategory cat);
	
	/**@return <code>false</code> to continue loading.*/
	public abstract boolean handleCBodyMissing(CMvEntry ent, IConfigCategory cat);
	
	public abstract void postLoad(IStellarConfig subConfig);
	
	
	public void saveConfig(IStellarConfig subConfig) {
		
		IConfigCategory props = subConfig.getRootEntry().getChildEntry(CPropLangStrs.basicprops).getCategory();
		
		IConfigProperty<Double> mu = props.getProperty(CPropLangStrs.msun);
		IConfigProperty<Double> yr = props.getProperty(CPropLangStrs.yr);
		IConfigProperty<Double> day = props.getProperty(CPropLangStrs.day);
		IConfigProperty<Double> au = props.getProperty(CPropLangStrs.au);

		mu.simSetVal(ins.Msun);
		yr.simSetVal(ins.yr);
		day.simSetVal(ins.yr);
		au.simSetVal(ins.Au);
		
		CConfigUtil.walkConfigTree(subConfig, new SaveWalker());

	}
	
	public class SaveWalker implements ICfgTreeWalker<CMvEntry> {

		@Override
		public CMvEntry getRepresentation(ICategoryEntry entry, CMvEntry parent) {
			
			if(entry.getCategory().isImmutable())
				return null;
			
			return CMvCfgBase.this.findEntry(parent, entry.getName());
		}
		
		@Override
		public WalkState onPreWalk(ICategoryEntry entry, CMvEntry ent) {
			if(entry.getCategory().isImmutable())
				return WalkState.Pass;
			
			IConfigCategory cat = entry.getCategory();
			
			IConfigProperty<IOrbitType> typeOrbit = cat.getProperty(CPropLangStrs.orbtype);
			
			if(typeOrbit == null)
			{
				if(ent.hasParent())
					if(handleOrbitMissing(ent, cat))
						return WalkState.Terminate;
			}
			else if(ent.orbit() != null)
			{
				typeOrbit.simSetEnabled(true);
				typeOrbit.simSetVal(ent.orbit().getOrbitType());
				typeOrbit.simSetEnabled(false);
				 
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

				ent.cbody().getCBodyType().save(ent.cbody(), cat);
			} else {
				typeCBody.simSetEnabled(true);
				typeCBody.simSetVal(null);
			}
			
			return WalkState.Normal;
		}

		@Override
		public void onPostWalk(ICategoryEntry entry, CMvEntry ent) { }
	}
	
	public void formatEntryCategory(IConfigCategory cat)
	{
		IConfigProperty name = CPropLangStrs.addProperty(cat, "string", CPropLangStrs.name, cat.getName());
		
		EntryNameRelation rel = new EntryNameRelation();
		rel.setCategory(cat);
		cat.addPropertyRelation(rel, name);
		
		CPropLangStrs.addProperty(cat, "udouble", CPropLangStrs.mass, 1.0);
		
		if(!cat.getCategoryEntry().getParentEntry().isRootEntry())
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
	public void onNew(ICategoryEntry parent, String name) { }
	
	@Override
	public void onPostCreated(IConfigCategory cat) {
		formatEntryCategory(cat);
		
		if(findEntry(cat.getCategoryEntry()) == null)
			onRenew(cat);
	}
	
	public void onRenew(IConfigCategory cat) {
		CMvEntry added;
		
		if(cat.getCategoryEntry().getParentEntry().isRootEntry())
			added = addEntry(cat.getName(), null);
		else {
			CMvEntry par = findEntry(cat.getCategoryEntry().getParentEntry());
			added = addEntry(cat.getName(), par);
		}
	}
	
	@Override
	public void onRemove(IConfigCategory cat) {
		this.onRemoveRaw(cat.getCategoryEntry());
	}
	
	public void onRemoveRaw(ICategoryEntry ent) {
		CMvEntry entry = findEntry(ent);
		
		if(entry != null)
			ins.removeEntry(entry);
	}
	
	@Override
	public void onMigrate(IConfigCategory cat, ICategoryEntry before) {
		onRemoveRaw(before);
		onRenew(cat);
	}

	@Override
	public void onNameChange(IConfigCategory cat, String before) {
		IConfigProperty<String> name = cat.getProperty(CPropLangStrs.name);
		
		//This will call EntryNameRelation.onValueChange(0) if name is not same
		if(!name.getVal().equals(cat.getName()))
			name.simSetVal(cat.getName());
		
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
				//This will call onNameChange()
				cat.getCategoryEntry().changeName(name.getVal());
			}
		}

	}
	
	
	public CMvEntry findEntry(CMvEntry par, String name)
	{
		if(par == null)
		{
			if(name.equals(ins.root.getName()))
				return ins.root;
		}
		else
		{
			for(CMvEntry sat : par.getSatelliteList())
			if(sat.getName().equals(name))
				return sat;
		}
		
		return null;
	}
	
	public CMvEntry findEntry(ICategoryEntry entry)
	{
		if(entry == null || entry.getParentEntry() == null)
		{
			System.err.println("Fail!");
		}
		if(entry.getParentEntry().isRootEntry())
			return ins.root;
		else return findEntry(findEntry(entry.getParentEntry()), entry.getName());
	}
	
	public ICategoryEntry findCategory(IStellarConfig cfg, CMvEntry ent)
	{
		if(ent.hasParent())
			return findCategory(cfg, ent.getParent()).getChildEntry(ent.getName());
		return cfg.getRootEntry().getChildEntry(ent.getName());
	}

}
