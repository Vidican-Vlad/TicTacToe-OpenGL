package giu3;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;

public class MouseInput implements  MouseListener {
	int windowHeight, windowWidth;
	float scale;
	Board board;

	public MouseInput(int width, int height, float scale, Board board) {
		this.windowHeight = height;
		this.windowWidth = width;
		this.scale = scale;
		this.board = board;
	}
	
	 float pixelsTof(int n, int maxVal) {
		 
		float cr = this.scale*2/maxVal;
		
		return n*cr-scale;
		
	}
	 
	public void updateSize(int windowHeight, int windowWidth)
	{
		this.windowHeight = windowHeight;
		this.windowWidth = windowWidth;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		float x = pixelsTof(e.getX(), this.windowWidth);
		float y = pixelsTof(e.getY(), this.windowHeight);
		board.changeClicked(x, y*-1);
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	


}
