package stellarium.config.file;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.List;

import com.google.gson.stream.JsonWriter;

import cpw.mods.fml.relauncher.ReflectionHelper;
import stellarium.config.core.ICategoryEntry;
import stellarium.config.core.StellarConfigProperty;
import stellarium.config.element.IDoubleElement;
import stellarium.config.element.IEnumElement;
import stellarium.config.element.IIntegerElement;
import stellarium.config.element.IPropElement;
import stellarium.config.element.IStringElement;
import stellarium.config.json.IJsonPropertyWriter;
import stellarium.config.json.JsonCfgPropHandler;
import stellarium.lang.CPropLangUtil;

public class CommentedPropertyWriter implements IJsonPropertyWriter {
	
	private Field fwr;
	
	public CommentedPropertyWriter() {
		fwr = ReflectionHelper.findField(JsonWriter.class, "out");
	}

	@Override
	public void writeProperty(JsonWriter out, StellarConfigProperty prop,
			JsonCfgPropHandler property) throws IOException {

		Writer nativeout = this.getWriter(out);
		
		this.addComment(nativeout, property.getExpl());
		
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
	
	private void addComment(Writer nativeout, String comment)
			throws IOException {
		if(comment != null)
		{
			nativeout.append("\n/*");
			nativeout.append(CPropLangUtil.getLocalizedString(comment));
			nativeout.append("*/");
		}
	}
	
	private Writer getWriter(JsonWriter writer)
	{
		try {
			return (Writer) fwr.get(writer);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
