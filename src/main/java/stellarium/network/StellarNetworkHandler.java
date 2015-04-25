package stellarium.network;

import java.util.EnumMap;

import com.google.common.collect.Maps;

import stellarium.config.core.ConfigDataPhysicalManager;
import stellarium.config.network.ConfigEntryMessage;
import stellarium.config.network.ConfigInfoMessage;
import stellarium.config.network.NetworkCfgOrganizer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class StellarNetworkHandler {
	
	public SimpleNetworkWrapper network;
	private int index = 0;
	
	private EnumMap<Side, NetworkCfgOrganizer> organizer = Maps.newEnumMap(Side.class);
	
	public StellarNetworkHandler() {
		network = NetworkRegistry.INSTANCE.newSimpleChannel("Stellarium");
		this.onRegisterMessage();
	}
	
	public void onRegisterMessage() {
		this.registerMessage(ConfigInfoMessage.ConfigInfoMessageHandler.class,
				ConfigInfoMessage.class, Side.CLIENT);
		this.registerMessage(ConfigEntryMessage.ConfigEntryMsgHandler.class,
				ConfigEntryMessage.class, Side.CLIENT);
		this.registerMessage(InitialConnectionMessage.InitialConnectionHandler.class,
				InitialConnectionMessage.class, Side.CLIENT);
		this.registerMessage(InitialConnectionReply.InitialConnectionReplyHandler.class,
				InitialConnectionReply.class, Side.SERVER);
	}
	
    public <REQ extends IMessage, REPLY extends IMessage> void registerMessage(Class<? extends IMessageHandler<REQ, REPLY>> messageHandler, Class<REQ> requestMessageType, Side side) {
    	network.registerMessage(messageHandler, requestMessageType, index++, side);
    }
    
    
    public NetworkCfgOrganizer getClientOrganizer() {
		return organizer.get(Side.CLIENT);
	}
    
    public void finishClientOrganization() {
		organizer.get(Side.CLIENT).onSetupClient();
	}
    
    public void finishServerOrganization() {
    	organizer.get(Side.SERVER).onSetupServer();
    }
	
	public void refreshOrganizer(Side side) {
		organizer.put(side, new NetworkCfgOrganizer(this.network));
	}
	
	
    @SubscribeEvent
	public void onServerConnectionFromClient(FMLNetworkEvent.ServerConnectionFromClientEvent event) {
		if(event.handler instanceof NetHandlerPlayServer) {
			NetHandlerPlayServer handler = (NetHandlerPlayServer) event.handler;
			network.sendTo(new InitialConnectionMessage(ConfigDataPhysicalManager.getManager(Side.SERVER).getAdditionalDataMap()),
					handler.playerEntity);
		}
	}
    
	public void onConnectionReply(EntityPlayerMP player) {
		organizer.get(Side.SERVER).sendConfigTo(player);
	}
	
}
