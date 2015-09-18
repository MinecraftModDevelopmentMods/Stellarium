package stellarium.lighting;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import sciapi.api.value.euclidian.EVector;
import stellarium.mech.OpFilter;
import stellarium.mech.OpFilter.WaveFilter;
import stellarium.mech.Wavelength;
import stellarium.util.math.SpCoord;
import stellarium.util.math.Spmath;
import stellarium.util.math.VecMath;
import stellarium.view.ViewPoint;

public class LightModel {
	
	private Set<ILightSource> srcSet = Sets.newIdentityHashSet();
	private ILightingEntryContainer system;
	private CalculationUnit unit = new CalculationUnit();
	
	public LightModel(ILightingEntryContainer system) {
		this.system = system;
	}
	
	public void registerLightSource(ILightSource src) {
		srcSet.add(src);
	}
	
	public void unregisterLightSource(ILightSource src) {
		srcSet.remove(src);
	}
	
	
	public void updateLighting(ViewPoint vp, OpFilter filter, float partime) {
		if(filter.isRGB()) {
			
		} else {
			for(ILightSource src : srcSet)
			{
				unit.setup(src, vp, filter, partime);
				unit.calcLight(system.getRootEntry());
			}
		}
	}
	
	private class CalculationUnit {
		private ILightSource src;
		private ViewPoint vp;
		private OpFilter filter;
		private float partime;
		
		private EVector pos = new EVector(3);
		private double dist2;
		private double srcRadius;
		
		private EVector bufferVec = new EVector(3), bvec1 = new EVector(3), bvec2 = new EVector(3);
		
		private Multimap<ILightingEntry, LightingShade> shadeMap = HashMultimap.create();
		private Map<ILightingEntry, BufferData> bufferData = Maps.newHashMap();
		
		protected void setup(ILightSource src, ViewPoint vp, OpFilter filter, float partime) {
			this.src = src;
			this.vp = vp;
			this.filter = filter;
			this.partime = partime;
			
			pos.set(src.getPosition(partime));
			this.dist2 = Spmath.getD(VecMath.size2(VecMath.sub(this.pos, vp.getEcRPos())));
			this.srcRadius = src.getMaxRadius();
		}
		
		protected void calcLight(ILightingEntry rootEntry) {
			shadeMap.clear();
			
			this.setSystemBufferData(rootEntry);
			this.calcShade(rootEntry);
			
			for(WaveFilter wfilter : filter.getFilterList()) {
				Wavelength wl = wfilter.wl;
				
				double lum = src.getLuminosity(wl);
				
				src.addLightingData(wl, new LightingDataSource(lum / dist2));
			}
		}
		
		protected void setSystemBufferData(ILightingEntry entry) {
			this.createOrSetBufferData(entry);
			
			for(ILightingEntry childEntry : entry.getChildList())
				this.setSystemBufferData(childEntry);
		}
		
		protected void calcShade(ILightingEntry entry) {
			List<BufferData> dataList = Lists.newArrayList();
			BufferData lightSystem = setupBufferList(entry, dataList);
			
			Collections.sort(dataList, BufferData.comparator);
			
			int j;
			boolean crossBound;
			BufferData current, cur2;
			
			for(int i = 0; i < dataList.size(); i++) {
				j = (i + 1) % dataList.size();
				current = dataList.get(i);
				crossBound = (current.aMax < current.aMin);
				
				while(j != i && (dataList.get(j).aMin < current.aMax || crossBound && dataList.get(j).aMin > current.aMin)) {
					cur2 = dataList.get(j);
					
					if(!(current.dMax < cur2.dMin && current.dMin > cur2.dMax))
						this.calcShadeBetween(current.entry, cur2.entry);
					j = (j + 1) % dataList.size();
				}
			}
			
			for(ILightingEntry childEntry : entry.getChildList())
				calcShade(childEntry);
			
			if(lightSystem != null && lightSystem.entry.hasChildEntry())
			{
				for(ILightingEntry childEntry : entry.getChildList())
					calcShadeBetween(lightSystem.entry, childEntry);
			}
			
			this.revertBuffer(entry);
		}
		
