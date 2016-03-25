/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bidirectional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


/**
 *
 * Name      : SP Harish
 * Institute : IIIT Allahabad
 * @author spharish
 */
public class Test {
    
}

class BidirectionalSearch {
    public static boolean bidirectionalSearch() {
        boolean result = false;
        return result;
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
                
                case DOWN:
                    newPosition = new Position(this.positionOfAgent.x + 1, this.positionOfAgent.y);
                    newNode = new Node(newPosition,this, Action.DOWN, this.getPathCost()+ComputeCostOfPath.computeCost(this.positionOfAgent, newPosition), this.depth + 1, ComputeCostOfPath.computeCost(newPosition, goal));
                    childNodes.add(newNode);
                    break;
                
                case LEFT:
                    newPosition = new Position(this.positionOfAgent.x, this.positionOfAgent.y - 1);
                    newNode = new Node(newPosition,this, Action.LEFT, this.getPathCost()+ComputeCostOfPath.computeCost(this.positionOfAgent, newPosition), this.depth + 1, ComputeCostOfPath.computeCost(newPosition, goal));
                    childNodes.add(newNode);
                    break;
                
                case UP:
                    newPosition = new Position(this.positionOfAgent.x - 1, this.positionOfAgent.y);
                    newNode = new Node(newPosition,this, Action.UP, this.getPathCost()+ComputeCostOfPath.computeCost(this.positionOfAgent, newPosition),this.depth + 1, ComputeCostOfPath.computeCost(newPosition, goal));
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
        if(positionOfAgent.x < dimX - 1) {
            if(bitMap[positionOfAgent.x + 1][positionOfAgent.y] != 1) validActions.add(Action.DOWN);
        }
        if(positionOfAgent.y > 0) {
            if(bitMap[positionOfAgent.x][positionOfAgent.y - 1] != 1) validActions.add(Action.LEFT);
        }
        if(positionOfAgent.x > 0) {
            if(bitMap[positionOfAgent.x - 1][positionOfAgent.y] != 1) validActions.add(Action.UP);
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
    RIGHT,DOWN,LEFT,UP;
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