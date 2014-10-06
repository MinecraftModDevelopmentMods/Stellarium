package stellarium.util.math;

import java.nio.DoubleBuffer;


public class Coord {
	
	//Axis
	Vec i, j, k;
	
	//Creator
	public Coord(Vec vec, Vec vec2, Vec vec3) {
		i=vec; j=vec2; k=vec3;
	}
	

	
	public Vec Transform(Vec vec){
		return new Vec(Vec.Dot(vec,i),Vec.Dot(vec,j),Vec.Dot(vec,k));
	}
	
	public void Normalize(){
		i = Vec.Div(i, i.Size());
		j = Vec.Div(j, j.Size());
		k = Vec.Div(k, k.Size());
	}
	
	
	public Coord InverseCoord(){
		
		Coord Inv = new Coord(
				new Vec(mf(j.y, j.z, k.y, k.z), mf(i.z, i.y, k.z, k.y), mf(i.y, i.z, j.y, j.z)),
				new Vec(mf(j.z, j.x, k.z, k.x), mf(i.x, i.z, k.x, k.z), mf(i.z, i.x, j.z, j.x)),
				new Vec(mf(j.x, j.y, k.x, k.y), mf(i.y, i.x, k.y, k.x),mf(i.x, i.y, j.x, j.y)));
		
		Inv.Normalize();
		
		return Inv;
	}
	
	public DoubleBuffer ToDoubleBuffer(){
    	DoubleBuffer dbuf = DoubleBuffer.allocate(16);
    	dbuf.put(i.x).put(i.y).put(i.z).put(0.0)
    	.put(j.x).put(j.y).put(j.z).put(0.0)
    	.put(k.x).put(k.y).put(k.z).put(0.0)
    	.put(0.0).put(0.0).put(0.0).put(1.0);
    	return dbuf;
	}
	
	protected double mf(double a, double b, double c, double d){
		return a*d-b*c;
	}
}
