package online.money_daisuki.api.monkey.basegame.math;

import com.jme3.math.FastMath;

public final class FloatLinearInterpolation2D {
	private final float translationSpeed;
	
	private float x;
	private float y;
	
	public FloatLinearInterpolation2D(final float translationSpeed) {
		this(translationSpeed, 0, 0);
	}
	public FloatLinearInterpolation2D(final float translationSpeed, final float initX, final float initY) {
		this.translationSpeed = translationSpeed;
		this.x = initX;
		this.y = initY;
	}
	public void translate(final float targetX, final float targetY, final float tps) {
		final float stepWidth = translationSpeed * tps;
		
		x = translate0(x, targetX, stepWidth);
		y = translate0(y, targetY, stepWidth);
	}
	private float translate0(final float start, final float target, final float width) {
		return(Math.fma(width, target, Math.fma(-width, start, start)));
	}
	public void setTranslation(final float x, final float y) {
		this.x = x;
		this.y = y;
	}
	public float getX() {
		return (x);
	}
	public float getY() {
		return (y);
	}
	public float getAngle() {
		return(FastMath.atan2(y, x));
	}
	public float getAngleX() {
		return(FastMath.acos(x));
	}
	public float getAngleY() {
		return(FastMath.asin(y));
	}
	public float getDistance() {
		return((float)Math.hypot(x, y));
	}
}
