package stellarium.config.json;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import stellarium.config.ICfgMessage;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public interface IJsonContainer {
	
	/**
	 * Read this container as Json.
	 * Will throw Exception if this container is not a Json file.
	 * */
	public JsonObject readJson();
	
	/**
	 * Writes Json Object to this container. Any file would be overridden.
	 * */
	public void writeJson(JsonConfigHandler handler);
	
	/**
	 * Gets Property Writer Object for this container.
	 * */
	public IJsonPropertyWriter getPropertyWriter();
	
	/**
	 * Apply Type Adapter factory to Gson.
	 *  (Gson should be created here)
	 * */
	public void applyFactoryToGson(JsonCfgTypeAdapterFactory factory);
	
	/**
	 * Creates sub-container.
	 * If it is invalid, then just gives null.
	 * */
	public IJsonContainer makeSubContainer(String sub);
	
	/**Removes the sub-container, if it exists.*/
	public void removeSubContainer(String sub);
	
	/**
	 * Gives the sub-container.
	 * NOTE: this will give new instance of container, not same with the original.
	 * */
	public IJsonContainer getSubContainer(String sub);
	
	/**
	 * Moves the sub-container from 'before' to 'after'.
	 * */
	public IJsonContainer moveSubContainer(String before, String after);
	
	/**
	 * gets all sub-containers.
	 * Will give empty list if no sub-containers are present.
	 * */
	public List<String> getAllSubContainerNames();
	
	/**Adds load & fail message for this container*/
	public void addLoadFailMessage(String title, ICfgMessage msg);
}
