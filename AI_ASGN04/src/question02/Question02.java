/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package question02;

import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * Name      : SP Harish
 * Institute : IIIT Allahabad
 * @author spharish
 */
public class Question02 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try(Scanner in = new Scanner(System.in)) {
            int t = Integer.parseInt(in.nextLine());
            String[] inp = new String[t];
            for(int i = 0; i < t; i++) inp[i] = in.nextLine();
            for(int i = 0; i < t; i++) {
                BFS bfs = new BFS();
                int[][] board = bfs.prepareBoard(inp[i]);
                Node sNode = new Node(board,1,null);
                System.out.println(bfs.decision(sNode));
            }
        }
    }
    
}

class BFS {
    public static boolean goalTest(Node node) {
        
        boolean rslt;
        for (int[] boardState : node.boardState) {
            rslt = true;
            for (int j = 0; j < node.boardState[0].length - 1; j++) {
                if (boardState[j] != boardState[j + 1] || boardState[j] == -1) {
                    rslt = false;
                }
            }
            if(rslt == true) {
                return true;
            }
        }
        
        
        for(int i  = 0; i < node.boardState.length; i++) {
            rslt = true;
            for(int j = 0; j < node.boardState[0].length - 1; j++) {
                if(node.boardState[j][i] != node.boardState[j + 1][i] || node.boardState[j][i] == -1) rslt = false;
            }
            if(rslt == true) {
                return true;
                
            }
        }
        
        rslt = true;
        for(int i  = 0; i < node.boardState.length - 1; i++) {
            if(node.boardState[i][i] != node.boardState[i + 1][i + 1] || node.boardState[i][i] == -1) rslt = false;       
        }
        if(rslt == true) {
            return true;
        }
        
        rslt = true;
        for(int i  = 0; i < node.boardState.length - 1; i++) {
                if(node.boardState[node.boardState.length - i - 1][i] != node.boardState[node.boardState.length - i - 2][i + 1] || node.boardState[node.boardState.length - i - 1][i] == -1) rslt = false;
        }
        return rslt == true;
    }
    
    public int bfs(Node node) {
        ArrayList<Node> fringe = new ArrayList<>();
        fringe.add(node);
        while(!fringe.isEmpty()) {
            Node currNode = fringe.get(0);
            /*for(int i  = 0; i < currNode.boardState.length; i++) {
                for(int j = 0; j < currNode.boardState[0].length; j++) System.out.print(currNode.boardState[i][j]+" ");
            }*/
            //System.out.println();
            fringe.remove(0);
            ArrayList<Node> children = currNode.expandNode();
            if(children != null) {
                for(Node childNode:children) fringe.add(childNode);
            }
            if(currNode.isTerminal()) return currNode.depth;
        }
        
        return -1;
    }
    
    public int[][] prepareBoard(String inp) {
        
        String[] str = inp.split(" ");
        int dimension = Integer.parseInt(str[0]);
        int [][] board = new int[dimension][dimension];
        for(int i = 0; i < dimension; i++) {
            for(int j = 0; j < dimension; j++) {
                if(str[1].charAt(i*dimension + j) == '*') board[i][j] = -1;
                else if(str[1].charAt(i*dimension + j) == '1') board[i][j] = 1;
                else if(str[1].charAt(i*dimension + j) == '2') board[i][j] = 2;  
            }
        }
        return board;
    }
    
    
    public int decision(Node node) {
        if(node.isTerminal()) {
            return Winner.findWinner(node);
        }
        else {
            ArrayList<Node> children = node.expandNode();
            int[] decisions = new int[children.size()];
            for(int i = 0; i < decisions.length; i++) {
                decisions[i] = decision(children.get(i));
            }
            if(node.type == 1) {
                int max = decisions[0];
                for(int i = 0; i < decisions.length; i++)
                    if(decisions[i] > max) max = decisions[i];
                return max;
            }
            else {
                int min = decisions[0];
                for(int i = 0; i < decisions.length; i++)
                    if(decisions[i] < min) min = decisions[i];
                return min;
            }
        }
    }
}

class Node {
    int boardState[][];
    int type;
    Node parent;
    int depth;
    int utility;

    public Node(int[][] boardState, int type, Node parent) {
        this.boardState = boardState;
        this.type = type;
        this.parent = parent;
        if(parent != null) this.depth = parent.depth + 1;
        else this.depth = 0;
    }
    
    ArrayList<Node> expandNode() {
        ArrayList<Node> children = new ArrayList<>();
        for(int i = 0; i < boardState.length; i++) {
            for(int j = 0; j < boardState[0].length; j++) {
                if(boardState[i][j] == -1) {
                    Node childNode;
                    int childState[][] = new int[boardState.length][boardState[0].length];
                    for(int k = 0; k < boardState.length; k++) 
                        System.arraycopy(boardState[k], 0, childState[k], 0, boardState[0].length);                    
                    if(type == 1) {
                      
                        childState[i][j] = 1;
                        childNode = new Node(childState,2,this);
                        children.add(childNode);
                    }
                    else if(type == 2) {
                       
                        childState[i][j] = 2;
                        childNode = new Node(childState,1,this);
                        children.add(childNode);
                    }
                    
                }
            } 
        }
        /*for (Node currNode:children) {
            for(int i  = 0; i < currNode.boardState.length; i++) {
                for(int j = 0; j < currNode.boardState[0].length; j++) System.out.print(currNode.boardState[i][j]+" ");
            }
            System.out.println();
        }*/
        return children;
    }
    
    boolean isTerminal() {
        if(BFS.goalTest(this)) return true;
        else {
            for (int[] boardState1 : this.boardState) {
                for (int j = 0; j < this.boardState[0].length; j++) {
                    if (boardState1[j] == -1) {
                        return false;
                    }
                }
            }
            return true;
        }
    }
        
    
}


class Winner {
    public static int findWinner(Node node) {
        boolean rslt;
        for (int[] boardState : node.boardState) {
            rslt = true;
            for (int j = 0; j < node.boardState[0].length - 1; j++) {
                if (boardState[j] != boardState[j + 1] || boardState[j] == -1) {
                    rslt = false;
                }
            }
            if(rslt == true) {
                if(boardState[0] == 1) return 1;
                else return -1;
            }
        }
        
        
        for(int i  = 0; i < node.boardState.length; i++) {
            rslt = true;
            for(int j = 0; j < node.boardState[0].length - 1; j++) {
                if(node.boardState[j][i] != node.boardState[j + 1][i] || node.boardState[j][i] == -1) rslt = false;
            }
            if(rslt == true) {
                if(node.boardState[0][i] == 1) return 1;
                else return -1;
            }
        }
        
        rslt = true;
        for(int i  = 0; i < node.boardState.length - 1; i++) {
            if(node.boardState[i][i] != node.boardState[i + 1][i + 1] || node.boardState[i][i] == -1) rslt = false;       
        }
        if(rslt == true) {
            if(node.boardState[0][0] == 1) return 1;
                else return -1;
        }
        
        rslt = true;
        for(int i  = 0; i < node.boardState.length - 1; i++) {
                if(node.boardState[node.boardState.length - i - 1][i] != node.boardState[node.boardState.length - i - 2][i + 1] || node.boardState[node.boardState.length - i - 1][i] == -1) rslt = false;
        }
        if(rslt == true) {
            if(node.boardState[node.boardState.length - 1][0] == 1) return 1;
            else return -1;
        }
        
        return 0;
    }
}