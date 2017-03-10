package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import gui.GameWindow;
import gui.RenderableEntityManager;
import gui.ScoreKeeperRepresentation;
import shared.EntityMeta;
import shared.InputInfo;
import shared.StateUpdate;

public class ClientToServer extends Thread {

	private Socket m_socket;
	private ObjectInputStream m_input;
	private ObjectOutputStream m_output;
	private GameWindow m_gui;

	/* Constructor for a client to server connection */
	public ClientToServer(GameWindow gui, String serverHostname, int serverPort) {
		try {
			// Creates socket and creates in and output streams.
			System.out.println("Creating socket.");
			m_socket = new Socket(InetAddress.getByName(serverHostname), serverPort);
			m_output = new ObjectOutputStream(m_socket.getOutputStream());
			m_input = new ObjectInputStream(m_socket.getInputStream());

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		// Binds connection to gui.
		m_gui = gui;
	}

	/* Handshake function to recieve from server if connection is allowed or not */
	public boolean handshake() {
		try {
			return ((String) m_input.readObject()).equals("SUCCESS");
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return false;
	}

	/* Used to send input from client to server */
	public void sendInputs(InputInfo inputs) {
//		System.out.println("Sending inputs");
		try  {
			m_output.writeObject(inputs.copy());
			m_output.flush();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	/* Run function that is used when starting thread */
	public void run() {
		receiveStates();
	}
	/* Receive function that will recieve latest state from server tell GUI to repaint */
	public void receiveStates() {
		try {
			StateUpdate state;
			while (m_socket.isConnected() && !m_socket.isClosed()
					&& (state = (StateUpdate) m_input.readObject()) != null) {
//				System.out.println("State received");
				// Updates the state for all entities.
				for (EntityMeta ent : state.m_entities) {
					RenderableEntityManager.getInstance().updateState(ent);
				}
				// Updates score.
				ScoreKeeperRepresentation.getInstance().updateScore(state.m_scores[0], state.m_scores[1]);
//				System.out.println("Received state");
				// Repaints.
				m_gui.repaint();
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		} finally {
			try {
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