public abstract class FieldOperators<T> {
	
	public abstract T zero();
	public abstract T one();
	public abstract T add(T x, T y);
	public abstract T multiply(T x, T y);
	public abstract T negate(T x);
	public abstract T reciprocal(T x) throws IllegalArgumentException;
	public T subtract(T x, T y) {
		return add(x, negate(y));
	}
	public T divide(T x, T y) throws IllegalArgumentException {
		return multiply(x, reciprocal(y));
	}
	
	
	public abstract boolean equals(T x, T y);
	public abstract T productEqsY(T x, T y);
}