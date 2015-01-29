package stellarium.config.file;

import java.io.*;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.resources.Locale;

import com.google.common.collect.Lists;
import com.google.gson.*;
import com.google.gson.stream.MalformedJsonException;

import cpw.mods.fml.common.registry.LanguageRegistry;
import stellarium.config.IConfigCategory;
import stellarium.config.json.IJsonContainer;
import stellarium.config.json.JsonCommentedObj;
import stellarium.config.json.JsonConfigHandler;
import stellarium.config.util.CfgIteWrapper;
import stellarium.construct.CPropLangUtil;

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
		gb.registerTypeAdapter(JsonCommentedObj.class, new FileJsonCmTypeAdapter(gson));
		gson = gb.create();
	}
	
	@Override
	public JsonCommentedObj readJson() throws IOException {
		File file = new File(pardir, name + ext);
		
		if(!file.exists())
		{
			return new JsonCommentedObj();
		} else if(file.isDirectory() || !file.canRead()) {
			throw new MalformedJsonException("This file is not a Json file");
		} else {
			FileReader fr = new FileReader(file);
			BufferedReader reader = new BufferedReader(fr);
			
			return gson.fromJson(reader, JsonCommentedObj.class);
		}
	}

	@Override
	public void writeJson(JsonCommentedObj obj) {
		
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
		
		//addCommentToJson(jch, obj);
		
		try {
			FileWriter fw = new FileWriter(file);
			BufferedWriter writer = new BufferedWriter(fw);
			
			gson.toJson(obj, writer);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
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
