package stellarium.construct;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import stellarium.config.json.JsonConfigCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.util.StatCollector;

import com.google.common.collect.Lists;
import com.google.gson.*;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class CPropLangUtil {
	
	public static String getCurLang()
	{
		return FMLCommonHandler.instance().getCurrentLanguage();
	}
	
	
	public static String getLocalizedString(String tar)
	{
		return LanguageRegistry.instance().getStringLocalization(tar);
	}
	
	public static String getLocalizedString(String tar, String lang)
	{
		return LanguageRegistry.instance().getStringLocalization(tar, lang);
	}

}
