package stellarium.config.gui.entry;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.Tessellator;
import stellarium.config.core.StellarConfigProperty;
import stellarium.config.element.IIntegerElement;
import stellarium.config.element.IPropElement;
import stellarium.config.gui.ConfigGuiUtil;
import stellarium.config.gui.GuiConfigCatEntry;
import stellarium.config.gui.GuiPropertyHandler;
import stellarium.config.gui.GuiStellarConfig;

public class IntegerContent implements IGuiCfgPropContent {

	private final IIntegerElement theElement;
    private final StellarConfigProperty property;
    
    private GuiTextField textFieldValue;
    private boolean isEnabled;
    
    private int scrWidth;
	
	public IntegerContent(StellarConfigProperty property, IIntegerElement element) {
		this.theElement = element;
		this.property = property;
		this.isEnabled = property.isEnabled();
	}

	@Override
	public void setup(GuiStellarConfig screen, GuiConfigCatEntry list,
			GuiPropertyHandler property, int xContent) {
		this.scrWidth = screen.width;
		
		this.textFieldValue = new GuiTextField(screen.getFontRenderer(), xContent, 0, this.getContentWidth(), ConfigGuiUtil.VER_SIZE);
		textFieldValue.setText(String.valueOf(theElement.getValue()));
	}

	@Override
	public int getContentWidth() {
		return ConfigGuiUtil.getContentWidth(scrWidth);
	}

	@Override
	public boolean isNull() {
		return false;
	}

	@Override
	public void setEnabled(boolean enable) {
		isEnabled = enable;
	}

	@Override
	public void onValueUpdate(IPropElement element) {
		textFieldValue.setText(String.valueOf(theElement.getValue()));
	}

	@Override
	public void updateCursorCounter() {
		textFieldValue.updateCursorCounter();
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseEvent) {
		textFieldValue.mouseClicked(mouseX, mouseY, mouseEvent);
	}

	@Override
	public void keyTyped(char eventChar, int eventKey) {
        if (this.isEnabled || eventKey == Keyboard.KEY_LEFT || eventKey == Keyboard.KEY_RIGHT || eventKey == Keyboard.KEY_HOME || eventKey == Keyboard.KEY_END)
        {
            String validChars = "0123456789";
            String before = this.textFieldValue.getText();
            if (validChars.contains(String.valueOf(eventChar))
                    || (!before.startsWith("-") && this.textFieldValue.getCursorPosition() == 0 && eventChar == '-')
                    || eventKey == Keyboard.KEY_BACK || eventKey == Keyboard.KEY_DELETE
                    || eventKey == Keyboard.KEY_LEFT || eventKey == Keyboard.KEY_RIGHT || eventKey == Keyboard.KEY_HOME || eventKey == Keyboard.KEY_END) {
                this.textFieldValue.textboxKeyTyped((this.isEnabled ? eventChar : Keyboard.CHAR_NONE), eventKey);
            
                if(!textFieldValue.getText().equals("")) 
                {
                	theElement.setValue(Integer.valueOf(textFieldValue.getText()));
                	property.updateValue();
                }
            }
        }
	}

	@Override
	public boolean mousePressed(int index, int mouseX, int mouseY,
			int mouseEvent, int relativeX, int relativeY) {
		return false;
	}

	@Override
	public void mouseReleased(int index, int mouseX, int mouseY,
			int mouseEvent, int relativeX, int relativeY) { }

	@Override
	public void drawEntry(int slotIndex, int x, int y, int listWidth,
			int slotHeight, Tessellator tessellator, int mouseX, int mouseY,
			boolean isSelected) {
		textFieldValue.setEnabled(isEnabled);
		textFieldValue.yPosition = y;
		textFieldValue.drawTextBox();
	}

	@Override
	public void onGuiClosed() { }

	@Override
	public void drawToolTip(int mouseX, int mouseY, float partialTicks) { }

}
