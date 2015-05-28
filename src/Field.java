public abstract class Field<T> {
	
	/**
	 * Returns the additive identity of this field.
	 * @return the additive identity of this field
	 */
	public abstract T zero();
	
	/**
	 * Returns the multiplicative identity of this field.
	 * @return the multiplicative identity of this field
	 */
	public abstract T one();
	
	
	/**
	 * Returns the sum of the two specified elements.
	 * @param x an addend
	 * @param y an addend
	 * @return the sum of {@code x} and {@code y}
	 */
	public abstract T add(T x, T y);
	
	/**
	 * Returns the product of the two specified elements.
	 * @param x a multiplicand
	 * @param y a multiplicand
	 * @return the product of {@code x} and {@code y}
	 */
	public abstract T multiply(T x, T y);
	
	/**
	 * Returns the additive inverse of the specified element.
	 * @param x the element whose additive inverse to compute
	 * @return the additive inverse of the specified element
	 */
	public abstract T negate(T x);
	
	/**
	 * Returns the multiplicative inverse of the specified element.
	 * @param x the element whose multiplicative inverse to compute
	 * @return the multiplicative inverse of the specified element
	 * @throws NoReciprocalException 
	 * @throws ArithmeticException if {@code x} equals {@code zero()}
	 */
	public abstract T reciprocal(T x) throws IllegalArgumentException;
	
	/**
	 * Returns the first element minus the second element.
	 * @param x the minuend
	 * @param y the subtrahend
	 * @return the first element minus the second element
	 */
	public T subtract(T x, T y) {
		return add(x, negate(y));
	}
	
	/**
	 * Returns the first element divided by the second element.
	 * @param x the dividend
	 * @param y the divisor
	 * @return the first element divided by the second element
	 * @throws NoReciprocalException 
	 * @throws ArithmeticException if {@code y} equals {@code zero()}
	 */
	public T divide(T x, T y) throws IllegalArgumentException {
		return multiply(x, reciprocal(y));
	}
	
	
	/**
	 * Tests whether the two specified elements are equal.
	 * Note that the elements are not required to implement their own {@code equals()} correctly &ndash; {@code x.equals(y)} can mismatch {@code f.equals(x, y)}.
	 * @param x an element to test for equality
	 * @param y an element to test for equality
	 * @return {@code true} if the two specified elements are equal, {@code false} otherwise
	 */
	public abstract boolean equals(T x, T y);
	
	
	/**
	 * Find and return an element, z, such that x*z = y
	 * @param x an element in the equation
	 * @param y an element the equation is constrained to be equal to
	 * @return z such that x*z = y
	 */
	public abstract T productEqsY(T x, T y);
	
}