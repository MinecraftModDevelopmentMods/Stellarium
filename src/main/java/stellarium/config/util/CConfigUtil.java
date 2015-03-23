package stellarium.config.util;

import stellarium.config.IStellarConfig;
import stellarium.config.core.ICategoryEntry;
import stellarium.config.util.ICfgTreeWalker.WalkState;

public class CConfigUtil {

	/**
	 * Gives Configuration Category Iteration Wrapper.
	 * This excludes the root entry.
	 * @param cfg the configuration to iterate
	 * */
	public static CfgIteWrapper getCfgIteWrapper(IStellarConfig cfg)
	{
		return new CfgIteWrapper(cfg);
	}
	
	/**
	 * Gives Configuration Category Iteration Wrapper.
	 * This excludes the root entry.
	 * @param rentry the root entry to iterate
	 * */
	public static CfgIteWrapper getCfgIteWrapper(ICategoryEntry rentry)
	{
		return new CfgIteWrapper(rentry);
	}
	
	/**
	 * Walks configuration tree.
	 * @param cfg the configuration to walk
	 * @param walker the tree walker
	 * */
	public static <T> void walkConfigTree(IStellarConfig cfg, ICfgTreeWalker<T> walker)
	{
		walkConfigTree(cfg.getRootEntry(), walker, true);
	}
	
	/**
	 * Walks configuration tree from the root entry.
	 * @param rentry the root entry to start walking
	 * @param walker the tree walker
	 * @param excludeRootEntry flag for excluding root entry
	 * */
	public static <T> void walkConfigTree(ICategoryEntry rentry, ICfgTreeWalker<T> walker, boolean excludeRootEntry)
	{
		if(!excludeRootEntry)
			walkTreeInternal(rentry, null, walker);
		else {
			if(rentry.hasChildEntry())
				for(ICategoryEntry child : rentry)
					walkTreeInternal(child, null, walker);
		}
	}
	
	public static <T> WalkState walkTreeInternal(ICategoryEntry current, T parrep, ICfgTreeWalker<T> walker)
	{
		T currep = walker.getRepresentation(current, parrep);
		
		WalkState state = walker.onPreWalk(current, currep);
		
		if(state != WalkState.Normal)
			return state;
		
		if(current.hasChildEntry())
			for(ICategoryEntry child : current)
			{
				if(walkTreeInternal(child, currep, walker) == WalkState.Terminate)
					return WalkState.Terminate;
			}
		
		walker.onPostWalk(current, currep);
		
		return state;
	}
}
