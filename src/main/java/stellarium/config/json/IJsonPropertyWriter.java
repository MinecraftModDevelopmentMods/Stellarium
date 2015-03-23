package stellarium.config.json;

import java.io.IOException;

import stellarium.config.core.StellarConfigProperty;

import com.google.gson.stream.JsonWriter;

public interface IJsonPropertyWriter {

	void writeProperty(JsonWriter out, StellarConfigProperty cat,
			JsonCfgPropHandler property) throws IOException;

}
