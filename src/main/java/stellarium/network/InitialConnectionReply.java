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

public class InitialConnectionReply implements IMessage {
	
	public InitialConnectionReply() { }
		
	@Override
	public void fromBytes(ByteBuf buf) { }

	@Override
	public void toBytes(ByteBuf buf) { }
	
	public static class InitialConnectionReplyHandler implements IMessageHandler<InitialConnectionReply, IMessage> {
		
		@Override
		public IMessage onMessage(InitialConnectionReply message,
				MessageContext ctx) {
			Stellarium.instance.getNetHandler().onConnectionReply(ctx.getServerHandler().playerEntity);
	    	
			return null;
		}
		
	}

}
