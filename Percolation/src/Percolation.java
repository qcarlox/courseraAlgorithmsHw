
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author windows
 */
public class Percolation {
    private boolean[][] grid;
    private int size;
    private WeightedQuickUnionUF weightedQuickUnionUF;
    private int[][] siteIndex;
    public Percolation(int n){
        if(n <= 0){
            throw new IllegalArgumentException();
        }
        // create n-by-n grid, with all sites blocked
        this.size = n;
        this.grid = new boolean[n][n];
        this.siteIndex = new int[n][n];
        int k=1;
        for(int row=0; row<this.size; row++){
            for(int column=0; column<this.size; column++){
                this.grid[row][column] = false;
                this.siteIndex[row][column] = k;
                k++;
            }
        }
        this.weightedQuickUnionUF = new WeightedQuickUnionUF(n*n+2);
    }   
    public void open(int row, int col){//input 1 indexed
        if(row < 1 || row > this.size || col < 1 || col > this.size){
            throw new IllegalArgumentException();
        }
        this.grid[row-1][col-1] = true;
        connectToNeighbours(row, col);
    }
    private void connectToNeighbours(int row, int col){//input 1 indexed
        
        for(int i=row-1; i<= row+1; i= i+2){
            if(i>=1 && i<=this.size && col>=1 && col<=this.size){
                if(isOpen(i, col)){
                    this.weightedQuickUnionUF.union(this.siteIndex[i-1][col-1], this.siteIndex[row-1][col-1]);
                }
            }
        }
        for(int j=col-1; j<= col+1; j= j+2){
            if(row>=1 && row<=this.size && j>=1 && j<=this.size){
                if(isOpen(row, j)){
                    this.weightedQuickUnionUF.union(this.siteIndex[row-1][j-1], this.siteIndex[row-1][col-1]);
                }
            }
        }
        if(row == 1){
            this.weightedQuickUnionUF.union(this.siteIndex[row-1][col-1], 0);
        }
        if(row == this.size){
            this.weightedQuickUnionUF.union(this.siteIndex[row-1][col-1], this.size*this.size+1);
        }
        
    }
    public boolean isOpen(int row, int col){// 1 indexed
       // is site (row, col) open?
        if(row < 1 || row > this.size || col < 1 || col > this.size){
            throw new IllegalArgumentException();
        }
        return this.grid[row-1][col-1];
    }
    public boolean isFull(int row, int col){
       // is site (row, col) full?
        if(row < 1 || row > this.size || col < 1 || col > this.size){
            throw new IllegalArgumentException();
        }
        return this.weightedQuickUnionUF.connected(0,this.siteIndex[row-1][col-1]);
    }
    public int numberOfOpenSites(){
        // number of open sites
        int numberOfOpenSites = 0;
        for(int row=1; row<=this.size; row++){
            for(int column=1; column<=this.size; column++){
                if(isOpen(row, column)){
                    numberOfOpenSites++;
                }
            }
        }
        return numberOfOpenSites;
    }
    public boolean percolates(){
        // does the system percolate?
        return this.weightedQuickUnionUF.connected(0, this.size*this.size+1);
    }
    public static void main(String[] args){
        // test client (optional)
        int n = 10;
        Percolation percolation = new Percolation(n);
        int openedSites = 0;
        while(!percolation.percolates()){
            int row = StdRandom.uniform(n)+1;
            int col = StdRandom.uniform(n)+1;
            //System.out.println("row: " + row);
            //System.out.println("col: " + col);
            if(!percolation.isOpen(row, col)){
                percolation.open(row, col);
                openedSites++;
            }
        }
        System.out.println("opened sites: " + openedSites);
        System.out.println("Total sites: " + n*n);
        System.out.println("Percentage: " + (double)openedSites/(n*n));
        
    }
}
