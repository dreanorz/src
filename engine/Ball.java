package engine;

import shared.Const;
import shared.Vector2D;

/* Ball class on the engine side */
public class Ball extends GameEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Ball(final Vector2D position, final Vector2D speed) {
		super(0, position, speed, new Vector2D(0, 0), Const.BALL_MAX_ACCELERATION, Const.BALL_MAX_SPEED, Const.BALL_FRICTION);
	}

	@Override
	public double getRadius() {
		return Const.BALL_RADIUS;
	}

	@Override
	public boolean givesPoints() {
		return true;
	};
}