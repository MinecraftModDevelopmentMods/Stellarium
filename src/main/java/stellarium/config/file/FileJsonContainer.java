package stellarium.config.file;

import io.netty.util.Timer;

import java.io.*;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchEvent.Modifier;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import com.google.common.collect.Lists;
import com.google.gson.*;
import com.google.gson.stream.MalformedJsonException;

import cpw.mods.fml.common.registry.LanguageRegistry;
import stellarium.config.ICfgMessage;
import stellarium.config.IConfigCategory;
import stellarium.config.StrMessage;
import stellarium.config.json.IJsonContainer;
import stellarium.config.json.IJsonPropertyWriter;
import stellarium.config.json.JsonCfgTypeAdapterFactory;
import stellarium.config.json.JsonConfigHandler;
import stellarium.config.util.CfgIteWrapper;
import stellarium.lang.CPropLangUtil;

public class FileJsonContainer implements IJsonContainer {
	
	private static String ext = ".cfg";
	private static String log = ".log";
	private static String err = "_err";
	private static String sep = File.separator;
	
	public boolean isFailed = false;
	
	protected File pardir;
	protected File logdir;
	protected String name;
	protected String lvl;
	
	protected Gson gson;
	
	protected SimpleDateFormat consoleDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
	protected SimpleDateFormat fileDateFormat = new SimpleDateFormat("MMM dd HH:mm:ss ", Locale.ENGLISH);
	
	public static FileJsonContainer getRootContainer(File root, String title)
	{
		root.mkdirs();

		File file = new File(root, title + err + log);
		return new FileJsonContainer(root, file, title, title);
	}
	
	public FileJsonContainer(File pdir, File ldir, String level, String pname)
	{
		pardir = pdir;
		logdir = ldir;
		name = pname;
		lvl = level;
	}
	
	@Override
	public JsonObject readJson() {
		File file = new File(pardir, name + ext);
		
		if(!file.exists())
		{
			return new JsonObject();
		} else if(file.isDirectory() || !file.canRead()) {
			Exception e = new MalformedJsonException("This file is not a Json file:" + file);
			addLoadFailMessage("MalformedJsonException", new StrMessage(e.getLocalizedMessage()));
			isFailed = true;
		} else {
			FileReader fr;
			
			try {
				fr = new FileReader(file);
				BufferedReader reader = new BufferedReader(fr);
	
				JsonObject obj = gson.fromJson(reader, JsonObject.class);
				
				reader.close();
				
				return (obj != null)? obj : new JsonObject();
			} catch (FileNotFoundException e) {
				addLoadFailMessage("FileNotFoundException", new StrMessage(e.getLocalizedMessage()));
				isFailed = true;
			} catch (IOException e) {
				addLoadFailMessage(e.getClass().getName(), new StrMessage(e.getLocalizedMessage()));
				isFailed = true;
			}
		}
		return null;
	}

	@Override
	public IJsonPropertyWriter getPropertyWriter() {
		return new CommentedPropertyWriter();
	}

	@Override
	public void applyFactoryToGson(JsonCfgTypeAdapterFactory factory) {
		GsonBuilder gb = new GsonBuilder();
		gb.setPrettyPrinting();
		
		gb.registerTypeAdapterFactory(factory);
		gson = gb.create();
	}

