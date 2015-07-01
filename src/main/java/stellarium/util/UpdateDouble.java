package stellarium.util;

public class UpdateDouble implements Cloneable {
	private double val0, vald;
	private double current;
	
	public UpdateDouble(double val0, double vald) {
		this.val0 = val0;
		this.vald = vald;
	}

	public void reset(double v0, double vd){
		this.val0 = v0;
		this.vald = vd;
	}

	public void update(double time) {
		this.current = this.val0 + this.vald * time;
	}
	
	public double getDifference() {
		return this.vald;
	}
	
	public double getValue() {
		return this.current;
	}

	public double getValue(double time) {
		return this.val0 + this.vald * time;
	}
	
	
	@Override
	public UpdateDouble clone() {
		return new UpdateDouble(val0, vald);
	}
}
