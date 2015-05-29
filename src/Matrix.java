/* 
 * Gauss-Jordan elimination over any field (Java)
 * by Nayuki Minase
 * modified by Justin Hsu, Nattaworn Ngampairojpibul
 * http://nayuki.eigenstate.org/page/gauss-jordan-elimination-over-any-field-java
 */


public final class Matrix<T> implements Cloneable {
	
	/* Basic matrix implementation */
	
	// The values of the matrix, initially null
	private Object[][] values;
	public Object[][] bVector; 

	// The field used to operate on the values in the matrix
	private Field<T> f;
	
	/**
	 * Constructs a blank matrix with the specified number of rows and columns, with operations from the specified field. All the elements are initially {@code null}.
	 * @param rows the number of rows in this matrix
	 * @param cols the number of columns in this matrix
	 * @param f the field used to operate on the values in this matrix
	 * @throws IllegalArgumentException if {@code rows} &le; 0 or {@code cols} &le; 0
	 * @throws NullPointerException if {@code f} is {@code null}
	 */
	public Matrix(int rows, int cols, Field<T> f) {
		if (rows <= 0 || cols <= 0)
			throw new IllegalArgumentException("Invalid number of rows or columns");
		if (f == null)
			throw new NullPointerException();
		values = new Object[rows][cols];
		this.f = f;
		bVector = new Object[rows][1];
	}
	
	/**
	 * Returns the number of rows in this matrix, which is positive.
	 * @return the number of rows in this matrix
	 */
	public int rowCount() {
		return values.length;
	}
	
	
	/**
	 * Returns the number of columns in this matrix, which is positive.
	 * @return the number of columns in this matrix
	 */
	public int columnCount() {
		return values[0].length;
	}
	
	
	/**
	 * Returns the element at the specified location in this matrix.
	 * @param row the row to read from
	 * @param col the column to read from
	 * @return the element at the specified location in this matrix
	 * @throws IndexOutOfBoundsException if the specified row or column exceeds the bounds of the matrix
	 */
	@SuppressWarnings("unchecked")
	public T get(int row, int col) {
		if (row < 0 || row >= values.length || col < 0 || col >= values[row].length)
			throw new IndexOutOfBoundsException("Row or column index out of bounds");
		return (T)values[row][col];
	}
	
	/**
	 * Returns the element at the specified location in this matrix.
	 * @param row the row to read from
	 * @param col the column to read from
	 * @return the element at the specified location in this matrix
	 * @throws IndexOutOfBoundsException if the specified row or column exceeds the bounds of the matrix
	 */
	@SuppressWarnings("unchecked")
	public T getBVector(int row, int col) {
		if (row < 0 || row >= values.length || col < 0 || col >= values[row].length)
			throw new IndexOutOfBoundsException("Row or column index out of bounds");
		return (T)bVector[row][col];
	}
	
	
	/**
	 * Stores the specified element at the specified location in this matrix.
	 * @param row the row to write to
	 * @param col the column to write to
	 * @param val the element value to write
	 * @throws IndexOutOfBoundsException if the specified row or column exceeds the bounds of the matrix
	 */
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
	/**
	 * Returns a clone of this matrix. The field and underlying values are shallow-copied because they are assumed to be immutable.
	 * @return a clone of this matrix
	 */
	public Matrix<T> clone() {
		int rows = rowCount();
		int cols = columnCount();
		Matrix<T> result = new Matrix<T>(rows, cols, f);
		for (int i = 0; i < values.length; i++)
			System.arraycopy(values[i], 0, result.values[i], 0, cols);
		return result;
	}
	
	
	/* Basic matrix operations */
	
