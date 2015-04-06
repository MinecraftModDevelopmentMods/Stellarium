package stellarium.config.core;

import java.util.List;

import com.google.common.collect.Lists;

import stellarium.config.ConfigPropTypeRegistry;
import stellarium.config.IConfigPropHandler;
import stellarium.config.IConfigProperty;
import stellarium.config.IMConfigProperty;
import stellarium.config.core.handler.IPropertyHandler;
import stellarium.config.element.*;

public class StellarConfigProperty<T> implements IMConfigProperty<T> {
	
	protected StellarConfigCategory par; 
	protected String name, type, expl;
	protected IConfigPropHandler<T> handle;
	protected boolean singular = false;
	
	protected IPropertyHandler prophandler, propinvhandler;
	
	protected List<String> namelist = Lists.newArrayList();
	protected List<IPropElement> ellist = Lists.newArrayList();
	
	protected boolean enabled = true;
	protected T def;
	
	public StellarConfigProperty(StellarConfigCategory cat, String ptype, String pname, T def)
	{
		this.par = cat;
		this.name = pname;
		this.type = ptype;
		this.handle = ConfigPropTypeRegistry.getHandler(ptype);
		if(handle == null)
			throw new IllegalArgumentException("The Property Type: " + ptype + "Does Not Exist!");
		this.def = def;
	}
	
	public void onConstruct() {
		handle.onConstruct(this);
		
		if(namelist.size() == 1 && namelist.get(0).equals(name))
			singular = true;
		
		handle.onSetVal(this, def);
		
		this.addElementToPropHandler(prophandler);
		if(propinvhandler != null)
			this.addElementToPropHandler(propinvhandler);
	}
	
	private void addElementToPropHandler(IPropertyHandler handler) {
		for(int i = 0; i < namelist.size(); i++) {
			String subname = namelist.get(i);
			IPropElement propel = ellist.get(i);
			
			if(propel instanceof IDoubleElement)
			{
				handler.onElementAdded(subname, (IDoubleElement) propel);
			} else if(propel instanceof IIntegerElement) {
				handler.onElementAdded(subname, (IIntegerElement) propel);
			} else if(propel instanceof IStringElement) {
				handler.onElementAdded(subname, (IStringElement) propel);
			} else if(propel instanceof IEnumElement) {
				handler.onElementAdded(subname, (IEnumElement) propel);
			}
		}
	}
	
	public boolean isSingular()
	{
		return this.singular;
	}
	
	public IPropertyHandler getHandler() {
		return this.prophandler;
	}
	
	public void setHandler(IPropertyHandler prophandler) {
		this.prophandler = prophandler;
		this.addElementToPropHandler(prophandler);
	}
	
	public void setInvHandler(IPropertyHandler propinvhandler) {
		this.propinvhandler = propinvhandler;
		if(propinvhandler != null)
			this.addElementToPropHandler(propinvhandler);
	}
	
	public StellarConfigCategory getCategory() {
		return this.par;
	}

	
	public List<String> getNameList() {
		return this.namelist;
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
	public boolean isEnabled() {
		return enabled;
	}
	
	public void updateValue() {
		if(!enabled)
			return;
		
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
		
		prophandler.onValueUpdate();
		if(propinvhandler != null)
			propinvhandler.onValueUpdate();
	}
	
	@Override
	public void setEnabled(boolean enable) {
		enabled = enable;
		
		prophandler.setEnabled(enable);
		if(propinvhandler != null)
			propinvhandler.setEnabled(enable);
	}
	
	
	@Override
	public IConfigProperty<T> setExpl(String expl) {
		this.expl = expl;
		
		prophandler.setExpl(expl);
		if(propinvhandler != null)
			propinvhandler.setExpl(expl);
		return this;
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
	
	@Override
	public void addElement(String subname, EnumPropElement e) {
		namelist.add(subname);
		ellist.add(newElement(subname, e));
	}

	private IPropElement newElement(String subname, EnumPropElement e) {
		switch(e)
		{
		case Double:
			return new DoubleElement();

		case Enum:
			return new EnumElement();
			
		case Integer:
			return new IntegerElement();
			
		case String:
			return new StringElement();
		
		default:
			return null;
		}
	}
	
	protected class DoubleElement implements IDoubleElement {

		double currentValue;
		
		@Override
		public void setValue(double val) {
			currentValue = val;
		}

		@Override
		public double getValue() {
			return currentValue;
		}
		
	}

	protected class IntegerElement implements IIntegerElement {

		int currentValue;
		
		@Override
		public void setValue(int val) {
			currentValue = val;
		}

		@Override
		public int getValue() {
			return currentValue;
		}
	
	}

	protected class StringElement implements IStringElement {

		String currentValue;
		
		@Override
		public void setValue(String val) {
			currentValue = val;
		}

		@Override
		public String getValue() {
			return currentValue;
		}
	
	}
	
	protected class EnumElement implements IEnumElement {

		String valrange[];
		int ind = 0;
		
		@Override
		public void setValRange(String... str) {
			valrange = str;
		}

		@Override
		public void setValue(int index) {
			ind = index % valrange.length;
		}

		@Override
		public void setValue(String val) {
			for(int i = 0; i < valrange.length; i++)
				if(valrange[i].equals(val))
				{
					ind = i;
					return;
				}
		}

		@Override
		public String getValue() {
			return valrange[ind];
		}

		@Override
		public int getIndex() {
			return ind;
		}
		
	}

}
