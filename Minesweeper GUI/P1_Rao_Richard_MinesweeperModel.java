public class MinesweeperModel implements MSModelInterface{
	
	Boolean[][] field;
	int[][] cover;
	/*
	 0 = revealed
	 1 = covered
	 2 = flagged
	 */
	MinesweeperModel(int a, int b, int c){
		this.createNewGrid(a, b, c);
	}
	
	@Override
	public int getRows() {
		return field.length;
	}
	@Override
	public int getCols() {
		return field[0].length;
	}
	@Override
	public boolean isMine(int row, int col) {
		if(field[row][col]){
			return true;
		}
		return false;
	}
	@Override
	public int getNumNeighboringMines(int r, int c) {
		int num = 0;
		if(r > 0 && field[r-1][c]){
			num++;
		}
		if(c > 0 && field[r][c-1]){
			num++;
		}
		if(r < field.length -1 && field[r+1][c]){
			num++;
		}
		if(c < field[0].length - 1 && field[r][c+1]){
			num++;
		}
		if(r > 0 && c > 0 && field[r-1][c-1]){
			num++;
		}
		if(r < field.length -1 && c < field[0].length - 1 && field[r+1][c+1]){
			num++;
		}
		if(r < field.length - 1 && c > 0 && field[r+1][c-1]){
			num++;
		}
		if(r > 0 && c < field[0].length - 1 && field[r-1][c+1]){
			num++;
		}
		return num;
	}
	@Override
	public boolean isRevealed(int row, int col) {
		if(cover[row][col] == 0){
			return true;
		}else{
			return false;
		}
	}
	@Override
	public int totalMines() {
		int tot = 0;
		for(int row = 0; row < field.length; row++){
			for(int col = 0; col < field[0].length; col++){
				if(field[row][col]){
					tot++;
				}
			}
		}
		return tot;
	}
	@Override
	public void setFlag(int row, int col, boolean b) {
		if(b)	cover[row][col] = 2;
		else     cover[row][col] = 1;
	}
	@Override
	public void createNewGrid(int numRows, int numCols, int numMines) {
		field = new Boolean[numRows][numCols];
		cover = new int[numRows][numCols];
		for(int row = 0; row < numRows; row++){
			for(int col = 0; col < numCols; col++){
				field[row][col] = false;
				cover[row][col] = 1;
			}
		}
		for(int i = 0; i < numMines; i++){
			int r = (int)(Math.random()*numRows);
			int c = (int)(Math.random()*numCols);
			if(field[r][c] == true){
				i--;
			}else{
				field[r][c] = true;
			}
		}
	}

	@Override
	public void reveal(int row, int col) {
		if(row >= field.length || col >= field.length || row < 0 || col < 0){
			
		}
		
		else if(this.getNumNeighboringMines(row, col) != 0){
			cover[row][col] = 0;
		}
		else if(cover[row][col] == 1 && !field[row][col]){
			cover[row][col] = 0;
			reveal(row+1, col);
			reveal(row+1, col+1);
			reveal(row, col+1);
			reveal(row-1, col);
			reveal(row-1, col-1);
			reveal(row+1, col-1);
			reveal(row-1, col+1);
			reveal(row, col-1);
		}else{
			cover[row][col] = 0;
		}
	}
	
	@Override
	public boolean isGameWon() {
		int totRev = 0;
		for(int row = 0; row < field.length; row++){
			for(int col = 0; col < field.length; col++){
				if(this.isRevealed(row, col)){
					totRev++;
				}
			}
		}
		if(((field.length) * (field[0].length)) - totRev == this.totalMines()){
			return true;
		}
		return false;
	}
	@Override
	public int getNumFlagsUsed() {
		int tot = 0;
		for(int row = 0; row < field.length; row++){
			for(int col = 0 ; col < field.length; col++){
				if(cover[row][col] == 2){
					tot ++;
				}
			}
		}
		return tot;
	}
	@Override
	public boolean isFlag(int row, int col) {
		if(cover[row][col] == 2){
			return true;
		}
		return false;
	}
	@Override
	public int minesLeft(){
		int tot= 0;
		for(int row = 0; row < field.length; row++){
			for(int col = 0; col < field.length; col++){
				if(field[row][col]){
					tot++;
				}
			}
		}
		return tot-this.getNumFlagsUsed();
	}
	public void revealAll(){
		for(int row = 0; row < this.getRows(); row++){
			for(int col = 0; col < this.getCols(); col ++){
				cover[row][col] = 0;
			}
		}
	}
}
