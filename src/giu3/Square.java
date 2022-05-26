package giu3;

public class Square {
	private float x,y;
	
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}


	public void moveSquare(float x,float y) {
		this.x=x;
		this.y=y;
	}
}
