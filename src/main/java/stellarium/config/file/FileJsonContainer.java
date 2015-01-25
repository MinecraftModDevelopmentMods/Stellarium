package stellarium.config.file;

import java.io.*;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.resources.Locale;

import com.google.common.collect.Lists;
import com.google.gson.*;
import com.google.gson.stream.MalformedJsonException;

import cpw.mods.fml.common.registry.LanguageRegistry;
import stellarium.config.json.IJsonContainer;
import stellarium.construct.CPropLangUtil;
import stellarium.stellars.background.BrStar;

public class FileJsonContainer implements IJsonContainer {

	private static String ext = ".cfg";
	
	protected File pardir;
	protected String name;
	
	protected Gson gson;
	
	public FileJsonContainer(File pdir, String pname)
	{
		pardir = pdir;
		name = pname;
		
		GsonBuilder gb = new GsonBuilder();
		gb.setPrettyPrinting();
		gson = gb.create();
	}
	
	@Override
	public JsonObject readJson() throws IOException {
		File file = new File(pardir, name + ext);
				
		if(!file.exists())
		{
			return new JsonObject();
		} else if(file.isDirectory() || !file.canRead()) {
			throw new MalformedJsonException("This file is not a Json file");
		} else {
			FileReader fr = new FileReader(file);
			BufferedReader reader = new BufferedReader(fr);
			
			JsonParser parser = new JsonParser();
			JsonElement elm = parser.parse(reader);
			
			if(!elm.isJsonObject())
				throw new MalformedJsonException("This json element is not a Json Object");
			
			JsonObject obj = elm.getAsJsonObject();
			
			return elm.getAsJsonObject();
		}
		
		// TODO Translation(?)
	}

	@Override
	public void writeJson(JsonObject obj) {
		
		File file = new File(pardir, name + ext);
		
		if(!file.exists() || file.isDirectory() || !file.canWrite())
		{
			if(file.exists())
				file.delete();
			
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			FileWriter fw = new FileWriter(file);
			BufferedWriter writer = new BufferedWriter(fw);
			
			gson.toJson(obj, writer);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Translation(?)

	}

	@Override
	public IJsonContainer makeSubContainer(String sub) {
		File file = new File(this.pardir, this.name);
		file.mkdirs();
		
		if(file.isDirectory())
			return new FileJsonContainer(file, sub);
		
		return null;
	}

	@Override
	public void removeSubContainer(String sub) {
		File file = new File(this.pardir, this.name);
		File subf = new File(file, sub);
		
		if(subf.exists())
			subf.delete();
	}

	@Override
	public IJsonContainer getSubContainer(String sub) {
		File file = new File(this.pardir, this.name);
		
		if(file.isDirectory())
			return new FileJsonContainer(file, sub);
		
		return null;
	}

	@Override
	public Iterable<String> getAllSubContainerNames() {
		File file = new File(this.pardir, this.name);
		file.mkdirs();
		
		return Lists.newArrayList(file.list());
	}

	@Override
	public void addLoadFailMessage(String title, String msg) {
		// TODO Auto-generated method stub

	}

}
