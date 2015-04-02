package stellarium.config.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import stellarium.config.EnumCategoryType;
import stellarium.config.core.StellarConfiguration;

public class ConfigGuiUtil {
	
	public static final String CHANGE_CHAR = "\u00B6";
	public static final String CHECK_CHAR = "\u2714";
//	public static final String COPY_CHAR = "\u2398";
	public static final String DELETE_CHAR = "\u2716";
	public static final String UP_CHAR = "\u25B2";
	public static final String DOWN_CHAR = "\u25BC";
	public static final String EXPAND_CHAR = "+";
	public static final String CONTRACT_CHAR = "-";
	
	public static final ResourceLocation ICON_ADD = new ResourceLocation("stellarium", "gui/plus.png");
	public static final ResourceLocation ICON_COPY = new ResourceLocation("stellarium", "gui/copy.png");
	public static final ResourceLocation ICON_LOCK = new ResourceLocation("stellarium", "gui/lock.png");
	public static final ResourceLocation ICON_UNLOCK = new ResourceLocation("stellarium", "gui/unlock.png");
	public static final ResourceLocation ICON_PROPREL = new ResourceLocation("stellarium", "gui/fuzzy.png");
	
	public static int VER_SIZE = 18;
	public static int DEF_WIDTH = 100;
	public static int TOOLTIP_TIMING = 400;
	
	public static int colors[] = {0xff7777, 0x77ff77, 0x7777ff, 0xffff77, 0x77ffff, 0xff77ff, 0xffaa77, 0xaa77ff, 0x77ffaa, 0xff77aa, 0x77aaff, 0xaaff77};
	
	public static int getContentWidth(int scrWidth) {
		return scrWidth / 3 - VER_SIZE;
	}
	
	public static GuiStellarConfig getCfgGui(GuiScreen parScreen, StellarConfiguration config, String title)
	{
		GuiStellarConfig guihandler = new GuiStellarConfig(parScreen, config, title);
		
		if(parScreen == null || !(parScreen instanceof GuiStellarConfig))
		{
			config.setInvHandler(config.getHandler());
			config.setHandler(guihandler);
			config.onFormat();
		}
		
		return guihandler;
	}
	
	public static void onCfgGuiClosed(GuiScreen parScreen, StellarConfiguration config)
	{
		if(parScreen == null || !(parScreen instanceof GuiStellarConfig))
		{
			config.setHandler(config.getInvHandler());
			config.setInvHandler(null);
		}
	}
	
	public static GuiConfigBase getCfgHandler(GuiScreen parentScreen, GuiStellarConfig guiConfig, StellarConfiguration config, String title, EnumCategoryType type)
	{
		if(type.isConfigList())
			return new GuiConfigCfgList(parentScreen, guiConfig, config, title);
		else return new GuiConfigCatList(parentScreen, guiConfig, config, title);
	}

	public static GuiCfgCatViewBase getCfgCatView(GuiStellarConfig guiConfig, GuiConfigCatList parList, Minecraft mc, int width, int viewX, EnumCategoryType type) {
		if(type == EnumCategoryType.List)
			return new GuiCfgCatViewList(mc, guiConfig, parList, width, viewX);
		else return new GuiCfgCatViewTree(mc, guiConfig, parList, width, viewX);
	}

}
