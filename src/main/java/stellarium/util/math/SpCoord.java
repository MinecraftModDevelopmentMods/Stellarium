package stellarium.util.math;

//Right Ascension(RA) and Declination(Dec)
//-Azimuth and Height
public class SpCoord {
	//RA or -Azimuth
	public double x;
	//Dec or Height
	public double y;
	
	public SpCoord(double a, double b){
		x=a; y=b;
	}
}
