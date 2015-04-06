package stellarium.gui.config;

import cpw.mods.fml.client.config.GuiConfig;
import stellarium.Stellarium;
import stellarium.lang.CLangStrs;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.ConfigElement;

public class DefCfgGuiProvider implements IGuiCfgProvider {
	
	private String title, category;
	
	public DefCfgGuiProvider(String title, String category)
	{
		this.title = title;
		this.category = category;
	}

	@Override
	public GuiScreen getCfgGui(GuiStellarConfigMain parScreen) {
		return new GuiConfig(parScreen, new ConfigElement(Stellarium.instance.getCfgHook().getCategory(category)).getChildElements(),
				Stellarium.modid, Stellarium.name, false, false, I18n.format(CLangStrs.defaultConfig));
	}

	@Override
	public String getUnlocalizedName() {
		return title;
	}

}
