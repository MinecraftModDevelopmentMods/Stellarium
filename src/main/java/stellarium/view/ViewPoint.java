package stellarium.view;

import java.util.HashMap;
import java.util.Map;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import scala.collection.immutable.Map.Map1;
import sciapi.api.value.IValRef;
import sciapi.api.value.euclidian.CrossUtil;
import sciapi.api.value.euclidian.EVector;
import sciapi.api.value.euclidian.IEVector;
import stellarium.settings.StellarSettings;
import stellarium.sky.ISkySet;
import stellarium.stellars.local.*;
import stellarium.util.math.*;
import stellarium.world.IWorldHandler;


public class ViewPoint {
	
	//The WorldHandler
	protected IWorldHandler world;
	
	

	//EcRPos of Viewpoint
	public EVector EcRPos = new EVector(3);

	
	//The StellarManager
	public StellarSettings manager;
	
	private ISkySet skyset;
	
	public ISkySet getSkySet()
	{
		return skyset;
	}
	
	public EVector getEcRPos() {
		// TODO Auto-generated method stub
		return null;
	}

	
/*	public Vec GetLocPos(Vec TarEcRPos){
		Vec tar=Vec.Sub(TarEcRPos, EcRPos);
		if(HostCBody!=null) return HorCoord.Transform(TarEcRPos);
		else return tar;
	}*/
}
