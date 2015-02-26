package stellarium.config.json;

import java.util.List;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import stellarium.config.ConfigPropTypeRegistry;
import stellarium.config.IConfigPropHandler;
import stellarium.config.IConfigProperty;
import stellarium.config.IMConfigProperty;
import stellarium.config.core.StellarConfigCategory;
import stellarium.config.core.StellarConfigProperty;
import stellarium.config.element.*;

public class JsonConfigProperty<T> extends StellarConfigProperty<T> implements IMConfigProperty<T> {
	
	public JsonConfigProperty(JsonConfigCategory cat, String ptype, String pname, T def)
	{
		super(cat, ptype, pname, def);
	}

	@Override
	public IConfigProperty<T> setExpl(String expl) {
		((JsonConfigCategory) par).setExpl(this, expl);
		return this;
	}
	
	
	protected CfgElement getElementProt(String subname) {
		for(int i = 0; i < namelist.size(); i++)
		{
			if(namelist.get(i).equals(subname))
				return (CfgElement) ellist.get(i);
		}
		
		return null;
	}

	
	@Override
	protected IPropElement newElement(String subname, EnumPropElement e)
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
