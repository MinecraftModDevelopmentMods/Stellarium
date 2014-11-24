package stellarium.objs.mv;

import java.util.Iterator;
import java.util.List;

import stellarium.config.IStellarConfig;
import stellarium.objs.mv.cbody.CBody;
import stellarium.util.math.SpCoord;
import stellarium.view.ViewPoint;

/**Logical StellarMv for Configuration*/
public class StellarMvLogical implements Iterable<CMvEntry> {
	
	public String id;
	public int renderId;
	public CMvEntry root;
	private CMvCfgLogical cfg;
	
	public double Msun, syr, sday, Au;
	
	public StellarMvLogical(String pid)
	{
		id = pid;
		cfg = new CMvCfgLogical(this);
	}
	
	public String getID()
	{
		return id;
	}
	
	public void setID(String pid)
	{
		id = pid;
	}
	
	protected CMvEntry newEntry(CMvEntry par, String name) {
		CMvEntry ne = new CMvEntry(this, par, name);
		if(par != null)
			par.addSatellite(ne);
		else root = ne;
		return ne;
	}
	
	protected void removeEntry(CMvEntry mv)
	{
		if(mv.hasAdditive())
			mv.additive().getAdditiveType().onRemove(mv.additive());
		if(!mv.isVirtual())
			mv.cbody().getCBodyType().onRemove(mv.cbody());
		mv.orbit().getOrbitType().onRemove(mv.orbit());
		
		mv.getParent().removeSatellite(mv);
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
		cfg.formatConfig(subConfig);
	}

	public void loadFromConfig(IStellarConfig subConfig) {
		cfg.loadConfig(subConfig);
	}

	public void saveAsConfig(IStellarConfig subConfig) {
		cfg.saveConfig(subConfig);
	}
	
	
	public void formatMv(StellarMv nmv)
	{
		//TODO Total Stub
		nmv.Au = Au;
		nmv.Msun = Msun;
		nmv.sday = sday;
		nmv.syr = syr;
		
		
	}
}
