
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;
import java.util.TreeSet;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author windows
 */
public class PointSET {
    private TreeSet<Point2D> pointTreeSet;
    public PointSET(){
        // construct an empty set of points 
        this.pointTreeSet = new TreeSet<Point2D>();
    }
    public boolean isEmpty(){  
        // is the set empty? 
        return this.pointTreeSet.isEmpty();
    }
    public int size(){      
        // number of points in the set 
        return this.pointTreeSet.size();
    }
    public void insert(Point2D p){  
        // add the point to the set (if it is not already in the set)
        if(p == null){
            throw new IllegalArgumentException();
        }
        if(this.pointTreeSet.contains(p) == false){
            this.pointTreeSet.add(p);
        }
    }
    public boolean contains(Point2D p){   
        // does the set contain point p?
        if(p == null){
            throw new IllegalArgumentException();
        }
        return this.pointTreeSet.contains(p);
    }
    public void draw(){                  
        // draw all points to standard draw 
        StdDraw.enableDoubleBuffering();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        StdDraw.setXscale(0, 1);
        StdDraw.setYscale(0, 1);
        for (Point2D p : this.pointTreeSet){
            p.draw();
        }
        StdDraw.show();
    }
    public Iterable<Point2D> range(RectHV rect){
        if(rect == null){
            throw new IllegalArgumentException();
        }
        // all points that are inside the rectangle (or on the boundary)
        Stack<Point2D> pointStack = new Stack<Point2D>();
        for (Point2D p : this.pointTreeSet){
            if(rect.contains(p)){
                pointStack.push(p);
            }
        }
        return pointStack;
    }
    public Point2D nearest(Point2D p){
        if(p == null){
            throw new IllegalArgumentException();
        }
        // a nearest neighbor in the set to point p; null if the set is empty
        if(this.pointTreeSet.isEmpty()){
            return null;
        }
        Point2D nearest = this.pointTreeSet.first();
        for (Point2D point : this.pointTreeSet){
            if(point.distanceTo(p) < nearest.distanceTo(p)){
                nearest = point;
            }
        }
        return nearest;
    }
    public static void main(String[] args){   
        // unit testing of the methods (optional) 
    }    
}
