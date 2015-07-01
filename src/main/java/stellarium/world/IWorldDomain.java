package stellarium.world;

public interface IWorldDomain<LocalPos, GlobalPos> {
	
	/**
	 * Checks if this position is in this domain.
	 * @param pos the global expression of a position
	 * */
	public boolean isInDomain(GlobalPos pos);
	
	/**
	 * Converts the position from global form to local form. <p>
	 * will throw an exception when {@link #isInDomain(GlobalPos)} is false.
	 * @param pos the global expression of a position
	 * @return local expression of the position
	 * @throws java.lang.IllegalArgumentException
	 * */
	public LocalPos getLocalPos(GlobalPos pos);
	
	/**
	 * Converts the position from local form to global form.
	 * @param pos the local expression of a position
	 * @return global expression of the position
	 * */
	public GlobalPos getGlobalPos(LocalPos pos);

}
