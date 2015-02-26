package stellarium.config;

public interface ICfgMessage {
	
	/**
	 * gives Configuration Message String.
	 * */
	public String getMessage();
	
	/**
	 * gives Objects for formatting Message.
	 * */
	public Object[] getMsgObjects();

}
