package giu3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.jogamp.opengl.GL2;

public class Board {
	int currPlayer = 1;
	List<Cell> cells = new ArrayList<Cell>();
	//board will be a square, so size will be used for both length and width
	//board should split into 9 equal squares, thus, we use one variable for both cell width and height
	GL2 gl;
	float size, scale, cellWidthHeight, TopLeftX, TopLeftY;
	int d = 0;
	static String [] winLines = {
			"012", "345", "678",
			"036", "147", "258",
			"048","246"
	};
	
	int gameState[] = {-1,-1,-1,-1,-1,-1,-1,-1,-1};
	int[] winningLine = null;
	Point start = null,end = null;
	boolean Over = false;
	int absoluteWidth, absoluteHeight;
	AI ai = new AI(this);
	int nrPlayers = 1;

	
	public Board(GL2 gl, float size, float scale, int absoluteWidth, int absoluteHeight)
	{
		this.gl = gl;
		this.size = size;
		this.scale = scale;
		if(size > scale*2)
			size = scale*2;
		
		cellWidthHeight = size/3;
		TopLeftX = 0f - 3*cellWidthHeight/2;
		TopLeftY = 0f + 3*cellWidthHeight/2;
		
		
		for(int i=0; i<3;i++)
		{
			for(int j=0; j<3;j++)
			{
				float innerTLX = TopLeftX + j*cellWidthHeight;
				float innerTLY = TopLeftY - (2-i) *cellWidthHeight;
				cells.add(new Cell(innerTLX, innerTLY, innerTLX, innerTLY-cellWidthHeight, innerTLX+cellWidthHeight, innerTLY-cellWidthHeight, innerTLX+cellWidthHeight, innerTLY,0.25f));
			}
		}
		
	}
	public void setAbsoluteSize(int height, int width)
	{
		this.absoluteHeight = height;
		this.absoluteWidth = width;
	}
	
	public void setWinningLine(int c1, int c2, int c3)
	{
		int temp[] = {c1, c2, c3};
		this.winningLine = temp;
	}
	
	public void drawBoard()
	{
		float minSize = this.absoluteHeight;
			if(this.absoluteHeight > this.absoluteWidth)
				minSize = this.absoluteWidth;
		
			
		gl.glLineWidth(1f);
		for(Cell el : cells)
		{
			el.draw(gl, (float) Math.floor(minSize*0.15));
		}
		
		if(this.winningLine != null)
		{
			
			cells.get(winningLine[0]).drawGreen(gl);
			cells.get(winningLine[1]).drawGreen(gl);
			cells.get(winningLine[2]).drawGreen(gl);
			
			if(start == null || end == null)
			{
				if((winningLine[1] - winningLine[0]) == 1)
				{
					//win on a row
					start = cells.get(winningLine[0]).getMiddleLeft();
					end = cells.get(winningLine[2]).getMiddleRight();
				}
				else if((winningLine[1] - winningLine[0]) == 3)
				{
					//win on a collumn
					start = cells.get(winningLine[0]).getMiddleBot();
					end = cells.get(winningLine[2]).getMiddleTop();
				}
				else if((winningLine[1] - winningLine[0]) == 2)
				{
					//win on 2-4-6 diagonal
					start = cells.get(winningLine[0]).dr;
					end = cells.get(winningLine[2]).tl;
				}
				else
				{
					// win on 0-4-8 diagonal
					start = cells.get(winningLine[0]).dl;
					end = cells.get(winningLine[2]).tr;
				}
			}
			
			gl.glColor3f(0f,1f,0f);
			gl.glLineWidth(2f);
			gl.glBegin(GL2.GL_LINES);
			gl.glVertex2f(start.x,start.y);
			gl.glVertex2f(end.x, end.y);
			gl.glEnd();
		}
			
	}
	
	public void changeClicked(float x, float y)
	{
		if(this.Over)
			System.out.println("OVER!");
		for(int i=0 ; i<9 && !this.Over; i++)
		{
			if(cells.get(i).IncludesPoint2f(x, y)) //&& cells.get(i).cellUnused())
			{
				Cell c = cells.get(i);  
				System.out.println(String.format("( %f, %f ) - (%f, %f) - ( %f, %f )",c.tl.x, c.tl.y, x, y, c.dr.x, c.dr.y));
				
				if(c.cellUnused())
				{
					cells.get(i).setState(currPlayer);
					this.gameState[i] = currPlayer;
					checkIfWon();
					if(!this.Over)
					{
						currPlayer = -currPlayer + 1;
						if(nrPlayers == 1)
						{
							this.ai.MakeMove();
							currPlayer = 1;
						}
						checkIfWon();	
					}
					break;
				}
				
				
			}
			
		}
			
	}
	void checkIfWon()
	{
		if(Board.isWin(this.gameState, 0, 4, 8) != 1)
		{
			this.setWinningLine(0, 4, 8);
			this.Over = true;
		}
		if(Board.isWin(this.gameState, 2, 4, 6) != 1)
		{
			this.setWinningLine(2, 4, 6);
			this.Over = true;
		}
		for(int i=0; i<3; i++)
		{
			
			//check on rows
			if(Board.isWin(this.gameState, i*3, i*3+1, i*3+2) != 1)
			{
				
				this.setWinningLine(i*3, i*3+1, i*3+2);
				this.Over = true;
			}
			if(Board.isWin(this.gameState, i, i+3, i+6) != 1)
			{
				this.setWinningLine(i, i+3, i+6);
				this.Over = true;
			}
		}
	}
	
	static int isWin(int gameState[], int c1, int c2, int c3)
	{	
		if((gameState[c1] == gameState[c2]) && (gameState[c1] == gameState[c3]) && (gameState[c1] == 0))
			return 2;
		if((gameState[c1] == gameState[c2]) && (gameState[c1] == gameState[c3]) && (gameState[c1] == 1))
			return -2;
		return 1;
	
		
	}
	void gameLogic ()
	{
		/*     +-----------+
		 *     | 6 | 7 | 7 |
		 *     |-----------|
		 *     | 3 | 4 | 5 |
		 *     |-----------|
		 *     | 0 | 1 | 2 |
		 *     +-----------+
		 * 
		 */
		for(String line : Board.winLines)
		{
			
			char[] line2 = line.toCharArray();
			if(this.gameState[Character.getNumericValue(line2[0])] == this.currPlayer &&
			this.gameState[Character.getNumericValue(line2[1])] == this.currPlayer &&
			this.gameState[Character.getNumericValue(line2[2])] == this.currPlayer)
			{
				this.Over = true;
				System.out.println(line);
				this.setWinningLine(Character.getNumericValue(line2[0]), Character.getNumericValue(line2[1]), Character.getNumericValue(line2[2]));
			}
		}
		
	}
	
	void printGameState()
	{
		for(int el : this.gameState)
		{
			System.out.print(el + " ");
		}
		System.out.print("\n");
	}

	void resetState()
	{
		Arrays.fill(this.gameState, -1);
		this.Over = false;
		this.currPlayer = 1;
		this.start = null;
		this.end = null;
		this.winningLine = null;
		for(int i=0; i<9; i++)
		{
			cells.get(i).setState(-1);
		}	
	}
	void changeNrPlayers(int n)
	{
		if(n == 1 || n == 2)
		{
			this.nrPlayers = n;
			this.resetState();
		}
	}
}
