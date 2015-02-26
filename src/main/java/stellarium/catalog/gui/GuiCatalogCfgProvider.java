package stellarium.catalog.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import stellarium.config.gui.GuiConfigHandler;
import stellarium.config.gui.GuiStellarConfig;
import stellarium.config.gui.gui.IGuiCfgProvider;
import stellarium.lang.CLangStrs;

public class GuiCatalogCfgProvider implements IGuiCfgProvider {

	@Override
	public GuiScreen getCfgGui(GuiScreen parScreen) {
		//TODO have to do some things
		GuiConfigHandler handler = new GuiConfigHandler(null);
		return new GuiStellarConfig(parScreen, handler, I18n.format(CLangStrs.catalogConfig));
	}

	@Override
	public String getUnlocalizedName() {
		return CLangStrs.catalogConfig;
	}

}
