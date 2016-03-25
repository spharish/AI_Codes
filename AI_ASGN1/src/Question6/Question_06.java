/**
 *
 * Name      : SP Harish
 * Institute : IIIT Allahabad
 * @author spharish
 */

/*	AI-532 ASSIGNMENT 1 QUESTION 05
 * 	IRM2013014 ADRISH BANERJEE
 *
 * 
 * 	P = Minimum time taken.
 * 	E = Enviroment is the the shop and the cars
 * 	A = Actuators are something to locomote and repair tyres.
 * 	S =	A sensor indicating the current location,
 * 	    A sensor indicating the tire at current location is faulty or not
 */

package Question6;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Stack;

class D {
	
	int x;
	int[][] arr;
	
	D(int x) {
		this.x = x;
		arr = new int[x][5];
	}
	
}

public class Question_06 {
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
				System.out.print("Shop ");		
				a.repair_tyre();
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
	
	int N;
	boolean is_carrying;
	boolean tyre_type;
	int[][] faults;
	HashMap<Integer, Int_Array> hm = new HashMap<Integer, Int_Array>(N*4);
	double[][] graph;
	int count;

//-------------------------------------------------------
	
//Agent Constructor
	Agent(Environment e) {
		s = new Sensor();
		act = new Actuator();
		is_carrying = false;
		tyre_type = false;	
		N = e.N;
		faults = new int[(e.N)*4][2];
		graph = new double[N*4][N*4];
		
// Intialize HashMap
		
		for(int i = 1; i <= N; i++) {
			for(int j = 1; j <= 4; j++) {
				Int_Array arr = new Int_Array(2);
				arr.arr[0] = i;
				arr.arr[1] = j;
				hm.put((i - 1)*4 + j,arr);
			}
		}
// Print HashMap
/*		
		Set set = hm.entrySet();
		Iterator i = set.iterator();
		while(i.hasNext()) {
			Map.Entry<Integer, Int_Array> me = (Map.Entry<Integer, Int_Array>)i.next();
			System.out.print(me.getKey()+ " : ");
			Int_Array test = new Int_Array(2);
			test = me.getValue();
			System.out.println(test.arr[0]+" "+test.arr[1]);
			
		}
		
*/		
		
		//Initialize Graph
		for(int i = 0; i < N*4; i++) {
			for(int j = 0; j < N*4; j++) {
				graph[i][j] = 0;
			}
		}
		
		//Make graph
		for(int i = 1; i <= N*4; i++) {
				int node_num = i;
				if(node_num%4 == 1) {
					graph[node_num - 1][node_num] = 0.49999;
					graph[node_num - 1][node_num + 2] = 1;
					if(node_num + 5 <= N*4) graph[node_num - 1][node_num + 4] = 0.99999;
				}
				else if(node_num%4 == 2) {
					graph[node_num - 1][node_num] = 1;
					graph[node_num - 1][node_num - 2] = 0.49999;
					if(node_num - 5 >= 1) graph[node_num - 1][node_num - 6] = 0.99999;
				}
				else if(node_num%4 == 3) {
					graph[node_num - 1][node_num - 2] = 1;
					graph[node_num - 1][node_num] = 0.5;
					if(node_num - 3 >= 1) graph[node_num - 1][node_num - 4] = 1;
				}
				else if(node_num%4 == 0){
					graph[node_num - 1][node_num - 2] = 0.5;
					graph[node_num - 1][node_num - 4] = 1;
					if(node_num + 3 <= N*4) graph[node_num - 1][node_num + 2] = 1;
				}
		}		
		
		//Print Graph
		/*
		for(int i = 0; i < N*4; i++) {
			for(int j = 0; j < N*4; j++) {
				System.out.print(graph[i][j]+" ");
			}
			System.out.println();
		}
		*/
	}
	
	
	
