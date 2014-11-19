package stellarium.objs.mv;

import java.util.Iterator;
import java.util.List;

import stellarium.catalog.EnumCatalogType;
import stellarium.catalog.IStellarCatalog;
import stellarium.config.EnumCategoryType;
import stellarium.config.ICfgArrMListener;
import stellarium.config.IConfigCategory;
import stellarium.config.IConfigProperty;
import stellarium.config.IMConfigProperty;
import stellarium.config.IPropertyRelation;
import stellarium.config.IStellarConfig;
import stellarium.objs.IStellarObj;
import stellarium.objs.mv.cbody.CBody;
import stellarium.objs.mv.cbody.CBodyRenderer;
import stellarium.render.StellarRenderingRegistry;
import stellarium.util.math.SpCoord;
import stellarium.view.ViewPoint;

public class StellarMv extends StellarMvLogical implements Iterable<CMvEntry> {
		
	public boolean isRemote;
	
	public List<CBody> bodies;
	
	private CMvCfgPhysical cfg2;
	
	public StellarMv(String pid, int rid, boolean remote)
	{
		super(pid);
		renderId = rid;
		isRemote = remote;
		cfg2 = new CMvCfgPhysical(this);
	}
	
	public void update(int tick) {
		
		for(CMvEntry entry : this)
		{
			//TODO Total Stub(unit)
			entry.orbit().update();
			if(!entry.isVirtual())
				entry.cbody().update(tick);
		}
		
	}
	
	public List<CBody> getList(ViewPoint vp, SpCoord dir, double hfov) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public CMvEntry findEntry(String name)
	{
		for(CMvEntry e : this)
		{
			if(e.getName().equals(name))
				return e;
		}
		
		return null;
	}

	public void formatConfig(IStellarConfig subConfig) {
		cfg2.formatConfig(subConfig);
	}

	public void loadFromConfig(IStellarConfig subConfig) {
		cfg2.loadConfig(subConfig);
	}

	public void saveAsConfig(IStellarConfig subConfig) {
		cfg2.saveConfig(subConfig);
	}
}
