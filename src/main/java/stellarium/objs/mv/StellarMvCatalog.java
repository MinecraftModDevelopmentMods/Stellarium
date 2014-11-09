package stellarium.objs.mv;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import stellarium.catalog.EnumCatalogType;
import stellarium.catalog.IStellarCatalog;
import stellarium.config.EnumCategoryType;
import stellarium.config.IStellarConfig;
import stellarium.objs.IStellarObj;
import stellarium.objs.mv.cbody.CBody;
import stellarium.objs.mv.cbody.CBodyRenderer;
import stellarium.render.StellarRenderingRegistry;
import stellarium.util.math.SpCoord;
import stellarium.view.ViewPoint;

public class StellarMvCatalog implements IStellarCatalog {
	public boolean isRemote;
	
	public int renderId;
	
	private Map<String, StellarMv> mvs = Maps.newHashMap();
	private StellarMv current;
	
	public StellarMvCatalog(boolean remote)
	{
		isRemote = remote;
	}
	
	public void addMv(String id, StellarMv mv)
	{
		mvs.put(id, mv);
	}
	
	public void setMv(String id)
	{
		current = mvs.get(id);
	}

	
	@Override
	public void formatConfig(IStellarConfig cfg) {
		cfg.setCategoryType(EnumCategoryType.ConfigList);
		cfg.setModifiable(true);
		
		
	}
	
	@Override
	public void load(IStellarConfig cfg) {
		loadFromConfig(cfg);
		
		renderId = StellarRenderingRegistry.nextRenderId();
		StellarRenderingRegistry.registerRenderer(renderId, new CBodyRenderer());
	}
	
	@Override
	public void applyConfig(IStellarConfig cfg) {
		loadFromConfig(cfg);
	}
	
	//Always called when loaded (either dynamically or statically)
	public void loadFromConfig(IStellarConfig cfg) {
		;
	}
	
	@Override
	public void update(int tick) {
		
		current.update(tick);
		
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
	public int getRUpTick() {
		return 1;
	}

	@Override
	public List<IStellarObj> getList(ViewPoint vp, SpCoord dir, double hfov) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getMag() {
		return -30.0;
	}

	@Override
	public double prioritySearch() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double priorityRender() {
		// TODO Auto-generated method stub
		return 0;
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
