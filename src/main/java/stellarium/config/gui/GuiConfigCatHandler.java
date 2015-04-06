package stellarium.config.gui;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import com.google.common.collect.Lists;

import cpw.mods.fml.client.config.HoverChecker;
import stellarium.config.IConfigProperty;
import stellarium.config.IPropertyRelation;
import stellarium.config.core.PropertyList;
import stellarium.config.core.StellarConfigCategory;
import stellarium.config.core.StellarConfigCategory.PropertyRelation;
import stellarium.config.core.StellarConfigProperty;
import stellarium.config.core.handler.ICategoryHandler;
import stellarium.config.core.handler.IPropertyHandler;
import stellarium.config.gui.entry.IGuiCfgPropElement;
import stellarium.lang.CPropLangUtil;

public class GuiConfigCatHandler implements ICategoryHandler, Iterable<IGuiCfgPropElement> {
	
	private GuiConfigCatList cfgHandler;
	protected StellarConfigCategory category;
	private List<GuiPropertyRelation> proprels = Lists.newArrayList();
	
	public GuiConfigCatHandler(GuiConfigCatList cfgHandler, StellarConfigCategory category) {
		this.cfgHandler = cfgHandler;
		this.category = category;
	}
	

	public void onSetup(GuiConfigCatEntry catEntry) {
		for(StellarConfigProperty property : category.getPropList())
		{
			GuiPropertyHandler prophandler = this.getPropHandler(property);
			for(int i = 0; i < prophandler.getElementNum(); i++)
			{
				IGuiCfgPropElement element = prophandler.getElement(i);
				if(!element.isSubContent())
					element.setup(cfgHandler.guiConfig, catEntry, prophandler, catEntry.getLabelX(), catEntry.getContentX());
				else element.setup(cfgHandler.guiConfig, catEntry, prophandler, catEntry.getSubLabelX(), catEntry.getSubContentX());
			}
		}
	}

	@Override
	public IPropertyHandler getNewProp(StellarConfigProperty prop) {
		return new GuiPropertyHandler(prop);
	}

	@Override
	public void onRemoveProp(StellarConfigProperty prop) { }

	@Override
	public void onPropertyRelationAdded(PropertyRelation pr) {
		GuiPropertyRelation guiRel = new GuiPropertyRelation(pr);
		proprels.add(guiRel);
		
		for(IConfigProperty property : pr.relprops) {
			this.getPropHandler((StellarConfigProperty) property).addPropertyRelation(guiRel);
		}
	}

	@Override
	public void onPropertyRelationRemoved(PropertyRelation pr) {		
		Iterator<GuiPropertyRelation> ite = proprels.iterator();
		GuiPropertyRelation relation = null;
		
		while(ite.hasNext()) {
			relation = ite.next();
			if(relation.equals(pr.proprel))
			{
				ite.remove();
				break;
			}
		}
		
		if(relation == null)
			return;
		
		for(IConfigProperty property : pr.relprops) {
			this.getPropHandler((StellarConfigProperty) property).removePropertyRelation(relation);
		}
	}
	
	public class GuiPropertyRelation {

		private final IPropertyRelation proprel;
		private final int color;
		private HoverChecker tooltipChecker;
		
		public GuiPropertyRelation(PropertyRelation pr) {
			this.proprel = pr.proprel;
			
			int count[] = new int[ConfigGuiUtil.colors.length];
			int color = 0, minCount = 100;
			
			for(int i = 0; i < count.length; i++)
				count[i] = 0;
			
			for(GuiPropertyRelation relation : proprels)
				count[relation.getColorIndex()]++;
			
			for(int i = 0; i < count.length; i++)
			{
				if(count[i] < minCount)
				{
					minCount = count[i];
					color = i;
				}
			}
			
			this.color = color;
		}
		
		public int getColorIndex() {
			return color;
		}
		
		public int getColor() {
			return ConfigGuiUtil.colors[color];
		}
		
		public void setTooltipChecker(HoverChecker checker) {
			this.tooltipChecker = checker;
		}
		
		public HoverChecker getTooltipChecker() {
			return this.tooltipChecker;
		}

		public String getLocalizedTooltipString() {
			return CPropLangUtil.getLocalizedFromID(proprel.getRelationToolTip());
		}
		
		@Override
		public boolean equals(Object o) {
			if(o instanceof IPropertyRelation) {
				return o.equals(this.proprel);
			}
			if(o instanceof GuiPropertyRelation)
				return ((GuiPropertyRelation) o).proprel.equals(this.proprel);
			return false;
		}
	}
	

	@Override
	public Iterator<IGuiCfgPropElement> iterator() {
		return new PropElementIterator();
	}
	
	protected class PropElementIterator implements Iterator<IGuiCfgPropElement> {

		PropertyList.PropertyListEntry curEntry = category.getPropList().getFirstEntry();
		int nextSubIndex = 0;
		
		@Override
		public boolean hasNext() {
			if(curEntry == null)
				return false;
			if(nextSubIndex < getPropHandler(curEntry.getProperty()).getElementNum())
				return true;
			if(curEntry.hasNext())
				return true;
			
			return false;
		}

		@Override
		public IGuiCfgPropElement next() {
			if(!this.hasNext())
				throw new NoSuchElementException();
			
			if(nextSubIndex >= getPropHandler(curEntry.getProperty()).getElementNum())
			{
				curEntry = curEntry.getNext();
				this.nextSubIndex = 0;
			}
			
			return getPropHandler(curEntry.getProperty()).getElement(nextSubIndex++);
		}

		@Override
		public void remove() { }
	}
	
	public GuiPropertyHandler getPropHandler(StellarConfigProperty property) {
		return (GuiPropertyHandler) property.getHandler();
	}

	public IGuiCfgPropElement getElement(int index) {
		int elemNum;
		for(StellarConfigProperty property : category.getPropList()) {
			elemNum = this.getPropHandler(property).getElementNum();
			if(index < elemNum)
				return this.getPropHandler(property).getElement(index);
			index -= elemNum;
		}
		
		return null;
	}

	public int getElementSize() {
		int cnt = 0;
		
		for(StellarConfigProperty property : category.getPropList()) {
			cnt += this.getPropHandler(property).getElementNum();
		}
		
		return cnt;
	}

}
