/**
 *
 * Name      : SP Harish
 * Institute : IIIT Allahabad
 * @author spharish
 */


/**	AI-532 ASSIGNMENT 1 QUESTION 04
 * 	
 *
 * 
 * 	P = Spend maximum time in front of the car
 * 	E = Enviroment is the the shop and the cars
 * 	A = Actuators are something to locomote and repair tyres.
 * 	S =	A sensor indicating the current location,
 * 	    A sensor indicating the tire at current location is faulty or not
 */
package Question4;
import java.util.Scanner;

class D {
	
	int x;
	int[][] arr;
	
	D(int x) {
		this.x = x;
		arr = new int[x][5];
	}
	
}

public class Question_4 {
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
				int x = -1,y = -1;
				for(int j = 0; j < input[i].x;j++) {
					if(input[i].arr[j][0] == 1) {
						for(int k = 1; k < 5; k++) {
							if(input[i].arr[j][k] == 1) {
								x = j + 1;
								y = k;
							}
						}
					}
				}
				Agent a = new Agent(E, x, y);
				System.out.print("Shop ");
				a.repair_tyre(E);
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
	
//	State/Model -
// ------------------------------------------------------
	
	boolean is_carrying;
	boolean tyre_type;
	int[] fault_location = new int[2];

//-------------------------------------------------------
	
	Agent(Environment e,int x,int y) {
		s = new Sensor();
		act = new Actuator();
		is_carrying = false;
		tyre_type = false;
		fault_location[0] = x;
		fault_location[1] = y;
		
	}
	
	void repair_tyre(Environment e) {
		//System.out.println("Fault location : C"+ fault_location[0]+ fault_location[1]+ " ");
		//System.out.print("C"+s.get_current_location(e)[0]+s.get_current_location(e)[1]+" ");
		
		
		//Going to faulty tyre location -
		is_carrying = true;
		if(fault_location[0] > (e.N)/2) {
			while(fault_location[0] > s.get_current_location(e)[0]) {
				act.move_right(e);
				System.out.print("C"+s.get_current_location(e)[0]+s.get_current_location(e)[1]+" ");
			}
			if(fault_location[1] == 3) {
				act.move_down(e);
				System.out.print("C"+s.get_current_location(e)[0]+s.get_current_location(e)[1]+" ");
			}
			else if(fault_location[1] == 1 || fault_location[1] == 4) {
				act.move_right(e);
				System.out.print("C"+s.get_current_location(e)[0]+s.get_current_location(e)[1]+" ");
				if(fault_location[1] == 4) {
					act.move_down(e);
					System.out.print("C"+s.get_current_location(e)[0]+s.get_current_location(e)[1]+" ");
				}
			}
			
		}
		else {
			System.out.print(s.get_current_location(e)[0]+s.get_current_location(e)[1]+" ");
			while(fault_location[0] < s.get_current_location(e)[0]) {
				act.move_right(e);
				System.out.print("C"+s.get_current_location(e)[0]+s.get_current_location(e)[1]+" ");
			}
			if(fault_location[1] == 4) {
				act.move_down(e);
				System.out.print("C"+s.get_current_location(e)[0]+s.get_current_location(e)[1]+" ");
			}
			else if(fault_location[1] == 2 || fault_location[1] == 3) {
				act.move_left(e);
				System.out.print("C"+s.get_current_location(e)[0]+s.get_current_location(e)[1]+" ");
				if(fault_location[1] == 3) {
					act.move_down(e);
					System.out.print("C"+s.get_current_location(e)[0]+s.get_current_location(e)[1]+" ");
				}
			}
		}
		
		is_carrying = false;
	    e.arr[s.get_current_location(e)[0] - 1][s.get_current_location(e)[1]] = 0;
	    
	    //Returning -
	    
		if(fault_location[1] == 3 || fault_location[1] == 4) {
			act.move_up(e);
			System.out.print("C"+s.get_current_location(e)[0]+s.get_current_location(e)[1]+" ");
		}
		
		while(s.get_current_location(e)[0] > (e.N)/2) {
			if(s.get_current_location(e)[0] == (e.N)/2 + 1 && s.get_current_location(e)[1] == 2) break;
			act.move_left(e);
			System.out.print("C"+s.get_current_location(e)[0]+s.get_current_location(e)[1]+" ");
		}
		
		while(s.get_current_location(e)[0] <= (e.N)/2) {
			if(s.get_current_location(e)[0] == (e.N)/2 && s.get_current_location(e)[1] == 1) break;
			act.move_right(e);
			System.out.print("C"+s.get_current_location(e)[0]+s.get_current_location(e)[1]+" ");
		}
	    
	}

	
	
	
	
}
