/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package question06;

import com.sun.javafx.scene.control.skin.VirtualFlow;
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
public class Question06 {

    public static void main(String[] args) {
        try (Scanner in = new Scanner(System.in)) {
            int dimX = in.nextInt();
            int dimY = in.nextInt();
            int[][] bitMap = new int[dimX][dimY];
            for (int i = 0; i < dimX; i++) {
                for (int j = 0; j < dimY; j++) {
                    bitMap[i][j] = in.nextInt();
                }
            }
            int queries = in.nextInt();
            int[][] query = new int[queries][2];
            for (int i = 0; i < queries; i++) {
                query[i][0] = in.nextInt();
                query[i][1] = in.nextInt();
            }
            Environment e = new Environment(bitMap);
            Sensors s = new Sensors(e);
            ProblemSolvingAgent agent = new ProblemSolvingAgent();
            double[] sum = new double[24];
            Position a = new Position(0, 0);
            Position b = new Position(0, dimY - 1);
            Position c = new Position(dimX - 1, dimY - 1);
            Position d = new Position(dimX - 1, 0);
            Position[][] paths = new Position[24][4];
            paths[0][0] = d; paths[0][1] = a; paths[0][2] = b; paths[0][3] = c;
            paths[1][0] = d; paths[1][1] = a; paths[1][2] = c; paths[1][3] = b;
            paths[2][0] = d; paths[2][1] = b; paths[2][2] = a; paths[2][3] = c;
            paths[3][0] = d; paths[3][1] = b; paths[3][2] = c; paths[3][3] = a;
            paths[4][0] = d; paths[4][1] = c; paths[4][2] = a; paths[4][3] = b;
            paths[5][0] = d; paths[5][1] = c; paths[5][2] = b; paths[5][3] = a;
            paths[6][0] = a; paths[6][1] = d; paths[6][2] = b; paths[6][3] = c;
            paths[7][0] = a; paths[7][1] = d; paths[7][2] = b; paths[7][3] = c;
            paths[8][0] = b; paths[8][1] = d; paths[8][2] = a; paths[8][3] = c;
            paths[9][0] = b; paths[9][1] = d; paths[9][2] = c; paths[9][3] = a;
            paths[10][0] = c; paths[10][1] = d; paths[10][2] = a; paths[10][3] = b;
            paths[11][0] = c; paths[11][1] = d; paths[11][2] = b; paths[11][3] = a;
            paths[12][0] = a; paths[12][1] = b; paths[12][2] = d; paths[12][3] = c;
            paths[13][0] = a; paths[13][1] = c; paths[13][2] = d; paths[13][3] = b;
            paths[14][0] = b; paths[14][1] = a; paths[14][2] = d; paths[14][3] = c;
            paths[15][0] = b; paths[15][1] = c; paths[15][2] = d; paths[15][3] = a;
            paths[16][0] = c; paths[16][1] = a; paths[16][2] = d; paths[16][3] = b;
            paths[17][0] = c; paths[17][1] = b; paths[17][2] = d; paths[17][3] = a;
            paths[18][0] = a; paths[18][1] = b; paths[18][2] = c; paths[18][3] = d;
            paths[19][0] = a; paths[19][1] = c; paths[19][2] = b; paths[19][3] = d;
            paths[20][0] = b; paths[20][1] = a; paths[20][2] = c; paths[20][3] = d;
            paths[21][0] = b; paths[21][1] = c; paths[21][2] = a; paths[21][3] = d;
            paths[22][0] = c; paths[22][1] = a; paths[22][2] = b; paths[22][3] = d;
            paths[23][0] = c; paths[23][1] = b; paths[23][2] = a; paths[23][3] = d;
            
            
            for (int i = 0; i < queries; i++) {
                Position source = new Position(query[i][0], query[i][1]);
                double dist = Double.MAX_VALUE;
                for(int j = 0; j < 24; j++) {
                    double newDist = agent.uniformCostSearch(source, paths[j][0], s)+agent.uniformCostSearch(paths[j][0], paths[j][1], s)+agent.uniformCostSearch(paths[j][1], paths[j][2], s)+agent.uniformCostSearch(paths[j][2], paths[j][3], s);
                    if(newDist < dist) dist = newDist;
                }
                System.out.println((int)(dist + 0.5));

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
        List<Action> plan = new ArrayList<>();
        Comparator<Node> comparator = new DistanceComparator();
        PriorityQueue<Node> frontier = new PriorityQueue<>(10, comparator);
        Node start = new Node(source, null, null, 0, 0, ComputeCostOfPath.computeCost(source, goal));
        frontier.add(start);
        Node solution = null;
        int pass = 1;
        while (true) {
            if (frontier.isEmpty()) {
                return -1;
            }
            Node tmp = frontier.poll();
            if (tmp.getPositionOfAgent().x == goal.x && tmp.getPositionOfAgent().y == goal.y) {
                solution = tmp;
                break;
            }
            List<Node> children = tmp.expandNode(s, goal);
            for (Node child : children) {
                boolean isPresent = false;
                Node alreadyInFrontier = null;
                for (Node front : frontier) {
                    if (child.getPositionOfAgent().x == front.getPositionOfAgent().x && child.getPositionOfAgent().y == front.getPositionOfAgent().y) {
                        isPresent = true;
                        alreadyInFrontier = front;
                    }
                }
                if (isPresent == false) {
                    frontier.add(child);
                    //System.out.print("Pass ->"+pass+" ");
                    //System.out.print(child.getAction()+" ");
                    //System.out.print(child.getPositionOfAgent().x+" "+child.getPositionOfAgent().y+" ");
                    //System.out.println(child.getPathCost());

                } else if (alreadyInFrontier != null) {
                    if (alreadyInFrontier.getPathCost() > child.getPathCost()) {
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
        if (solution != null) {
            return solution.getPathCost();
        }
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
        for (Action actions : validActions) {
            Position newPosition;
            Node newNode;
            switch (actions) {
                case RIGHT:
                    newPosition = new Position(this.positionOfAgent.x, this.positionOfAgent.y + 1);
                    newNode = new Node(newPosition, this, Action.RIGHT, this.getPathCost() + ComputeCostOfPath.computeCost(this.positionOfAgent, newPosition), this.depth + 1, ComputeCostOfPath.computeCost(newPosition, goal));
                    childNodes.add(newNode);
                    break;
                case DOWN_RIGHT:
                    newPosition = new Position(this.positionOfAgent.x + 1, this.positionOfAgent.y + 1);
                    newNode = new Node(newPosition, this, Action.DOWN_RIGHT, this.getPathCost() + ComputeCostOfPath.computeCost(this.positionOfAgent, newPosition), this.depth + 1, ComputeCostOfPath.computeCost(newPosition, goal));
                    childNodes.add(newNode);
                    break;
                case DOWN:
                    newPosition = new Position(this.positionOfAgent.x + 1, this.positionOfAgent.y);
                    newNode = new Node(newPosition, this, Action.DOWN, this.getPathCost() + ComputeCostOfPath.computeCost(this.positionOfAgent, newPosition), this.depth + 1, ComputeCostOfPath.computeCost(newPosition, goal));
                    childNodes.add(newNode);
                    break;
                case DOWN_LEFT:
                    newPosition = new Position(this.positionOfAgent.x + 1, this.positionOfAgent.y - 1);
                    newNode = new Node(newPosition, this, Action.DOWN_LEFT, this.getPathCost() + ComputeCostOfPath.computeCost(this.positionOfAgent, newPosition), this.depth + 1, ComputeCostOfPath.computeCost(newPosition, goal));
                    childNodes.add(newNode);
                    break;
                case LEFT:
                    newPosition = new Position(this.positionOfAgent.x, this.positionOfAgent.y - 1);
                    newNode = new Node(newPosition, this, Action.LEFT, this.getPathCost() + ComputeCostOfPath.computeCost(this.positionOfAgent, newPosition), this.depth + 1, ComputeCostOfPath.computeCost(newPosition, goal));
                    childNodes.add(newNode);
                    break;
                case UP_LEFT:
                    newPosition = new Position(this.positionOfAgent.x - 1, this.positionOfAgent.y - 1);
                    newNode = new Node(newPosition, this, Action.UP_LEFT, this.getPathCost() + ComputeCostOfPath.computeCost(this.positionOfAgent, newPosition), this.depth + 1, ComputeCostOfPath.computeCost(newPosition, goal));
                    childNodes.add(newNode);
                    break;
                case UP:
                    newPosition = new Position(this.positionOfAgent.x - 1, this.positionOfAgent.y);
                    newNode = new Node(newPosition, this, Action.UP, this.getPathCost() + ComputeCostOfPath.computeCost(this.positionOfAgent, newPosition), this.depth + 1, ComputeCostOfPath.computeCost(newPosition, goal));
                    childNodes.add(newNode);
                    break;
                case UP_RIGHT:
                    newPosition = new Position(this.positionOfAgent.x - 1, this.positionOfAgent.y + 1);
                    newNode = new Node(newPosition, this, Action.UP_RIGHT, this.getPathCost() + ComputeCostOfPath.computeCost(this.positionOfAgent, newPosition), this.depth + 1, ComputeCostOfPath.computeCost(newPosition, goal));
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
        if (positionOfAgent.y < dimY - 1) {
            if (bitMap[positionOfAgent.x][positionOfAgent.y + 1] != 1) {
                validActions.add(Action.RIGHT);
            }
        }
        if (positionOfAgent.x < dimX - 1 && positionOfAgent.y < dimY - 1) {
            if (bitMap[positionOfAgent.x + 1][positionOfAgent.y + 1] != 1) {
                validActions.add(Action.DOWN_RIGHT);
            }
        }
        if (positionOfAgent.x < dimX - 1) {
            if (bitMap[positionOfAgent.x + 1][positionOfAgent.y] != 1) {
                validActions.add(Action.DOWN);
            }
        }
        if (positionOfAgent.x < dimX - 1 && positionOfAgent.y > 0) {
            if (bitMap[positionOfAgent.x + 1][positionOfAgent.y - 1] != 1) {
                validActions.add(Action.DOWN_LEFT);
            }
        }
        if (positionOfAgent.y > 0) {
            if (bitMap[positionOfAgent.x][positionOfAgent.y - 1] != 1) {
                validActions.add(Action.LEFT);
            }
        }
        if (positionOfAgent.x > 0 && positionOfAgent.y > 0) {
            if (bitMap[positionOfAgent.x - 1][positionOfAgent.y - 1] != 1) {
                validActions.add(Action.UP_LEFT);
            }
        }
        if (positionOfAgent.x > 0) {
            if (bitMap[positionOfAgent.x - 1][positionOfAgent.y] != 1) {
                validActions.add(Action.UP);
            }
        }
        if (positionOfAgent.x > 0 && positionOfAgent.y < dimY - 1) {
            if (bitMap[positionOfAgent.x - 1][positionOfAgent.y + 1] != 1) {
                validActions.add(Action.UP_RIGHT);
            }
        }
        return validActions;
    }

}

class ComputeCostOfPath {

    static double computeCost(Position p1, Position p2) {
        double cost;
        cost = Math.sqrt(Math.pow((p1.x - p2.x), 2) + Math.pow((p1.y - p2.y), 2));
        return cost;
    }
}

enum Action {

    RIGHT, DOWN_RIGHT, DOWN, DOWN_LEFT, LEFT, UP_LEFT, UP, UP_RIGHT;
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
        if (x.getPathCost() + x.getEstimatedPathCost() < y.getPathCost() + y.getEstimatedPathCost()) {
            return -1;
        }
        if (x.getPathCost() + x.getEstimatedPathCost() > y.getPathCost() + y.getEstimatedPathCost()) {
            return 1;
        }
        return 0;
    }

}
