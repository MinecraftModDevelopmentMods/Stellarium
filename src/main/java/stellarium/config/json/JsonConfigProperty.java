package stellarium.config.json;

import java.util.List;

import com.google.common.collect.Lists;

import stellarium.config.ConfigPropTypeRegistry;
import stellarium.config.IConfigPropHandler;
import stellarium.config.IConfigProperty;
import stellarium.config.IMConfigProperty;
import stellarium.config.element.*;
import stellarium.config.json.JsonConfigCategory.PropertyRelation;

public class JsonConfigProperty<T> implements IMConfigProperty<T> {
	
	protected JsonConfigCategory par;
	
	public JsonConfigProperty(JsonConfigCategory cat, String ptype, String pname, T def)
	{
		par = cat;
		name = pname;
		handle = ConfigPropTypeRegistry.getHandler(ptype);
		handle.onConstruct(this);
		
		setVal(def);
	}
	
	protected String name, expl;
	protected IConfigPropHandler<T> handle;
	protected boolean enabled = true;

	@Override
	public T getVal() {
		return handle.getValue(this);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public IConfigProperty<T> setExpl(String pexpl) {
		expl = pexpl;
		return this;
	}

	@Override
	public void simSetVal(T val) {
		if(!enabled)
			return;
		// TODO Auto-generated method stub
		
		setVal(val);
		
		List<PropertyRelation> lr = par.proprels.get(name);
		
		if(lr != null)
		{
			for(PropertyRelation pr : lr)
			{
				for(int j = 0; j < pr.relprops.length; j++)
					if(pr.relprops[j] == this)
						pr.proprel.onValueChange(j);
			}
		}
	}

	@Override
	public void simSetEnabled(boolean enabled) {
		// TODO Auto-generated method stub
		
		setEnabled(enabled);
		
		List<PropertyRelation> lr = par.proprels.get(name);
	
		if(lr != null)
		{
			for(PropertyRelation pr : lr)
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
	public void setEnabled(boolean enable) {
		enabled = enable;
	}
	
	@Override
	public void setVal(T val) {
		handle.onSetVal(this, val);
	}

	
	
	protected List<String> namelist = Lists.newArrayList();
	protected List<IPropElement> ellist = Lists.newArrayList();
	
	@Override
	public void addElement(String subname, EnumPropElement e) {
		namelist.add(subname);
		ellist.add(getElement(e));
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

	
	public IPropElement getElement(EnumPropElement e)
	{
		switch(e)
		{
		case Double:
			return new DoubleElement();
			
		case Integer:
			return new IntElement();

		case String:
			return new StringElement();

		case Enum:
			return new EnumElement();
			
		default:
			return null;
		}
	}
	
	public class DoubleElement implements IDoubleElement {
		
		public double tval;

		@Override
		public void setValue(double val) {
			tval = val;
		}

		@Override
		public double getValue() {
			return tval;
		}
		
	}
	
	public class IntElement implements IIntegerElement {
		
		public int tval;

		@Override
		public void setValue(int val) {
			tval = val;
		}

		@Override
		public int getValue() {
			return tval;
		}
		
	}
	
	public class StringElement implements IStringElement {
		
		public String tval;

		@Override
		public void setValue(String val) {
			tval = val;
		}

		@Override
		public String getValue() {
			return tval;
		}
		
	}
	
	public class EnumElement implements IEnumElement {
		
		String valrange[];
		int ind;

		@Override
		public void setValRange(String... str) {
			valrange = str;
		}

		@Override
		public void setValue(int index) {
			ind = index;
		}

		@Override
		public void setValue(String val) {
			for(int i = 0; i < valrange.length; i++)
				if(valrange[i].equals(val))
					ind = i;
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
