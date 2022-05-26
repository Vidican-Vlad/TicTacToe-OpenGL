package giu3;

public class Point {
	float x,y, padding;
	
	public Point(float x, float y, float padding) {
		this.x=x;
		this.y=y;
		this.padding = padding;
	}
	public Point (float x, float y)
	{
		this.x=x;
		this.y=y;
	}
	
	public float XwoPadding()
	{
		return this.x-padding;
	}
	public float XwPadding()
	{
		return this.x+padding;
	}
	public float YwPadding()
	{
		return this.y+padding;
	}
	public float YwoPadding()
	{
		return this.y-padding;
	}
	
	
	public void print()
	{
		System.out.println(String.format("X= %f ; Y= %f", this.x, this.y));
	}

}
