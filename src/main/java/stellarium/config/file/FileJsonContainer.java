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
	private static String sep = ".";
	
	public boolean isFailed = false;
	
	protected File pardir;
	protected File logdir;
	protected String name;
	protected String lvl;
	
	protected Gson gson;
	
	public static FileJsonContainer getRootContainer(File root, String title)
	{
		File file = new File(root, title);
		file.mkdirs();
		return new FileJsonContainer(root, file, title, title);
	}
	
	public FileJsonContainer(File pdir, File ldir, String level, String pname)
	{		
		pardir = pdir;
		logdir = ldir;
		name = pname;
		lvl = level;
		
		GsonBuilder gb = new GsonBuilder();
		gb.setPrettyPrinting();
		gb.registerTypeAdapter(JsonCommentedObj.class, new FileJsonCmTypeAdapter(gson));
		gson = gb.create();
	}
	
	@Override
	public JsonCommentedObj readJson() {
		File file = new File(pardir, name + ext);
		
		if(!file.exists())
		{
			return new JsonCommentedObj();
		} else if(file.isDirectory() || !file.canRead()) {
			Exception e = new MalformedJsonException("This file is not a Json file:" + file);
			addLoadFailMessage("MalformedJsonException", e.getLocalizedMessage());
			isFailed = true;
		} else {
			FileReader fr;
			
			try {
				fr = new FileReader(file);
				BufferedReader reader = new BufferedReader(fr);
	
				return gson.fromJson(reader, JsonCommentedObj.class);

			} catch (FileNotFoundException e) {
				addLoadFailMessage("FileNotFoundException", e.getLocalizedMessage());
				isFailed = true;
			}
		}
		return null;
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
				addLoadFailMessage("IOException", e.getLocalizedMessage());
				isFailed = true;
			}
		}
				
		try {
			FileWriter fw = new FileWriter(file);
			BufferedWriter writer = new BufferedWriter(fw);
			
			gson.toJson(obj, writer);
			
		} catch (IOException e) {
			addLoadFailMessage("IOException", e.getLocalizedMessage());
			isFailed = true;
		}
	}

	@Override
	public IJsonContainer makeSubContainer(String sub) {
		File file = new File(this.pardir, this.name);
		file.mkdirs();
		
		if(file.isDirectory())
			return new FileJsonContainer(file, logdir, lvl + sep + sub, sub);
		
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
			return new FileJsonContainer(file, logdir, lvl + sep + sub, sub);
		
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
		
		if(!logdir.exists() || logdir.isDirectory() || !logdir.canWrite())
		{
			if(logdir.exists())
				logdir.delete();
			
			try {
				logdir.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				isFailed = true;
			}
		}

		try {
			FileWriter fw = new FileWriter(logdir, true);
			BufferedWriter writer = new BufferedWriter(fw);
			
			writer.append(String.format("<%s>[%s]: %s", lvl, title,
					CPropLangUtil.getLocalizedString(msg)));
			
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
			isFailed = true;
		}
		
	}

}
