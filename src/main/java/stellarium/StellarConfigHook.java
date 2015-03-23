package stellarium;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import stellarium.settings.StellarSettings;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class StellarConfigHook {
	
	protected Configuration config;
	
	protected Property Mag_Limit, turb, Moon_Frac;
	
	public StellarConfigHook(Configuration config)
	{
		this.config = config;
	}
	
	public void onLoad()
	{
        Mag_Limit=config.get(Configuration.CATEGORY_GENERAL, "Mag_Limit", 5.0);
        Mag_Limit.comment="Limit of magnitude can be seen on naked eye.\n" +
        		"If you want to increase FPS, you can set this property a bit little (e.g. 0.3)\n" +
        		"and FPS will be exponentially improved";
       
        Mag_Limit.setMaxValue(7.0);
        Mag_Limit.setMinValue(1.0);
        
        StellarSettings.Mag_Limit=(float)Mag_Limit.getDouble(5.0);
        
        
        turb=config.get(Configuration.CATEGORY_GENERAL, "Twinkling(Turbulance)", 0.3);
        turb.comment="Degree of the twinkling effect of star.\n"
        		+ "It determines the turbulance of atmosphere, which actually cause the twinkling effect";
        
        turb.setMaxValue(3.0);
        turb.setMinValue(0.0);
        
        StellarSettings.Turb=(float)turb.getDouble(0.3);
        
        
        Moon_Frac=config.get(Configuration.CATEGORY_GENERAL, "Moon_Fragments_Number", 16);
        Moon_Frac.comment="Moon is drawn with fragments\n" +
        		"Less fragments will increase FPS, but the moon become more defective\n";
        
        Moon_Frac.setMaxValue(50);
        Moon_Frac.setMinValue(0);
                
        StellarSettings.ImgFrac=Moon_Frac.getInt(16);
	}
	
	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
	{
		if(event.modID == Stellarium.modid)
			onLoad();
	}

}
