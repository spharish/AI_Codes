/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package value.iteration;

import java.util.Scanner;

/**
 *
 * Name      : SP Harish
 * Institute : IIIT Allahabad
 * @author spharish
 */
public class ValueIteration {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try (Scanner in = new Scanner(System.in)) {
            int t = in.nextInt();
            Input[] input = new Input[t];
            for (int i = 0; i < t; i++) {
                int dimX = in.nextInt();
                int dimY = in.nextInt();
                double reward = in.nextDouble();
                int[][] map = new int[dimX][dimY];
                for (int j = 0; j < dimX; j++) {
                    for (int k = 0; k < dimY; k++) {
                        map[j][k] = in.nextInt();
                    }
                }
                input[i] = new Input(map, reward);
            }
            for(int  i = 0; i < t; i++) {
                Environment e = new Environment(input[i].getInpMatrix(), input[i].getReward());
                Agent a = new Agent();
                a.valueIteration(e, 1, 0.001);
            }

        }
    }

}

class State {
    
    private final int x;
    private final int y;

    public State(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return the x
     */
    public int getX() {
        return x;
    }

    /**
     * @return the y
     */
    public int getY() {
        return y;
    }
}

class Input {

    private int[][] inpMatrix;
    private double reward;

    public Input(int[][] inpMatrix, double reward) {
        this.inpMatrix = inpMatrix;
        this.reward = reward;
    }

    /**
     * @return the inpMatrix
     */
    public int[][] getInpMatrix() {
        return inpMatrix;
    }

    /**
     * @param inpMatrix the inpMatrix to set
     */
    public void setInpMatrix(int[][] inpMatrix) {
        this.inpMatrix = inpMatrix;
    }

    /**
     * @return the reward
     */
    public double getReward() {
        return reward;
    }

    /**
     * @param reward the reward to set
     */
    public void setReward(double reward) {
        this.reward = reward;
    }

}

class Environment {

    int[][] map;
    double reward;

    public Environment(int[][] map, double reward) {
        this.map = map;
        this.reward = reward;
    }

    public State move(State s, Action act) {
        if (act == Action.d) {
            if (s.getX() + 1 < map.length) {
                if (map[s.getX() + 1][s.getY()] != -100) {
                    return new State(s.getX() + 1, s.getY());
                }
            }
            return s;
        } else if (act == Action.u) {
            if (s.getX() - 1 >= 0) {
                if (map[s.getX() - 1][s.getY()] != -100) {
                    return new State(s.getX() - 1, s.getY());
                }
            }
            return s;
        } else if (act == Action.r) {
            if (s.getY() + 1 < map[0].length) {
                if (map[s.getX()][s.getY() + 1] != -100) {
                    return new State(s.getX(), s.getY() + 1);
                }
            }
            return s;
        } else {
            if (s.getY() - 1 >= 0) {
                if (map[s.getX()][s.getY() - 1] != -100) {
                    return new State(s.getX(), s.getY() - 1);
                }
            }
            return s;
        }
    }

}

class Agent {

    public void valueIteration(Environment e, double gamma, double epsilon) {
        double[][] v = new double[e.map.length][e.map[0].length];
        double[][] vNew = new double[e.map.length][e.map[0].length];

        double delta;

        //Initialize v
        for (int i = 0; i < v.length; i++) {
            for (int j = 0; j < v[0].length; j++) {
                v[i][j] = 0;
                vNew[i][j] = 0;
            }
        }

        //Iterate - 
        while (true) {
            
            delta = 0;
            
            for (int i = 0; i < e.map.length; i++) {
                for (int j = 0; j < e.map[0].length; j++) {
                    if (e.map[i][j] == 0) {
                        vNew[i][j] = e.reward + gamma*maxUtilityNeighbor(i, j, e, v);
                    }
                    else {
                        vNew[i][j] = e.map[i][j];
                    }
                }
            }
            
            for(int i = 0; i < e.map.length; i++) {
                for(int j = 0; j < e.map[0].length; j++) {
                    delta += Math.abs(vNew[i][j] - v[i][j]);
                }
            }
            
            if(delta <= epsilon*gamma*(1 - gamma)) break;
            else {
                for(int i = 0; i < v.length; i++)
                    System.arraycopy(vNew[i], 0, v[i], 0, v[0].length);
            }
        }
        
        for(double[] row:v) {
            for(int i = 0; i < row.length; i++) System.out.printf("%.2f ",(Math.round(row[i]*100)/100.0));
            System.out.println();
        }
        
        for(int i = 0; i < v.length; i++) {
            for(int j = 0; j < v[0].length; j++) {
                if(e.map[i][j] == 0) {
                    System.out.print(argmaxUtilityNeighbor(i, j, e, v)+" ");
                }
                else {
                    System.out.print("- ");
                }
            }
            System.out.println();
        }

    }

