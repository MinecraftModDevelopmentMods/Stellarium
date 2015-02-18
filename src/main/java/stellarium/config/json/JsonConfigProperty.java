package stellarium.config.json;

import java.util.List;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import stellarium.config.ConfigPropTypeRegistry;
import stellarium.config.IConfigPropHandler;
import stellarium.config.IConfigProperty;
import stellarium.config.IMConfigProperty;
import stellarium.config.element.*;
import stellarium.config.json.JsonConfigCategory.PropertyRelation;

public class JsonConfigProperty<T> implements IMConfigProperty<T> {
	
	protected JsonConfigCategory par;
	protected boolean singular = false;
	
	public JsonConfigProperty(JsonConfigCategory cat, String ptype, String pname, T def)
	{
		par = cat;
		name = pname;
		handle = ConfigPropTypeRegistry.getHandler(ptype);
		handle.onConstruct(this);
		
		if(namelist.size() == 1 && namelist.get(0).equals(pname))
			singular = true;
		
		setVal(def);
	}
	
	public boolean isSingular()
	{
		return this.singular;
	}
	
	protected String name;
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
	public IConfigProperty<T> setExpl(String expl) {
		par.setExpl(this, expl);
		return this;
	}

	@Override
	public void simSetVal(T val) {
		if(!enabled)
			return;
		// TODO SetVal Simulation
		
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
		// TODO SetEnabled Simulation
		
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
		ellist.add(newElement(e));
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
	
	protected CfgElement getElementProt(String subname) {
		for(int i = 0; i < namelist.size(); i++)
		{
			if(namelist.get(i).equals(subname))
				return (CfgElement) ellist.get(i);
		}
		
		return null;
	}

	
	protected IPropElement newElement(EnumPropElement e)
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
	
	public abstract class CfgElement {
		
		public abstract void addToJson(JsonObject jo, String elname);
		
	}
	
	public class DoubleElement extends CfgElement implements IDoubleElement {
		
		public double tval = 0.0;
		
		public JsonObject jobj;
		public String eln;
		
		@Override
		public void setValue(double val) {
			tval = val;
			
			if(jobj != null)
				jobj.addProperty(eln, val);
		}

		@Override
		public double getValue() {
			return jobj.get(eln).getAsDouble();
		}

		@Override
		public void addToJson(JsonObject jo, String elname) {
			jo.addProperty(elname, tval);
			
			jobj = jo;
			eln = elname;
		}
		
	}
	
	public class IntElement extends CfgElement implements IIntegerElement {
		
		public int tval;

		public JsonObject jobj;
		public String eln;
		
		@Override
		public void setValue(int val) {
			tval = val;
			
			if(jobj != null)
				jobj.addProperty(eln, val);
		}

		@Override
		public int getValue() {
			return jobj.get(eln).getAsInt();
		}
		
		@Override
		public void addToJson(JsonObject jo, String elname) {
			jo.addProperty(elname, tval);
			
			jobj = jo;
			eln = elname;
		}
	}
	
	public class StringElement extends CfgElement implements IStringElement {
		
		public String tval;

		public JsonObject jobj;
		public String eln;
		
		@Override
		public void setValue(String val) {
			tval = val;
			
			if(jobj != null)
				jobj.addProperty(eln, val);
		}

		@Override
		public String getValue() {
			return jobj.get(eln).getAsString();
		}
		
		@Override
		public void addToJson(JsonObject jo, String elname) {
			jo.addProperty(elname, tval);
			
			jobj = jo;
			eln = elname;
		}
		
	}
	
	public class EnumElement extends CfgElement implements IEnumElement {
		
		String valrange[];
		int ind = 0;
		
		public JsonObject jobj;
		public String eln;

		@Override
		public void setValRange(String... str) {
			valrange = str;
		}

		@Override
		public void setValue(int index) {
			ind = index;
			
			if(jobj != null)
				jobj.addProperty(eln, valrange[index]);
		}

		@Override
		public void setValue(String val) {
			for(int i = 0; i < valrange.length; i++)
				if(valrange[i].equals(val))
				{
					ind = i;
			
					if(jobj != null)
						jobj.addProperty(eln, val);
					
					return;
				}
		}

		@Override
		public String getValue() {
			return jobj.get(eln).getAsString();
		}

		@Override
		public int getIndex() {
			for(int i = 0; i < valrange.length; i++)
				if(valrange[i].equals(jobj.get(eln).getAsString()))
				{
					ind = i;
				}
			
			return ind;
		}
		
		@Override
		public void addToJson(JsonObject jo, String elname) {
			jo.addProperty(elname, valrange[ind]);
			
			jobj = jo;
			eln = elname;
		}
		
	}

}
