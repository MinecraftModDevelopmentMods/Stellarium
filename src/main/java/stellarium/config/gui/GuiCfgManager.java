package stellarium.config.gui;

import java.util.List;

import com.google.common.collect.Lists;

import stellarium.config.ILoadSaveHandler;
import stellarium.config.core.StellarConfigHandler;
import stellarium.config.file.FileCfgManager;

public class GuiCfgManager implements ILoadSaveHandler {
	
	private final FileCfgManager fm;
	List<StellarConfigHandler> cfghandler = Lists.newArrayList();
	
	public GuiCfgManager(FileCfgManager fm)
	{
		this.fm = fm;
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
		fm.onSave();
	}

	@Override
	public void onSave() {
		fm.onApply();
		for(StellarConfigHandler handler : cfghandler)
			handler.onSave();
	}

}
