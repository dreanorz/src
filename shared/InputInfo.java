package shared;

import java.io.Serializable;

public class InputInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public boolean m_rightKey;
	public boolean m_leftKey;
	public boolean m_accelerateKey;
	public boolean m_brakeKey;
	
	
	public String toString(){
		return "[Right key: " + m_rightKey + ", " +
				"Left key: " + m_leftKey + ", " +
				"Accelerate key: " + m_accelerateKey + ", " +
				"Brake key: " + m_brakeKey +  "]";
	}
	
	public InputInfo copy(){
		InputInfo ret = new InputInfo();
		ret.m_rightKey = m_rightKey;
		ret.m_leftKey = m_leftKey;
		ret.m_accelerateKey = m_accelerateKey;
		ret.m_brakeKey = m_brakeKey;
		return ret;
	}
	
	public InputInfo(){
		m_rightKey = false;
		m_leftKey = false;
		m_accelerateKey = false;
		m_brakeKey = false;
	}
}