	/*int select_best_plan() {
		double time1 = time_to_carry_both(fault_location1[0],fault_location1[1],fault_location2[0],fault_location2[1]);
		double time2 = time_to_carry_one(fault_location1[0],fault_location1[1],fault_location2[0],fault_location2[1]);
		int choice = (time1 < time2)?1:2;
		return choice;
	}*/
	
	
	/*double distance_from_shop(int x, int y) {
		double distance = 0;
		if(x > (N/2)) {
			double base_distance = (double)(1.5*(x - (N/2 + 1)));
			if(y == 1) distance = (double) (base_distance + 0.5);
			else if(y == 2) distance = base_distance;
			else if(y == 3) distance = (double)(base_distance + 1.0);
			else if(y == 4) distance = (double) (base_distance + 1.5);		
		}
		else {
			double base_distance =(double)(1.5*(x - (N/2)));
			if(y == 2) distance = (double) (base_distance - 0.5);
			else if(y == 1) distance = base_distance;
			else if(y == 4) distance = (double)(base_distance - 1.0);
			else if(y == 3) distance = (double) (base_distance - 1.5);	
		}
		return distance;
		
	}*/
	
	double distance_between_cars(int x1, int y1, int x2, int y2) {
		double distance = 0;
		Dijkstra dist_obj = new Dijkstra();
		//System.out.println(x1+" "+y1+" "+x2+" "+y2);
		int n1 = 4*x1 - (4 - y1);
		int n2 = 4*x2 - (4 - y2);
		//System.out.println("node 1 = * "+n1);
		//System.out.println("node 2 = * "+n2);
		distance = dist_obj.min_dist(graph, n1, n2, N*4);
		return distance;
	}
	/*
	double time_to_carry_both(int x1, int y1, int x2, int y2) {
		double d1 = distance_from_shop(x1, y1);
		double d2 = distance_from_shop(x2, y2);
		d1 = Math.abs(d1);
		d2 = Math.abs(d2);
		double dmin = (d1 < d2)?d1:d2;
		double dmax = (d1 > d2)?d1:d2;
		double dbet = (distance_between_cars(x1,y1,x2,y2));
		return 4*dmin+2*dbet+ dmax;	
	}
	*/
	/*
	double time_to_carry_one(int x1, int y1, int x2, int y2) {

		double d = -1;
		double d1 = distance_from_shop(x1, y1);
		double d2 = distance_from_shop(x2, y2);	
		if(d1*d2 >= 0) d = d1 + d2;
		else if(d1 > d2) d = d1 - d2;
		else if(d2 > d1) d = d2 - d1;
		d = Math.abs(d);
		return (double)(3*d);
	}
*/
	
	void inspect(Environment e) {
		
		int[] current_loc = new int[2];
		current_loc = s.get_current_location(e);
		
		while(current_loc[0] >= 1) {
			System.out.print("C"+current_loc[0]+current_loc[1]+" ");
			if(e.arr[s.get_current_location(e)[0] - 1][s.get_current_location(e)[1]] == 1) {
				faults[count][0] = s.get_current_location(e)[0];
				faults[count++][1] = s.get_current_location(e)[1];
			}
			if(current_loc[0] == 1 && current_loc[1] == 2) break;
			act.move_left(e);
			current_loc = s.get_current_location(e);

		}
		
		act.move_down(e);
		current_loc = s.get_current_location(e);

		
		while(current_loc[0] <= e.N) {
			System.out.print("C"+current_loc[0]+current_loc[1]+" ");
			if(e.arr[s.get_current_location(e)[0] - 1][s.get_current_location(e)[1]] == 1) {
				faults[count][0] = s.get_current_location(e)[0];
				faults[count++][1] = s.get_current_location(e)[1];
			}
			if(current_loc[0] == e.N && current_loc[1] == 4) break;
			act.move_right(e);
			current_loc = s.get_current_location(e);
		}
		
		current_loc = s.get_current_location(e);
		act.move_up(e);

		while(current_loc[0]  > (e.N)/2) {
			if(e.arr[s.get_current_location(e)[0] - 1][s.get_current_location(e)[1]] == 1) {
				faults[count][0] = s.get_current_location(e)[0];
				faults[count++][1] = s.get_current_location(e)[1];
			}
			System.out.print("C"+current_loc[0]+current_loc[1]+" ");
			act.move_left(e);
			current_loc = s.get_current_location(e);
		}
		
		/*
		for(int i = 0; i < count; i++) {
			System.out.println(faults[i][0]+""+faults[i][1]);
		}
		*/
	}
	
