
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author windows
 */
import java.util.Comparator;
public class Solver {
    private MinPQ<SearchNode> priorityMinPQ;
    private MinPQ<SearchNode> twinPriorityMinPQ;
    private SearchNode lastNode;
    private SearchNode twinLastNode;
    
    private class SearchNode{
        Board board;
        int moves;
        SearchNode previousNode;
        
        public SearchNode(Board currentBoard, int moves, SearchNode previousNode){
            this.board = currentBoard;
            this.moves = moves;
            this.previousNode = previousNode;
        }
    }
    
    private class ByMoves implements Comparator<SearchNode>{
        public int compare(SearchNode a, SearchNode b){
            return (int) Math.signum((a.moves + a.board.manhattan())-(b.moves + b.board.manhattan()));
        }
    }
    public Solver(Board initial){           
        // find a solution to the initial board (using the A* algorithm)
        SearchNode initialNode = new SearchNode(initial, 0, null);
        SearchNode twinInitialNode = new SearchNode(initial.twin(), 0, null);
        priorityMinPQ = new MinPQ(new ByMoves());
        twinPriorityMinPQ = new MinPQ(new ByMoves());
        priorityMinPQ.insert(initialNode);
        twinPriorityMinPQ.insert(twinInitialNode);
        
        SearchNode minNode = priorityMinPQ.delMin();
        SearchNode twinMinNode = twinPriorityMinPQ.delMin();
        while(!minNode.board.isGoal() && !twinMinNode.board.isGoal()){
            for(Board board: minNode.board.neighbors()){
                if(minNode.previousNode == null){
                    priorityMinPQ.insert(new SearchNode(board, minNode.moves+1, minNode));
                }
                else if(!board.equals(minNode.previousNode.board)){
                    priorityMinPQ.insert(new SearchNode(board, minNode.moves+1, minNode));
                }
            }
            for(Board board: twinMinNode.board.neighbors()){
                if(twinMinNode.previousNode == null){
                    twinPriorityMinPQ.insert(new SearchNode(board, twinMinNode.moves+1, twinMinNode));
                }
                else if(!board.equals(twinMinNode.previousNode.board)){
                    twinPriorityMinPQ.insert(new SearchNode(board, twinMinNode.moves+1, twinMinNode));
                }
            }
            minNode = priorityMinPQ.delMin();
            twinMinNode = twinPriorityMinPQ.delMin();
        }
        this.lastNode = minNode;
        this.twinLastNode = twinMinNode;
        
    }
    public boolean isSolvable(){            
        // is the initial board solvable?
        return this.lastNode.board.isGoal();
    }
    public int moves(){                     
        // min number of moves to solve initial board; -1 if unsolvable
        if(this.isSolvable()){
            return this.lastNode.moves;
        }
        else{
            return -1;
        }
    }
    public Iterable<Board> solution(){      
        // sequence of boards in a shortest solution; null if unsolvable
        if(this.isSolvable() == false){
            return null;
        }
        else{
            Stack<Board> stack = new Stack<Board>();
            SearchNode node = this.lastNode;
            while(node != null){
                stack.push(node.board);
                node = node.previousNode;
            }
            return stack;
        }
    }
    public static void main(String[] args){ 
        // solve a slider puzzle (given below)
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        // solve the puzzle
        //System.out.print(initial);
        //System.out.print(initial.twin());
        
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
        
    }
}