	@Override
	public void writeJson(JsonConfigHandler handler) {
		
		File file = new File(pardir, name + ext);
		
		if(!file.exists() || file.isDirectory() || !file.canWrite())
		{
			if(file.exists())
				file.delete();
			
			try {
				file.createNewFile();
			} catch (IOException e) {
				addLoadFailMessage(e.getClass().getName(), new StrMessage(e.getLocalizedMessage()));
				isFailed = true;
			}
		}
		
		try {
			FileWriter fw = new FileWriter(file);
			BufferedWriter writer = new BufferedWriter(fw);

			gson.toJson(handler, writer);
			writer.close();
			
		} catch (IOException e) {
			addLoadFailMessage(e.getClass().getName(), new StrMessage(e.getLocalizedMessage()));
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
	public IJsonContainer moveSubContainer(String before, String after) {
		File file = new File(this.pardir, this.name);
		file.mkdirs();
		
		File beffile = new File(file, before);
		
		if(beffile.exists())
		{
			File afterfile = new File(file, after);
			
			try {
				this.copy(beffile.toPath(), afterfile.toPath());
				this.delete(beffile.toPath());
			} catch (IOException e) {
				addLoadFailMessage(e.getClass().getName(), new StrMessage(e.getLocalizedMessage()));
				isFailed = true;
				return this.makeSubContainer(after);
			}
			
			return new FileJsonContainer(file, logdir, lvl + sep + after, after);
		}
		else return this.makeSubContainer(after);
	}

	@Override
	public void removeSubContainer(String sub) {
		File file = new File(this.pardir, this.name);
		File subf = new File(file, sub);
		
		if(subf.exists())
		{
			try {
				this.delete(subf.toPath());
			} catch (IOException e) {
				addLoadFailMessage(e.getClass().getName(), new StrMessage(e.getLocalizedMessage()));
				isFailed = true;
			}
		}
	}

	@Override
	public IJsonContainer getSubContainer(String sub) {
		File file = new File(this.pardir, this.name);
		
		if(file.isDirectory())
			return new FileJsonContainer(file, logdir, lvl + sep + sub, sub);
		
		return null;
	}

	@Override
	public List<String> getAllSubContainerNames() {
		File file = new File(this.pardir, this.name);
		file.mkdirs();
		
		List<String> list = Lists.newArrayList();
		
		for(String str : file.list())
		{
			int p = str.lastIndexOf(".");
			if(p >= 1)
				list.add(str.substring(0, p));
			else list.add(str);
		}
		
		return list;
	}

	@Override
	public void addLoadFailMessage(String title, ICfgMessage msg) {
		
		Date date = new Date(System.currentTimeMillis());
		
		String time = consoleDateFormat.format(date).toString();
		System.err.println(String.format("[%s] {%s} [%s]: %s", time, lvl, title,
				CPropLangUtil.getLocalizedMessage(msg)));
		
		
		time = fileDateFormat.format(date).toString();
		
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
			FileOutputStream fos = new FileOutputStream(logdir, true);  
			Writer out = new OutputStreamWriter(fos, "UTF-8");
			BufferedWriter writer = new BufferedWriter(out);
			
			writer.append(String.format("[%s] {%s} [%s]: %s\n\n", time, lvl, title,
					CPropLangUtil.getLocalizedMessage(msg)));
			
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
			isFailed = true;
		}
		
	}
	
	public void copy(Path from, Path to) throws IOException
	{
		Files.walkFileTree(from, new Copier(from, to));
	}
	
	public void delete(Path dir) throws IOException
	{
		Files.walkFileTree(dir, new Remover());
	}
	
	
	public class Copier implements FileVisitor<Path> {
		
		private Path from, to;
		
		public Copier(Path from, Path to) {
			this.from = from;
			this.to = to;
		}

		@Override
		public FileVisitResult preVisitDirectory(Path dir,
				BasicFileAttributes attrs) throws IOException {
			Path fileto = to.resolve(from.relativize(dir));
			Files.copy(dir, fileto, StandardCopyOption.REPLACE_EXISTING);
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
				throws IOException {
			Path fileto = to.resolve(from.relativize(file));
			Files.copy(file, fileto, StandardCopyOption.REPLACE_EXISTING);
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFileFailed(Path file, IOException exc)
				throws IOException {
			throw exc;
		}

		@Override
		public FileVisitResult postVisitDirectory(Path dir, IOException exc)
				throws IOException {
			if(exc != null)
				throw exc;
			return FileVisitResult.CONTINUE;
		}
	}
	
	
	public class Remover implements FileVisitor<Path> {

		@Override
		public FileVisitResult preVisitDirectory(Path dir,
				BasicFileAttributes attrs) throws IOException {
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
				throws IOException {
			Files.delete(file);
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFileFailed(Path file, IOException exc)
				throws IOException {
			throw exc;
		}

		@Override
		public FileVisitResult postVisitDirectory(Path dir, IOException exc)
				throws IOException {
			if(exc != null)
				throw exc;
			Files.delete(dir);
			return FileVisitResult.CONTINUE;
		}	
	}

}
