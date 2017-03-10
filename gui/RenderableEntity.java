package gui;

import shared.Vector2D;

/* Class used for rendering stuff. Ship and ball are based on this. */
public abstract class RenderableEntity {
	private final int m_entityID;
	private final Vector2D m_position = new Vector2D();
	private final Vector2D m_direction = new Vector2D(); // Should always be
															// unit vector;
	// determines the object's facing

	public abstract void render(java.awt.Graphics g);

	public RenderableEntity(final int ID){
		m_entityID = ID;
	}
	
	public void updateEntity(final Vector2D newPos,final Vector2D newDir) {
		m_position.set(newPos.getX(), newPos.getY());
		m_direction.set(newDir.getX(), newDir.getY());
	}

	public Vector2D getDirection() {
		return m_direction;
	}

	public Vector2D getPosition() {
		return m_position;
	}
	
	public int getID(){
		return m_entityID;
	}
}
