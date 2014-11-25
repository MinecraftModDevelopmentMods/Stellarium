package stellarium.construct;

import stellarium.config.IConfigCategory;
import stellarium.config.IConfigProperty;

public class CPropLangStrs {
	
	//------------------------------------ Types -----------------------------------//
	public static final String orbtype = "OrbT";
	public static final String cbtype = "CBT";
	
	
	//-------------------------- StellarMv Basic Properties ------------------------//
	public static final String basicprops = "BRProps";
	
	public static final String msun = "Msun";
	public static final String yr = "yr";
	public static final String day = "day";
	public static final String au = "Au";
	
	//------------------------------ MvEntry Properties ----------------------------//
	public static final String name = "Name";
	public static final String mass = "Mass";
	
	
	public static void onRegister()
	{
		CPropLangRegistry.instance().register(basicprops, "cmv.basicprops");
		
		//these has .expl
		CPropLangRegistry.instance().register(msun, "cmv.msun");
		CPropLangRegistry.instance().register(yr, "cmv.yr");
		CPropLangRegistry.instance().register(day, "cmv.day");
		CPropLangRegistry.instance().register(au, "cmv.au");
		
		//these has .expl
		CPropLangRegistry.instance().register(orbtype, "cmv.orbtype");
		CPropLangRegistry.instance().register(cbtype, "cmv.cbtype");
		
		//these has .expl
		CPropLangRegistry.instance().register(name, "cmv.ent.name");
		CPropLangRegistry.instance().register(mass, "cmv.ent.mass");

		
		
	}
	
	
	public static String getExpl(String propid)
	{
		return CPropLangRegistry.instance().getLangfromID(propid) + ".expl";
	}
	
	public static <T> IConfigProperty<T> addProperty(IConfigCategory cat, String proptype, String propname, T def)
	{
		return cat.addProperty(proptype, propname, def).setExpl(getExpl(propname));
	}
}
