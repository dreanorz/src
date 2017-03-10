package client;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JLabel;
import javax.swing.JWindow;

public class InputMenu implements KeyListener {
	Frame m_owner;
	JWindow m_window;
	String m_keyBind = "-1";
	public InputMenu(Frame owner){
		m_owner = owner;
	}
	
	public int getKeyBind(String text){
		
		m_window = new JWindow(m_owner);
		m_window.add(new JLabel(text), BorderLayout.NORTH);
		m_window.add(new JLabel(m_keyBind), BorderLayout.CENTER);
		return Integer.parseInt(m_keyBind);
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		m_keyBind = Integer.toString(e.getKeyCode());
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

}
