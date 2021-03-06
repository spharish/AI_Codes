/**
 *
 * Name      : SP Harish
 * Institute : IIIT Allahabad
 * @author spharish
 */
/* AI-532 Assignment 1 Question 1
 * 
 * 
 * 
 * P = There is no performance measure.
 * E = The car shop and the cars
 * A = Something to help the agent locomote.
 * S = A sensor indicating the current location,
 * 	   A sensor indicating the tire at current location is faulty or not	
 *
 * 
 */
package Question1;
import java.util.Scanner;

class D {
	
	int x;
	int[][] arr;
	
	D(int x) {
		this.x = x;
		arr = new int[x][5];
	}
	
}

public class Question_1 {
	public static void main(String[] args) {
		try {
			Scanner s = new Scanner(System.in);
			int t = s.nextInt();
			D[] input = new D[t];
			for(int i = 0; i < t; i++) {
				int N = s.nextInt();
				D obj = new D(N);
				for(int j = 0; j < N; j++) {
					for(int k = 0; k < 5; k++) {
						obj.arr[j][k] = s.nextInt();
					}
				}
				input[i] = obj;
			}
		
			s.close();
			
			for(int i = 0; i < t; i++) {
				Environment E = new Environment(input[i].x, input[i].arr);
				Agent a = new Agent(E);
				System.out.print("Shop ");
				a.inspect(E);
				System.out.println("Shop");			
			}
		
	
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

}

class Environment {
	int N;
	int[][] arr;
	int[] agent_location = new int[2];
	
	Environment(int N, int[][] arr) {
		this.N = N;
		this.arr = arr;
		agent_location[0] = N/2;
		agent_location[1] = 1;
	}
	
	
}

class Sensor {
	int[] get_current_location(Environment e) {
		return e.agent_location;
	}
	
	boolean is_faulty(Environment e) {
		int x = e.agent_location[0];
		int y = e.agent_location[1];
		if(e.arr[x - 1][y] == 1) return true;
		else return false;
	}
	
	boolean is_car_present(Environment e) {
		int x = e.agent_location[0];
		if(e.arr[x - 1][0] == 1) return true;
		else return false;
	}
	
}

class Actuator {
	void move_left(Environment e) {
		int y = e.agent_location[1];
		if(y == 1) e.agent_location[1] = e.agent_location[1] + 1;
		else if(y == 2) {
			e.agent_location[0] = e.agent_location[0] - 1;
			e.agent_location[1] = e.agent_location[1] - 1;
		}
		else if(y == 3) {
			e.agent_location[0] = e.agent_location[0] - 1;
			e.agent_location[1] = e.agent_location[1] + 1;
		}
		else if(y == 4) {
			e.agent_location[1] = e.agent_location[1] - 1;
		}
	}
	
	void move_right(Environment e) {
		int y = e.agent_location[1];
		if(y == 2) e.agent_location[1] = e.agent_location[1] - 1;
		else if(y == 1) {
			e.agent_location[0] = e.agent_location[0] + 1;
			e.agent_location[1] = e.agent_location[1] + 1;
		}
		else if(y == 4) {
			e.agent_location[0] = e.agent_location[0] + 1;
			e.agent_location[1] = e.agent_location[1] - 1;
		}
		else if(y == 3) {
			e.agent_location[1] = e.agent_location[1] + 1;
		}		
	}
	
	void move_up(Environment e) {
		int y = e.agent_location[1];
		if(y == 1) {
			
		}
		else if(y == 2) {
		}
		else if(y == 3) {
			e.agent_location[1] = 2;
		}
		else if(y == 4) {
			e.agent_location[1] = 1;
		}	
	}
	
	void move_down(Environment e) {
		int y = e.agent_location[1];
		if(y == 1) e.agent_location[1] = 4;
		else if(y == 2) {
			e.agent_location[1] = 3;
		}
		else if(y == 3) {
		}
		else if(y == 4) {
		}	
	}
	
}

class Agent {
	
	Sensor s;
	Actuator act;
	
	Agent(Environment e) {
		s = new Sensor();
		act = new Actuator();
	}
	
	boolean result;
	
	void inspect(Environment e) {
		int[] current_loc = new int[2];
		current_loc = s.get_current_location(e);
		
		while(current_loc[0] >= 1) {
			if(s.is_car_present(e)) result = s.is_faulty(e);
			System.out.print("C"+current_loc[0]+current_loc[1]+" ");
			if(current_loc[0] == 1 && current_loc[1] == 2) break;
			act.move_left(e);
			current_loc = s.get_current_location(e);

		}
		
		act.move_down(e);
		current_loc = s.get_current_location(e);

		
		while(current_loc[0] <= e.N) {
			if(s.is_car_present(e)) result = s.is_faulty(e);
			System.out.print("C"+current_loc[0]+current_loc[1]+" ");
			if(current_loc[0] == e.N && current_loc[1] == 4) break;
			act.move_right(e);
			current_loc = s.get_current_location(e);
		}
		
		current_loc = s.get_current_location(e);
		act.move_up(e);

		while(current_loc[0]  > (e.N)/2) {
			if(s.is_car_present(e)) result = s.is_faulty(e);
			System.out.print("C"+current_loc[0]+current_loc[1]+" ");
			act.move_left(e);
			current_loc = s.get_current_location(e);
		}
	}
	
	
}
