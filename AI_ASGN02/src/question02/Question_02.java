/**
 *
 * Name      : SP Harish
 * Institute : IIIT Allahabad
 * @author spharish
 */
/*
 * 
 * Title: Java Program to solve the n-puzzle problem
 * 
 * P = Minimum cost of path
 * E = n*n board 
 * A = Something to swap blank position
 * S = Sensor which indicates the board state at some time
 */

package question02;

import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;
import java.util.Vector;

class Input {
	
	int N;
	int[][] board;
	
	Input(int N) {
		this.N = N;
		board = new int[N][N];
	}
	
}

public class Question_02 {
	public static void main(String[] args) {
		try {
			Scanner s = new Scanner(System.in);
			int t = s.nextInt();
			Input[] input = new Input[t];
			for(int i = 0; i < t; i++) {
				int N = s.nextInt();
				Input obj = new Input(N);
				for(int j = 0; j < N; j++) {
					for(int k = 0; k < N; k++) {
						obj.board[j][k] = s.nextInt();
					}
				}
				input[i] = obj;
			}
		
			s.close();
			
			for(int i = 0; i < t; i++) {
				Environment e = new Environment(input[i].N,input[i].board);
				Sensor sense = new Sensor(e);
				Agent a = new Agent(sense);
				a.execute(sense);
			}
		
	
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

}


class Environment {
	 int size;
	 int[][] board;
	
	public Environment(int size, int[][] board) {
		this.size = size;
		this.board = board;
	}

}

class Sensor {
	
	Environment e;
	
	public Sensor(Environment e) {
		this.e = e;
	}
	
	int[][] getBoard() {
		return e.board;
	}
	
	int getSize() {
		return e.size;
	}
}
	

class Agent {
	
	private List<Action> seq;
	private int[][] goal;
	private int[][] state;
	private int size;
	
	
	public Agent(Sensor s) {
		seq = new ArrayList<Action>();
		goal = new int[s.getSize()][s.getSize()];
		size = s.getSize();
		
		state = new int[s.getSize()][s.getSize()];
		
		for(int i = 0; i < s.getSize(); i++) {
			for(int j = 0; j < s.getSize(); j++) {
				state[i][j] = s.getBoard()[i][j];
			}
		}
		
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				System.out.print(state[i][j]+ " ");
			}
		}
		System.out.println();
	}
	
