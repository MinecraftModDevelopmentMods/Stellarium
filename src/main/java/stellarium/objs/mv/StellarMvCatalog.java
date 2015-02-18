package stellarium.objs.mv;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import stellarium.StellarManager;
import stellarium.catalog.EnumCatalogType;
import stellarium.catalog.IStellarCatalog;
import stellarium.catalog.IStellarCatalogData;
import stellarium.catalog.IStellarCatalogProvider;
import stellarium.config.ConfigPropTypeRegistry;
import stellarium.config.EnumCategoryType;
import stellarium.config.ICfgArrMListener;
import stellarium.config.IConfigCategory;
import stellarium.config.IStellarConfig;
import stellarium.objs.IStellarObj;
import stellarium.objs.mv.cbody.CBody;
import stellarium.objs.mv.cbody.CBodyRenderer;
import stellarium.objs.mv.cbody.TypeCBodyPropHandler;
import stellarium.objs.mv.orbit.TypeOrbitPropHandler;
import stellarium.render.StellarRenderingRegistry;
import stellarium.util.math.SpCoord;
import stellarium.view.ViewPoint;

public class StellarMvCatalog implements IStellarCatalogProvider<StellarMvLogical> {
	
	public int renderId;
	
	private StellarMvFormatter formatter = new StellarMvFormatter();
	
	@Override
	public StellarMvLogical provideCatalogData(boolean isPhysical) {
		return isPhysical? new StellarMv(this, renderId) : new StellarMvLogical(this);
	}
	
	@Override
	public IStellarCatalog provideCatalog(StellarManager manager, StellarMvLogical data) {
		if(data instanceof StellarMv)
		{
			StellarMv mv = (StellarMv) data;
			mv.setManager(manager);
			return mv;
		}
		else {
			StellarMv mv = new StellarMv(manager, this, renderId);
			formatter.formatMv(data, mv);
			return mv;
		}
	}
	
	@Override
	public void load() {
		ConfigPropTypeRegistry.register("typeOrbit", new TypeOrbitPropHandler());
		ConfigPropTypeRegistry.register("typeCBody", new TypeCBodyPropHandler());
				
		renderId = StellarRenderingRegistry.nextRenderId();
		StellarRenderingRegistry.registerRenderer(renderId, new CBodyRenderer());
	}
	
	
	@Override
	public boolean isVariable() {
		return true;
	}

	@Override
	public boolean isPointy() {
		return false;
	}
	
	@Override
	public double getMag() {
		return -30.0;
	}

	@Override
	public double prioritySearch() {
		return 0.0;
	}

	@Override
	public double priorityRender() {
		return 0.0;
	}

	@Override
	public EnumCatalogType getType() {
		return EnumCatalogType.Moving;
	}

	@Override
	public String getCatalogName() {
		return "Moving";
	}
}
