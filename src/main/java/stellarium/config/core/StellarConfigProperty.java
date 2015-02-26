package stellarium.config.core;

import java.util.List;

import com.google.common.collect.Lists;

import stellarium.config.ConfigPropTypeRegistry;
import stellarium.config.IConfigPropHandler;
import stellarium.config.IConfigProperty;
import stellarium.config.IMConfigProperty;
import stellarium.config.element.EnumPropElement;
import stellarium.config.element.IPropElement;

public abstract class StellarConfigProperty<T> implements IMConfigProperty<T> {
	
	protected StellarConfigCategory par; 
	protected String name;
	protected IConfigPropHandler<T> handle;
	protected boolean singular = false;
	
	public List<String> namelist = Lists.newArrayList();
	protected List<IPropElement> ellist = Lists.newArrayList();
	
	protected boolean enabled = true;
	
	public StellarConfigProperty(StellarConfigCategory cat, String ptype, String pname, T def)
	{
		this.par = cat;
		this.name = pname;
		this.handle = ConfigPropTypeRegistry.getHandler(ptype);
		handle.onConstruct(this);
		
		if(namelist.size() == 1 && namelist.get(0).equals(pname))
			singular = true;
		
		setVal(def);
	}
	
	public boolean isSingular()
	{
		return this.singular;
	}

	@Override
	public T getVal() {
		return handle.getValue(this);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void simSetVal(T val) {
		if(!enabled)
			return;
		
		setVal(val);
		
		List<StellarConfigCategory.PropertyRelation> lr = par.proprels.get(name);
		
		if(lr != null)
		{
			for(StellarConfigCategory.PropertyRelation pr : lr)
			{
				for(int j = 0; j < pr.relprops.length; j++)
					if(pr.relprops[j] == this)
						pr.proprel.onValueChange(j);
			}
		}
	}

	@Override
	public void simSetEnabled(boolean enabled) {
		
		setEnabled(enabled);
		
		List<StellarConfigCategory.PropertyRelation> lr = par.proprels.get(name);
	
		if(lr != null)
		{
			for(StellarConfigCategory.PropertyRelation pr : lr)
			{
				for(int j = 0; j < pr.relprops.length; j++)
				{
					if(pr.relprops[j] == this)
					{
						if(enabled)
							pr.proprel.onEnable(j);
						else pr.proprel.onDisable(j);
					}
				}
			}
		}
	}
	
	@Override
	public void setVal(T val) {
		handle.onSetVal(this, val);
	}
	
	@Override
	public void setEnabled(boolean enable) {
		enabled = enable;
	}
	
	
	abstract protected IPropElement newElement(String subname, EnumPropElement e);

	@Override
	public void addElement(String subname, EnumPropElement e) {
		namelist.add(subname);
		ellist.add(newElement(subname, e));
	}

	@SuppressWarnings("hiding")
	@Override
	public <T extends IPropElement> T getElement(String subname) {
		for(int i = 0; i < namelist.size(); i++)
		{
			if(namelist.get(i).equals(subname))
				return (T) ellist.get(i);
		}
		
		return null;
	}

}
