package stellarium.config.gui;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import stellarium.config.ICfgArrMListener;
import stellarium.config.IConfigCategory;
import stellarium.config.IConfigProperty;
import stellarium.config.IPropertyRelation;
import stellarium.config.IStellarConfig;
import stellarium.config.core.ConfigEntry;
import stellarium.config.core.StellarConfigCategory;
import stellarium.config.core.StellarConfigCategory.PropertyRelation;

public class GuiCfgCatHandler extends StellarConfigCategory implements IConfigCategory, ICfgChangeNotifier {

	protected boolean isImmutable;
	protected Map<String, GuiCfgPropHandler> propmap = Maps.newHashMap();
	
	public GuiCfgCatHandler(GuiConfigHandler handler, String cid) {
		this(handler, null, cid);
	}
	
	public GuiCfgCatHandler(GuiConfigHandler handler, GuiCfgCatHandler parcat, String cid) {	
		super(handler, parcat, cid);
	}
	
	
	public void setDisplayName(String name) {
		super.setDisplayName(name);
		listener.onCfgChange(EnumCfgChangeType.ValueModified, this);
	}
	
	
	@Override
	public <T> IConfigProperty<T> addProperty(String proptype, String propname,
			T def) {
		if(propmap.containsKey(propname))
			return propmap.get(propname);
		
		GuiCfgPropHandler gcp = new GuiCfgPropHandler(this, proptype, propname, def);
		
		propmap.put(propname, gcp);
		
		//TODO GUI Property add
		listener.onCfgChange(EnumCfgChangeType.Add, gcp);

		return gcp;
	}

	@Override
	public void removeProperty(String propname) {

		if(!propmap.containsKey(propname))
			return;
		
		GuiCfgPropHandler gcp = propmap.get(propname);
		
		//TODO GUI Property remove
		listener.onCfgChange(EnumCfgChangeType.Remove, gcp);
		
		propmap.remove(propname);
		
		//Clear Relations
		List<PropertyRelation> lr = proprels.get(propname);
		
		for(PropertyRelation pr : lr)
		{
			for(IConfigProperty prop : pr.relprops)
				proprels.get(prop.getName()).remove(pr);
		}
	}

	@Override
	public <T> IConfigProperty<T> getProperty(String propname) {
		return propmap.get(propname);
	}
	
	
	private ICfgChangeListener listener = null;

	@Override
	public void setListener(ICfgChangeListener listener) {
		this.listener = listener;
	}
	
}
