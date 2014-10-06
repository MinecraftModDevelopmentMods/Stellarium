package stellarium.util.math;

public class Vecf {
	
	//Coordinates
	public float x, y, z;
	
	//Creator
	public Vecf(float a, float b, float c){
		x=a; y=b; z=c;
	}
	public Vecf() {
		x=y=z=0;
	}
	
	//Size
	public float Size(){
		return (float) Math.sqrt(x*x+y*y+z*z);
	}
	
	//Add
	public static final Vecf Add(Vecf a,Vecf b){
		return new Vecf(a.x+b.x,a.y+b.y,a.z+b.z);
	}
	
	//Subtract
	public static final Vecf Sub(Vecf a,Vecf b){
		return new Vecf(a.x-b.x,a.y-b.y,a.z-b.z);
	}
	
	//Multiply
	public static final Vecf Mul(Vecf a, float m){
		return new Vecf(a.x*m,a.y*m,a.z*m);
	}
	
	//Divide
	public static final Vecf Div(Vecf a, float d){
		return new Vecf(a.x/d,a.y/d,a.z/d);
	}
	
	//Dot Product
	public static final float Dot(Vecf a, Vecf b){
		return a.x*b.x+a.y*b.y+a.z*b.z;
	}
	
	//Cross Product
	public static final Vecf Cross(Vecf a, Vecf b){
		return new Vecf(a.y*b.z-a.z*b.y,a.z*b.x-a.x*b.z,a.x*b.y-a.y*b.x);
	}
}
