package stellarium.config.gui;

import java.util.List;

import com.google.common.collect.Lists;

import stellarium.config.ConfigDataRegistry;
import stellarium.config.core.StellarConfiguration;
import stellarium.config.file.FileCfgManager;
import stellarium.gui.config.IGuiCfgMultipleProvider;
import stellarium.gui.config.IGuiCfgProvider;

public class StellarCfgGuiMProvider implements IGuiCfgMultipleProvider {
	
	FileCfgManager fm;
	
	public StellarCfgGuiMProvider(FileCfgManager fm)
	{
		this.fm = fm;
	}
	
	@Override
	public List<IGuiCfgProvider> getProviders() {
		
		List<IGuiCfgProvider> list = Lists.newArrayList();
		
		for(ConfigDataRegistry.ConfigRegistryData data : ConfigDataRegistry.getImmutableList())
			list.add(new StellarConfigGuiProvider(ConfigDataRegistry.getConfig(data), data));
		
		return list;
	}

}
