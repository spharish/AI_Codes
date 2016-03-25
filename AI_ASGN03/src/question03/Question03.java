/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package question03;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 *
 * Name      : SP Harish
 * Institute : IIIT Allahabad
 * @author spharish
 */
public class Question03 {
    public static void main(String[] args) {
        try(Scanner in = new Scanner(System.in)) {
            int dimX = in.nextInt();
            int dimY = in.nextInt();
            int[][] bitMap = new int[dimX][dimY];
            for(int i = 0 ; i < dimX; i++) {
                for(int j = 0; j < dimY; j++) {
                    bitMap[i][j] = in.nextInt();
                }
            }
            int queries = in.nextInt();
            int[][] query = new int[queries][2];
            for(int i = 0 ; i < queries; i++) {
                query[i][0] = in.nextInt();
                query[i][1] = in.nextInt();
            }
            Environment e = new Environment(bitMap);
            Sensors s = new Sensors(e);
            ProblemSolvingAgent agent  = new ProblemSolvingAgent();
            for(int i = 0 ; i < queries; i++) {
                Position source = new Position(query[i][0], query[i][1]);
                double sum = agent.uniformCostSearch(source, new Position(0,0), s);
                double newsum = agent.uniformCostSearch(source, new Position(0,dimY - 1), s);
                if(newsum < sum) sum = newsum;
                newsum = agent.uniformCostSearch(source, new Position(dimX - 1,dimY - 1), s);
                if(newsum < sum) sum = newsum;
                newsum = agent.uniformCostSearch(source, new Position(dimX - 1,0), s);
                if(newsum < sum) sum = newsum;
                System.out.println((int)(sum + 0.5));
                
            }
        }
    }
    
}

class Environment {

    private final int[][] bitMap;

    public Environment(int[][] bitMap) {
        this.bitMap = bitMap;
    }

    /**
     * @return the bitMap
     */
    public int[][] getBitMap() {
        return bitMap;
    }

}

class Sensors {
    Environment e;
    
    Sensors(Environment e) {
        this.e = e;
    }
    
    int[][] getBitMap() {
        return e.getBitMap();
    }
}


class ProblemSolvingAgent {
    
    public double uniformCostSearch(Position source, Position goal, Sensors s) {
        List<Action> plan  = new ArrayList<>();
        Comparator<Node> comparator = new DistanceComparator();
        PriorityQueue<Node> frontier = new PriorityQueue<>(10, comparator);
        Node start = new Node(source, null, null, 0, 0, ComputeCostOfPath.computeCost(source, goal));
        frontier.add(start);
        Node solution = null;
        int pass = 1;
        while(true) {
            if(frontier.isEmpty()) return -1;
            Node tmp  = frontier.poll();
            if(tmp.getPositionOfAgent().x == goal.x && tmp.getPositionOfAgent().y == goal.y) {
                solution = tmp;
                break;
            }
            List<Node> children = tmp.expandNode(s, goal);
            for(Node child : children) {
                boolean isPresent = false;
                Node alreadyInFrontier = null;
                for(Node front : frontier) {
                    if(child.getPositionOfAgent().x == front.getPositionOfAgent().x && child.getPositionOfAgent().y == front.getPositionOfAgent().y) {
                        isPresent = true;
                        alreadyInFrontier = front;
                    }
                }
                if(isPresent == false) {
                    frontier.add(child);
                    //System.out.print("Pass ->"+pass+" ");
                    //System.out.print(child.getAction()+" ");
                    //System.out.print(child.getPositionOfAgent().x+" "+child.getPositionOfAgent().y+" ");
                    //System.out.println(child.getPathCost());
                    
                }
                else if(alreadyInFrontier != null) {
                    if(alreadyInFrontier.getPathCost() > child.getPathCost()) {
                        frontier.remove(alreadyInFrontier);
                        frontier.add(child);
                       //System.out.print("Pass ->"+pass+" ");
                       //System.out.print(child.getAction()+" ");
                       //System.out.print(child.getPositionOfAgent().x+" "+child.getPositionOfAgent().y+" ");
                       //System.out.println(child.getPathCost());
                    }
                }
            }
            pass++;
        }
        if(solution != null) return solution.getPathCost();
        return -1;
    }
    
    
    
}


