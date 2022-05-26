package giu3;

public class Sun {
	private float m_x;
	private float m_y;
	private float m_radius;
	private boolean m_to_left;
	
	public Sun()
	{
		m_x = 0f;
		m_y = 0f;
		m_radius = 0.15f;
		m_to_left = true;
	}
	
	public Sun(float x, float y, float radius)
	{
		m_x = x;
		m_y = y;
		m_radius = radius;
		m_to_left = true;
	}
	
	public float getX()
	{
		return m_x;
	}
	
	public float getY()
	{
		return m_y;
	}
	
	public void incrementX(float amount)
	{
		m_x += amount;
	}
	
	public void decrementX(float amount)
	{
		m_x -= amount;
	}
	
	public void setX(float x)
	{
		m_x = x;
	}
	
	public void setY(float y)
	{
		m_y = y;
	}
	
	public void setRadius(float radius)
	{
		m_radius = radius;
	}
	
	public void goRight()
	{
		m_to_left = false;
	}
	
	public void goLeft()
	{
		m_to_left = true;
	}
	
	public boolean isMovingLeft()
	{
		return m_to_left;
	}
	
	public float getRadius()
	{
		return m_radius;
	}
	
}
