package stellarium.util;

import stellarium.util.math.*;

public class DVec {
	public Vec pre, post;
	
	public void Set(Vec v){
		pre=post;
		post=v;
	}
	
	public Vec Get(){
		return post;
	}
	
	public Vec Get(double part){
		return Vec.Add(Vec.Mul(pre, 1-part), Vec.Mul(post, part));
	}
}