	private int[][] formulateGoal(int[][] matrix, int N) {
		
		int[][] goal;
		Index blankLocation;
		int missingNumber = -1;
		
		boolean[] isPresent = new boolean[N*N];
		blankLocation = new Index();
		goal = new int[N][N];
		
		for(int i = 0; i < N*N; i++) isPresent[i] = false;
		
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				if(matrix[i][j] != -1) {
					isPresent[matrix[i][j] - 1] = true;
				}
				else {
					blankLocation.x = i;
					blankLocation.y = j;
				}
			}
		}
		
		for(int i = 0; i < N*N; i++) {
			if(isPresent[i] == false) {
				missingNumber = i + 1;
			}
		}
		
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				if(i*N+j+1 == missingNumber) {
					goal[i][j] = -1;
				}
				else {
					goal[i][j] = i*N + j + 1;
				}
			}
		}
		
		/* Print Goal
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) System.out.print(goal[i][j] + " ");
			System.out.println();
		}
		*/
		return goal;
	}
	
	public Action execute(Sensor s) {
		Action action = null;
		
		if(seq.size() == 0) {
			goal = formulateGoal(state, size);
			seq = breadthFirstSearch(state,size,goal);
		}
		
		//System.out.println(seq.size());
		
		if(seq.size() > 0) {
			action = seq.remove(0);
		}
		
		return action;
	}
	
	private List<Action> breadthFirstSearch(int[][] startState, int N, int[][] goal) {
		
		Stack<Action> S = new Stack<Action>();
		
		Queue<SearchStructure> searchQueue = new LinkedList<SearchStructure>();
		
		Vector<SearchStructure> states = new Vector<SearchStructure>();
		
		SearchStructure startStruct = new SearchStructure(-1,N);
		
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				startStruct.state[i][j] = startState[i][j];
			}
		}
		
		SearchStructure popVal = startStruct;
		searchQueue.add(popVal);
		boolean isEqual = true;
		
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				if(popVal.state[i][j] != goal[i][j]) {
					isEqual = false;
				}
			}
		}
		
		int parent = 0;
		
		while(isEqual != true) {
			
			SearchStructure tmp = searchQueue.element();
			Index tmpBlankLoc = new Index();
			
			for(int i = 0; i < N; i++) {
				for(int j = 0; j < N; j++) {
					if(tmp.state[i][j] == -1) {
						tmpBlankLoc.x = i;
						tmpBlankLoc.y = j;
					}
				}
			}
			
			if(canGoLeft(tmpBlankLoc, N)) {
				SearchStructure child = new SearchStructure(parent, N);
				for(int i = 0; i < N; i++) {
					for(int j = 0; j < N; j++) {
						child.state[i][j] = tmp.state[i][j];
					}
				}
				int tmpVal = child.state[tmpBlankLoc.x - 1][tmpBlankLoc.y];
				child.state[tmpBlankLoc.x - 1][tmpBlankLoc.y] = -1;
				child.state[tmpBlankLoc.x][tmpBlankLoc.y] = tmpVal;
				searchQueue.add(child);
			}
			
			if(canGoUp(tmpBlankLoc, N)) {
				SearchStructure child = new SearchStructure(parent, N);
				for(int i = 0; i < N; i++) {
					for(int j = 0; j < N; j++) {
						child.state[i][j] = tmp.state[i][j];
					}
				}
				int tmpVal = child.state[tmpBlankLoc.x][tmpBlankLoc.y + 1];
				child.state[tmpBlankLoc.x][tmpBlankLoc.y + 1] = -1;
				child.state[tmpBlankLoc.x][tmpBlankLoc.y] = tmpVal;
				searchQueue.add(child);
			}
			
			if(canGoRight(tmpBlankLoc, N)) {
				SearchStructure child = new SearchStructure(parent, N);
				for(int i = 0; i < N; i++) {
					for(int j = 0; j < N; j++) {
						child.state[i][j] = tmp.state[i][j];
					}
				}
				int tmpVal = child.state[tmpBlankLoc.x + 1][tmpBlankLoc.y];
				child.state[tmpBlankLoc.x + 1][tmpBlankLoc.y] = -1;
				child.state[tmpBlankLoc.x][tmpBlankLoc.y] = tmpVal;
				searchQueue.add(child);
			}
			
			if(canGoDown(tmpBlankLoc, N)) {
				SearchStructure child = new SearchStructure(parent, N);
				for(int i = 0; i < N; i++) {
					for(int j = 0; j < N; j++) {
						child.state[i][j] = tmp.state[i][j];
					}
				}
				int tmpVal = child.state[tmpBlankLoc.x][tmpBlankLoc.y - 1];
				child.state[tmpBlankLoc.x][tmpBlankLoc.y - 1] = -1;
				child.state[tmpBlankLoc.x][tmpBlankLoc.y] = tmpVal;
				searchQueue.add(child);
			}
			
			popVal = searchQueue.remove();
			
			isEqual = compareToGoal(popVal.state, N);
			
			states.add(parent, popVal);
			parent++;
		}
		
		
		
		SearchStructure solution = states.lastElement();
		while(solution.parent != -1) {
			int[] tmpAct = new int[N*N];
			for(int i = 0; i < N; i++) {
				for(int j = 0; j < N; j++) {
					tmpAct[i*N+j] = solution.state[i][j];
				}
			}
			Action act = new Action(tmpAct);
			S.push(act);
			solution = states.elementAt(solution.parent);
		}
		
		while(S.empty() == false) {
			Action printAct = S.pop();
			for(int i = 0; i < N*N; i++) System.out.print(printAct.curState[i]+" ");
			System.out.println();
		}
		
		return seq;
	}
	
	
	
	boolean canGoLeft(Index blankLoc, int N) {
		if(blankLoc.x > 0) return true;
		return false;
	}
	
	boolean canGoUp(Index blankLoc, int N) {
		if(blankLoc.y < N - 1) return true;
		return false;
	}
	
	boolean canGoRight(Index blankLoc, int N) {
		if(blankLoc.x < N - 1) return true;
		return false;
	}
	
	boolean canGoDown(Index blankLoc, int N) {
		if(blankLoc.y > 0) return true;
		return false;
	}
	
	
	boolean compareToGoal(int[][] matrix, int N) {
		boolean result = true;
		
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				if(matrix[i][j] != goal[i][j]) {
					result = false;
				}
			}
		}
		if(result == true) return result;
		
		int[][] rot1 = new int[N][N];
		rot1 = rotateMatrix(matrix, N);
		
		result = true;
		
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				if(rot1[i][j] != goal[i][j]) {
					result = false;
				}
			}
		}
		if(result == true) return result;
		
		rot1 = rotateMatrix(rot1, N);
		
		result = true;
		
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				if(rot1[i][j] != goal[i][j]) {
					result = false;
				}
			}
		}
		if(result == true) return result;
		
		rot1 = rotateMatrix(rot1, N);
		
		result = true;
		
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				if(rot1[i][j] != goal[i][j]) {
					result = false;
				}
			}
		}
		if(result == true) return result;
		
		int[][] flip1 = new int[N][N];
		flip1 = flipMatrix(matrix, N);
		
		result = true;
		
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				if(flip1[i][j] != goal[i][j]) {
					result = false;
				}
			}
		}
		if(result == true) return result;
		
		rot1 = rotateMatrix(flip1, N);
		
		result = true;
		
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				if(rot1[i][j] != goal[i][j]) {
					result = false;
				}
			}
		}
		if(result == true) return result;
		
		rot1 = rotateMatrix(rot1, N);
		
		result = true;
		
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				if(rot1[i][j] != goal[i][j]) {
					result = false;
				}
			}
		}
		if(result == true) return result;
		
		rot1 = rotateMatrix(rot1, N);
		
		result = true;
		
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				if(rot1[i][j] != goal[i][j]) {
					result = false;
				}
			}
		}
		if(result == true) return result;
		
		return result;
	}
	
	int[][] rotateMatrix(int[][] matrix, int n) {
		
		int[][] rotatedMatrix = new int[n][n];
		
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < n; j++) {
				rotatedMatrix[i][j] = matrix[n - j - 1][i];
			}
		}
		
		return rotatedMatrix;
	}
	
	int[][] flipMatrix(int[][] matrix, int n) {
		int[][] flipped = new int[n][n];
		
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < n; j++) flipped[i][j] = matrix[i][n - j - 1];
		}
		
		return flipped;
	}

	

	
}

class SearchStructure {
	int parent;
	int[][] state;
	
	SearchStructure(int parent, int N) {
		this.parent = parent;
		state = new int[N][N];
	}
}

class Index {
	int x;
	int y;
	
	Index() {
		x = 0;
		y = 0;
	}
}

class Action {
	int[] curState;
	
	Action(int[] curState) {
		this.curState = curState;
	}
}