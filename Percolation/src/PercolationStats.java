
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author windows
 */
public class PercolationStats{
    private int gridSize;
    private int numberOfTrials;
    private double[] resultsFromTrials;
    public PercolationStats(int n, int trials){
        // perform trials independent experiments on an n-by-n grid
        this.gridSize = n;
        this.numberOfTrials = trials;
        this.resultsFromTrials = new double[trials];
        for(int i=0; i<this.numberOfTrials; i++){
            this.resultsFromTrials[i] = this.runTrial();
        }
    }
    public double mean(){                          // sample mean of percolation threshold
        return StdStats.mean(this.resultsFromTrials);
    }
    public double stddev(){                        // sample standard deviation of percolation threshold
        return StdStats.stddev(this.resultsFromTrials);
    }
    public double confidenceLo(){                  // low  endpoint of 95% confidence interval
        return this.mean()-1.96*this.stddev()/Math.sqrt(this.numberOfTrials);
    }
    public double confidenceHi(){                  // high endpoint of 95% confidence interval
        return this.mean()+1.96*this.stddev()/Math.sqrt(this.numberOfTrials);
    }
    public static void main(String[] args){
        // test client (described below)
        int n = StdIn.readInt();
        int trials = StdIn.readInt();
        PercolationStats percolationStats = new PercolationStats(n, trials);
        StdOut.println("mean = " + percolationStats.mean());
        StdOut.println("stddev = " + percolationStats.stddev());
        StdOut.println("95% confidence interval = [" + percolationStats.confidenceLo() + ", " + percolationStats.confidenceHi() + "]");
    }
    private double runTrial(){
        Percolation percolation = new Percolation(this.gridSize);
        int openedSites = 0;
        while(!percolation.percolates()){
            int row = StdRandom.uniform(this.gridSize)+1;
            int col = StdRandom.uniform(this.gridSize)+1;
            //System.out.println("row: " + row);
            //System.out.println("col: " + col);
            if(!percolation.isOpen(row, col)){
                percolation.open(row, col);
                openedSites++;
            }
        }
        return (double)openedSites/(this.gridSize*this.gridSize);
    }
}
