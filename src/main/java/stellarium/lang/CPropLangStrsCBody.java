package stellarium.lang;

public class CPropLangStrsCBody {

	//--------------------------------- Orbit Types --------------------------------//
	//Stationary Orbit (Root-Default)
	public static String storb = "St";
	
	//--------------------------------- CBody Types --------------------------------//
	//Star Body 
	public static String starbody = "Star";

	//CBody basic Properties
	public static final String tidalLocked = "tidalLock";

	public static final String pole = "pole";
	public static final String dayViewVernal = "tickVV";

	public static final String periodRotation = "P_rot";
	public static final String hasPrecession = "has_prec";
	public static final String periodPrecession = "P_prec";


	
	public static void onRegister()
	{
		//Orbit Types
		CPropLangRegistry.instance().register(storb, "cmv.orbtype.stationary");
		
		//CBody Types
		CPropLangRegistry.instance().register(starbody, "cmv.cbodytype.star");
		
		//these has .expl
		CPropLangRegistry.instance().register(tidalLocked, "cmv.cbody.prop.tidallock");
		CPropLangRegistry.instance().register(pole, "cmv.cbody.prop.pole");
		CPropLangRegistry.instance().register(dayViewVernal, "cmv.cbody.prop.dayviewvernal");
		CPropLangRegistry.instance().register(periodRotation, "cmv.cbody.prop.rotation");
		CPropLangRegistry.instance().register(hasPrecession, "cmv.cbody.prop.hasprec");
		CPropLangRegistry.instance().register(periodPrecession, "cmv.cbody.prop.precession");
	}
	
}
