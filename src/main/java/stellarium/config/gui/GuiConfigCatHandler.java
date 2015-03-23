package stellarium.config.gui;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import stellarium.config.core.PropertyList;
import stellarium.config.core.StellarConfigCategory;
import stellarium.config.core.StellarConfigCategory.PropertyRelation;
import stellarium.config.core.StellarConfigProperty;
import stellarium.config.core.handler.ICategoryHandler;
import stellarium.config.core.handler.IPropertyHandler;
import stellarium.config.gui.entry.IGuiCfgPropElement;

public class GuiConfigCatHandler implements ICategoryHandler, Iterable<IGuiCfgPropElement> {
	
	private GuiConfigCatList cfgHandler;
	private StellarConfigCategory category;
	
	public GuiConfigCatHandler(GuiConfigCatList cfgHandler, StellarConfigCategory category) {
		this.cfgHandler = cfgHandler;
		this.category = category;
	}
	

	public void onSetup(GuiConfigCatEntry catEntry) {
		// TODO Auto-generated method stub
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
		return new GuiPropertyHandler(cfgHandler, this, prop);
	}

	@Override
	public void onRemoveProp(StellarConfigProperty prop) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPropertyRelationAdded(PropertyRelation pr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPropertyRelationRemoved(PropertyRelation pr) {
		// TODO Auto-generated method stub

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
