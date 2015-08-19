package stellarium.mech;

public class Wavelength {
	
	public static Wavelength visible;

	/**Wavelength, as nm*/
	public double wlen;
	
	/**Type of this wavelength*/
	public EnumWavelength type;
	
	public Wavelength(double wavelength, EnumWavelength type) {
		this.wlen = wavelength;
		this.type = type;
	}

	public double getWidth() {
		return type.getWidth();
	}
	
	
	@Override
	public int hashCode() {
		long bits = Double.doubleToLongBits(wlen);
        int bit = (int)(bits ^ (bits >>> 32));
		return bit ^ type.hashCode();
	}
			
}
