
public class Main {

	public static void main(String[] args) {
		Board board = new Board(3, 2);
	}
	
	public static void printCombi(int n, int r) {
		int[] array = new int[r];
		for (int i = 1; i < r + 1; i++) {
			array[r - 1] = i;
		}
		while (array[0] < n - r + 1) {
			// increment array
			array[r - 1]++;
			// propagate
			for (int i = r - 1; i >= 1; i--) {
				if (array[i] > n) {
					array[i] = array[i - 1] + 1;
					array[i - 1]++;
				}
			}
			// print array
			System.out.print("{");
			for (int i = 0; i < array.length; i++) {
				System.out.print("  " + array[i]);
			}
			System.out.print("}");
			System.out.println();
		}
	}

}
