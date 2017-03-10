package shared;

import java.io.Serializable;

public class Vector2D implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double m_x;
    private double m_y;

    public Vector2D() {
	m_x = 0.0;
	m_y = 0.0;
    }

    public Vector2D(double px, double py) {
	m_x = px; 
	m_y = py;
    }

    public Vector2D(final Vector2D v) {
	m_x = v.getX();
	m_y = v.getY();
    }
    
    public String toString(){
    	return "[" + m_x + ", " + m_y + "]";
    }
    
    public double getX() {
	return m_x;
    }
    
    public double getY() {
	return m_y;
    }

    public double length() {
	return Math.sqrt(m_x * m_x + m_y * m_y);
    }

    public void set(double px, double py) {
	m_x = px; 
	m_y = py;
    }

    public void setX(double px) {
	m_x = px;
    }

    public void setY(double py) {
	m_y = py;
    }

    public void increaseX(double delta) {
	m_x += delta;
    }

    public void increaseY(double delta) {
	m_y += delta;
    }

    public void scale(double factor) {
	m_x *= factor; 
	m_y *= factor;
    }

    public void invert() {
	m_x = -m_x; 
	m_y = -m_y;
    }
    
    public void add(final Vector2D v) {
	m_x += v.getX(); 
	m_y += v.getY();
    }

    public void subtract(final Vector2D v) {
	m_x -= v.getX();
	m_y -= v.getY();
    }

    public double dotProduct(final Vector2D v) {
	return m_x * v.getX() + m_y * v.getY();
    }

    public void rotate(double radians) {
	double angle = Math.atan2(m_y, m_x);
	angle += radians;
	double l = length();
	m_y = l * Math.sin(angle);
	m_x = l * Math.cos(angle);
    }

    public void setLength(double l) {
	if(!(m_x == 0 && m_y == 0)) {
	    double r = Math.sqrt(l * l / (m_x * m_x + m_y * m_y));
	    m_x *= r;
	    m_y *= r;
	}
    }

    public void makeUnitVector() {
	double c = Math.sqrt(m_x * m_x + m_y * m_y);
	if(c != 0) {
	    m_x /= c;
	    m_y /= c;
	}
    }
    
    public Vector2D minusOperator(final Vector2D v) {
	return new Vector2D(m_x - v.getX(), m_y - v.getY());
    }

    public Vector2D multiplyOperator(double factor) {
	return new Vector2D(m_x * factor, m_y * factor);
    }
}
