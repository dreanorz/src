package gui;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import client.Client;
import shared.Const;

public class GameWindow extends Frame implements WindowListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// private Image background;
	private Image offScreenImage;
	private Graphics offScreenGraphicsCtx; // Used for double buffering

	// private final static int YOFFSET = 34;
	// private final static int XOFFSET = 4;

	public GameWindow() {
		addWindowListener(this);
		setSize(Const.DISPLAY_WIDTH, Const.DISPLAY_HEIGHT);
		setTitle(Const.APP_NAME);
		initializeEntities();
		setResizable(false);
		setVisible(true);
	}

	private void initializeEntities() {
		RenderableEntityManager entites = RenderableEntityManager.getInstance();
		// Sub-optimal to have to have this hard coded (especially the ID) and
		// this has to be the same on the server to get the correct
		// representation. This solution only allows four players but that is
		// ok.
		entites.addBall();
		entites.addShip(1, Color.RED);
		entites.addShip(2, Color.RED);
		entites.addShip(3, Color.GREEN);
		entites.addShip(4, Color.GREEN);
	}

	@Override
	public void update(Graphics g2) {
		Graphics2D g = (Graphics2D) g2;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		if (offScreenGraphicsCtx == null) {
			offScreenImage = createImage(getSize().width, getSize().height);
			offScreenGraphicsCtx = offScreenImage.getGraphics();
		}

		offScreenGraphicsCtx.setColor(Const.BG_COLOR);
		offScreenGraphicsCtx.fillRect(0, 0, getSize().width, getSize().height);
		RenderableEntityManager.getInstance().renderAll(offScreenGraphicsCtx);
		ScoreKeeperRepresentation.getInstance().render(offScreenGraphicsCtx);

		if (Const.SHOW_FPS) {
			offScreenGraphicsCtx.drawString("FPS: " + (int) Client.m_actualUpdateRate, 10, 50);
		}

		// Draw the scene onto the screen
		if (offScreenImage != null) {
			g.drawImage(offScreenImage, 0, 0, this);
		}

	}

	@Override
	public void paint(Graphics g) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		System.exit(0);
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}
}