		protected void calcShadeBetween(ILightingEntry ent1, ILightingEntry ent2) {
			BufferData data1 = bufferData.get(ent1);
			BufferData data2 = bufferData.get(ent2);
			
			if(data2.checked)
			{
				if(data1.checked)
					this.checkShade(ent1, ent2);
				else
					this.calcShadeWithChecked(ent2, ent1);
				
				return;
			}
			
			if(data1.checked)
			{
				this.calcShadeWithChecked(ent1, ent2);
				return;
			}
			
			List<BufferData> dataList1 = Lists.newArrayList();
			List<BufferData> dataList2 = Lists.newArrayList();
			
			BufferData lightSystem = setupBufferList(ent1, dataList1);
			BufferData lightSystem2 = setupBufferList(ent2, dataList2);
			
			dataList1.addAll(dataList2);
			
			int j;
			boolean crossBound;
			BufferData current, cur2;
			
			for(int i = 0; i < dataList1.size(); i++) {
				current = dataList1.get(i);
				crossBound = (current.aMax < current.aMin);
				j = (i + 1) % dataList1.size();
				
				while(i != j && (dataList1.get(j).aMin < current.aMax || crossBound && dataList1.get(j).aMin > current.aMin)) {
					cur2 = dataList1.get(j);
					
					if(!(current.dMax < cur2.dMin && current.dMin > cur2.dMax))
					{
						if(dataList2.contains(current)^dataList2.contains(cur2))
							this.calcShadeBetween(current.entry, cur2.entry);
					}
					
					j = (j + 1) % dataList1.size();
				}
			}
			
			if(lightSystem != null && lightSystem.entry.hasChildEntry())
			{
				for(ILightingEntry childEntry : ent2.getChildList())
					calcShadeBetween(lightSystem.entry, childEntry);
			}
			
			if(lightSystem2 != null && lightSystem2.entry.hasChildEntry())
			{
				for(ILightingEntry childEntry : ent1.getChildList())
					calcShadeBetween(lightSystem.entry, childEntry);
			}
			
			this.revertBuffer(ent1);
			this.revertBuffer(ent2);
		}
		
		protected void calcShadeWithChecked(ILightingEntry checked, ILightingEntry unchecked) {
			List<BufferData> dataList = Lists.newArrayList();
			BufferData lightSystem = setupBufferList(unchecked, dataList);
			
			BufferData current = bufferData.get(checked);
			BufferData cur2;
			boolean crossBound = (current.aMax < current.aMin);
			
			for(int i = 0; i < dataList.size(); i++) {
				cur2 = dataList.get(i);
				
				if(!(current.aMax < cur2.aMin && current.aMin > cur2.aMax)) {
					if(!(current.dMax < cur2.dMin && current.dMin > cur2.dMax)) {
						checkShade(checked, cur2.entry);
					}
				}
			}

			this.revertBuffer(unchecked);
		}
		
		protected void checkShade(ILightingEntry ent1, ILightingEntry ent2) {
			double dist1 = bufferData.get(ent1).dist;
			double dist2 = bufferData.get(ent2).dist;
			
			if(dist1 < dist2)
				this.checkShadeInternal(ent1, ent2);
			else this.checkShadeInternal(ent2, ent1);
		}
		
		private void checkShadeInternal(ILightingEntry near, ILightingEntry far) {
			EVector pos1 = bufferData.get(near).pos;
			EVector pos2 = bufferData.get(far).pos;
			double dist1 = bufferData.get(near).dist;
			
			double radius1 = near.cbody().getMaxRadius();
			double radius2 = far.cbody().getMaxRadius();
			
			bufferVec.set(VecMath.div(1 + radius1 / this.srcRadius, pos1));
			bvec1.set(VecMath.sub(pos1, bufferVec));
			bvec2.set(VecMath.sub(pos2, bufferVec));
			double angle1 = (radius1 + this.srcRadius) / dist1;
			double angle2 = radius2 / Spmath.getD(VecMath.size(bvec2));
			
			if(angle1 > 0.05) angle1 = Math.asin(angle1);
			if(angle2 > 0.05) angle2 = Math.asin(angle2);
			
			double angle = VecMath.getAngle(bvec1, bvec2);
			
			if(angle < angle1 + angle2)
				shadeMap.put(far, new LightingShade(pos1, pos2, this.srcRadius, radius1, radius2, dist1, bufferData.get(far).dist));
		}
		
