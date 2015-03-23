package stellarium.config.util;

import stellarium.config.core.ICategoryEntry;

public interface ICfgTreeWalker<T> {

	/**
	 * Called before {@link #onPreWalk} to get the custom representation for this entry.
	 * @param entry this entry
	 * @param parent the representation of parent entry, can be null.
	 * @return the representation of current entry, can be null.
	 * */
	public T getRepresentation(ICategoryEntry entry, T parent);
	
	/**
	 * Called before walking child entries.
	 * @param entry this entry
	 * @param current the representation of current entry, can be null.
	 * @return the walk state to pass or terminate the walking
	 * */
	public WalkState onPreWalk(ICategoryEntry entry, T current);
	
	/**
	 * Called after walking child entries.
	 * @param entry this entry
	 * @param current the representation of current entry, can be null.
	 * */
	public void onPostWalk(ICategoryEntry entry, T current);
	
	
	public enum WalkState {
		Normal, Pass, Terminate;
	}
}
