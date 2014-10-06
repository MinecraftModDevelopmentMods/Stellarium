package stellarium.util.math;


//Rotation class
public class Rotate {
	
	byte sig;
	double c, s;
	
	//Rotate creator: b='X' or 'Y' or 'Z', angle: Rotation angle
	public Rotate(char b, double angle){
		c=Math.cos(angle);
		s=Math.sin(angle);
		sig=(byte)b;
	}
	
	//Rotate
	public Vec Rot(Vec vec){
		Vec p;
		switch(sig){
		case 'X':
			p=new Vec(vec.x,
				c*vec.y-s*vec.z,
				s*vec.y+c*vec.z);
			break;
		case 'Y':
			p=new Vec(
					s*vec.z+c*vec.x,
					vec.y,
					c*vec.z-s*vec.x);
			break;
		case 'Z':
			p=new Vec(
					c*vec.x-s*vec.y,
					s*vec.x+c*vec.y,
					vec.z);
			break;
		default:
			p=new Vec(0.0,0.0,0.0);
		}
		return p;
	}
}
