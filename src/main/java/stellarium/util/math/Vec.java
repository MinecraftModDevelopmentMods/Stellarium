package stellarium.util.math;

public class Vec {
	
	//Coordinates
	public double x, y, z;
	
	//Creator
	public Vec(double a, double b, double c){
		x=a; y=b; z=c;
	}
	public Vec() {
		x=y=z=0;
	}
	
	//Size
	public double Size(){
		return Math.sqrt(x*x+y*y+z*z);
	}
	
	public double Size2(){
		return x*x+y*y+z*z;
	}
	
	//Add
	public static final Vec Add(Vec a,Vec b){
		return new Vec(a.x+b.x,a.y+b.y,a.z+b.z);
	}
	
	//Subtract
	public static final Vec Sub(Vec a,Vec b){
		return new Vec(a.x-b.x,a.y-b.y,a.z-b.z);
	}
	
	//Multiply
	public static final Vec Mul(Vec a, double m){
		return new Vec(a.x*m,a.y*m,a.z*m);
	}
	
	//Divide
	public static final Vec Div(Vec a, double d){
		return new Vec(a.x/d,a.y/d,a.z/d);
	}
	
	//Dot Product
	public static final double Dot(Vec a, Vec b){
		return a.x*b.x+a.y*b.y+a.z*b.z;
	}
	
	//Cross Product
	public static final Vec Cross(Vec a, Vec b){
		return new Vec(a.y*b.z-a.z*b.y,a.z*b.x-a.x*b.z,a.x*b.y-a.y*b.x);
	}
}