class Node {
    private final Position positionOfAgent;
    private final Node parent;
    private final Action action;
    private final double pathCost;
    private final int depth;
    private final double estimatedPathCost;
    
    public Node(Position positionOfAgent, Node parent, Action action, double pathCost, int depth, double estimatedPathCost) {
        this.positionOfAgent = positionOfAgent;
        this.parent = parent;
        this.action = action;
        this.pathCost = pathCost;
        this.depth = depth;
        this.estimatedPathCost = estimatedPathCost;
    }

    /**
     * @return the positionOfAgent
     */
    public Position getPositionOfAgent() {
        return positionOfAgent;
    }

    /**
     * @return the parent
     */
    public Node getParent() {
        return parent;
    }

    /**
     * @return the action
     */
    public Action getAction() {
        return action;
    }

    /**
     * @return the pathCost
     */
    public double getPathCost() {
        return pathCost;
    }
    
    public List<Node> expandNode(Sensors s, Position goal) {
        List<Node> childNodes = new ArrayList<>();
        List<Action> validActions = ValidActions.computLegalActions(s, this.positionOfAgent);
        for(Action actions : validActions) {
            Position newPosition;
            Node newNode;
            switch(actions) {
                case RIGHT:
                    newPosition = new Position(this.positionOfAgent.x, this.positionOfAgent.y + 1);
                    newNode = new Node(newPosition,this, Action.RIGHT, this.getPathCost()+ComputeCostOfPath.computeCost(this.positionOfAgent, newPosition),this.depth + 1, ComputeCostOfPath.computeCost(newPosition, goal));
                    childNodes.add(newNode);
                    break;
                case DOWN_RIGHT:
                    newPosition = new Position(this.positionOfAgent.x + 1, this.positionOfAgent.y + 1);
                    newNode = new Node(newPosition,this, Action.DOWN_RIGHT,this.getPathCost()+ComputeCostOfPath.computeCost(this.positionOfAgent, newPosition),this.depth + 1, ComputeCostOfPath.computeCost(newPosition, goal));
                    childNodes.add(newNode);
                    break;
                case DOWN:
                    newPosition = new Position(this.positionOfAgent.x + 1, this.positionOfAgent.y);
                    newNode = new Node(newPosition,this, Action.DOWN, this.getPathCost()+ComputeCostOfPath.computeCost(this.positionOfAgent, newPosition), this.depth + 1, ComputeCostOfPath.computeCost(newPosition, goal));
                    childNodes.add(newNode);
                    break;
                case DOWN_LEFT:
                    newPosition = new Position(this.positionOfAgent.x + 1, this.positionOfAgent.y - 1);
                    newNode = new Node(newPosition,this, Action.DOWN_LEFT, this.getPathCost()+ComputeCostOfPath.computeCost(this.positionOfAgent, newPosition), this.depth + 1, ComputeCostOfPath.computeCost(newPosition, goal));
                    childNodes.add(newNode);
                    break;
                case LEFT:
                    newPosition = new Position(this.positionOfAgent.x, this.positionOfAgent.y - 1);
                    newNode = new Node(newPosition,this, Action.LEFT, this.getPathCost()+ComputeCostOfPath.computeCost(this.positionOfAgent, newPosition), this.depth + 1, ComputeCostOfPath.computeCost(newPosition, goal));
                    childNodes.add(newNode);
                    break;
                case UP_LEFT:
                    newPosition = new Position(this.positionOfAgent.x - 1, this.positionOfAgent.y - 1);
                    newNode = new Node(newPosition,this, Action.UP_LEFT, this.getPathCost()+ComputeCostOfPath.computeCost(this.positionOfAgent, newPosition),this.depth + 1, ComputeCostOfPath.computeCost(newPosition, goal));
                    childNodes.add(newNode);
                    break;
                case UP:
                    newPosition = new Position(this.positionOfAgent.x - 1, this.positionOfAgent.y);
                    newNode = new Node(newPosition,this, Action.UP, this.getPathCost()+ComputeCostOfPath.computeCost(this.positionOfAgent, newPosition),this.depth + 1, ComputeCostOfPath.computeCost(newPosition, goal));
                    childNodes.add(newNode);
                    break;
                case UP_RIGHT:
                    newPosition = new Position(this.positionOfAgent.x - 1, this.positionOfAgent.y + 1);
                    newNode = new Node(newPosition,this, Action.UP_RIGHT, this.getPathCost()+ComputeCostOfPath.computeCost(this.positionOfAgent, newPosition),this.depth + 1, ComputeCostOfPath.computeCost(newPosition, goal));
                    childNodes.add(newNode);
                    break;
            }
        }
        return childNodes;
    }

