
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
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
public class SAP {
    private Digraph graph;
    private int lastV;
    private int lastW;
    private Iterable<Integer> lastIterableV;
    private Iterable<Integer> lastIterableW;
    private int ancestor;
    private int length;
    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G){
        if(G == null){
            throw new IllegalArgumentException();
        }
        this.graph = new Digraph(G);
        this.lastV = -1;
        this.lastW = -1;
        this.lastIterableV = null;
        this.lastIterableW = null;
    }
    private void findLengthAndAncestor(BreadthFirstDirectedPaths bfsV, BreadthFirstDirectedPaths bfsW){
        int shortestPath = Integer.MAX_VALUE;
        int ancestor = -1;
        for(int k=0; k<this.graph.V(); k++){
            if(bfsV.hasPathTo(k) && bfsW.hasPathTo(k)){
                int distance = bfsV.distTo(k) + bfsW.distTo(k);
                if(distance < shortestPath){
                    shortestPath = distance;
                    ancestor = k;
                }
            }
        }
        if(shortestPath == Integer.MAX_VALUE){
            this.length =  -1;
        }
        else{
            this.length =  shortestPath;
        }
        this.ancestor = ancestor;
    }
    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w){
        if(v < 0 || v >= this.graph.V() || w < 0 || w >= this.graph.V()){
            throw new IllegalArgumentException();
        }
        this.lastIterableV = null;
        this.lastIterableW = null;
        if(v != lastV || w != lastW){
            BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(this.graph, v);
            BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(this.graph, w);
            this.findLengthAndAncestor(bfsV, bfsW);
        }
        return this.length;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w){
        if(v < 0 || v >= this.graph.V() || w < 0 || w >= this.graph.V()){
            throw new IllegalArgumentException();
        }
        this.lastIterableV = null;
        this.lastIterableW = null;
        if(v != lastV || w != lastW){
            BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(this.graph, v);
            BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(this.graph, w);
            this.findLengthAndAncestor(bfsV, bfsW);
        }
        return this.ancestor;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w){
        if(v == null || w == null){
            throw new IllegalArgumentException();
        }
        this.lastV = -1;
        this.lastW = -1;
        if(v != lastIterableV || w != lastIterableW){
            BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(this.graph, v);
            BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(this.graph, w);
            this.findLengthAndAncestor(bfsV, bfsW);
        }
        return this.length;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w){
        if(v == null || w == null){
            throw new IllegalArgumentException();
        }
        this.lastV = -1;
        this.lastW = -1;
        if(v != lastIterableV || w != lastIterableW){
            BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(this.graph, v);
            BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(this.graph, w);
            this.findLengthAndAncestor(bfsV, bfsW);
        }
        return this.ancestor;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        
        
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
        
    }
}