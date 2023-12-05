package online.money_daisuki.api.monkey.basegame.mayunused;

public final class LinearInterpolation2D {
	private final double translationSpeed;
	
	private double x;
	private double y;
	
	public LinearInterpolation2D(final double translationSpeed) {
		this(translationSpeed, 0, 0);
	}
	public LinearInterpolation2D(final double translationSpeed, final double initX, final double initY) {
		this.translationSpeed = translationSpeed;
		this.x = initX;
		this.y = initY;
	}
	public void translate(final double targetX, final double targetY, final double tps) {
		final double stepWidth = translationSpeed * tps;
		
		x = translate0(x, targetX, stepWidth);
		y = translate0(y, targetY, stepWidth);
	}
	private double translate0(final double start, final double target, final double width) {
		return(Math.fma(width, target, Math.fma(-width, start, start)));
	}
	public void setTranslation(final double x, final double y) {
		this.x = x;
		this.y = y;
	}
	
	public double getY() {
		return (y);
	}
	public double getX() {
		return (x);
	}
	public double getAngle() {
		return(Math.atan2(y, x));
	}
	public double getAngleX() {
		return(Math.acos(x));
	}
	public double getAngleY() {
		return(Math.asin(y));
	}
	public double getDistance() {
		return(Math.hypot(x, y));
	}
}
