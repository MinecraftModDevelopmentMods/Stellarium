package stellarium.network;

import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;

import com.google.common.collect.Maps;

import stellarium.Stellarium;
import stellarium.config.IConfigAdditionalData;
import stellarium.config.core.ConfigDataPhysicalManager;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;

public class InitialConnectionMessage implements IMessage {

	private Map<String, IConfigAdditionalData> dataMap;
	private int nodeCount;
	
	public InitialConnectionMessage() {
		this.dataMap = ConfigDataPhysicalManager.getManager(Side.CLIENT).getDefaultAdditionalDataMap();
	}
	
	public InitialConnectionMessage(int nodeCount, Map<String, IConfigAdditionalData> dataMap) {
		this.nodeCount = nodeCount;
		this.dataMap = dataMap;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		nodeCount = buf.readInt();
		while(buf.isReadable()) {
			String key = ByteBufUtils.readUTF8String(buf);
			dataMap.get(key).fromNBT(ByteBufUtils.readTag(buf));
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(nodeCount);
		for(String key : dataMap.keySet()) {
			ByteBufUtils.writeUTF8String(buf, key);
			
			NBTTagCompound comp = new NBTTagCompound();
			dataMap.get(key).toNBT(comp);
			ByteBufUtils.writeTag(buf, comp);
		}
	}
	
	public static class InitialConnectionHandler implements IMessageHandler<InitialConnectionMessage, InitialConnectionReply> {

		@Override
		public InitialConnectionReply onMessage(InitialConnectionMessage message,
				MessageContext ctx) {
	    	ConfigDataPhysicalManager.getManager(Side.CLIENT).onFormatClient(message.dataMap);
	    	Stellarium.instance.getNetHandler().onConnectedToServer(message.nodeCount);
	    	
			return new InitialConnectionReply();
		}
		
	}

}
