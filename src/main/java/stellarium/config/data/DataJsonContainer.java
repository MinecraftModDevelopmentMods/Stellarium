package stellarium.config.data;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import stellarium.config.file.FileJsonCmTypeAdapter;
import stellarium.config.json.IJsonContainer;
import stellarium.config.json.JsonCommentedObj;

public class DataJsonContainer implements IJsonContainer {
	
	protected JsonObject data = new JsonObject();
	protected Gson gson;
	protected String level;
	
	public DataJsonContainer()
	{
		gson = new Gson();
	}

	@Override
	public JsonCommentedObj readJson() {
		return new JsonCommentedObj(data);
	}

	@Override
	public void writeJson(JsonCommentedObj obj) {
		data = obj.getObj();
	}

	@Override
	public IJsonContainer makeSubContainer(String sub) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeSubContainer(String sub) {
		// TODO Auto-generated method stub

	}

	@Override
	public IJsonContainer getSubContainer(String sub) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<String> getAllSubContainerNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addLoadFailMessage(String title, String msg) {
		throw new IllegalDataException(level, title, msg);
	}

}
