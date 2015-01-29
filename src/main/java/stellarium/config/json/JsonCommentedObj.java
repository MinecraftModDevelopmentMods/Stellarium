package stellarium.config.json;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonCommentedObj {
	
	private JsonObject obj;
	private final Map<JsonElement, String> explmap = new HashMap();
	
	public JsonCommentedObj()
	{
		obj = new JsonObject();
	}
	
	public JsonCommentedObj(JsonObject pobj)
	{
		obj = pobj;
	}
	
	public void setComment(JsonElement element, String str)
	{
		explmap.put(element, str);
	}
	
	public String getComment(JsonElement element)
	{
		return explmap.get(element);
	}
	
	public JsonObject getObj()
	{
		return obj;
	}

	public void add(String cid, JsonObject tobj) {
		obj.add(cid, obj);
	}

	public void remove(String cid) {
		obj.remove(cid);
	}

}
