package stellarium.config.json;

import java.io.IOException;

import stellarium.config.core.ICategoryEntry;
import stellarium.config.core.StellarConfigCategory;
import stellarium.config.core.StellarConfigProperty;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class ConfigHandlerTypeAdapter extends TypeAdapter<JsonConfigHandler> {

	protected Gson gson;
	protected IJsonPropertyWriter writer;
	
	public ConfigHandlerTypeAdapter(Gson gson, IJsonPropertyWriter writer)
	{
		this.gson = gson;
		this.writer = writer;
	}
	
	@Override
	public void write(JsonWriter out, JsonConfigHandler value)
			throws IOException {
		if(value.categoryMap.isEmpty())
		{
			out.beginObject();
			out.endObject();
			return;
		} else {
			for(ICategoryEntry entry : value.categoryMap.keySet())
			{
				writeConfig(out, value, entry.getConfig().getRootEntry());
				break;
			}
		}
	}
	
	public void writeConfig(JsonWriter out, JsonConfigHandler cfghandler, ICategoryEntry entry)
			throws IOException {
		out.beginObject();
		
		if(!entry.isRootEntry())
		{
			out.name(JsonConfigHandler.CATEGORY_INDICATOR);
			out.value(true);
			JsonCfgCatHandler category = cfghandler.categoryMap.get(entry);
			StellarConfigCategory cat = (StellarConfigCategory) entry.getCategory();
			writeCategory(out, cat, category);
		}
		
		for(ICategoryEntry sub : entry)
		{
			out.name(sub.getName());
			writeConfig(out, cfghandler, sub);
		}
		
		out.endObject();
	}
	
	public void writeCategory(JsonWriter out, StellarConfigCategory cat, JsonCfgCatHandler category)
			throws IOException{
		
		for(StellarConfigProperty prop : cat.getPropList())
		{
			JsonCfgPropHandler property = category.propmap.get(prop.getName());
			writer.writeProperty(out, prop, property);
		}
	}

	@Override
	public JsonConfigHandler read(JsonReader in) throws IOException {
		//Not Used
		return null;
	}

}
