package shared;

import java.io.Serializable;

public class EntityMeta implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public final int m_entityID;
	public final Vector2D m_position;
    public final Vector2D m_direction;	// Should always be unit vector; determines the object's facing

    public EntityMeta(final int id, final Vector2D position, final Vector2D direction){
    	m_entityID = id;
    	m_position = position;
    	m_direction = direction;
    }

}
