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
import stellarium.config.core.handler.IPropertyHandler;
import stellarium.config.element.*;

public class JsonCfgPropHandler implements IPropertyHandler {
	
	protected JsonCfgCatHandler cat;
	protected StellarConfigProperty prop;
	protected String propname;
	protected JsonElement json;
	protected String expl;
	
	public JsonCfgPropHandler(JsonCfgCatHandler cat, StellarConfigProperty prop, String pname, JsonElement element)
	{
		this.cat = cat;
		this.prop = prop;
		this.propname = pname;
		this.json = element;
	}

	@Override
	public void setEnabled(boolean enable) { }
	
	@Override
	public void onValueUpdate() { }
	
	@Override
	public void onElementAdded(String subname, IDoubleElement element) {
		if(json != null)
		{
			if(json.isJsonObject())
				element.setValue(json.getAsJsonObject().getAsJsonPrimitive(subname).getAsDouble());
			else element.setValue(json.getAsDouble());
		}
	}

	@Override
	public void onElementAdded(String subname, IEnumElement element) {
		if(json != null)
		{
			if(json.isJsonObject())
				element.setValue(json.getAsJsonObject().getAsJsonPrimitive(subname).getAsString());
			else element.setValue(json.getAsString());
		}
	}

	@Override
	public void onElementAdded(String subname, IIntegerElement element) {
		if(json != null)
		{
			if(json.isJsonObject())
				element.setValue(json.getAsJsonObject().getAsJsonPrimitive(subname).getAsInt());
			else element.setValue(json.getAsInt());
		}
	}

	@Override
	public void onElementAdded(String subname, IStringElement element) {
		if(json != null)
		{
			if(json.isJsonObject())
				element.setValue(json.getAsJsonObject().getAsJsonPrimitive(subname).getAsString());
			else element.setValue(json.getAsString());
		}
	}

	@Override
	public void setExpl(String expl) {
		this.expl = expl;
	}

	public String getExpl() {
		return this.expl;
	}
	
}
