package engine;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import shared.Const;
import shared.Vector2D;

/* Holds all entities in the engine on the server side. */
public class EntityManager {
	private static List<GameEntity> m_entities =  Collections.synchronizedList(new LinkedList<GameEntity>());

	private static class SingletonHolder {
		public static final EntityManager instance = new EntityManager();
	}

	public static EntityManager getInstance() {
		return SingletonHolder.instance;
	}

	private EntityManager() {
	}
	
	public void addEntity(GameEntity ent){
		if(ent != null)
			m_entities.add(ent);
	}

	public void updatePositions() {
		for (ListIterator<GameEntity> itr = m_entities.listIterator(0); itr.hasNext();) {
			itr.next().move();
		}
	}

	public void checkBorderCollisions(int screenWidth, int screenHeight) {
		double newX = 0.0, newY = 0.0, radius = 0;
		boolean reset = false;
		for (ListIterator<GameEntity> itr = m_entities.listIterator(0); itr.hasNext();) {
			GameEntity e = itr.next();
			newX = e.getPosition().getX();
			newY = e.getPosition().getY();
			radius = e.getRadius();

			if (newX + radius > (screenWidth - Const.WINDOW_BORDER_WIDTH)) {
				newX = screenWidth - radius - Const.WINDOW_BORDER_WIDTH;
				e.deflectX();
				if (e.givesPoints()) {
					ScoreKeeper.getInstance().changeScores(1, 0);
					reset = true;
				}
			} else if ((newX - e.getRadius()) < Const.WINDOW_BORDER_WIDTH) {
				newX = radius + Const.WINDOW_BORDER_WIDTH;
				e.deflectX();
				if (e.givesPoints()) {
					ScoreKeeper.getInstance().changeScores(0, 1);
					reset = true;
				}
			}

			if (newY + radius > (screenHeight - Const.WINDOW_BOTTOM_HEIGHT)) {
				newY = screenHeight - radius - Const.WINDOW_BOTTOM_HEIGHT;
				e.deflectY();
			} else if (newY - radius < Const.WINDOW_TOP_HEIGHT) {
				newY = radius + Const.WINDOW_TOP_HEIGHT;
				e.deflectY();
			}

			e.setPosition(newX, newY);
		}

		if (reset) {
			resetPositions();
		}
	}

	public void checkShipCollisions() {
		Vector2D v; // Vector from center of one ship to the other

		for (ListIterator<GameEntity> itr = m_entities.listIterator(0); itr.hasNext();) {
			GameEntity s1 = itr.next();
			if (itr.hasNext()) {
				for (ListIterator<GameEntity> itr2 = m_entities.listIterator(itr.nextIndex()); itr2.hasNext();) {
					GameEntity s2 = itr2.next();
					v = s1.getPosition().minusOperator(s2.getPosition());
					double dist = v.length();

					if (v.length() < (s1.getRadius() + s2.getRadius())) { // Simple
																			// collision
																			// detection;
																			// just
																			// assume
																			// that
																			// ships
																			// will
																			// overlap
																			// during
																			// collision
						// Displace ships to avoid drawing overlap
						// Simplification: just displace both ships an equal
						// amount
						v.setLength((s1.getRadius() + s2.getRadius() - dist) / 2);
						s1.displace(v);
						v.invert();
						s2.displace(v);

						// Update movement vectors (assume perfect, rigid
						// collision with no momentum loss and equal masses)
						v.makeUnitVector(); // Normalize v
						// Compute momentum along v
						double comp1 = s1.getSpeed().dotProduct(v);
						double comp2 = s2.getSpeed().dotProduct(v);
						double m = comp1 - comp2; // 2(comp1-comp2) / mass1 +
													// mass2 = 2(comp1-comp2) /
													// 2 = comp1 - comp2
						v.setLength(m);
						s2.changeSpeed(v);
						v.invert();
						s1.changeSpeed(v);
					}
				}
			}
		}
	}

	private void resetPositions() {
		for (ListIterator<GameEntity> itr = m_entities.listIterator(0); itr.hasNext();) {
			itr.next().resetPosition();
		}
	}

	public static GameEntity getEntity(final int ID){
		for(GameEntity ent : m_entities){
			if(ent.getID() == ID) 
				return ent;
		}
		return null;
	}
	
	public static List<GameEntity> getEntites() {
		return m_entities;
	}

}