    public double maxUtilityNeighbor(int i, int j, Environment e, double[][] v) {
        State currentState = new State(i, j);
        double[] val = new double[4];

        State s1;
        State s2;
        State s3;

        //Try to move up
        s1 = e.move(currentState, Action.u);
        s2 = e.move(currentState, Action.r);
        s3 = e.move(currentState, Action.l);
        val[0] = 0.8*v[s1.getX()][s1.getY()] + 0.1*v[s2.getX()][s2.getY()] + 0.1*v[s3.getX()][s3.getY()];
        
        //Try to move down
        s1 = e.move(currentState, Action.d);
        s2 = e.move(currentState, Action.r);
        s3 = e.move(currentState, Action.l);
        val[1] = 0.8*v[s1.getX()][s1.getY()] + 0.1*v[s2.getX()][s2.getY()] + 0.1*v[s3.getX()][s3.getY()];
        
        //Try to move right
        s1 = e.move(currentState, Action.r);
        s2 = e.move(currentState, Action.u);
        s3 = e.move(currentState, Action.d);
        val[2] = 0.8*v[s1.getX()][s1.getY()] + 0.1*v[s2.getX()][s2.getY()] + 0.1*v[s3.getX()][s3.getY()];
        
        //Try to move left
        s1 = e.move(currentState, Action.l);
        s2 = e.move(currentState, Action.u);
        s3 = e.move(currentState, Action.d);
        val[3] = 0.8*v[s1.getX()][s1.getY()] + 0.1*v[s2.getX()][s2.getY()] + 0.1*v[s3.getX()][s3.getY()];
        
        double max = val[0];
        for(int k = 1; k < val.length; k++) if(val[k] > max) max = val[k];
             
        return max;
    }
    
    
    public Action argmaxUtilityNeighbor(int i, int j, Environment e, double[][] v) {
        State currentState = new State(i, j);
        double[] val = new double[4];

        State s1;
        State s2;
        State s3;
        
                
        //Try to move down
        s1 = e.move(currentState, Action.d);
        s2 = e.move(currentState, Action.r);
        s3 = e.move(currentState, Action.l);
        val[0] = 0.8*v[s1.getX()][s1.getY()] + 0.1*v[s2.getX()][s2.getY()] + 0.1*v[s3.getX()][s3.getY()];

        //Try to move up
        s1 = e.move(currentState, Action.u);
        s2 = e.move(currentState, Action.r);
        s3 = e.move(currentState, Action.l);
        val[1] = 0.8*v[s1.getX()][s1.getY()] + 0.1*v[s2.getX()][s2.getY()] + 0.1*v[s3.getX()][s3.getY()];

        
        //Try to move right
        s1 = e.move(currentState, Action.r);
        s2 = e.move(currentState, Action.u);
        s3 = e.move(currentState, Action.d);
        val[2] = 0.8*v[s1.getX()][s1.getY()] + 0.1*v[s2.getX()][s2.getY()] + 0.1*v[s3.getX()][s3.getY()];
        
        //Try to move left
        s1 = e.move(currentState, Action.l);
        s2 = e.move(currentState, Action.u);
        s3 = e.move(currentState, Action.d);
        val[3] = 0.8*v[s1.getX()][s1.getY()] + 0.1*v[s2.getX()][s2.getY()] + 0.1*v[s3.getX()][s3.getY()];
        
        double max = val[0];
        int act = 0;
        for(int k = 1; k < val.length; k++) 
            if(val[k] > max) {
                max = val[k];
                act = k;
            }
        
        if(act == 0) return Action.d;
        else if(act == 1) return Action.u;
        else if(act == 2) return Action.r;
        else return Action.l;
    }
}

enum Action {
    d, u, r, l;
}
