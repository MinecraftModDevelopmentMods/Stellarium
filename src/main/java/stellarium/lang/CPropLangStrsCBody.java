package stellarium.lang;

public class CPropLangStrsCBody {

	//--------------------------------- Orbit Types --------------------------------//
	//Stationary Orbit (Root-Default)
	public static String storb = "St";
	//Orbit with small eccentricity
	public static final String seccorb = "secc";
	
	//Stationary Orbit Properties
	public static final String position = "pos";
	
	//Small Eccentricity Orbit Properties
	public static final String semiMajorAxis = "a";
	public static final String eccentricity = "e";
	public static final String inclination = "i";
	public static final String inclinationChange = "id";
	public static final String longitudeAscNode = "Om";
	public static final String longitudeAscNodeChange = "Omd";
	
	public static final String isMajor = "isMajor";

	public static final String argumentPeri = "w";
	public static final String argumentPeriChange = "wd";
	public static final String meanAnomaly = "M";
	
	public static final String longitudePeri = "wbar";
	public static final String longitudePeriChange = "wbard";
	public static final String meanLongitude = "L";
	
	
	//--------------------------------- CBody Types --------------------------------//
	//Star Body 
	public static String starbody = "Star";
	
	//CBody basic variables
	public static final String pole = "pole";
	public static final String primeMeridian = "prMer";
	public static final String east = "east";

	//CBody basic Properties
	public static final String tidalLocked = "tidalLock";
	
	public static final String propPole = "pPole";
	public static final String dayViewVernal = "tickVV";

	public static final String periodRotation = "P_rot";
	public static final String hasPrecession = "has_prec";
	public static final String periodPrecession = "P_prec";
	
	
	//------------------------ Orbit Loading Failure Messages ----------------------//
	public static final String eccOutOfRange = "Too big eccenticity; it should be smaller than 0.5";


	//------------------------ CBody Loading Failure Messages ----------------------//
	public static final String calculationError = "Calculation Error, Some of config value might be invalid";
	public static final String invalidPrecession = "Invalid Precession Period";
	public static final String invalidRotation = "Invalid Rotation Period";

	
	public static void onRegister()
	{
		//Orbit Types
		CPropLangRegistry.instance().register(storb, "cmv.orbtype.stationary");
		CPropLangRegistry.instance().register(seccorb, "cmv.orbtype.smallecc");
		
		//these has .expl
		CPropLangRegistry.instance().register(position, "cmv.orbit.prop.position");
		
		//these has .expl {
		CPropLangRegistry.instance().register(semiMajorAxis, "cmv.orbit.prop.semimajoraxis");
		CPropLangRegistry.instance().register(eccentricity, "cmv.orbit.prop.eccentricity");
		CPropLangRegistry.instance().register(inclination, "cmv.orbit.prop.inclination");
		CPropLangRegistry.instance().register(inclinationChange, "cmv.orbit.prop.inclinationd");
		CPropLangRegistry.instance().register(longitudeAscNode, "cmv.orbit.prop.longascnode");
		CPropLangRegistry.instance().register(longitudeAscNodeChange, "cmv.orbit.prop.longascnoded");
		
		CPropLangRegistry.instance().register(isMajor, "cmv.orbit.prop.ismajor");

		CPropLangRegistry.instance().register(argumentPeri, "cmv.orbit.prop.argperi");
		CPropLangRegistry.instance().register(argumentPeriChange, "cmv.orbit.prop.argperid");
		CPropLangRegistry.instance().register(meanAnomaly, "cmv.orbit.prop.meananomaly");
		
		CPropLangRegistry.instance().register(longitudePeri, "cmv.orbit.prop.meananomaly");
		CPropLangRegistry.instance().register(longitudePeriChange, "cmv.orbit.prop.meananomaly");
		CPropLangRegistry.instance().register(meanLongitude, "cmv.orbit.prop.meananomaly");
		//} end
		
		
		//CBody Types
		CPropLangRegistry.instance().register(starbody, "cmv.cbodytype.star");
		
		//these has .expl
		CPropLangRegistry.instance().register(pole, "cmv.cbody.var.pole");
		CPropLangRegistry.instance().register(primeMeridian, "cmv.cbody.var.prmer");
		CPropLangRegistry.instance().register(east, "cmv.cbody.var.east");
		
		//these has .expl
		CPropLangRegistry.instance().register(propPole, "cmv.cbody.prop.pole");
		CPropLangRegistry.instance().register(tidalLocked, "cmv.cbody.prop.tidallock");
		CPropLangRegistry.instance().register(dayViewVernal, "cmv.cbody.prop.dayviewvernal");
		CPropLangRegistry.instance().register(periodRotation, "cmv.cbody.prop.rotation");
		CPropLangRegistry.instance().register(hasPrecession, "cmv.cbody.prop.hasprec");
		CPropLangRegistry.instance().register(periodPrecession, "cmv.cbody.prop.precession");
		
		//these has .expl
			//with one parameter: name of variable with error
		CPropLangRegistry.instance().register(calculationError, "cmv.cbody.err.calcerror");
		
		CPropLangRegistry.instance().register(invalidPrecession, "cmv.cbody.err.precession");
		CPropLangRegistry.instance().register(invalidRotation, "cmv.cbody.err.rotation");
	}
	
}
