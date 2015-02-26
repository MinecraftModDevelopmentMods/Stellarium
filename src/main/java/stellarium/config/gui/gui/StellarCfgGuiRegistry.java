package stellarium.config.gui.gui;

import java.util.List;

import net.minecraft.client.gui.GuiScreen;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class StellarCfgGuiRegistry {
	
	private static StellarCfgGuiRegistry instance = new StellarCfgGuiRegistry();
	private List<IGuiCfgMultipleProvider> providers = Lists.newArrayList();
	
	static {
		register(new IGuiCfgProvider() {

			@Override
			public GuiScreen getCfgGui(GuiScreen parScreen) {
				return new GuiStellarConfigMain(parScreen, ((GuiStellarConfigMain)parScreen).title + "!");
			}

			@Override
			public String getUnlocalizedName() {
				return "Haha!";
			}
			
		});
	}
	
	public static void register(IGuiCfgMultipleProvider prov)
	{
		instance.providers.add(prov);
	}
	
	public static void register(IGuiCfgProvider prov)
	{
		register(instance.new SingleGuiCfgProvider(prov));
	}
	
	public static ImmutableList<IGuiCfgMultipleProvider> getMProvList()
	{
		return ImmutableList.copyOf(instance.providers);
	}
	
	public static ImmutableList<IGuiCfgProvider> getProvList()
	{
		ImmutableList.Builder<IGuiCfgProvider> builder = ImmutableList.builder();
		
		for(IGuiCfgMultipleProvider mprov : getMProvList())
			builder.addAll(mprov.getProviders());
		
		return builder.build();
	}
	
	
	public class SingleGuiCfgProvider implements IGuiCfgMultipleProvider
	{
		
		private List<IGuiCfgProvider> provs;
		
		public SingleGuiCfgProvider(IGuiCfgProvider prov) {
			this.provs = Lists.newArrayList(prov);
		}

		@Override
		public List<IGuiCfgProvider> getProviders() {
			return provs;
		}
		
	}

}
