package stellarium.lighting;

import java.util.List;
import java.util.Set;

import sciapi.api.value.IValRef;
import sciapi.api.value.euclidian.EVector;
import sciapi.api.value.euclidian.EVectorSet;
import sciapi.api.value.numerics.IReal;
import stellarium.mech.OpFilter;
import stellarium.mech.OpFilter.WaveFilter;
import stellarium.mech.Wavelength;
import stellarium.util.math.SpCoord;
import stellarium.util.math.Spmath;
import stellarium.util.math.VecMath;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class LightModel {
	
	private Set<ILightSource> srcSet = Sets.newIdentityHashSet();
	private Set<Reflector> reflectorSet = Sets.newIdentityHashSet();
		
	public void registerLightSource(ILightSource src) {
		srcSet.add(src);
	}
	
	public void unregisterLightSource(ILightSource src) {
		srcSet.remove(src);
	}
	
	public void registerReflector(IReflector reflector) {
		reflectorSet.add(new Reflector(reflector));
	}
	
	public void unregisterReflector(IReflector reflector) {
		reflectorSet.remove(new Reflector(reflector));
	}
	
	public void updateLighting(OpFilter filter, float partime) {
		if(filter.isRGB()) {
			
		} else {
			for(ILightSource src : srcSet)
			{
				for(Reflector reflector : reflectorSet)
					reflector.calcReflector(src, partime);
				
				
				
				for(WaveFilter wfilter : filter.getFilterList()) {
					Wavelength wl = wfilter.wl;
					
					EVector pos = src.getPosition(partime);
					double lum = src.getLuminosity(wl);
					
					src.addLightingData(wl, new LightingDataSource(lum, pos));
				}
			}
			
		}
	}

	private void setShades() {
		// TODO Auto-generated method stub
		
	}
	
	private class Reflector{
		private IReflector instance;
		private SpCoord spPos = new SpCoord();
		private double distance;
		
		public Reflector(IReflector reflector) {
			this.instance = reflector;
		}
		
		public IReflector getInstance() {
			return this.instance;
		}

		public void calcReflector(ILightSource src, float partime) {
			EVector pos = new EVector(3);
			pos.set(VecMath.sub(instance.getPosition(partime), src.getPosition(partime)));
			distance = Spmath.getD(VecMath.size(pos));
			spPos.setWithVec(pos);
		}
	}
	
	private class EclipsingSet {
		private Reflector ref1, ref2;
		
		public EclipsingSet(Reflector ref1, Reflector ref2) {
			this.ref1 = ref1;
			this.ref2 = ref2;
		}
	}
}
