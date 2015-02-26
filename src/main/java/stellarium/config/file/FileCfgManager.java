package stellarium.config.file;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

import stellarium.config.ConfigDataRegistry;
import stellarium.config.ILoadSaveHandler;
import stellarium.config.core.StellarConfigHandler;
import stellarium.config.json.JsonConfigHandler;

public class FileCfgManager implements ILoadSaveHandler {
	
	private List<StellarConfigHandler> cfghandler = Lists.newArrayList();
	
	public FileCfgManager(File cfgdir)
	{
		cfgdir.mkdirs();
		
		for(ConfigDataRegistry.ConfigRegistryData data : ConfigDataRegistry.getImmutableList())
		{
			FileJsonContainer root = FileJsonContainer.getRootContainer(cfgdir, data.title);
			JsonConfigHandler cfg = new JsonConfigHandler(root, data);
			cfghandler.add(cfg);
		}
	}

	@Override
	public void onFormat() {
		for(StellarConfigHandler handler : cfghandler)
			handler.onFormat();
	}

	@Override
	public void onApply() {
		for(StellarConfigHandler handler : cfghandler)
			handler.onApply();
	}

	@Override
	public void onSave() {
		for(StellarConfigHandler handler : cfghandler)
			handler.onSave();
	}
	
}
