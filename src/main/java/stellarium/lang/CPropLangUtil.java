package stellarium.lang;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import stellarium.config.ICfgMessage;
import stellarium.config.json.JsonCfgCatHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
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
	
	public static String getLocalizedFromID(String id)
	{
		return getLocalizedString(CPropLangRegistry.instance().getLangfromID(id));
	}
	
	public static String getLocalizedMessageFromID(ICfgMessage msg)
	{
		return I18n.format(CPropLangRegistry.instance().getLangfromID(msg.getMessage()),
				msg.getMsgObjects());
	}
	
	public static String getLocalizedString(String tar)
	{
		return I18n.format(tar);
	}
	
	public static String getLocalizedMessage(ICfgMessage msg)
	{
		return I18n.format(msg.getMessage(), msg.getMsgObjects());
	}

}
