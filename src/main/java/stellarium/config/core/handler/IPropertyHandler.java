package stellarium.config.core.handler;

import stellarium.config.element.IDoubleElement;
import stellarium.config.element.IEnumElement;
import stellarium.config.element.IIntegerElement;
import stellarium.config.element.IStringElement;

public interface IPropertyHandler {

	public void setExpl(String expl);

	public void setEnabled(boolean enable);

	public void onValueUpdate();
	
	public void onElementAdded(String subname, IDoubleElement element);
	public void onElementAdded(String subname, IEnumElement element);
	public void onElementAdded(String subname, IIntegerElement element);
	public void onElementAdded(String subname, IStringElement element);

}
