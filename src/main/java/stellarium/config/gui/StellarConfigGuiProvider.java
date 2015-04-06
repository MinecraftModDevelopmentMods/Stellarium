package stellarium.config.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import stellarium.config.ConfigDataRegistry;
import stellarium.config.EnumCategoryType;
import stellarium.config.core.StellarConfiguration;
import stellarium.gui.config.GuiStellarConfigMain;
import stellarium.gui.config.IGuiCfgProvider;
import stellarium.lang.CLangStrs;

public class StellarConfigGuiProvider implements IGuiCfgProvider {
	
	private final ConfigDataRegistry.ConfigRegistryData data;
	private final StellarConfiguration config;
	
	public StellarConfigGuiProvider(StellarConfiguration config, ConfigDataRegistry.ConfigRegistryData data)
	{
		this.config = config;
		this.data = data;
	}
	
	@Override
	public GuiScreen getCfgGui(GuiStellarConfigMain parScreen) {		
		return ConfigGuiUtil.getCfgGui(parScreen, config, I18n.format(data.title));
	}

	@Override
	public String getUnlocalizedName() {
		return data.title;
	}

}
