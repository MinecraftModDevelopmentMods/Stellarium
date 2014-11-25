package stellarium.objs.mv;

import java.util.Map;

import com.google.common.collect.Maps;

public class StellarMvFormatter {

	Map<String, MvInfo> infmap = Maps.newHashMap();
	
	public void formatMv(StellarMvLogical lmv, StellarMv nmv)
	{
		//TODO Total Stub
		nmv.Au = lmv.Au;
		nmv.Msun = lmv.Msun;
		nmv.day = lmv.day;
		nmv.yr = lmv.yr;
		
		for(CMvEntry ent : lmv)
		{
			for(CMvEntry sat : ent.getSatelliteList())
			{
				
			}
		}
	}
	
	public MvInfo getInfo(CMvEntry ent)
	{
		MvInfo inf = infmap.get(ent.getName());
		if(inf == null)
			infmap.put(ent.getName(), inf = new MvInfo());
		return inf;
	}
	
	public class MvInfo
	{
		public double mass_sum;
	}
	
}
