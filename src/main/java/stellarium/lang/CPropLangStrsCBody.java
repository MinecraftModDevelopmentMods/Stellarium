package stellarium.lang;

public class CPropLangStrsCBody {

	//--------------------------------- Orbit Types --------------------------------//
	//Stationary Orbit (Root-Default)
	public static String storb = "St";
	
	//--------------------------------- CBody Types --------------------------------//
	//Star Body
	public static String starbody = "Star";
	
	public static void onRegister()
	{
		//Orbits
		CPropLangRegistry.instance().register(storb, "cmv.orbtype.stationary");
		
		//CBodies
		CPropLangRegistry.instance().register(starbody, "cmv.cbodytype.star");
	}
	
}
