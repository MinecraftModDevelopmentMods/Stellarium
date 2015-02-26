package stellarium.config.gui;

import java.util.List;

import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.renderer.Tessellator;

import com.google.common.collect.Lists;

import stellarium.config.ConfigPropTypeRegistry;
import stellarium.config.IConfigPropHandler;
import stellarium.config.IConfigProperty;
import stellarium.config.IMConfigProperty;
import stellarium.config.core.StellarConfigCategory;
import stellarium.config.core.StellarConfigProperty;
import stellarium.config.element.*;

public class GuiCfgPropHandler<T> extends StellarConfigProperty<T> implements IMConfigProperty<T>, ICfgChangeNotifier {

	protected T beforeval;
	protected String expl;
			
	public GuiCfgPropHandler(GuiCfgCatHandler cat, String ptype, String pname, T def)
	{
		super(cat, ptype, pname, def);
		beforeval = def;
	}
	
	@Override
	public IConfigProperty<T> setExpl(String expl) {
		this.expl = expl;
		return this;
	}

	@Override
	public void setEnabled(boolean enable) {
		super.setEnabled(enable);
		// TODO GUI setEnabled.
		listener.onCfgChange(EnumCfgChangeType.AccessModified, this);
	}

	@Override
	public void setVal(T val) {
		beforeval = this.getVal();
		super.setVal(val);
		// TODO GUI setVal.
		listener.onCfgChange(EnumCfgChangeType.ValueModified, this);
	}
	
	
	public T getBeforeValue() {
		return beforeval;
	}
	
	
	@Override
	protected IPropElement newElement(String subname, EnumPropElement e) {
		switch(e)
		{
		case Double:
			return new GuiDoubleElement(subname);
	
		case Integer:
			return new GuiIntElement(subname);

		case String:
			return new GuiStringElement(subname);

		case Enum:
			return new GuiEnumElement(subname);
			
		default:
			return null;
		}
	}
	
	public IGuiCfgElement getGuiElement(String subname) {
		for(int i = 0; i < namelist.size(); i++)
		{
			if(namelist.get(i).equals(subname))
				return (IGuiCfgElement) ellist.get(i);
		}
		
		return null;
	}
	
	
	public interface IGuiCfgElement extends GuiListExtended.IGuiListEntry {
		
		public void setup(GuiStellarConfig cfg);
		
		public int getWidth();
		
		public void setEnabled(boolean enable);
		
        public void updateCursorCounter();
        
        public void drawToolTip(int mouseX, int mouseY);
		
		public void mouseClicked(int x, int y, int mouseevent);
        public void keyTyped(char eventChar, int eventKey);
		
	}
	
	public class GuiDoubleElement implements IGuiCfgElement, IDoubleElement {

		public GuiDoubleElement(String subname) {
			// TODO Auto-generated constructor stub
		}

		@Override
		public void setValue(double val) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public double getValue() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void drawEntry(int p_148279_1_, int p_148279_2_,
				int p_148279_3_, int p_148279_4_, int p_148279_5_,
				Tessellator p_148279_6_, int p_148279_7_, int p_148279_8_,
				boolean p_148279_9_) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean mousePressed(int p_148278_1_, int p_148278_2_,
				int p_148278_3_, int p_148278_4_, int p_148278_5_,
				int p_148278_6_) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void mouseReleased(int p_148277_1_, int p_148277_2_,
				int p_148277_3_, int p_148277_4_, int p_148277_5_,
				int p_148277_6_) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setup(GuiStellarConfig cfg) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public int getWidth() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void setEnabled(boolean enable) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void updateCursorCounter() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void drawToolTip(int mouseX, int mouseY) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseClicked(int x, int y, int mouseevent) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(char eventChar, int eventKey) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	public class GuiIntElement implements IGuiCfgElement, IIntegerElement {

		public GuiIntElement(String subname) {
			// TODO Auto-generated constructor stub
		}

		@Override
		public void setValue(int val) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public int getValue() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void drawEntry(int p_148279_1_, int p_148279_2_,
				int p_148279_3_, int p_148279_4_, int p_148279_5_,
				Tessellator p_148279_6_, int p_148279_7_, int p_148279_8_,
				boolean p_148279_9_) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean mousePressed(int p_148278_1_, int p_148278_2_,
				int p_148278_3_, int p_148278_4_, int p_148278_5_,
				int p_148278_6_) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void mouseReleased(int p_148277_1_, int p_148277_2_,
				int p_148277_3_, int p_148277_4_, int p_148277_5_,
				int p_148277_6_) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setup(GuiStellarConfig cfg) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public int getWidth() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void setEnabled(boolean enable) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void updateCursorCounter() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void drawToolTip(int mouseX, int mouseY) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseClicked(int x, int y, int mouseevent) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(char eventChar, int eventKey) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	public class GuiStringElement implements IGuiCfgElement, IStringElement {

		public GuiStringElement(String subname) {
			// TODO Auto-generated constructor stub
		}

		@Override
		public void setValue(String val) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public String getValue() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void drawEntry(int p_148279_1_, int p_148279_2_,
				int p_148279_3_, int p_148279_4_, int p_148279_5_,
				Tessellator p_148279_6_, int p_148279_7_, int p_148279_8_,
				boolean p_148279_9_) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean mousePressed(int p_148278_1_, int p_148278_2_,
				int p_148278_3_, int p_148278_4_, int p_148278_5_,
				int p_148278_6_) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void mouseReleased(int p_148277_1_, int p_148277_2_,
				int p_148277_3_, int p_148277_4_, int p_148277_5_,
				int p_148277_6_) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setup(GuiStellarConfig cfg) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public int getWidth() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void setEnabled(boolean enable) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void updateCursorCounter() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void drawToolTip(int mouseX, int mouseY) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseClicked(int x, int y, int mouseevent) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(char eventChar, int eventKey) {
			// TODO Auto-generated method stub
			
		}

	}
	
	public class GuiEnumElement implements IGuiCfgElement, IEnumElement {

		public GuiEnumElement(String subname) {
			// TODO Auto-generated constructor stub
		}

		@Override
		public void setValRange(String... str) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setValue(int index) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setValue(String val) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public String getValue() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getIndex() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void drawEntry(int p_148279_1_, int p_148279_2_,
				int p_148279_3_, int p_148279_4_, int p_148279_5_,
				Tessellator p_148279_6_, int p_148279_7_, int p_148279_8_,
				boolean p_148279_9_) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean mousePressed(int p_148278_1_, int p_148278_2_,
				int p_148278_3_, int p_148278_4_, int p_148278_5_,
				int p_148278_6_) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void mouseReleased(int p_148277_1_, int p_148277_2_,
				int p_148277_3_, int p_148277_4_, int p_148277_5_,
				int p_148277_6_) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setup(GuiStellarConfig cfg) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public int getWidth() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void setEnabled(boolean enable) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void updateCursorCounter() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void drawToolTip(int mouseX, int mouseY) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseClicked(int x, int y, int mouseevent) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(char eventChar, int eventKey) {
			// TODO Auto-generated method stub
			
		}

	}


	private ICfgChangeListener listener = null;
	
	@Override
	public void setListener(ICfgChangeListener listener) {
		this.listener = listener;
	}

}
