package stellarium.config.gui;

import java.util.List;

import com.google.common.collect.Lists;

import stellarium.config.EnumCategoryType;
import stellarium.config.ICfgArrMListener;
import stellarium.config.IConfigCategory;
import stellarium.config.IStellarConfig;

public class GuiConfigHandler implements IStellarConfig {
	
	protected List<ICfgArrMListener> listenList = Lists.newArrayList();
	protected EnumCategoryType cattype = EnumCategoryType.List;
	protected boolean modifiable = false;
	protected boolean warn = false;

	@Override
	public void setCategoryType(EnumCategoryType t) {
		cattype = t;
		
	}

	@Override
	public void setModifiable(boolean modif, boolean warn) {
		this.modifiable = modif;
		this.warn = warn;
	}

	@Override
	public void addAMListener(ICfgArrMListener list) {
		listenList.add(list);
	}

	@Override
	public void markImmutable(IConfigCategory cat) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isImmutable(IConfigCategory cat) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IConfigCategory addCategory(String cid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeCategory(String cid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IConfigCategory getCategory(String cid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IConfigCategory> getAllCategories() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IStellarConfig getSubConfig(IConfigCategory cat) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IConfigCategory addSubCategory(IConfigCategory parent, String subid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeSubCategory(IConfigCategory parent, String subid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IConfigCategory getSubCategory(IConfigCategory parent, String subid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IConfigCategory> getAllSubCategories(IConfigCategory parent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void loadCategories() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addLoadFailMessage(String title, String msg) {
		// TODO Auto-generated method stub
		
	}

}
