package stellarium.objs.mv;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

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

/**Physical StellarMv for gameplay*/
public class StellarMv extends StellarMvLogical implements Iterable<CMvEntry> {
		
	public boolean isRemote;
	public List<CBody> bodies = Lists.newArrayList();
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
	
	protected void removeEntry(CMvEntry entry) {
		super.removeEntry(entry);
		bodies.remove(entry.cbody());
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
