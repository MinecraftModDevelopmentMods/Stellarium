package stellarium.util.math;


public class Rotatef {
	
	byte sig;
	float c, s;
	
	//Rotatef creator: b='X' or 'Y' or 'Z', angle: Rotation angle
	public Rotatef(char b, float angle){
		c=Spmath.cosf(angle);
		s=Spmath.sinf(angle);
		sig=(byte)b;
	}
	
	//Rotatef
	public Vecf Rot(Vecf ecRPos){
		Vecf p;
		switch(sig){
		case 'X':
			p=new Vecf(ecRPos.x,
				c*ecRPos.y-s*ecRPos.z,
				s*ecRPos.y+c*ecRPos.z);
			break;
		case 'Y':
			p=new Vecf(
					s*ecRPos.z+c*ecRPos.x,
					ecRPos.y,
					c*ecRPos.z-s*ecRPos.x);
			break;
		case 'Z':
			p=new Vecf(
					c*ecRPos.x-s*ecRPos.y,
					s*ecRPos.x+c*ecRPos.y,
					ecRPos.z);
			break;
		default:
			p=new Vecf(0.0f,0.0f,0.0f);
		}
		return p;
	}
}

