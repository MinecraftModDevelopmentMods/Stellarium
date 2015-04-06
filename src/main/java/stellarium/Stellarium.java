package stellarium;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import net.minecraftforge.common.MinecraftForge;
import net.minecraft.item.Item;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.common.*;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import stellarium.catalog.CCatalogCfgData;
import stellarium.catalog.StellarCatalogRegistry;
import stellarium.config.ConfigDataRegistry;
import stellarium.config.file.FileCfgManager;
import stellarium.config.gui.StellarConfigGuiProvider;
import stellarium.gui.config.DefCfgGuiProvider;
import stellarium.gui.config.StellarCfgGuiRegistry;
import stellarium.lang.CLangStrs;
import stellarium.lang.CPropLangStrs;
import stellarium.settings.StellarSettings;
import stellarium.stellars.orbit.*;
import stellarium.stellars.cbody.*;
import stellarium.world.StellarWorldProvider;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.*;
import cpw.mods.fml.relauncher.Side;

@Mod(modid=Stellarium.modid, name=Stellarium.name, version=Stellarium.version, dependencies="required-after:sciapi",
	guiFactory="stellarium.StellarGuiFactory")
public class Stellarium {
	
		public static final String modid = "stellarium";
		public static final String name = "Stellarium";
		public static final String version = "0.2.0";

        // The instance of Stellarium
        @Instance(Stellarium.modid)
        public static Stellarium instance;
        
//        public static ITickHandler tickhandler=new StellarTickHandler();
        private StellarSettings manager;
        
        @SidedProxy(clientSide="stellarium.ClientProxy", serverSide="stellarium.ServerProxy")
        public static BaseProxy proxy;
        
        //Default Configuration
        private StellarConfigHook cfghook;
        
        //Stellar Configuration
        private FileCfgManager filemanager;
        
        //Catalog Data
        private CCatalogCfgData catdata;
        
        
		public StellarConfigHook getCfgHook() {
			return cfghook;
		}
        
        @EventHandler
        public void preInit(FMLPreInitializationEvent event) throws IOException{
        	//Default Configurations
            cfghook = new StellarConfigHook(event.getSuggestedConfigurationFile());
            cfghook.onPreInit();         
            
            //File Side Configuration Manager
            filemanager = new FileCfgManager(new File(event.getModConfigurationDirectory(), "Stellarium"));
            
            //Loads Catalog
            StellarCatalogRegistry.onLoad();
            
            //creates Logical Catalog Cfg Data. For GUI & Text Config.            
            catdata = new CCatalogCfgData(false);
            ConfigDataRegistry.register(CPropLangStrs.catalog, catdata, catdata);
            
            
            //Setup Stellar Settings
            manager = new StellarSettings();
			StellarSettings.InitializeStars();
			
			//Proxy Settings
			proxy.initSided(manager);
			proxy.initCfgGui(filemanager);
			
			
			MinecraftForge.EVENT_BUS.register(new StellarEventHook());
        }
        
        @EventHandler
        public void load(FMLInitializationEvent event) {
        	ConfigDataRegistry.onFormat();
        	filemanager.onFormat();
        	filemanager.onApply();
        	
			StellarSettings.Initialize();
        }
        
        @EventHandler
        public void postInit(FMLPostInitializationEvent event) {
        	DimensionManager.unregisterDimension(0);
        	DimensionManager.unregisterProviderType(0);
        	DimensionManager.registerProviderType(0, StellarWorldProvider.class, true);
        	DimensionManager.registerDimension(0, 0);
        }
        
        @EventHandler
        public void onServerLoad(FMLServerStartingEvent event) {
        	StellarManager.resetManager(Side.SERVER);
        }
}