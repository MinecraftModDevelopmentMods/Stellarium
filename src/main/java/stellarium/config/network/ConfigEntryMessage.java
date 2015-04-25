package stellarium.config.network;

import java.util.UUID;

import stellarium.Stellarium;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class ConfigEntryMessage implements IMessage {
	
	private UUID uuid;
	private UUID parId;
	private String title;

	public ConfigEntryMessage() { }
	
	public ConfigEntryMessage(UUID parId, UUID id) {
		this.parId = parId;
		this.uuid = id;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		long lsb = buf.readLong();
		long msb = buf.readLong();
		uuid = new UUID(msb, lsb);
		
		lsb = buf.readLong();
		msb = buf.readLong();
		parId = new UUID(msb, lsb);
		
		title = ByteBufUtils.readUTF8String(buf);
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(uuid.getLeastSignificantBits());
		buf.writeLong(uuid.getMostSignificantBits());
		
		buf.writeLong(parId.getLeastSignificantBits());
		buf.writeLong(parId.getMostSignificantBits());
		
		ByteBufUtils.writeUTF8String(buf, title);
	}
	
	public UUID getParentID() {
		return parId;
	}
	
	public UUID getID() {
		return uuid;
	}

	public String getTitle() {
		return title;
	}
	
	
	public static class ConfigEntryMsgHandler implements IMessageHandler<ConfigEntryMessage, IMessage> {

		@Override
		public IMessage onMessage(ConfigEntryMessage message, MessageContext ctx) {
			NetworkCfgOrganizer organizer = Stellarium.instance.getNetHandler().getClientOrganizer();
			organizer.receiveConfig(message.getParentID(), message.getID(), message.title);
			return null;
		}
		
	}

}
