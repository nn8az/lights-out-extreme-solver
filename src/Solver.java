public class Solver {
	Matrix<Integer> A;
	public int ASize;
	public int boardRow;
	public int boardCol;
	public int state;

	public Solver(int boardRow, int boardCol, int s) {
		this.boardRow = boardRow;
		this.boardCol = boardCol;
		ASize = boardRow * boardCol;
		state = s;
		A = new Matrix<Integer>(ASize, ASize, new Zn(state));
		for (int Arow = 0; Arow < ASize; Arow++) {
			for (int Acol = 0; Acol < ASize; Acol++) {
				int i, j, i_, j_ = 0;
				i = Arow / boardCol; // index (i, j) is the tile that you're setting
								// the equation up for
				j = Arow % boardCol;
				i_ = Acol / boardCol; // index (i_, j_) is the index of where you are
									// pressing
				j_ = Acol % boardCol;
				if (i_ >= 0 && i_ <= ASize && j_ >= 0 && j_ <= ASize) {
					if (Math.abs(i - i_) + Math.abs(j - j_) <= 1) {
						A.set(Arow, Acol, (Integer) 1);
					} else {
						A.set(Arow, Acol, (Integer) 0);
					}
				}
			}
		}
	}

	public void setBVector(int[] bVector) {
		// Check for illegal input
		if (bVector.length != ASize) {
			throw new IllegalArgumentException(
					"The b vector does not have the correct dimension.");
		}
		for (int i = 0; i < bVector.length; i++) {
			A.setBVector(i, 0, (Integer) bVector[i]);
		}
	}

	public void RowReduce() {
		A.reducedRowEchelonForm();
	}
	
	public boolean hasSolution() {
		for (int curr_Row = ASize - 1; curr_Row >= 0; curr_Row--) { // Go through each row of A starting from the bottom
			for (int i = 0; i < ASize; i++) {
				if ((int)A.get(curr_Row, i) != 0) { // If it isn't all zero
					return true;
				}
			}
			if ((int)A.getBVector(curr_Row, 0) != 0) { // If it is all zero with non zero value for b vector
				return false;
			}
		}
		return true;
	}
	
	public int[][] publishSolution() {
		int[][] solution = new int[boardRow][boardCol];
		for (int i = 0; i < ASize; i++) {
			solution[i / boardCol][i % boardCol] = A.getBVector(i, 0);
		}
		return solution;
	}
	
	public void printA() {
		for (int i = 0; i < ASize; i++) {
			for (int j = 0; j < ASize; j++) {
				System.out.print(A.get(i, j) + " ");
			}
			System.out.println();
		}
	}
	
	public double percentSolvable() {
		return 1 / Math.pow(state, zeroRowCount());
	}
	
	public int zeroRowCount() {
		int count = 0;
		for (int curr_Row = ASize - 1; curr_Row >= 0; curr_Row--) { // Go through each row of A starting from the bottom
			for (int i = 0; i < ASize; i++) {
				if ((int)A.get(curr_Row, i) != 0) { // If it isn't all zero
					return count;
				}
			}
			count++;
		}
		return count;
	}
}