	/**
	 * Swaps the two specified rows of this matrix.
	 * @param row0 one row to swap
	 * @param row1 the other row to swap
	 * @throws IndexOutOfBoundsException if a specified row exceeds the bounds of the matrix
	 */
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
	
	
	/**
	 * Multiplies the specified row in this matrix by the specified factor. In other words, row *= factor.
	 * @param row the row index to operate on
	 * @param factor the factor to multiply by
	 */
	public void multiplyRow(int row, T factor) {
		for (int j = 0, cols = columnCount(); j < cols; j++) {
			set(row, j, f.multiply(get(row, j), factor));
		}
		setBVector(row, 0, f.multiply(getBVector(row, 0), factor));
	}
	
	
	/**
	 * Adds the first specified row in this matrix multiplied by the specified factor to the second specified row.
	 * In other words, destRow += srcRow * factor.
	 * @param srcRow the index of the row to read and multiply
	 * @param destRow the index of the row to accumulate to
	 * @param factor the factor to multiply by
	 */
	public void addRows(int srcRow, int destRow, T factor) {
		for (int j = 0, cols = columnCount(); j < cols; j++) {
			set(destRow, j, f.add(get(destRow, j), f.multiply(get(srcRow, j), factor)));
		}
		setBVector(destRow, 0, f.add(getBVector(destRow, 0), f.multiply(getBVector(srcRow, 0), factor)));
	}
	
	
	/**
	 * Returns the product of this matrix with the specified matrix. (Remember that matrix multiplication is not commutative.)
	 * @param other the second matrix multiplicand
	 * @return the product of this matrix with the specified matrix
	 */
	public Matrix<T> multiply(Matrix<T> other) {
		if (columnCount() != other.rowCount())
			throw new IllegalArgumentException("Incompatible matrix sizes for multiplication");
		int rows = rowCount();
		int cols = other.columnCount();
		int cells = columnCount();
		Matrix<T> result = new Matrix<T>(rows, cols, f);
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				T sum = f.zero();
				for (int k = 0; k < cells; k++)
					sum = f.add(f.multiply(get(i, k), other.get(k, j)), sum);
				result.set(i, j, sum);
			}
		}
		return result;
	}
	
	
	/* Advanced matrix operation methods */
	
	/**
	 * Converts this matrix to reduced row echelon form (RREF) using Gauss-Jordan elimination.
	 * @throws NoReciprocalException 
	 */
	public void reducedRowEchelonForm() throws IllegalArgumentException {
		int rows = rowCount();
		int cols = columnCount();
		
		// Compute row echelon form (REF)
		int numPivots = 0;
		for (int j = 0; j < cols; j++) {  // For each column
			// Find a pivot row for this column
			int pivotRow = numPivots; 
			while (pivotRow < rows && f.equals(get(pivotRow, j), f.zero()))
				pivotRow++;
			if (pivotRow == rows)
				continue;  // Cannot eliminate on this column
			swapRows(numPivots, pivotRow); //change
			pivotRow = numPivots;
			numPivots++;
			
			// Simplify the pivot row
			try {
				multiplyRow(pivotRow, f.reciprocal(get(pivotRow, j))); // try to turn pivot to one
			} catch(IllegalArgumentException e) {
				/**
				boolean swapped = false; // if it's not possible to turn pivot to one
				for (int swapRow = pivotRow + 1; swapRow < rows - 1; swapRow++) { // row swap for one pivot
					if (get(swapRow, j) == f.one()) {
						swapRows(pivotRow, swapRow);
						swapped = true;
						break;
					}
				}
				if (!swapped) { // if no row swap occurs
					for (int currRow = pivotRow + 1; currRow < rows - 1; currRow++) {
						// case 1: currRow can be multiplied by something to make the leading coefficient
						// equals the pivot
						try {
							T z = f.productEqsY(get(currRow, j), get(pivotRow, j));
							if (f.equals(z, f.zero()))
								continue;
							multiplyRow(currRow, z);
							addRows(pivotRow, currRow, f.negate(f.one()));
							continue;
						} catch(IllegalArgumentException e2) {
							
						}
						
						// case 2: pivotRow can be multiplied by something to make the pivot equals the currRow's
						// leading coefficient
						try {
							T z = f.productEqsY(get(pivotRow, j), get(currRow, j));
							if (f.equals(z, f.zero()))
								continue;
							multiplyRow(pivotRow, z);
							addRows(currRow, pivotRow, f.negate(f.one()));
							continue;
						} catch(IllegalArgumentException e2) {
							
						}
					}
				}
				**/
			}
			
			// Eliminate rows below
			for (int i = pivotRow + 1; i < rows; i++)
				addRows(pivotRow, i, f.negate(get(i, j)));
		}
		
		// Compute reduced row echelon form (RREF)
		for (int i = rows - 1; i >= 0; i--) {
			// Find pivot
			int pivotCol = 0;
			while (pivotCol < cols && f.equals(get(i, pivotCol), f.zero()))
				pivotCol++;
			if (pivotCol == cols)
				continue;  // Skip this all-zero row
			
			// Eliminate rows above
			for (int j = i - 1; j >= 0; j--) {
				addRows(i, j, f.negate(get(j, pivotCol)));
				/**
				if (f.equals(get(j, pivotCol), f.zero())) // if the elimination is successful, continues
					continue;
				// Case 1: Multiply Row J by the correct factor then add up
				try {
					T z = f.productEqsY(get(j, pivotCol), get(i, pivotCol));
					if (f.equals(z, f.zero()))
						continue;
					multiplyRow(j, z);
					addRows(i, j, f.negate(f.one()));
					continue;
				} catch(IllegalArgumentException e2) {}
				// Case 2: Multiply Row I by the correct factor then add up
				try {
					T z = f.productEqsY(get(i, pivotCol), get(j, pivotCol));
					if (f.equals(z, f.zero()))
						continue;
					multiplyRow(i, z);
					addRows(j, i, f.negate(f.one()));
					continue;
				} catch(IllegalArgumentException e2) {}

				System.out.println(toString());
				**/
			}
		}
	}
	
	public Object[][] getBVector() {
		return bVector;
	}
	
	public String toString() {
		String output = "";
		output += "\nMatrix:\n";
		for (int i = 0; i < values.length; i++) {
			for (int j = 0; j < values[0].length; j++) {
				output += values[i][j] + " ";
			}
			output += "\n";
		}
		return output;
	}
}