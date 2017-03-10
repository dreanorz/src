package engine;

import java.awt.Color;

import shared.Const;
import shared.Vector2D;

final class Client {
	// Temporary to remove compiler errors
}

public class Ship extends GameEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int rotation = 0; // Set to 1 when rotating clockwise, -1 when
								// rotating counterclockwise
	private boolean braking = false;

	public Ship(final int ID, final Vector2D position, final Vector2D speed, final Vector2D direction, final Color col) {
		super(ID, position, speed, direction, Const.SHIP_MAX_ACCELERATION, Const.SHIP_MAX_SPEED, Const.SHIP_FRICTION);
	}

	public void updateInput(final boolean rightKey, final boolean leftKey, final boolean accelerateKey, final boolean brakeKey) {
		if (rightKey) {
			rotation = 1;
		} else if (leftKey) {
			rotation = -1;
		} else {
			rotation = 0;
		}
		setAcceleration(accelerateKey ? Const.SHIP_MAX_ACCELERATION : 0);
		braking = brakeKey;
	}

	@Override
	public void move() {
		if (rotation != 0) {
			rotate(rotation * Const.SHIP_ROTATION);
			scaleSpeed(Const.SHIP_TURN_BRAKE_SCALE);
		}
		if (braking) {
			scaleSpeed(Const.SHIP_BRAKE_SCALE);
			setAcceleration(0);
		}
		super.move();
	}

	@Override
	public boolean givesPoints() {
		return false;
	}

	@Override
	public double getRadius() {
		return Const.SHIP_RADIUS;
	}
}