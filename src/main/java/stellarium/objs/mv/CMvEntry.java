package stellarium.objs.mv;

import java.util.List;

import com.google.common.collect.Lists;

import stellarium.lighting.ILightingEntry;
import stellarium.objs.mv.additive.CAdditive;
import stellarium.objs.mv.cbody.CBody;
import stellarium.objs.mv.orbit.Orbit;

public class CMvEntry implements ILightingEntry {
	
	public CMvEntry(StellarMvLogical main, CMvEntry par, String pname)
	{
		this.mv = main;
		this.parent = par;
		this.name = pname;
	}

	private final StellarMvLogical mv;
	
	private Orbit orbit;
	private CBody cbody;
	private CAdditive additive = null;
	
	private CMvEntry parent;
	private List<CMvEntry> satellites = Lists.newArrayList();

	private String name;
	private double mass;
	
	public StellarMvLogical getMain() { return mv; }
	
	public Orbit orbit() { return orbit; }
	public CBody cbody() { return cbody; }
	public CAdditive additive() { return additive; }
	
	public CMvEntry getParent() { return parent; }
	
	public List<CMvEntry> getSatelliteList() { return satellites; }
	public String getName() { return name; }
	public double getMass() { return mass; }
	
	public boolean hasParent() { return parent != null; }
	public boolean hasSatellites() { return !satellites.isEmpty(); }
	
	@Override
	public boolean isVirtual() { return cbody == null; }
	public boolean hasAdditive() { return additive != null; }
	
	@Override
	public boolean hasChildEntry() { return this.hasSatellites(); }

	@Override
	public List<ILightingEntry> getChildList() { return (List)satellites; }
	
	@Override
	public double getInfluenceSize() { return orbit.getInfluenceSize(); }
	
	
	protected CMvEntry setOrbit(Orbit orb) {
		orbit = orb;
		return this;
	}
	
	protected CMvEntry setCBody(CBody cb) {
		cbody = cb;
		return this;
	}
	
	protected CMvEntry setAdditive(CAdditive ad) {
		additive = ad;
		return this;
	}
	
	protected CMvEntry addSatellite(CMvEntry entry) {
		satellites.add(entry);
		return this;
	}
	
	protected CMvEntry removeSatellite(CMvEntry entry) {
		satellites.remove(entry);
		return this;
	}
	
	protected CMvEntry setMass(double m) {
		mass = m;
		return this;
	}
	
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
}
