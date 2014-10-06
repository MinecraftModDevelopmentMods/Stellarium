package stellarium.stellars.local;

import stellarium.util.*;
import stellarium.util.math.*;

public class LocalCValue {
	public Vec Pos;
	public double Dist;
	
	public DVec Pol;
	public DVec PrMer;
	public DVec East;
	public double Size;
	
	public void Get(double part, LocalCValue get){
//		get.Pos.Set(Pos.Get(part));
//		get.Dist.Set(Dist.Get(part));
		get.Pos = this.Pos;
		get.Dist = this.Dist;
		
		get.Pol.Set(Pol.Get(part));
		get.PrMer.Set(PrMer.Get(part));
		get.East.Set(East.Get(part));
//		get.Size.Set(Size.Get(part));
		get.Size=this.Size;
	}
}