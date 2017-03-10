package client;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import javax.swing.JOptionPane;

import shared.InputInfo;

public class InputListener extends KeyAdapter {
	private KeyConfig m_keys;
	private final InputInfo m_inputs;

	
	/* Gets input settings from client. */
	public InputListener() {

		int dialogResult = JOptionPane.showConfirmDialog(null,
				"Would you like to set your keybinds or use default [WASD]?", "Keys", JOptionPane.YES_NO_OPTION);
		if (dialogResult == JOptionPane.YES_OPTION) {

			int left, right, brake, acc;
			left = requestKeybinding(
					"What key do you want to use to rotate left? (press cancel on any or all of these to use standard (WASD))");
			right = requestKeybinding(
					"What key do you want to use to rotate right? (press cancel on any or all of these to use standard (WASD))");
			brake = requestKeybinding(
					"What key do you want to use to brake? (press cancel on any or all of these to use standard (WASD))");
			acc = requestKeybinding(
					"What key do you want to use to move forward? (press cancel on any or all of these to use standard (WASD))");
			if (left == -1 || right == -1 || brake == -1 || acc == -1) {
				setDefaults();
			} else {
				m_keys = new KeyConfig(left, right, brake, acc);
			}
		} else {
			setDefaults();
		}
		m_inputs = new InputInfo();
	}

	private void setDefaults() {
		m_keys = new KeyConfig(KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_S, KeyEvent.VK_W);
	}
	
	/* Function for requesting keybindnings from user */
	private int requestKeybinding(String text) {
		// Taken from
		// http://stackoverflow.com/questions/15313469/java-keyboard-keycodes-list
		Field[] fields = java.awt.event.KeyEvent.class.getDeclaredFields();
		int index = 0;
		Field[] possibilities = new Field[fields.length];
		for (Field f : fields) {
			if (Modifier.isStatic(f.getModifiers()) && f.getName().contains("VK_")) {
				possibilities[index++] = f;
			}
		}

		Field s = (Field) JOptionPane.showInputDialog(null, text, "Keybinding Dialogue", JOptionPane.QUESTION_MESSAGE,
				null, possibilities, 0);

		if (s != null) {
			try {
				System.out.println(s.getInt(s));
				return s.getInt(s);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
		return -1;
	}

	public InputInfo getInputs() {
		return m_inputs;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			System.exit(0);
		} else if (e.getKeyCode() == m_keys.rightKey()) {
			m_inputs.m_rightKey = true;
		} else if (e.getKeyCode() == m_keys.leftKey()) {
			m_inputs.m_leftKey = true;
		} else if (e.getKeyCode() == m_keys.accelerateKey()) {
			m_inputs.m_accelerateKey = true;
		} else if (e.getKeyCode() == m_keys.brakeKey()) {
			m_inputs.m_brakeKey = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == m_keys.rightKey()) {
			m_inputs.m_rightKey = false;
		} else if (e.getKeyCode() == m_keys.leftKey()) {
			m_inputs.m_leftKey = false;
		} else if (e.getKeyCode() == m_keys.accelerateKey()) {
			m_inputs.m_accelerateKey = false;
		} else if (e.getKeyCode() == m_keys.brakeKey()) {
			m_inputs.m_brakeKey = false;
		}
	}
}

class KeyConfig {
	private final int m_leftKey, m_rightKey, m_brakeKey, m_accelerateKey;

	public KeyConfig(int left, int right, int brake, int accelerate) {
		m_leftKey = left;
		m_rightKey = right;
		m_brakeKey = brake;
		m_accelerateKey = accelerate;
	}

	public final int leftKey() {
		return m_leftKey;
	}

	public final int rightKey() {
		return m_rightKey;
	}

	public final int brakeKey() {
		return m_brakeKey;
	}

	public final int accelerateKey() {
		return m_accelerateKey;
	}

}