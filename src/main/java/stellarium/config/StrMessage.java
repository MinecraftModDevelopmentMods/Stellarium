package stellarium.config;

public class StrMessage implements ICfgMessage {
	
	private String str;
	private Object[] objects;
	
	public StrMessage(String str, Object... objects)
	{
		this.str = str;
		this.objects = objects;
	}

	@Override
	public String getMessage() {
		return str;
	}

	@Override
	public Object[] getMsgObjects() {
		return objects;
	}

}
