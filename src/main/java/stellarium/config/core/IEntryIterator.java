package stellarium.config.core;

import java.util.Iterator;

/**
 * Iterator for Category Entry.
 * NOTE: Any entry cannot be removed by this iterator.
 * */
public interface IEntryIterator<T> extends Iterator<T> {
	
	/**
     * Returns {@code true} if the backward iteration has more entries.
     * (In other words, returns {@code true} if {@link #previous} would
     * return an entry rather than throwing an exception.)
     *
     * @return {@code true} if the iteration has more entries
     */
    boolean hasPrevious();

    /**
     * Returns the previous entry in the iteration.
     *
     * @return the previous entry in the iteration
     * @throws NoSuchElementException if the iteration has no more entries
     */
    T previous();

}
