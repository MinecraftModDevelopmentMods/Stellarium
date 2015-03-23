package stellarium.config.json;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import stellarium.config.core.ICategoryEntry;
import stellarium.config.core.StellarConfigCategory;
import stellarium.config.core.StellarConfigProperty;
import stellarium.config.element.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;

public class CommonJsonPropertyWriter implements IJsonPropertyWriter {

	@Override
	public void writeProperty(JsonWriter out, StellarConfigProperty prop,
			JsonCfgPropHandler property) throws IOException {

		if (prop.isSingular()) {
			IPropElement elem = prop.getElement(prop.getName());
			writeElement(out, prop.getName(), elem);
		} else {
			out.name(prop.getName());
			out.beginObject();
			
			List<String> list = prop.getNameList();
			
			for (String name : list)
				writeElement(out, name, prop.getElement(name));
			
			out.endObject();
		}
	}
	
	public void writeElement(JsonWriter out, String name, IPropElement elem)
			throws IOException {
		out.name(name);
		if (elem instanceof IDoubleElement) {
			out.value(((IDoubleElement) elem).getValue());
		} else if (elem instanceof IIntegerElement) {
			out.value(((IIntegerElement) elem).getValue());
		} else if (elem instanceof IStringElement) {
			out.value(((IStringElement) elem).getValue());
		} else if (elem instanceof IEnumElement) {
			out.value(((IEnumElement) elem).getValue());
		}
	}

}
