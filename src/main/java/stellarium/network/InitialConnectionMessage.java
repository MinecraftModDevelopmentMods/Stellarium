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
	
	public InitialConnectionMessage() {
		this.dataMap = ConfigDataPhysicalManager.getManager(Side.CLIENT).getCfgAdditionalDataMap();
	}
	
	public InitialConnectionMessage(Map<String, IConfigAdditionalData> dataMap) {
		this.dataMap = dataMap;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		while(buf.isReadable()) {
			String key = ByteBufUtils.readUTF8String(buf);
			dataMap.get(key).fromBytes(buf);
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		for(String key : dataMap.keySet()) {
			ByteBufUtils.writeUTF8String(buf, key);
			dataMap.get(key).toBytes(buf);
		}
	}
	
	public static class InitialConnectionHandler implements IMessageHandler<InitialConnectionMessage, InitialConnectionReply> {

		@Override
		public InitialConnectionReply onMessage(InitialConnectionMessage message,
				MessageContext ctx) {
	    	ConfigDataPhysicalManager.getManager(Side.CLIENT).onFormatClient(message.dataMap);
	    	
			return new InitialConnectionReply();
		}
		
	}

}
