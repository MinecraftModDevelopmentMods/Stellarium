package stellarium.objs.mv;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Stack;

import com.google.common.collect.Lists;

import stellarium.catalog.IStellarCatalogData;
import stellarium.catalog.IStellarCatalogProvider;
import stellarium.config.IConfigCategory;
import stellarium.config.IConfigFormatter;
import stellarium.config.IConfigurableData;
import stellarium.config.IStellarConfig;
import stellarium.objs.mv.cbody.CBody;
import stellarium.util.math.SpCoord;
import stellarium.view.ViewPoint;

/**Logical StellarMv for Configuration*/
public class StellarMvLogical implements IStellarCatalogData, Iterable<CMvEntry> {
	
	public int renderId;
	protected StellarMvCatalog provider;
	
	protected CMvEntry root;
	protected CMvCfgBase cfg;
	
	public double Msun, yr, day, Au;
	
	public StellarMvLogical(StellarMvCatalog par)
	{
		provider = par;
		cfg = new CMvCfgLogical(this);
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
		if(mv.orbit() != null)
			mv.orbit().getOrbitType().onRemove(mv.orbit());
		
		if(mv.hasParent())
			mv.getParent().removeSatellite(mv);
	}

	
	public class MvIterator implements Iterator<CMvEntry> {

		CMvEntry now = null;
		
		Stack<ListIterator> ites = new Stack();
		
		public boolean hasNextRec()
		{
			if(ites.isEmpty())
				return false;
						
			ListIterator ite = ites.pop();
			boolean hn = ite.hasNext() || hasNextRec();
			ites.push(ite);
			
			return hn;
		}
		
		@Override
		public boolean hasNext() {
			
			if(ites.isEmpty())
				return true;

			if(now != null && now.hasSatellites())
				return true;
			
			return hasNextRec();
			
		}

		@Override
		public CMvEntry next() {
			
			if(ites.isEmpty())
			{
				ListIterator<CMvEntry> rt = root.getSatelliteList().listIterator();
				ites.push(rt);
				return now = root;
			}
			
			if(now.hasSatellites())
			{
				ListIterator<CMvEntry> ite = now.getSatelliteList().listIterator();
				ites.push(ite);
				return now = ite.next();
			}
			
			while(!ites.isEmpty())
			{
				ListIterator<CMvEntry> ite = ites.pop();
				if(ite.hasNext())
				{
					ites.push(ite);
					return now = ite.next();
				}
			}
			
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
	
	@Override
	public void formatConfig(IStellarConfig subConfig) {
		cfg.formatConfig(subConfig);
	}

	@Override
	public void applyConfig(IStellarConfig config) {
		cfg.loadConfig(config);
	}
	
	@Override
	public void saveConfig(IStellarConfig subConfig) {
		cfg.saveConfig(subConfig);
	}

	@Override
	public IStellarCatalogProvider getProvider() {
		return provider;
	}

	
	@Override
	public IConfigFormatter getSubFormatter(String name) { return null; }
	@Override
	public IConfigurableData getSubData(String name) { return null; }
	
}
