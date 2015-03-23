package stellarium.objs.mv;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import stellarium.objs.mv.cbody.CBodyTStar;
import stellarium.objs.mv.cbody.ICBodyType;
import stellarium.objs.mv.orbit.IOrbitType;
import stellarium.objs.mv.orbit.OrbTStationary;

public class CMvTypeRegistry {
	
	private static CMvTypeRegistry ins;
	
	private Map<String, IOrbitType> orbtm = Maps.newHashMap();
	private Map<String, ICBodyType> cbtm = Maps.newHashMap();
	
	public static CMvTypeRegistry instance()
	{
		if(ins == null)
			ins = new CMvTypeRegistry();
		return ins;
	}
	
	public CMvTypeRegistry()
	{
		this.registerOrbType(new OrbTStationary());
		
		this.registerCBodyType(new CBodyTStar());
	}
	
	public void registerOrbType(IOrbitType orbt)
	{
		orbtm.put(orbt.getTypeName(), orbt);
		orbt.init();
	}
	
	public void registerCBodyType(ICBodyType cbt)
	{
		cbtm.put(cbt.getTypeName(), cbt);
		cbt.init();
	}
	
	
	public IOrbitType getOrbType(String name)
	{
		return orbtm.get(name);
	}
	
	public ICBodyType getCBodyType(String name)
	{
		return cbtm.get(name);
	}
	
	
	public Set<String> getRegOrbTypeNames()
	{
		return orbtm.keySet();
	}
	
	public Set<String> getRegCBodyTypeNames()
	{
		return cbtm.keySet();
	}


}
