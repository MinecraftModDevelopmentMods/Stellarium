package stellarium.lighting;

import java.util.List;

public interface ILightingEntry {
	
	public boolean isVirtual();

	public boolean hasChildEntry();

	public List<ILightingEntry> getChildList();

	public ILightObject cbody();

	public double getInfluenceSize();
	
	
	public int hashCode();

}
