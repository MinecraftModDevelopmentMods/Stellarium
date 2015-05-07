package stellarium.world;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import stellarium.Stellarium;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiCreateFlatWorld;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.world.WorldType;

public class StellarWorldType extends WorldType {
	
	public static String NAME_STELLAR_TYPE = Stellarium.name;

	public StellarWorldType() {
		super(NAME_STELLAR_TYPE);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void onCustomizeButton(Minecraft instance, GuiCreateWorld guiCreateWorld)
    {
//        instance.displayGuiScreen(new GuiCreateFlatWorld(guiCreateWorld, guiCreateWorld.field_146334_a));
    }
	
	@Override
	public boolean isCustomizable()
    {
        return true;
    }

}
