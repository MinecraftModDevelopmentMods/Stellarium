package stellarium.objs.mv;

import java.util.Iterator;
import java.util.List;

import stellarium.catalog.EnumCatalogType;
import stellarium.catalog.IStellarCatalog;
import stellarium.config.EnumCategoryType;
import stellarium.config.IStellarConfig;
import stellarium.objs.IStellarObj;
import stellarium.objs.mv.cbody.CBody;
import stellarium.objs.mv.cbody.CBodyRenderer;
import stellarium.render.StellarRenderingRegistry;
import stellarium.util.math.SpCoord;
import stellarium.view.ViewPoint;

public class StellarMv implements Iterable<CMvEntry> {
	
	public String id;
	
	public boolean isRemote;
	
	public int renderId;
	
	public CMvEntry root;
	public List<CBody> bodies;
	
	public StellarMv(String pid, boolean remote)
	{
		id = pid;
		isRemote = remote;
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
	
	protected CMvEntry newEntry(CMvEntry par, String name) {
		CMvEntry ne = new CMvEntry(this, par, name);
		par.addSatellite(ne);
		return ne;
	}

	
	public List<IStellarObj> getList(ViewPoint vp, SpCoord dir, double hfov) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public class MvIterator implements Iterator<CMvEntry> {

		CMvEntry now = null;
		
		@Override
		public boolean hasNext() {
			if(now == null)
				return true;
			if(now.hasSatellites())
				return true;
			if(!now.hasParent())
				return false;
			
			CMvEntry anc, anc2 = now;
			
			do
			{
				anc = anc2.getParent();
				
				if(anc.getSatelliteList().indexOf(anc2)+1 < anc.getSatelliteList().size())
					return true;
				
				anc2 = anc2.getParent();
			}
			while(anc2.hasParent());

			return false;
		}

		@Override
		public CMvEntry next() {
			if(now == null)
			{
				now = root;
				return now;
			}
			
			if(now.hasSatellites())
			{
				now = now.getSatelliteList().get(0);
				return now;
			}
			
			CMvEntry anc, anc2 = now;
			int ind;
			
			do
			{
				anc = anc2.getParent();
				
				ind = anc.getSatelliteList().indexOf(anc2);
				if(ind + 1 < anc.getSatelliteList().size())
				{
					now = anc.getSatelliteList().get(ind + 1);
					return now;
				}
				
				anc2 = anc2.getParent();
			}
			while(anc2.hasParent());
			
			return null;
		}

		@Override
		public void remove() {
			//Not Removable via this Iterator.
		}
		
	}

	
	public Iterator<CMvEntry> iterator()
	{
		return new MvIterator();
	}

}
