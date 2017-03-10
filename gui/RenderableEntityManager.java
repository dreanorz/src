package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import shared.EntityMeta;


public class RenderableEntityManager {

	private static List<RenderableEntity> m_entities = Collections.synchronizedList(new LinkedList<RenderableEntity>());

	private static class SingletonHolder {
		public static final RenderableEntityManager instance = new RenderableEntityManager();
	}

	public static RenderableEntityManager getInstance() {
		return SingletonHolder.instance;
	}

	private RenderableEntityManager() {
	}

	public void addShip(final int ID, final Color col) {
		m_entities.add(new ShipRepresentation(ID, col));
	}

	public void addBall() {
		m_entities.add(new BallRepresentation());
	}

	/* Updates the gamestate to show the most reason one */
	public void updateState(EntityMeta state) {
		getEntity(state.m_entityID).updateEntity(state.m_position, state.m_direction);
	}
	
	public RenderableEntity getEntity(final int ID){
		for(ListIterator<RenderableEntity> itr = m_entities.listIterator(); itr.hasNext();){
			RenderableEntity nextEntity = itr.next();
			if(nextEntity.getID() == ID){
				return nextEntity;
			}
		}
		return null;
	}
	
	
	public void renderAll(Graphics g) {
		for (ListIterator<RenderableEntity> itr = m_entities.listIterator(0); itr.hasNext();) {
			itr.next().render(g);
		}
	}
	
	// This shouldn't be necessary here
//	public static LinkedList<RenderableEntity> getState() {
//		return m_entities;
//	}

}
