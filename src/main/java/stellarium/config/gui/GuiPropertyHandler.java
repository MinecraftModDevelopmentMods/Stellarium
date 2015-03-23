package stellarium.config.gui;

import java.util.List;

import com.google.common.collect.Lists;

import stellarium.config.core.StellarConfigProperty;
import stellarium.config.core.handler.IPropertyHandler;
import stellarium.config.element.IDoubleElement;
import stellarium.config.element.IEnumElement;
import stellarium.config.element.IIntegerElement;
import stellarium.config.element.IStringElement;
import stellarium.config.gui.entry.CombinedPropElement;
import stellarium.config.gui.entry.DoubleContent;
import stellarium.config.gui.entry.EnumContent;
import stellarium.config.gui.entry.HeaderContent;
import stellarium.config.gui.entry.IGuiCfgPropElement;
import stellarium.config.gui.entry.IntegerContent;
import stellarium.config.gui.entry.NullContent;
import stellarium.config.gui.entry.StringContent;
import stellarium.lang.CPropLangUtil;

public class GuiPropertyHandler implements IPropertyHandler {
	
	private StellarConfigProperty property;
	
	private List<IGuiCfgPropElement> elemlist = Lists.newArrayList();
	private List<String> namelist = Lists.newArrayList();
	private String expl = "";

	public GuiPropertyHandler(GuiConfigCatList cfgHandler,
			GuiConfigCatHandler guiCfgCatHandler, StellarConfigProperty prop) {
		// TODO Auto-generated constructor stub
		this.property = prop;
	}
	
	public StellarConfigProperty getProperty()
	{
		return this.property;
	}

	@Override
	public void setExpl(String expl) {
		this.expl = CPropLangUtil.getLocalizedString(expl);
	}
	
	public String getExpl() {
		return this.expl;
	}
	
	public int getElementNum() {
		return elemlist.size();
	}
	
	public IGuiCfgPropElement getElement(int index) {
		return elemlist.get(index);
	}

	@Override
	public void setEnabled(boolean enable) {
		for(IGuiCfgPropElement elem : elemlist)
			elem.setEnabled(enable);
	}

	@Override
	public void onValueUpdate() {
		for(int i = 0; i < elemlist.size(); i++)
		{
			if(!elemlist.get(i).hasNoContent())
				elemlist.get(i).onValueUpdate(property.getElement(namelist.get(i)));
		}
	}
	
	private void addElement(String subname, IGuiCfgPropElement element) {
		elemlist.add(element);
		namelist.add(subname);
	}
	
	private void onPreAdd() {
		if(!property.isSingular() && elemlist.isEmpty())
		{
			this.addElement("", new CombinedPropElement(property.getName(), new NullContent(), true));
		}
	}

	@Override
	public void onElementAdded(String subname, IDoubleElement element) {
		this.onPreAdd();
		
		this.addElement(subname, new CombinedPropElement(subname, new DoubleContent(element), property.isSingular()));
	}

	@Override
	public void onElementAdded(String subname, IEnumElement element) {
		this.onPreAdd();

		this.addElement(subname, new CombinedPropElement(subname, new EnumContent(element), property.isSingular()));
	}

	@Override
	public void onElementAdded(String subname, IIntegerElement element) {
		this.onPreAdd();

		this.addElement(subname, new CombinedPropElement(subname, new IntegerContent(element), property.isSingular()));
	}

	@Override
	public void onElementAdded(String subname, IStringElement element) {
		this.onPreAdd();

		this.addElement(subname, new CombinedPropElement(subname, new StringContent(element), property.isSingular()));
	}

}
