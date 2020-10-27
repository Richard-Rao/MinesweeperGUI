
public interface MSModelInterface {
	int getRows();
	int getCols();
	boolean isMine(int row, int col);
	int getNumNeighboringMines(int row, int col);
	boolean isRevealed(int row, int col);
	int totalMines();
	boolean isFlag(int row, int col);
	void setFlag(int row, int col, boolean b);
	void createNewGrid(int numRows, int numCols, int numMines);
	void reveal(int row, int col);
	int minesLeft();
	boolean isGameWon();
	int getNumFlagsUsed();	
	void revealAll();

}