	void repair_tyre() {
		Dijkstra path_obj = new Dijkstra();
		Stack<Integer> s = new Stack<Integer>();
		if(count >= 1) {
			if(faults[0][0] > N/2) {
				s = path_obj.dijkstra(graph,4*(N/2 + 1) - 2, 4*(faults[0][0]) - (4 - faults[0][1]), N*4);
				System.out.print("C"+(N/2+1)+""+2+" ");
				while(s.isEmpty() != true) {
					Int_Array tmp = new Int_Array(2);
					tmp = hm.get(s.pop() + 1);
					System.out.print("C"+tmp.arr[0] + tmp.arr[1]+ " ");
				}
				for(int i = 0; i < count - 1; i++) {
					s = path_obj.dijkstra(graph,4*(faults[i][0]) - (4 - faults[i][1]), 4*(faults[i +1][0]) - (4 - faults[i + 1][1]), N*4);
					while(s.isEmpty() != true) {
						Int_Array tmp = new Int_Array(2);
						tmp = hm.get(s.pop() + 1);
						if(tmp.arr[0] == N/2 && tmp.arr[1] == 1) System.out.print("Shop ");
						System.out.print("C"+tmp.arr[0] + tmp.arr[1]+ " ");
						
					}
				}
				if(faults[count - 1][0] > N/2) {
					s = path_obj.dijkstra(graph,  4*(faults[count - 1][0]) - (4 - faults[count - 1][1]), 4*(N/2 + 1) - 2,  N*4);
					while(s.isEmpty() != true) {
						Int_Array tmp = new Int_Array(2);
						tmp = hm.get(s.pop() + 1);
						System.out.print("C"+tmp.arr[0] + tmp.arr[1]+ " ");
					}
					System.out.println("Shop");
				}
				else {
					s = path_obj.dijkstra(graph,  4*(faults[count - 1][0]) - (4 - faults[count - 1][1]), 4*(N/2) - 3,  N*4);
					while(s.isEmpty() != true) {
						Int_Array tmp = new Int_Array(2);
						tmp = hm.get(s.pop() + 1);
						System.out.print("C"+tmp.arr[0] + tmp.arr[1]+ " ");
					}
					System.out.println("Shop");
				}
	
			}
			else {
				s = path_obj.dijkstra(graph,4*(N/2) - 3, 4*(faults[0][0]) - (4 - faults[0][1]), N*4);
				System.out.print("C"+(N/2)+""+1+" ");
				while(s.isEmpty() != true) {
					Int_Array tmp = new Int_Array(2);
					tmp = hm.get(s.pop() + 1);
					System.out.print("C"+tmp.arr[0] + tmp.arr[1]+ " ");
				}
				for(int i = 0; i < count - 1; i++) {
					s = path_obj.dijkstra(graph,4*(faults[i][0]) - (4 - faults[i][1]), 4*(faults[i +1][0]) - (4 - faults[i + 1][1]), N*4);
					while(s.isEmpty() != true) {
						Int_Array tmp = new Int_Array(2);
						tmp = hm.get(s.pop() + 1);
						if(tmp.arr[0] == ((N/2) + 1) && tmp.arr[1] == 2) System.out.print("Shop ");
						System.out.print("C"+tmp.arr[0] + tmp.arr[1]+ " ");
					}
				}
				if(faults[count - 1][0] > N/2) {
					s = path_obj.dijkstra(graph,  4*(faults[count - 1][0]) - (4 - faults[count - 1][1]), 4*(N/2 + 1) - 2,  N*4);
					while(s.isEmpty() != true) {
						Int_Array tmp = new Int_Array(2);
						tmp = hm.get(s.pop() + 1);
						System.out.print("C"+tmp.arr[0] + tmp.arr[1]+ " ");
					}
					System.out.println("Shop");		
				}
				else {
					s = path_obj.dijkstra(graph,  4*(faults[count - 1][0]) - (4 - faults[count - 1][1]), 4*(N/2) - 3,  N*4);
					while(s.isEmpty() != true) {
						Int_Array tmp = new Int_Array(2);
						tmp = hm.get(s.pop() + 1);
						System.out.print("C"+tmp.arr[0] + tmp.arr[1]+ " ");
					}
					System.out.println("Shop");
				}
			}
		}
	}
}




