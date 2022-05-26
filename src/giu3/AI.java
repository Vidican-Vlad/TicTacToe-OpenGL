package giu3;

import java.util.ArrayList;

public class AI {
	Board board;
	
	
	public AI(Board board)
	{
		this.board = board;
	}
	
	public void MakeMove()
	{
		int bm = getBestMove(this.board.gameState);
		if(bm != -1)
		{
			
			this.board.gameState[bm] = 0;
			this.board.cells.get(bm).setState(0);
		}
	}
	
	int getBestMove(int[] gameState)
	{
		int bestScore = -100, choice = -1,score;
		ArrayList<Integer> options = new ArrayList<Integer>();
		for(int i = 0; i < 9; i++)
		{
			//consider all free cells as options
			if(gameState[i] == -1)
				options.add(i);
		}
		for(int move : options) {
			score = minmax(getStateForNewMove(gameState, move, 0), false);
			if(score > bestScore)
			{
				choice = move;
				bestScore = score;
			}
		}
		return choice;
	}
	
	int minmax(int gameState[], boolean isMaximizing)
	{
		// 0 is maximizing, X (1) is minimizing
		int result = evaluateState(gameState);
		if(result != 1)
			return result;
		
		int bestEval = isMaximizing ? -100 : 100;
		for(int i=0; i<9; i++)
		{
			if(gameState[i] == -1)
			{
				int eval;
				if(isMaximizing)
				{
					eval = minmax(getStateForNewMove(gameState, i, 0), false);
					bestEval = Math.max(eval, bestEval);
				}
				else
				{
					eval = minmax(getStateForNewMove(gameState, i, 1), true);
					bestEval = Math.min(eval, bestEval);
				}
			}
		}
		return bestEval;
	}
	
	int[] getStateForNewMove(int gameState[], int cell, int currPlayer)
	{
		//creating "virtual" gameState  arrays in order to avoid modifying the "real" game state during the evaluations of possible moves
		int result[] = gameState.clone();
		result[cell] = currPlayer;
		return result;
	}
	
	
	int  evaluateState(int gameState[])
	{
		//gameState is an int array where each the value of each index represents the state of that cell,  (1 = X, 0 = 0, -1 = free cell)
		//an eval of 2 is a win for 0, eval of -2 is a win for X, an eval of 0 is a tie, and an eval of 1 means game still in progress
		
		//check on diagonals
		int eval = Board.isWin(gameState, 0, 4, 8);
		if(eval != 1)
			return eval;
		eval = Board.isWin(gameState, 2, 4, 6);
		if(eval != 1)
			return eval;
		
		for(int i=0; i<3; i++)
		{
			//check on rows
			eval = Board.isWin(gameState, i*3, i*3+1, i*3+2);
			if(eval != 1)
				return eval;
			//check collumns
			eval = Board.isWin(gameState, i, i+3, i+6);
			if(eval != 1)
				return -2;
		}
		for(int i=0; i<9; i++)
		{
			//check if there is atleat one free cell left
			if(gameState[i] == -1)
				return 1;
		}
		//tie
		return 0;
		
	}
}
	

