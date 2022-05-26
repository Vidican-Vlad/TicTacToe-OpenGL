package giu3;

import java.util.concurrent.TimeUnit;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;

import javax.swing.JFrame;

import com.jogamp.opengl.util.Animator;

public class movingHouse
		extends JFrame
		implements GLEventListener
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Sun sun = new Sun();
	private GLCanvas canvas;
	private Animator animator;

	// For specifying the positions of the clipping planes (increase/decrease the distance) modify this variable.
 	// It is used by the glOrtho method.
	private double v_size = 1.0;

	// Application main entry point
	public static void main(String args[]) 
	{
		new movingHouse();
	}

	// Default constructor
	public movingHouse()
	{
		super("Java OpenGL");
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		
		this.setSize(800, 600);
		
		this.initializeJogl();
		
		this.setVisible(true);
	}
	
	private void initializeJogl()
	{
		// Creating a new GL profile.
		GLProfile glprofile = GLProfile.getDefault();
		// Creating an object to manipulate OpenGL parameters.
		GLCapabilities capabilities = new GLCapabilities(glprofile);
		
		// Setting some OpenGL parameters.
		capabilities.setHardwareAccelerated(true);
		capabilities.setDoubleBuffered(true);

		// Try to enable 2x anti aliasing. It should be supported on most hardware.
	    capabilities.setNumSamples(2);
	    capabilities.setSampleBuffers(true);
		
		// Creating an OpenGL display widget -- canvas.
		this.canvas = new GLCanvas(capabilities);
		
		// Adding the canvas in the center of the frame.
		this.getContentPane().add(this.canvas);
		
		// Adding an OpenGL event listener to the canvas.
		this.canvas.addGLEventListener(this);
		
		// Creating an animator that will redraw the scene 40 times per second.
		this.animator = new Animator(this.canvas);
			
		// Starting the animator.
		this.animator.start();
	}
	
	public void init(GLAutoDrawable canvas)
	{
		this.sun.setX(0.7f);
		this.sun.setY(0.7f);
		
		// Obtaining the GL instance associated with the canvas.
		GL2 gl = canvas.getGL().getGL2();
		
		// Setting the clear color -- the color which will be used to erase the canvas.
		gl.glClearColor(0, 0, 0, 0);
		
		// Selecting the modelview matrix.
		gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
		
		gl.glOrtho(-1, 1, -1, 1, -1, 1);

	}
	
	void displayCircleWithGL_LINE_LOOP(GL2 gl, float cx, float cy, float r, int num_segments)
	{
		// Erasing the canvas -- filling it with the clear color.
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);

		gl.glBegin(GL2.GL_LINE_LOOP);
	    for (int ii = 0; ii < num_segments; ii++)   
	    {
	        float theta = 2.0f * 3.1415926f * (float)(ii) / (float)(num_segments);	// get the current angle 
	        float x = (float) (r * Math.cos(theta));		//calculate the x component 
	        float y = (float) (r * Math.sin(theta));		//calculate the y component 
	        gl.glVertex2f(x + cx, y + cy);					//output vertex 
	    }
		gl.glEnd();
		
		// Forcing the scene to be rendered.
		gl.glFlush();
	}
	
	// x si y sunt coordonatele coltului din stanga jos a casei
	// size este cat de mare va fi casa (adica cat de mare e o latura a ei)
	void drawHouse(GL2 gl, float x, float y, float size)
	{
		int segments = (int)(10000 * size);		// cate puncte va avea o linie dreapta
		float granularity = size / segments;	// cata distanta e intre punctele care umplu casa, adica cat se misca cursorul de desenare la o iteratie
		
		drawBody(gl, x, y, size, segments, granularity);
		drawRoof(gl, x, y + size, size, segments, granularity);
	}
	
	void drawBody(GL2 gl, float x, float y, float size, int segments, float granularity)
	{
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		
		gl.glBegin(GL2.GL_TRIANGLES);
			gl.glColor3f(1.f, 0.647f, 0.f);
			gl.glVertex2f(x, y);
			gl.glVertex2f(x, y + size);
			gl.glVertex2f(x + size, y);
			
			gl.glVertex2f(x + size, y);
			gl.glVertex2f(x, y + size);
			gl.glVertex2f(x + size, y + size);
		gl.glEnd();
		
		// Deseneaza usa
		drawDoor(gl, x, y, size, segments, granularity);
		
		// Deseneaza geam
		drawWindow(gl, x + size * 0.1f, y + size / 2, size, segments, granularity);
		drawWindow(gl, x + size * 0.7f, y + size / 2, size, segments, granularity);
		
		// Forcing the scene to be rendered.
		gl.glFlush();
	}
	
	void drawWindow(GL2 gl, float x, float y, float size, int segments, float granularity)
	{
		// Vrem 20% din lungimea casei pe OX sa fie lungimea geamurilor si 33% din inaltimea casei pe OY sa fie inaltimea geamurilor
		gl.glBegin(GL2.GL_TRIANGLES);
			gl.glColor3f(0.122f, 0.553f, 0.729f);
			gl.glVertex2f(x, y);
			gl.glVertex2f(x, y + size * 0.33f);
			gl.glVertex2f(x + size * 0.2f, y);
			
			gl.glVertex2f(x + size * 0.2f, y);
			gl.glVertex2f(x, y + size * 0.33f);
			gl.glVertex2f(x + size * 0.2f, y + size * 0.33f);
		gl.glEnd();
		
		// Forcing the scene to be rendered.
		gl.glFlush();
	}
	
	void drawDoor(GL2 gl, float x, float y, float size, int segments, float granularity)
	{
		// Usa va avea 30% din lungimea casei si 50% din inaltime
		x = (float) ((2 * x + size) / 2 - size * 0.15);		// 0.15 aici pentru ca trebuie sa incepem cu 15 inainte de mijloc pentru a fi centrata usa
		
		gl.glBegin(GL2.GL_TRIANGLES);
			gl.glColor3f(0.392f, 0.274f, 0.137f);
			gl.glVertex2f(x, y);
			gl.glVertex2f(x, y + size / 2);
			gl.glVertex2f(x + size * 0.3f, y);
			
			gl.glVertex2f(x + size * 0.3f, y);
			gl.glVertex2f(x, y + size / 2);
			gl.glVertex2f(x + size * 0.3f, y + size / 2);
		gl.glEnd();
		
		// Forcing the scene to be rendered.
		gl.glFlush();
		
	}
	
	void drawRoof(GL2 gl, float x, float y, float size, int segments, float granularity)
	{
		// Acoperisul va fi cu 20% mai lung pe fiecare parte a patratului decat latura patratului si va avea 50% din inaltime
		gl.glBegin(GL2.GL_TRIANGLES);
			gl.glColor3f(1.f, 0.f, 0.f);
			gl.glVertex2f(x - 0.2f * size, y);
			gl.glVertex2f(x + size / 2, y + size / 2);
			gl.glVertex2f(x + size * 1.2f, y);
		gl.glEnd();
		
		gl.glFlush();
	}
	
	void drawSun(GL2 gl, float cx, float cy, float r, int num_segments)
	{
		gl.glColor3f(1.f, 1.f, 0.f);
		
		gl.glBegin(GL2.GL_TRIANGLE_FAN);
		    for (int ii = 0; ii < num_segments; ii++)   
		    {
		        float theta = 2.0f * 3.1415926f * (float)(ii) / (float)(num_segments);	// get the current angle 
		        float x = (float) (r * Math.cos(theta));		//calculate the x component 
		        float y = (float) (r * Math.sin(theta));		//calculate the y component 
		        gl.glVertex2f(x + cx, y + cy);					//output vertex 
		        
		    }
		gl.glEnd();
		
		// Forcing the scene to be rendered.
		gl.glFlush();
	}
	
	void moveTheSun()
	{
		// Verificam daca se misca la stanga si ajuns la limita ecranului
		if (this.sun.isMovingLeft())
		{
			if (this.sun.getX() < -1 + this.sun.getRadius())
			{
				this.sun.goRight();
				this.sun.incrementX(0.005f);
			}
			else
			{
				this.sun.decrementX(0.005f);
			}
		}
		else
		{
			if (this.sun.getX() > 1 - this.sun.getRadius())
			{
				this.sun.goLeft();
				this.sun.decrementX(0.005f);
			}
			else
			{
				this.sun.incrementX(0.005f);
			}
		}
	}
	
	public void display(GLAutoDrawable canvas)
	{
		GL2 gl = canvas.getGL().getGL2();
		
		// TEMA CASA SI SOARE
		drawHouse(gl, -0.6f, -0.6f, 0.3f);
		drawSun(gl, this.sun.getX(), this.sun.getY(), 0.15f, 200);
		moveTheSun();
	}
	
	public void reshape(GLAutoDrawable canvas, int left, int top, int width, int height)
	{
		GL2 gl = canvas.getGL().getGL2();
		
		// Selecting the viewport -- the display area -- to be the entire widget.
		gl.glViewport(0, 0, width, height);
		
		// Determining the width to height ratio of the widget.
		double ratio = (double) width / (double) height;
		
		// Selecting the projection matrix.
		gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
		
		gl.glLoadIdentity();
		
		if (ratio < 1)
			gl.glOrtho(-v_size, v_size, -v_size, v_size / ratio, -1, 1);
		else
			gl.glOrtho(-v_size, v_size * ratio, -v_size, v_size, -1, 1);

		// Selecting the modelview matrix.
		gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);		
	}
	
	public void displayChanged(GLAutoDrawable canvas, boolean modeChanged, boolean deviceChanged)
	{
		return;
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
	}
}