package stellarium.lang;

import stellarium.config.IConfigCategory;
import stellarium.config.IConfigProperty;

public class CPropLangStrs {
	
	//------------------------------------ Basics -----------------------------------//
	public static final String yes = "Yes";
	public static final String no = "No";
	public static final String size = "size";
	public static final String yaw = "yaw";
	public static final String pitch = "pitch";
	
	//------------------------------------ Types -----------------------------------//
	public static final String orbtype = "OrbT";
	public static final String cbtype = "CBT";
	
	//-------------------------------- Catalog Names -------------------------------//
	public static final String catalog = "Catalog";
	public static final String moving = "Moving";
	
	
	//-------------------------- StellarMv Basic Properties ------------------------//
	public static final String basicprops = "BRProps";
	
	public static final String msun = "Msun";
	public static final String yr = "yr";
	public static final String day = "day";
	public static final String au = "Au";
	
	//------------------------------ StellarMv Defaults ----------------------------//
	public static final String def = "(default)";

	
	//------------------------------ MvEntry Properties ----------------------------//
	public static final String name = "Name";
	public static final String mass = "Mass";
	
	//------------------------------ MvEntry Relations -----------------------------//
	public static final String nameRelationTooltip = "Name Relation";
	public static final String orbTypeRelation = "Orbit Type Relation";
	public static final String cbTypeRelation = "Celestial Body Type Relation";
	
	//--------------------------- Loading Failure Messages -------------------------//
	public static final String orbmissing = "Missing Orbit";
	public static final String cbmissing = "Missing CBody";
	public static final String orbNotLocked = "Orbit Type Not Locked";
	public static final String cbNotLocked = "CBody Type Not Locked";
	
	public static void onRegister()
	{
		CPropLangRegistry.instance().register(yes, "basis.yes");
		CPropLangRegistry.instance().register(no, "basis.no");
		CPropLangRegistry.instance().register(size, "basis.size");
		CPropLangRegistry.instance().register(yaw, "basis.yaw");
		CPropLangRegistry.instance().register(pitch, "basis.pitch");
		
		CPropLangRegistry.instance().register(moving, "cat.moving.name");
		
		CPropLangRegistry.instance().register(basicprops, "cmv.basicprops");
		
		//these has .expl
		CPropLangRegistry.instance().register(msun, "cmv.msun");
		CPropLangRegistry.instance().register(yr, "cmv.yr");
		CPropLangRegistry.instance().register(day, "cmv.day");
		CPropLangRegistry.instance().register(au, "cmv.au");
		
		CPropLangRegistry.instance().register(def, "cmv.def");
		
		//these has .expl, which contains the name of entry.
		CPropLangRegistry.instance().register(orbtype, "cmv.orbtype");
		CPropLangRegistry.instance().register(cbtype, "cmv.cbtype");
		
		//these has .expl
		CPropLangRegistry.instance().register(name, "cmv.ent.name");
		CPropLangRegistry.instance().register(mass, "cmv.ent.mass");
		
		CPropLangRegistry.instance().register(nameRelationTooltip, "cmv.ent.rel.name");
		CPropLangRegistry.instance().register(orbTypeRelation, "cmv.ent.rel.orbtype");
		CPropLangRegistry.instance().register(cbTypeRelation, "cmv.ent.rel.cbtype");

		//these has .expl
		CPropLangRegistry.instance().register(orbmissing, "cmv.ent.err.missingorbit");
		CPropLangRegistry.instance().register(cbmissing, "cmv.ent.err.missingcbody");
		CPropLangRegistry.instance().register(orbNotLocked, "cmv.ent.err.orbitnotlocked");
		CPropLangRegistry.instance().register(cbNotLocked, "cmv.ent.err.cbodynotlocked");
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
