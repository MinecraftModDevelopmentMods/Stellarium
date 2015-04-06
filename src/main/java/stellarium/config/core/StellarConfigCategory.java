package stellarium.config.core;

import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import net.minecraft.item.ItemStack;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonObject;

import stellarium.config.ICfgArrMListener;
import stellarium.config.IConfigCategory;
import stellarium.config.IConfigProperty;
import stellarium.config.IMConfigProperty;
import stellarium.config.IPropertyRelation;
import stellarium.config.IStellarConfig;
import stellarium.config.core.handler.ICategoryHandler;
import stellarium.config.core.handler.IPropertyHandler;
import stellarium.config.core.handler.NullCategoryHandler;

public class StellarConfigCategory implements IConfigCategory {
	
	protected final StellarConfiguration config;
	protected String name;
	protected ICategoryEntry entry = null;
	private ICategoryHandler handler, invhandler;
	
	protected Map<String, StellarConfigProperty> propmap = Maps.newHashMap();
	protected PropertyList proplist = new PropertyList(this);
	protected PropertyList.EntryIterator propite;
	
	protected Map<String, List> proprels = Maps.newHashMap();
	
	protected boolean isImmutable = false;
	
	private Map<String, IPropertyRelation> prels = Maps.newHashMap();

	public StellarConfigCategory(StellarConfiguration config, String name)
	{
		this.config = config;
		this.name = name;
		
		this.propite = proplist.backwardIterator();
	}
	
	
	public void setHandler(ICategoryHandler handler) {
		if(handler == null)
			this.handler = new NullCategoryHandler();
		else {
			this.handler = handler;
			
			for(StellarConfigProperty property : proplist)
				property.setHandler(handler.getNewProp(property));
		}
	}
	
	public void setInvHandler(ICategoryHandler invhandler) {
		this.invhandler = invhandler;
		
		if(this.invhandler != null)
			for(StellarConfigProperty property : proplist)
				property.setInvHandler(invhandler.getNewProp(property));
	}
	
	@Override
	public ICategoryEntry getCategoryEntry() {
		return this.entry;
	}
	
	public void setEntry(ICategoryEntry entry)
	{
		ICategoryEntry prev = this.entry;
		this.entry = entry;
		
		if(prev != null)
			config.onMigrate(this, prev);
	}
	
	public PropertyList getPropList()
	{
		return this.proplist;
	}
	
	
	@Override
	public void markImmutable() {
		config.handler.onMarkImmutable(this);
		this.isImmutable = true;
	}

	@Override
	public boolean isImmutable() {
		return this.isImmutable;
	}

	
	@Override
	public String getName() {
		return this.name;
	}
	
	public boolean setName(String name) {
		if(this.isImmutable || !config.preNameChange(this, name))
			return false;
		
		String pre = this.name;
		this.name = name;
		config.postNameChange(this, pre);
		
		return true;
	}
	
	protected void onRefresh() {
		proprels.clear();
		prels.clear();
	}

	
	@Override
	public IStellarConfig getConfig() {
		return config;
	}
	
	
	public void copy(IConfigCategory category) {
		StellarConfigCategory rcat = (StellarConfigCategory) category;
		boolean temp;
		
		if(rcat.isImmutable)
			this.markImmutable();
		
		for(StellarConfigProperty fromProp : rcat.getPropList()) {
			IConfigProperty toProp = this.addProperty(fromProp.type, fromProp.name, fromProp.def);
			
			toProp.setExpl(fromProp.expl);
			toProp.simSetEnabled(true);
			toProp.simSetVal(fromProp.getVal());
			toProp.simSetEnabled(fromProp.enabled);
		}
	}

	
	@Override
	public <T> IConfigProperty<T> addProperty(String proptype, String propname, T def) {
		if(propmap.containsKey(propname))
			return propmap.get(propname);
		
		StellarConfigProperty prop = new StellarConfigProperty(this, proptype, propname, def);
		
		propmap.put(propname, prop);
		propite.add(prop);
		if(propite.hasNext())
			propite.next();
		
		IPropertyHandler prophandler = handler.getNewProp(prop);
		prop.setHandler(prophandler);
		
		if(invhandler != null)
		{
			IPropertyHandler propinvhandler = invhandler.getNewProp(prop);
			prop.setInvHandler(propinvhandler);
		}
		
		prop.onConstruct();
		
		return prop;
	}

	@Override
	public void removeProperty(String propname) {
		if(!propmap.containsKey(propname))
			return;
		
		StellarConfigProperty prop = propmap.get(propname);
		
		//Clear Relations
		List<PropertyRelation> lr = proprels.get(propname);
		
		for(PropertyRelation pr : lr)
		{
			prels.remove(pr.proprel.getRelationToolTip());
			
			handler.onPropertyRelationRemoved(pr);
			if(invhandler != null)
				invhandler.onPropertyRelationRemoved(pr);
			
			for(IConfigProperty rprop : pr.relprops)
				proprels.get(prop.getName()).remove(pr);
		}
		
		handler.onRemoveProp(prop);
		if(invhandler != null)
			invhandler.onRemoveProp(prop);
		
		propmap.remove(propname);
		proplist.remove(prop);

	}

	@Override
	public <T> IConfigProperty<T> getProperty(String propname) {
		return propmap.get(propname);
	}

	@Override
	public IConfigProperty setPropAddEntry(IConfigProperty prop) {
		IConfigProperty prevprop = propite.get();
		
		if(prop == null)
			propite = proplist.backwardIterator();
		else propite = proplist.getEntryIterator(prop.getName());
		
		return prevprop;
	}
	
	
	@Override
	public void addPropertyRelation(IPropertyRelation rel,
			IConfigProperty... relprops) {
		if(prels.containsKey(rel.getRelationToolTip()))
			return;
		
		prels.put(rel.getRelationToolTip(), rel);
		
		PropertyRelation pr = new PropertyRelation(rel, relprops);
		
		List<IMConfigProperty> lp = Lists.newArrayList();
		
		for(IConfigProperty cp : relprops)
		{
			IMConfigProperty mp = (IMConfigProperty) cp;
			lp.add(mp);
		}
		
		IMConfigProperty[] mp = lp.toArray(new IMConfigProperty[0]);
		rel.setProps(mp);
		
		for(IConfigProperty prop : relprops)
		{
			if(!proprels.containsKey(prop.getName()))
				proprels.put(prop.getName(), Lists.newArrayList());
			
			List<PropertyRelation> l = proprels.get(prop.getName());
			l.add(pr);
		}
		
		handler.onPropertyRelationAdded(pr);
		if(invhandler != null)
			invhandler.onPropertyRelationAdded(pr);
	}
	
	public List<PropertyRelation> getPropertyRelations(String propname)
	{
		return proprels.get(propname);
	}
	
	public class PropertyRelation {
		public IPropertyRelation proprel;
		public IConfigProperty[] relprops;
		
		public PropertyRelation(IPropertyRelation propr, IConfigProperty... relp)
		{
			proprel = propr;
			relprops = relp;
		}
	}
	
}
