package giu3;

import com.jogamp.opengl.GL2;

public class Cell {
	int state = -1;
	Point tl,tr,dl,dr;
	float padding;
	//top-left, top-right, down-left, down-right
	
	public void draw(GL2 gl, float minSize)
	{
	
		gl.glColor3f(1f,1f,1f);
		gl.glLineWidth(2f);
		gl.glBegin(GL2.GL_QUADS);
		gl.glVertex2f(tl.x, tl.y);
		gl.glVertex2f(dl.x, dl.y);
		gl.glVertex2f(dr.x, dr.y);
		gl.glVertex2f(tr.x, tr.y);
		gl.glEnd();
		
		


		if(state == 0)
		{
			//draw 0
			gl.glColor3f(1f,0f,0f);
			gl.glPointSize(minSize);
			gl.glBegin(GL2.GL_POINTS);
			gl.glVertex2f((tl.x + tr.x)/2, (tl.y + dl.y)/2);
			gl.glEnd();
			gl.glPointSize((float)(minSize*0.75));
			gl.glColor3f(0f,0f,0f);
			gl.glBegin(GL2.GL_POINTS);
			gl.glVertex2f((tl.x + tr.x)/2, (tl.y + dl.y)/2);
			gl.glEnd();
			
		}
		
		if(state == 1)
		{
			//draw X
			gl.glColor3f(0f,0f,1f);
			gl.glLineWidth(minSize*1000);
			gl.glBegin(GL2.GL_LINES);
			gl.glVertex2f(tl.XwPadding(), tl.YwoPadding());
			gl.glVertex2f(dr.XwoPadding(), dr.YwPadding());
			gl.glVertex2f(tr.XwoPadding(), tr.YwoPadding());
			gl.glVertex2f(dl.XwPadding(), dl.YwPadding());
			gl.glEnd();
		}
	}

	public Cell(float tlX, float tlY, float dlX, float dlY,float drX, float drY, float trX, float trY, float padding) {
		this.tl = new Point(tlX, tlY, padding);
		this.dl = new Point(dlX, dlY, padding);
		this.dr = new Point(drX, drY, padding);
		this.tr = new Point(trX, trY, padding);
		state = -1;
	}
	
	public void drawGreen(GL2 gl)
	{
		gl.glColor3f(0f,1f,0f);
		gl.glBegin(GL2.GL_QUADS);
		gl.glVertex2f(tl.x, tl.y);
		gl.glVertex2f(dl.x, dl.y);
		gl.glVertex2f(dr.x, dr.y);
		gl.glVertex2f(tr.x, tr.y);
		gl.glEnd();
	}
	
	public Point getMiddleRight()
	{
		return new Point(this.dr.x,(this.dr.y + this.tr.y)/2);
	}
	public Point getMiddleLeft()
	{
		return new Point(this.dl.x,(this.dl.y + this.tl.y)/2);
	}
	public Point getMiddleTop() {
		return new Point((this.tl.x + this.tr.x)/2,this.tl.y);
	}
	public Point getMiddleBot() {
		return new Point((this.dl.x + this.dr.x)/2,this.dl.y);
	}
	public boolean IncludesPoint2f(float x, float y)
	{
		return !(x < tl.x || x > dr.x || y < dr.y || y > tl.y);
	}
	
	public boolean cellUnused()
	{
		return state==-1;
	}
	public void setState(int state)
	{
		this.state = state;
	}
	
}
 