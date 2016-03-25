
/**
 *
 * Name      : SP Harish
 * Institute : IIIT Allahabad
 * @author spharish
 */
package rotation;

public class Rotate {

	/**
	 * @param args
	 */
	

	
	public static void main(String[] args) {

		int[][] matrix = new int[3][3];
		
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) matrix[i][j] = i*3+j+1;
		}
		
		int[][] rot1 = rotateMatrix(matrix,3);
		
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) System.out.print(rot1[i][j]+" ");
			System.out.println();
		}
		System.out.println();
		System.out.println("*********************************");
		System.out.println();
		
		int[][] rot2 = rotateMatrix(rot1,3);
		
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) System.out.print(rot2[i][j]+" ");
			System.out.println();
		}
		System.out.println();
		System.out.println("*********************************");
		System.out.println();
		
		int[][] rot3 = rotateMatrix(rot2,3);
		
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) System.out.print(rot3[i][j]+" ");
			System.out.println();
		}
		
		System.out.println();
		System.out.println("*********************************");
		System.out.println();
		
		int[][] flip1 = flipMatrix(matrix,3);
		
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) System.out.print(flip1[i][j]+" ");
			System.out.println();
		}
		
		System.out.println();
		System.out.println("*********************************");
		System.out.println();
		
		rot1 = rotateMatrix(flip1,3);
		
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) System.out.print(rot1[i][j]+" ");
			System.out.println();
		}
		System.out.println();
		System.out.println("*********************************");
		System.out.println();
		
		rot2 = rotateMatrix(rot1,3);
		
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) System.out.print(rot2[i][j]+" ");
			System.out.println();
		}
		System.out.println();
		System.out.println("*********************************");
		System.out.println();
		
		rot3 = rotateMatrix(rot2,3);
		
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) System.out.print(rot3[i][j]+" ");
			System.out.println();
		}
		

	}
	
	static int[][] rotateMatrix(int[][] matrix, int n) {
		
		int[][] rotatedMatrix = new int[n][n];
		
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < n; j++) {
				rotatedMatrix[i][j] = matrix[n - j - 1][i];
			}
		}
		
		return rotatedMatrix;
	}
	
	static int[][] flipMatrix(int[][] matrix, int n) {
		int[][] flipped = new int[n][n];
		
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < n; j++) flipped[i][j] = matrix[i][n - j - 1];
		}
		
		return flipped;
	}

}
