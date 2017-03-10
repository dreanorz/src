package gui;

import java.awt.Color;
import java.awt.Graphics;

import shared.Const;

/* Minimal GUI representation for the ball */
public class BallRepresentation extends RenderableEntity {
	private Color m_color = Const.BALL_COLOR;
	
	public BallRepresentation() {
		super(0);
	}
	
	@Override
	public void render(Graphics g) {
		g.setColor(m_color);
		g.drawOval((int) getPosition().getX() - Const.BALL_RADIUS, (int) getPosition().getY() - Const.BALL_RADIUS,
				Const.BALL_RADIUS * 2, Const.BALL_RADIUS * 2);
	}

}
