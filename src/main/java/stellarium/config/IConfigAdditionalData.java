package stellarium.config;

import io.netty.buffer.ByteBuf;

public interface IConfigAdditionalData {

	/**
	 * Reads data from byte buffer.
	 * @param buf the data buffer
	 * */
	public void fromBytes(ByteBuf buf);

	/**
	 * Writes data to byte buffer.
	 * @param buf the data buffer
	 * */
	public void toBytes(ByteBuf buf);

}