		protected BufferData setupBufferList(ILightingEntry entry, List<BufferData> dataList) {
			BufferData lightSystem = null;
			
			if(!entry.isVirtual())
				dataList.add(bufferData.get(entry).resetRadius(entry.cbody().getMaxRadius() + this.srcRadius));
			
			for(ILightingEntry childEntry : entry.getChildList())
			{
				BufferData data = bufferData.get(childEntry);
				
				if(!data.lightInSystem)
					dataList.add(data);
				else lightSystem = data;
			}
						
			return lightSystem;
		}
		
		protected void revertBuffer(ILightingEntry entry) {
			BufferData data = bufferData.get(entry);
			data.revert(getEffRadius(entry) + this.srcRadius);
		}
		
		protected void createOrSetBufferData(ILightingEntry entry) {
			BufferData data;
			
			if(!bufferData.containsKey(entry)) {
				data = new BufferData(entry);
				bufferData.put(entry, data);
			} else {
				data = bufferData.get(entry);
			}
			
			data.pos.set(VecMath.sub(entry.cbody().getPosition(this.partime), this.pos));
			data.radius = getEffRadius(entry) + this.srcRadius;
			data.setup();
		}
		
		protected double getEffRadius(ILightingEntry entry) {
			if(entry.hasChildEntry())
				return entry.getInfluenceSize();
			else return entry.cbody().getMaxRadius();
		}
	}
	
	private static class BufferData {
		public ILightingEntry entry;
		public EVector pos = new EVector(3);
		public double radius, dist;
		public SpCoord coord = new SpCoord();
		public double angle;
		public double aMin, aMax, dMin, dMax;
		public boolean lightInSystem;
		public boolean checked;
				
		private static final double SCALE = 2 * Math.PI;
		protected static final BufferComparator comparator = new BufferComparator();
		
		public BufferData(ILightingEntry entry) {
			this.entry = entry;
		}

		protected void setup() {
			this.checked = !entry.hasChildEntry();
			
			this.dist = Spmath.getD(VecMath.size(this.pos));
			coord.setWithVec(VecMath.div(this.dist, this.pos));
			this.angle = this.radius / this.dist;
			
			this.lightInSystem = (this.angle >= 1.0);
			if(this.lightInSystem)
				return;
			
			if(this.angle > 0.05) this.angle = Math.asin(this.angle);
			this.aMin = (coord.x - this.angle) % SCALE;
			this.aMax = (coord.x + this.angle) % SCALE;
			this.dMin = (coord.y - this.angle) % SCALE;
			this.dMax = (coord.y + this.angle) % SCALE;
		}
		
		protected BufferData resetRadius(double radius) {
			this.checked = true;
			
			this.radius = radius;
			this.angle = this.radius / dist;
			
			this.lightInSystem = (this.angle >= 1.0);
			if(this.lightInSystem)
				return this;
			
			if(this.angle > 0.05) this.angle = Math.asin(this.angle);
			this.aMin = (coord.x - this.angle) % SCALE;
			this.aMax = (coord.x + this.angle) % SCALE;
			this.dMin = (coord.y - this.angle) % SCALE;
			this.dMax = (coord.y + this.angle) % SCALE;
			
			return this;
		}
		
		protected void revert(double radius) {
			this.resetRadius(radius);
			this.checked = !entry.hasChildEntry();
		}
		
		protected static class BufferComparator implements Comparator<BufferData> {
			@Override
			public int compare(BufferData data1, BufferData data2) {
				if(data1.aMin < data2.aMin)
					return -1;
				else if(data1.aMin > data2.aMin)
					return 1;
				else if(data1.dMin > data2.dMin)
					return -1;
				else if(data1.dMin < data2.dMin)
					return 1;
				return 0;
			}
		}
	}
	
	private class Reflector{
		private IReflector instance;
		private EVector direction = new EVector(3);
		private double distance;
		
		public Reflector(IReflector reflector) {
			this.instance = reflector;
		}
		
		public IReflector getInstance() {
			return this.instance;
		}

		public void calcReflector(ILightSource src, float partime) {
			direction.set(VecMath.sub(instance.getPosition(partime), src.getPosition(partime)));
			this.distance = Spmath.getD(VecMath.size(this.direction));
			direction.set(VecMath.div(this.distance, this.direction));
		}
	}
}
