package mines;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;

public class Mines {
	private Place board[][];
	private int numMines;
	private boolean showAll;

	public Mines(int height, int width, int numMines) {
		board = new Place[height][width];
		this.numMines = numMines;
		
		//initializing the rest of the board
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				if(board[i][j] == null)
					board[i][j] = new Place(i,j);
			}
		}		
		Random r = new Random();
		int x, y;
		
		// choosing a random place for the mines
		for (int i = 0; i < numMines; i++) {
			x = r.nextInt(height);
			y = r.nextInt(width);
			while (board[x][y].isMine()) {
				x = r.nextInt(height);
				y = r.nextInt(width);
			}
			//setting a mine
			board[x][y].setMine();
		}
	}

	public boolean addMine(int i, int j) {
		//setting a mine in this place
		if(i < 0 || i >= board.length || j < 0 || j >= board[0].length)
			return false;	
		if(!board[i][j].isMine()) {
			board[i][j].setMine();
			numMines++;
		}
		return true;
	}

	public boolean open(int i, int j) {
		//opening any place that is not already opened and its' neighbors if there is no mine near this place
		if (board[i][j].isMine())
			return false;
		if (!board[i][j].isOpen() && !board[i][j].isFlag()) {	
			board[i][j].openPlace();
			if(board[i][j].nearMines() == 0) {
				Collection<Place> neighbours = board[i][j].neighbours();
				for(Place p : neighbours)
					open(p.i,p.j);
			}
		}
		return true;
	}

	public void toggleFlag(int x, int y) {
		//placing a flag if there is no flag, otherwise removing the flag
		board[x][y].setFlag();
	}

	public boolean isDone() {
		// counting the number of opened places that are not mines
		int counter = 0;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				if (board[i][j].isOpen() && !board[i][j].isMine())
					counter++;
			}
		}
		return counter == board.length * board[0].length - numMines ? true : false;
	}

	public String get(int i, int j) {
		// returns the value of the place, if a place is closed return "F" for flag, otwerwise ".",
		//if a place is opened return "X" for a mine or a mount of mines around this place and " " for 0 mines around
		if(showAll == true) {
			if(board[i][j].isMine())
				return "X";
			else
				return board[i][j].nearMines() > 0 ? String.format("%d", board[i][j].nearMines()) : " ";
		}
		
		if (!board[i][j].isOpen())
			if (board[i][j].isFlag())
				return "F";
			else
				return ".";
		else if (board[i][j].isMine())
			return "X";
		else
			return board[i][j].nearMines() > 0 ? String.format("%d", board[i][j].nearMines()) : " ";
	}

	public void setShowAll(boolean showAll) {
		this.showAll = showAll;
	}

	public String toString() {
		// returns the board with each value of the places
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				sb.append(get(i, j));
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	
	
	
	private class Place {
		private int i,j;
		private boolean open = false, flag = false, mine = false;

		private Place(int i, int j) {
			//a constructor for a place, saving his indexes
			this.i = i;
			this.j = j;
		}

		public boolean isMine() {
			// true if the place is a mine, false otherwise
			return mine;
		}
		
		public void setMine() {
			//setting the flag mine to be true, means this place has a mine
			mine = true;
		}

		public boolean isOpen() {
			//returning if the place is open or not
			return open;
		}
		
		public void openPlace() {
			//open a place, true means the place is open now
			open = true;
		}
		
		public boolean isFlag() {
			// returning if there is a flag or not
			return flag;
		}
		
		public void setFlag() {
			// changing a flag from true to false or from false to true;
			flag = !flag;
		}

		public int nearMines() {
			// counting the number of mines near a place
			int counter = 0;
			Collection<Place> neighbours = board[i][j].neighbours();
			for(Place p : neighbours) {
				if(p.isMine())
					counter++;
			}
			return counter;
		}
		
		public Collection<Place> neighbours() {
			//creating a collection of this place's neighbors
			Collection<Place> neighbours = new HashSet<>();
			for(int x = i-1; x < i+2; x++) {
				for(int y = j-1; y < j+2; y++) {
					if(x == i && y == j)
						continue;
					if(x >= 0 && x < board.length && y >= 0 && y < board[0].length)
						neighbours.add(board[x][y]);
				}
			}
			return neighbours;
		}
	}
}
