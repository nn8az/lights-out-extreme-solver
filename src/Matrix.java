public final class Matrix<T> implements Cloneable {
	
	private Object[][] values;
	public Object[][] bVector; 

	private FieldOperators<T> op;
	
	public Matrix(int rows, int cols, FieldOperators<T> f) {
		values = new Object[rows][cols];
		this.op = f;
		bVector = new Object[rows][1];
	}
	
	public int rowCount() {
		return values.length;
	}
	public int columnCount() {
		return values[0].length;
	}
	
	public T get(int row, int col) {
		if (row < 0 || row >= values.length || col < 0 || col >= values[row].length)
			throw new IndexOutOfBoundsException("Row or column index out of bounds");
		return (T)values[row][col];
	}
	
	public T getBVector(int row, int col) {
		if (row < 0 || row >= values.length || col < 0 || col >= values[row].length)
			throw new IndexOutOfBoundsException("Row or column index out of bounds");
		return (T)bVector[row][col];
	}
	
	public void set(int row, int col, T val) {
		if (row < 0 || row >= values.length || col < 0 || col >= values[0].length)
			throw new IndexOutOfBoundsException("Row or column index out of bounds");
		values[row][col] = val;
	}
	
	public void setBVector(int row, int col, T val) {
		if (row < 0 || row >= values.length || col < 0 || col >= values[0].length)
			throw new IndexOutOfBoundsException("Row or column index out of bounds");
		bVector[row][0] = val;
	}

	public Matrix<T> clone() {
		int rows = rowCount();
		int cols = columnCount();
		Matrix<T> result = new Matrix<T>(rows, cols, op);
		for (int i = 0; i < values.length; i++)
			System.arraycopy(values[i], 0, result.values[i], 0, cols);
		return result;
	}
	
	public void swapRows(int row0, int row1) {
		if (row0 < 0 || row0 >= values.length || row1 < 0 || row1 >= values.length)
			throw new IndexOutOfBoundsException("Row index out of bounds");
		Object[] temp = values[row0];
		values[row0] = values[row1];
		values[row1] = temp;
		// change identity matrix
		Object[] temp2 = bVector[row0];
		bVector[row0] = bVector[row1];
		bVector[row1] = temp2;
	}
	
	public void multiplyRow(int row, T factor) {
		for (int j = 0, cols = columnCount(); j < cols; j++) {
			set(row, j, op.multiply(get(row, j), factor));
		}
		setBVector(row, 0, op.multiply(getBVector(row, 0), factor));
	}
	
	public void addRows(int srcRow, int destRow, T factor) {
		for (int j = 0, cols = columnCount(); j < cols; j++) {
			set(destRow, j, op.add(get(destRow, j), op.multiply(get(srcRow, j), factor)));
		}
		setBVector(destRow, 0, op.add(getBVector(destRow, 0), op.multiply(getBVector(srcRow, 0), factor)));
	}
	
	public Matrix<T> multiply(Matrix<T> other) {
		if (columnCount() != other.rowCount())
			throw new IllegalArgumentException("Incompatible matrix sizes for multiplication");
		int rows = rowCount();
		int cols = other.columnCount();
		int cells = columnCount();
		Matrix<T> result = new Matrix<T>(rows, cols, op);
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				T sum = op.zero();
				for (int k = 0; k < cells; k++)
					sum = op.add(op.multiply(get(i, k), other.get(k, j)), sum);
				result.set(i, j, sum);
			}
		}
		return result;
	}
	
	public void reducedRowEchelonForm() throws IllegalArgumentException {
		int rows = rowCount();
		int cols = columnCount();
		
		int numPivots = 0;
		for (int j = 0; j < cols; j++) {
			int pivotRow = numPivots; 
			while (pivotRow < rows && op.equals(get(pivotRow, j), op.zero()))
				pivotRow++;
			if (pivotRow == rows)
				continue;
			swapRows(numPivots, pivotRow);
			pivotRow = numPivots;
			numPivots++;
			
			try {
				multiplyRow(pivotRow, op.reciprocal(get(pivotRow, j))); // try to turn pivot to one
			} catch(IllegalArgumentException e) {
			}
			
			for (int i = pivotRow + 1; i < rows; i++)
				addRows(pivotRow, i, op.negate(get(i, j)));
		}
		
		for (int i = rows - 1; i >= 0; i--) {
			int pivotCol = 0;
			while (pivotCol < cols && op.equals(get(i, pivotCol), op.zero()))
				pivotCol++;
			if (pivotCol == cols)
				continue;
			for (int j = i - 1; j >= 0; j--) {
				addRows(i, j, op.negate(get(j, pivotCol)));
			}
		}
	}
	
	public Object[][] getBVector() {
		return bVector;
	}
}