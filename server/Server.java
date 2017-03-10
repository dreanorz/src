package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import engine.EntityManager;
import engine.GameEntity;
import engine.ScoreKeeper;
import engine.Ship;
import engine.World;
import shared.Const;
import shared.EntityMeta;
import shared.InputInfo;
import shared.StateUpdate;

public class Server {
	private List<ClientThread> m_connectedClients = new ArrayList<ClientThread>();
	private double m_lastTime = System.currentTimeMillis();
	private ServerSocket m_serverSocket;

	@SuppressWarnings("unused") // This is unused for now
	private double m_actualUpdateRate = 0.0;

	public static void main(String[] args) {
		int port = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter server port:", "Port?", JOptionPane.QUESTION_MESSAGE));

		// Setup port and socket
		Server instance = new Server();
		if (instance.waitForClients(port))
			instance.start();
		JOptionPane.showMessageDialog(null, "Main thread exiting.", "Bye main.", JOptionPane.INFORMATION_MESSAGE);
	}

	public Server() {

	}

	public boolean waitForClients(final int port) {
		try {
			m_serverSocket = new ServerSocket(port);
			// Wait for the right amount of clients to connect
			int currentID = 1;
			for (int i = 0; i < Const.NEEDEDPLAYERS; i++) {
				// Create a new client with its own socket
				System.out.println("Waiting for client: " + currentID);
				ClientThread client = new ClientThread(currentID++, m_serverSocket.accept());
				// Add the client if the handshake is successful
				m_connectedClients.add(client);
			}
			System.out.println("All clients connected.");
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return true;
	}

	public void start() {
		System.out.println("Starting simulation");
		World.getInstance().start();
		System.out.println("Starting clients");
		for (ClientThread c : m_connectedClients) {
			c.handshake();
			c.start();
		}
		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// TODO: Keep state up to date and sync clients?
		// Best effort? Just send recent states to clients all the time. Threads
		// should keep clients input up to date anyway?
		while (true) {
			if (newUpdate()) {
				// System.out.println("Sending update to clients");
				sendStates();
			}
			try {
				Thread.sleep((long) (Const.SERVER_INCREMENT / 2));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void sendStates() {
		StateUpdate state = getState();
		for (ClientThread c : m_connectedClients) {
			c.sendState(state);
		}
	}
	/* Function to get the latest game state from the engine */
	public StateUpdate getState() {
		List<EntityMeta> entityInfo = new ArrayList<EntityMeta>();
		int[] currentScores;
		for (GameEntity ent : EntityManager.getEntites()) {
			entityInfo.add(ent.getMeta());
		}
		currentScores = ScoreKeeper.getScores();
		return new StateUpdate(currentScores, entityInfo);
	}

	/**
	 * Check if enough time has passed to send state to clients
	 * 
	 * @return true if a new state update is to be sent to the clients
	 */
	private boolean newUpdate() {
		double currentTime = System.currentTimeMillis();
		double delta = currentTime - m_lastTime;
		boolean rv = (delta > Const.SERVER_INCREMENT);
		if (rv) {
			m_lastTime += Const.SERVER_INCREMENT;
			if (delta > 10 * Const.SERVER_INCREMENT) {
				m_lastTime = currentTime;
			}
			m_actualUpdateRate = 1000 / delta;
		}
		return rv;
	}

	/**
	 * Inner class of server that represents a client. This is made an inner
	 * class as it should be able to remove itself from the connected clients if
	 * it needs to.
	 *
	 */
	private class ClientThread extends Thread {
		private Socket m_socket;
		private ObjectInputStream m_input;
		private ObjectOutputStream m_output;
		private final int m_shipID;

		public ClientThread(final int shipID, Socket socket) {
			super("ClientThread");
			m_shipID = shipID;
			m_socket = socket;
			try {
				// Creating in and output streams.
				m_output = new ObjectOutputStream(m_socket.getOutputStream());
				m_input = new ObjectInputStream(m_socket.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}

		/* Function used to update the input from clients so that the engine has the latest input */
		private void parseInput(InputInfo inputs) {
			GameEntity entity = EntityManager.getEntity(m_shipID);
			if (entity instanceof Ship) {
				((Ship) entity).updateInput(inputs.m_rightKey, inputs.m_leftKey, inputs.m_accelerateKey,
						inputs.m_brakeKey);
			}
		}

		// Both functions involving write will be called by same thread i.e. no
		// risk for synchronization issues hopefully
		public void sendState(final StateUpdate state) {
			try {
				m_output.reset();
				m_output.writeObject(state);
				m_output.flush();
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
			}

		}

		// Doing this on the clientthread might make for faster initialization
		// but that doesn't make a big difference
		public boolean handshake() {
			try {
				// TODO: Conditions?? Is name necessary? Client choose team?
				// position? Requesting a team but joining the other if that
				// team is full? Version control?
				// Do handshake
				// send send handshake message
				// String msg = (String)input.readObject();
				// if(msg.equals("TEAM1")) {
				// // ... check if room on that team etc.
				// }
				m_output.writeObject("SUCCESS");
				m_output.flush();
			} catch (IOException/* | ClassNotFoundException */ e) {
				e.printStackTrace();
				System.exit(-1);
			}
			return true;
		}

		@Override
		public void run() {
			// Start client
			// Read input
			try {
				InputInfo inputs = (InputInfo) m_input.readObject();
				while (inputs != null) {
					inputs = (InputInfo) m_input.readObject();
					parseInput(inputs);
				}
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
				System.exit(-1);
			} finally {
				// Make sure the socket gets closed
				try {
					System.out.println("Closing socket.");
					m_input.close();
					m_output.close();
					m_socket.close();
				} catch (IOException e) {
					e.printStackTrace();
					System.exit(-1);
				}
			}
		}

	}

}
