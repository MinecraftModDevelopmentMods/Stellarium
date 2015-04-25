package stellarium.config.json;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

import stellarium.config.ICfgMessage;
import stellarium.config.StrMessage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public abstract class DummyJsonContainer implements IJsonContainer {
	
	protected Gson gson;

	@Override
	public IJsonPropertyWriter getPropertyWriter() {
		return new CommonJsonPropertyWriter();
	}

	@Override
	public void applyFactoryToGson(JsonCfgTypeAdapterFactory factory) {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapterFactory(factory);
		gson = builder.create();
	}
	
	@Override
	public JsonObject readJson() {
		try {
			StringReader sr = new StringReader(this.getContextString());
			BufferedReader reader = new BufferedReader(sr);
		
			JsonObject obj = gson.fromJson(reader, JsonObject.class);
		
			reader.close();
			
			return (obj != null)? obj : new JsonObject();
		} catch (IOException e) {
			addLoadFailMessage(e.getClass().getName(), new StrMessage(e.getLocalizedMessage()));
		}
		
		return null;
	}

	@Override
	public void writeJson(JsonConfigHandler handler) {
		try {
			StringWriter sw = new StringWriter();
			BufferedWriter writer = new BufferedWriter(sw);

			gson.toJson(handler, writer);
			
			this.setContextString(sw.toString());
			
			writer.close();
			
		} catch (IOException e) {
			addLoadFailMessage(e.getClass().getName(), new StrMessage(e.getLocalizedMessage()));
		}
	}
	
	public abstract String getContextString();
	public abstract void setContextString(String context);

}
