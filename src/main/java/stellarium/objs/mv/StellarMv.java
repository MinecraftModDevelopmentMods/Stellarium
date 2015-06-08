package stellarium.objs.mv;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

import stellarium.StellarManager;
import stellarium.catalog.EnumCatalogType;
import stellarium.catalog.IStellarCatalog;
import stellarium.catalog.IStellarCatalogProvider;
import stellarium.config.ConfigPropTypeRegistry;
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
import stellarium.objs.mv.cbody.TypeCBodyPropHandler;
import stellarium.objs.mv.orbit.TypeOrbitPropHandler;
import stellarium.render.StellarRenderingRegistry;
import stellarium.util.math.SpCoord;
import stellarium.view.ViewPoint;

/**Physical StellarMv for gameplay*/
public class StellarMv extends StellarMvLogical implements IStellarCatalog, Iterable<CMvEntry> {
		
	protected StellarManager manager;
	public List<CBody> bodies = Lists.newArrayList();
	
	public StellarMv(StellarMvCatalog prov, int rid)
	{
		super(prov);
		renderId = rid;
		cfg = new CMvCfgPhysical(this);
	}
	
	public StellarMv(StellarManager mn, StellarMvCatalog prov, int rid)
	{
		this(prov, rid);
		setManager(mn);
	}
	
	public void setManager(StellarManager mn)
	{
		manager = mn;
	}
	
	public void update(long tick) {
		double timeDay = tick / this.day;
		double timeYear = timeDay / this.yr;

		for(CMvEntry entry : this)
		{
			//TODO Total Stub(unit)
			entry.orbit().update(timeYear);
			if(!entry.isVirtual())
				entry.cbody().update(timeDay);
		}
	}

	@Override
	public List<CBody> getList(ViewPoint vp, SpCoord dir, double hfov) {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected void removeEntry(CMvEntry entry) {
		super.removeEntry(entry);
	}

	@Override
	public int getRUpTick() {
		return 1;
	}
	
	
	@Override
	public IStellarCatalogProvider getProvider() {
		return provider;
	}

	@Override
	public boolean isDisabled() { return false; }
}
