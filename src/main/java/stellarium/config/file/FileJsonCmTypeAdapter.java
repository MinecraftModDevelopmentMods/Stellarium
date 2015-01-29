package stellarium.config.file;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.Map;

import stellarium.config.json.JsonCommentedObj;
import stellarium.construct.CPropLangUtil;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import cpw.mods.fml.relauncher.ReflectionHelper;

public class FileJsonCmTypeAdapter extends TypeAdapter<JsonCommentedObj> {
	
	private Gson gson;
	
	public FileJsonCmTypeAdapter(Gson pgson)
	{
		gson = pgson;
	}
	
	@Override
	public void write(JsonWriter out, JsonCommentedObj value) throws IOException {
		write(out, getWriter(out), value, value.getObj());
	}
	
	public void write(JsonWriter out, Writer nativeout, 
			JsonCommentedObj cms, JsonElement value)
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
				addComment(nativeout, cms, value);
				out.name(e.getKey());
				write(out, nativeout, cms, e.getValue());
			}
			out.endObject();

		} else {
			throw new IllegalArgumentException("Couldn't write " + value.getClass());
		}
	}
	
	public void addComment(Writer nativeout, JsonCommentedObj cms, JsonElement value)
			throws IOException {
		if(cms.getComment(value) != null)
		{
			nativeout.append("\n/*");
			nativeout.append(CPropLangUtil.getLocalizedString(cms.getComment(value)));
			nativeout.append("*/\n");
		}
	}
	
	public Writer getWriter(JsonWriter writer)
	{
		try {
			return (Writer) ReflectionHelper.findField(writer.getClass(), "out")
					.get(writer);
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
