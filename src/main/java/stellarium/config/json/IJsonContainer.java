package stellarium.config.json;

import java.util.List;

import com.google.gson.JsonObject;

public interface IJsonContainer {
	
	/**
	 * Read this container as Json.
	 * Will throw Exception if this container is not a Json file.
	 * */
	public JsonObject readJson();
	
	/**Writes Json Object to this container.
	 * Will throw Exception if this container is not a Json file.
	 * */
	public void writeJson(JsonObject obj);

	
	/**creates sub-container*/
	public IJsonContainer makeSubContainer(String sub);
	
	/**removes sub-container*/
	public void removeSubContainer(String sub);
	
	/**gets sub-container*/
	public IJsonContainer getSubContainer(String sub);
	
	/**
	 * gets all sub-containers.
	 * Will give empty list if no sub-containers are present.
	 * */
	public Iterable<String> getAllSubContainerNames();
}
