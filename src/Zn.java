public class Zn extends FieldOperators<Integer> {

	public int n;

	public Zn(int n) {
		this.n = n;
	}

	@Override
	public Integer zero() {
		return 0;
	}

	@Override
	public Integer one() {
		return 1;
	}

	@Override
	public Integer add(Integer x, Integer y) {
		return (x + y) % n;
	}

	@Override
	public Integer multiply(Integer x, Integer y) {
		return (x * y) % n;
	}

	@Override
	public Integer negate(Integer x) {
		return multiply(x, n - 1);
	}

	@Override
	public Integer reciprocal(Integer x) throws IllegalArgumentException {
		for (int i = n - 1; i >= 0; i--) {
			if ((x * i) % n == 1) {
				return i;
			}
		}
		throw new IllegalArgumentException("Multiplicative inverse does not exist for " + x + " on the field Z{" + n + "}");
	}

	@Override
	public boolean equals(Integer x, Integer y) {
		return x == y;
	}

	@Override
	public Integer productEqsY(Integer x, Integer y) {
		for (int z = 0; z < n; z++) {
			if (equals(multiply(x, z), y))
				return z;
		}
		throw new IllegalArgumentException("No solution, z, exists for " + x +" * z = " + y);
	}

}
