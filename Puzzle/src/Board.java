
import edu.princeton.cs.algs4.Queue;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author windows
 */
public class Board {
    private int dimension;
    private int[][] blocks;
    
    
    public Board(int[][] blocks){
        // construct a board from an n-by-n array of blocks
        // (where blocks[i][j] = block in row i, column j)
        this.dimension = blocks[0].length;
        this.blocks = new int[dimension][dimension];
        for(int i=0; i<this.dimension; i++){
            for(int j=0; j<this.dimension; j++){
                this.blocks[i][j] = blocks[i][j];
            }
        }
    }
    public int dimension(){
        // board dimension n
        return this.dimension;
    }
    public int hamming(){
        // number of blocks out of place
        int numberOfBlocksOutOfPlace = 0;
        int blockNumber = 1;
        for(int i=0; i<this.dimension; i++){
            for(int j=0; j<this.dimension; j++){
                if(blockNumber != this.blocks[i][j] && blockNumber<=this.dimension*this.dimension-1){
                    numberOfBlocksOutOfPlace++;
                }
                blockNumber++;
            }
        }
        return numberOfBlocksOutOfPlace;
    }
    public int manhattan(){                 
        // sum of Manhattan distances between blocks and goal
        int sumOfNorms = 0;
        for(int i=0; i<this.dimension; i++){
            for(int j=0; j<this.dimension; j++){
                int number = this.blocks[i][j]-1;
                if(number >= 0){
                    int norm = Math.abs(j-(number%this.dimension)) + Math.abs(i-(number/this.dimension));
                    sumOfNorms += norm;
                }
            }
        }
        return sumOfNorms;
    }
    public boolean isGoal(){
        // is this board the goal board?
        return (this.hamming() == 0);
    }
    public Board twin(){
        // a board that is obtained by exchanging any pair of blocks
        Board twinBoard = new Board(blocks);
        if(twinBoard.blocks[0][0] != 0 && twinBoard.blocks[0][1] != 0){
            swap(twinBoard.blocks, 0, 0, 0, 1);
        }
        else if(twinBoard.blocks[0][0] != 0 && twinBoard.blocks[1][0] != 0){
            swap(twinBoard.blocks, 0, 0, 1, 0);
        }
        else if(twinBoard.blocks[0][0] != 0 && twinBoard.blocks[1][1] != 0){
            swap(twinBoard.blocks, 0, 0, 1, 1);
        }
        else{
            swap(twinBoard.blocks, 0, 1, 1, 1);
        }
        return twinBoard;
    }
    public boolean equals(Object y){
        // does this board equal y?
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if(this.dimension != that.dimension){
            return false;
        }
        for(int i=0; i<this.dimension; i++){
            for(int j=0; j<this.dimension; j++){
                if(this.blocks[i][j] != that.blocks[i][j]){
                    return false;
                }
            }
        }
        return true;
    }
    public Iterable<Board> neighbors(){
        // all neighboring boards
        Queue<Board> queue = new Queue<Board>();
        for(int i=0; i<this.dimension; i++){
            for(int j=0; j<this.dimension; j++){
                if(this.blocks[i][j] == 0){
                    for(int k=i-1; k<=i+1; k=k+2){
                        if(k >= 0 && k <= this.dimension-1){
                            Board neighborBoard = new Board(blocks);
                            swap(neighborBoard.blocks, i, j, k, j);
                            queue.enqueue(neighborBoard);
                        }
                    }
                    for(int k=j-1; k<=j+1; k=k+2){
                        if(k >= 0 && k <= this.dimension-1){
                            Board neighborBoard = new Board(blocks);
                            swap(neighborBoard.blocks, i, j, i, k);
                            queue.enqueue(neighborBoard);
                        }
                    }
                    break;
                }
            }
        }
        return queue;
    }
    private void swap(int[][] blocks, int i1, int j1, int i2, int j2){
        int temp = blocks[i1][j1];
        blocks[i1][j1] = blocks[i2][j2];
        blocks[i2][j2] = temp;
    }
    public String toString(){               
        // string representation of this board (in the output format specified below)
        StringBuilder s = new StringBuilder();
        s.append(this.dimension + "\n");
        for (int i = 0; i < this.dimension; i++) {
            for (int j = 0; j < this.dimension; j++) {
                s.append(String.format("%2d ", this.blocks[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }
    public static void main(String[] args){
        // unit tests (not graded)
        int blocks[][]={{1,2},{3,0}};
        Board board = new Board(blocks);
        System.out.println("original");
        System.out.println(board);
        System.out.println("dimension: " + board.dimension());
        System.out.println("hamming: " + board.hamming());
        System.out.println("manhattan: " + board.manhattan());
        System.out.println("isGoal: " + board.isGoal());
        System.out.println("twin");
        System.out.println(board.twin());
        System.out.println("equals: " + board.equals(board.twin()));
        System.out.println("neighbors");
        for(Board temp: board.neighbors()){
            System.out.println(temp);
        }   
    }
}
