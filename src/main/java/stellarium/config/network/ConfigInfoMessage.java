package stellarium.config.network;

import java.util.UUID;

import stellarium.Stellarium;
import stellarium.config.core.ConfigDataPhysicalManager;
import stellarium.config.core.StellarConfiguration;
import net.minecraft.util.ChatComponentText;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;

public class ConfigInfoMessage extends ConfigEntryMessage implements IMessage {
	
	private String context;
	
	public ConfigInfoMessage() { }
	
	public ConfigInfoMessage(UUID parId, UUID id, NetworkCfgNode node) {
		super(parId, id);
		this.context = node.getContextString();
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		
		this.context = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
		ByteBufUtils.writeUTF8String(buf, this.context);
	}
	
	public static class ConfigInfoMessageHandler implements IMessageHandler<ConfigInfoMessage, IMessage> {

		@Override
		public IMessage onMessage(ConfigInfoMessage message, MessageContext ctx) {
			
			NetworkCfgOrganizer organizer = Stellarium.instance.getNetHandler().getClientOrganizer();
			organizer.receiveConfig(message.getParentID(), message.getID(), message.getTitle());
			organizer.receiveCfgInfo(message.getID(), message.context);
			
			NetworkCfgNode node = organizer.getNetNode(message.getID());
			if(node.hasLoadFailMessage())
				ctx.getClientHandler().getNetworkManager().closeChannel(new ChatComponentText(node.getLoadFailMessage()));
			
			return null;
		}
		
	}

}
