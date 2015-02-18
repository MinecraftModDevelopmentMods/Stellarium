package stellarium.config.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import stellarium.config.core.ConfigEntry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonCommentedObj {
	
	private JsonObject obj;
	private final Map<ConfigEntry, String> explmap = new HashMap();
	
	public JsonCommentedObj()
	{
		obj = new JsonObject();
	}
	
	public JsonCommentedObj(JsonObject pobj)
	{
		obj = pobj;
	}
	
	public void setComment(ConfigEntry element, String str)
	{
		explmap.put(element, str);
	}
	
	public String getComment(ConfigEntry element)
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