class Int_Array {
	int[] arr;
	Int_Array(int x) {
		arr = new int[x];
	}
}

class Vertex {
	
	double dist;
	int prev;
	int num;
	
	Vertex() {
		dist = -1;
		prev = -1;
		num = -1;
	}
	
	
}

 class Dijkstra {
	 
	 Stack<Integer> dijkstra(double[][] graph, int source,int target, int N) {

		
		source = source - 1;
		target = target - 1;
		Vertex[] V = new Vertex[N];
		for(int i = 0; i < N; i++) {
			V[i] = new Vertex();
		}
		V[source].dist = 0;
		V[source].prev = -1;
		V[source].num = source;
		
		Comparator<Vertex> comparator = new DistanceComparator();
		PriorityQueue<Vertex> Q = new PriorityQueue<Vertex>(10, comparator);
		
		for(int i = 0; i < N; i++) {
			if(i != source) {
				V[i].dist = Double.MAX_VALUE;
				V[i].prev = -1;
				V[i].num = i;
			}
			Q.add(V[i]);
		}
		while(Q.peek() != null) {
			Vertex u = Q.poll();
			for(int i = 0; i < N; i++) {
				if(graph[u.num][i] != 0) {
					double alt = u.dist + graph[u.num][i];
					if(alt < V[i].dist) {
						V[i].dist = alt;
						V[i].prev = u.num;
					}
				}
			}
			Stack<Vertex> st = new Stack<Vertex>();
			while(Q.peek() != null) {
				st.push(Q.poll());
			}
			while(st.isEmpty() != true) {
				Q.add(st.pop());
			}
		}
		Stack<Integer> S = new Stack<Integer>();
		int u = target;
		while(V[u].prev != -1) {
			S.push(u);
			u = V[u].prev;
		}
		return S;
	}
	 
	double min_dist(double[][] graph, int source,int target, int N) {
			
			source =  source - 1;
			target = target - 1;
			Vertex[] V = new Vertex[N];
			for(int i = 0; i < N; i++) {
				V[i] = new Vertex();
			}
			V[source].dist = 0;
			V[source].prev = -1;
			V[source].num = source;
			
			Comparator<Vertex> comparator = new DistanceComparator();
			PriorityQueue<Vertex> Q = new PriorityQueue<Vertex>(10, comparator);
			
			for(int i = 0; i < N; i++) {
				if(i != source) {
					V[i].dist = Double.MAX_VALUE;
					V[i].prev = -1;
					V[i].num = i;
				}
				Q.add(V[i]);
			}
			while(Q.peek() != null) {
				Vertex u = Q.poll();
				for(int i = 0; i < N; i++) {
					if(graph[u.num][i] != 0) {
						double alt = u.dist + graph[u.num][i];
						if(alt < V[i].dist) {
							V[i].dist = alt;
							V[i].prev = u.num;
						}
					}
				}
				Stack<Vertex> st = new Stack<Vertex>();
				while(Q.peek() != null) {
					st.push(Q.poll());
				}
				while(st.isEmpty() != true) {
					Q.add(st.pop());
				}
			}
			
			/*
			Stack<Integer> S = new Stack<Integer>();
			int u = target;
			while(V[u].prev != -1) {
				S.push(u);
				u = V[u].prev;
			}
			*/
			
			double min_dist = V[target].dist;
			
			return min_dist;
			
		}
}

class DistanceComparator implements Comparator<Vertex> {
	@Override
	public int compare(Vertex x, Vertex y) {
		if(x.dist < y.dist) {
			return -1;
		}
		if(x.dist > y.dist) {
			return 1;
		}
		return 0;
	}
	
}
