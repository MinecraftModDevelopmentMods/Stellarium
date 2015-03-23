package stellarium.config.json;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

public class JsonCfgTypeAdapterFactory implements TypeAdapterFactory {

	IJsonPropertyWriter propWriter;
	
	public JsonCfgTypeAdapterFactory(IJsonPropertyWriter propWriter) {
		this.propWriter = propWriter;
	}
	
	@Override
	public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
		if(type.getRawType() == JsonConfigHandler.class)
			return (TypeAdapter<T>) new ConfigHandlerTypeAdapter(gson, this.propWriter);
		return null;
	}

}
