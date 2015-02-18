package stellarium.config.file;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Map;

import stellarium.config.core.ConfigEntry;
import stellarium.config.json.JsonCommentedObj;
import stellarium.construct.CPropLangUtil;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import cpw.mods.fml.relauncher.ReflectionHelper;

public class FileJsonCmTypeAdapter extends TypeAdapter<JsonCommentedObj> {
	
	private Gson gson;
	private Field fwr;
	
	public FileJsonCmTypeAdapter(Gson pgson)
	{
		gson = pgson;
		fwr = ReflectionHelper.findField(JsonWriter.class, "out");
	}
	
	@Override
	public void write(JsonWriter out, JsonCommentedObj value) throws IOException {
		write(out, getWriter(out), value, value.getObj(), new ConfigEntry());
	}
	
	public void write(JsonWriter out, Writer nativeout, 
			JsonCommentedObj cms, JsonElement value, ConfigEntry entry)
			throws IOException {
		
		if (value.isJsonPrimitive()) {
			JsonPrimitive primitive = value.getAsJsonPrimitive();
			if (primitive.isNumber()) {
				out.value(primitive.getAsNumber());
			} else if (primitive.isBoolean()) {
				out.value(primitive.getAsBoolean());
			} else {
				out.value(primitive.getAsString());
			}
		} else if (value.isJsonObject()) {
			out.beginObject();
			for (Map.Entry<String, JsonElement> e : value.getAsJsonObject().entrySet()) {
				ConfigEntry ne = new ConfigEntry(entry, e.getKey());
				addComment(nativeout, cms, ne);
				out.name(e.getKey());
				write(out, nativeout, cms, e.getValue(), ne);
			}
			out.endObject();

		} else {
			throw new IllegalArgumentException("Couldn't write " + value.getClass());
		}
	}
	
	public void addComment(Writer nativeout, JsonCommentedObj cms, ConfigEntry entry)
			throws IOException {
		if(cms.getComment(entry) != null)
		{
			nativeout.append("\n/*");
			nativeout.append(CPropLangUtil.getLocalizedString(cms.getComment(entry)));
			nativeout.append("*/\n");
		}
	}
	
	public Writer getWriter(JsonWriter writer)
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

	@Override
	public JsonCommentedObj read(JsonReader in) throws IOException {
		JsonObject obj = gson.fromJson(in, JsonObject.class);
		return new JsonCommentedObj(obj);
	}

}