    /**
     * @return the estimatedPathCost
     */
    public double getEstimatedPathCost() {
        return estimatedPathCost;
    }
    
        
}

class ValidActions {
    
    static List computLegalActions(Sensors s, Position positionOfAgent) {
        int[][] bitMap = s.getBitMap();
        int dimX = bitMap.length;
        int dimY = bitMap[0].length;
        List<Action> validActions = new ArrayList<>();
        if(positionOfAgent.y < dimY - 1) {
            if(bitMap[positionOfAgent.x][positionOfAgent.y + 1] != 1) validActions.add(Action.RIGHT);
        }
        if(positionOfAgent.x < dimX - 1 && positionOfAgent.y < dimY - 1) {
            if(bitMap[positionOfAgent.x + 1][positionOfAgent.y + 1] != 1) validActions.add(Action.DOWN_RIGHT);
        }
        if(positionOfAgent.x < dimX - 1) {
            if(bitMap[positionOfAgent.x + 1][positionOfAgent.y] != 1) validActions.add(Action.DOWN);
        }
        if(positionOfAgent.x < dimX - 1 && positionOfAgent.y > 0) {
            if(bitMap[positionOfAgent.x + 1][positionOfAgent.y - 1] != 1) validActions.add(Action.DOWN_LEFT);
        }
        if(positionOfAgent.y > 0) {
            if(bitMap[positionOfAgent.x][positionOfAgent.y - 1] != 1) validActions.add(Action.LEFT);
        }
        if(positionOfAgent.x > 0 && positionOfAgent.y > 0) {
            if(bitMap[positionOfAgent.x - 1][positionOfAgent.y - 1] != 1) validActions.add(Action.UP_LEFT);
        }
        if(positionOfAgent.x > 0) {
            if(bitMap[positionOfAgent.x - 1][positionOfAgent.y] != 1) validActions.add(Action.UP);
        }
        if(positionOfAgent.x > 0 && positionOfAgent.y < dimY - 1) {
            if(bitMap[positionOfAgent.x - 1][positionOfAgent.y + 1] != 1) validActions.add(Action.UP_RIGHT);
        }
        return validActions;
    }
    
}

class ComputeCostOfPath {
    static double computeCost(Position p1, Position p2) {
        double cost;
        cost = Math.sqrt(Math.pow((p1.x - p2.x),2) + Math.pow((p1.y - p2.y),2));
        return cost;
    }
}

enum Action {
    RIGHT,DOWN_RIGHT,DOWN,DOWN_LEFT,LEFT,UP_LEFT,UP,UP_RIGHT;
}

class Position {

    int x;
    int y;

    Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
}


class DistanceComparator implements Comparator<Node> {
	
        @Override
	public int compare(Node x, Node y) {
		if(x.getPathCost() + x.getEstimatedPathCost() < y.getPathCost() + y.getEstimatedPathCost()) {
			return -1;
		}
		if(x.getPathCost() + x.getEstimatedPathCost() > y.getPathCost() + y.getEstimatedPathCost()) {
			return 1;
		}
		return 0;
	}
	
}