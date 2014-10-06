package stellarium.world;

import stellarium.stellars.ExtinctionRefraction;
import stellarium.stellars.StellarManager;
import stellarium.util.math.Spmath;
import stellarium.util.math.Vec;
import net.minecraft.world.WorldProviderSurface;

public class StellarWorldProvider extends WorldProviderSurface {
	
    public float calculateCelestialAngle(long par1, float par3)
    {
    	if(StellarManager.Earth.EcRPos==null)
    		StellarManager.Update(par1+par3, isSurfaceWorld());
    	Vec sun=StellarManager.Sun.GetPosition();
    	double h=Math.asin(ExtinctionRefraction.Refraction(sun, true).z);
    	if(sun.x<0) h=Math.PI-h;
    	if(sun.x>0 && h<0) h=h+2*Math.PI;
    	return (float)(Spmath.fmod((h/2/Math.PI)+0.75,2*Math.PI));
    }

    public int getMoonPhase(long par1)
    {
    	if(StellarManager.Earth.EcRPos==null)
    		StellarManager.Update(par1, isSurfaceWorld());
    	return (int)(StellarManager.Moon.Phase_Time()*8);
    }

}
