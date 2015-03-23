package stellarium.config.file;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import stellarium.config.ConfigDataRegistry;
import stellarium.config.ConfigDataRegistry.ConfigRegistryData;
import stellarium.config.ILoadSaveHandler;
import stellarium.config.core.ICategoryEntry;
import stellarium.config.core.StellarConfiguration;
import stellarium.config.json.JsonConfigHandler;

public class FileCfgManager implements ILoadSaveHandler {
	
	private File cfgdir;
	
	public FileCfgManager(File cfgdir)
	{
		this.cfgdir = cfgdir;
		cfgdir.mkdirs();
	}
	

	@Override
	public void onFormat() {
		
		for(ConfigDataRegistry.ConfigRegistryData data : ConfigDataRegistry.getImmutableList())
		{
			StellarConfiguration handler = ConfigDataRegistry.getConfig(data);
			FileJsonContainer root = FileJsonContainer.getRootContainer(cfgdir, data.title);
			handler.setHandler(new JsonConfigHandler(root));
			handler.onFormat();
		}
	}

	@Override
	public void onApply() {
		for(StellarConfiguration handler : ConfigDataRegistry.getImmutableCfgList())
			applySub(handler);
	}
	
	private void applySub(StellarConfiguration subhandler)
	{
		if(subhandler.getCategoryType().isConfigList())
		{
			for(ICategoryEntry entry : subhandler.getRootEntry())
				applySub((StellarConfiguration) subhandler.getSubConfig(entry.getCategory()));
		}
		
		subhandler.onApply();
	}

	@Override
	public void onSave() {
		for(StellarConfiguration handler : ConfigDataRegistry.getImmutableCfgList())
			saveSub(handler);
	}
	
	private void saveSub(StellarConfiguration subhandler)
	{
		if(subhandler.getCategoryType().isConfigList())
		{
			for(ICategoryEntry entry : subhandler.getRootEntry())
				applySub((StellarConfiguration) subhandler.getSubConfig(entry.getCategory()));
		}
		
		subhandler.onSave();
	}	
}
