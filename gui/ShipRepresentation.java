package gui;

import java.awt.Color;

import shared.Const;

/* Minimal GUI representation for the ships */
public class ShipRepresentation extends RenderableEntity {

	private final Color m_color;
	
	
	public ShipRepresentation(final int ID, final Color col) {
		super(ID);
		// TODO: Should the server provide the color too to ensure consistency?
		m_color = col;
	}

	@Override
	public void render(java.awt.Graphics g) {
		Color originalColor = g.getColor();
		g.setColor(m_color);
		g.drawOval((int) getPosition().getX() - Const.SHIP_RADIUS, (int) getPosition().getY() - Const.SHIP_RADIUS, Const.SHIP_RADIUS * 2, Const.SHIP_RADIUS * 2);

		g.drawLine((int) getPosition().getX(), (int) getPosition().getY(), (int) (getPosition().getX() + getDirection().getX() * Const.SHIP_RADIUS), (int) (getPosition().getY() + getDirection().getY() * Const.SHIP_RADIUS));
		g.setColor(originalColor);
	}

}
