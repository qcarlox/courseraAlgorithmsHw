
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
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
public class KdTree {
    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
        
        public Node(Point2D p, RectHV rect, Node lb, Node rt){
            this.p = p;
            this.rect = rect;
            this.lb = lb;
            this.rt = rt;
        }
    }
    private Node root;
    private int size;
    public KdTree(){
        // construct an empty set of points 
        this.root = null;
        this.size = 0;
    }
    public boolean isEmpty(){  
        // is the set empty? 
        return this.root == null;
    }
    public int size(){      
        // number of points in the set 
        return this.size;
    }
    public void insert(Point2D p){
        if(p == null){
            throw new IllegalArgumentException();
        }
        // add the point to the set (if it is not already in the set)
        if(this.contains(p) == false){
            this.root = insert(root, p, 0, new RectHV(0, 0, 1, 1));
        }
    }
    private Node insert(Node h, Point2D p, int dimension, RectHV rect){
        if(h == null){
            this.size++;
            return new Node(p, rect, null, null);
        }
        if(p.equals(h.p)){
            return h;
        }
        if(dimension == 0){
            if(p.x() < h.p.x()){
                //RectHV(rect.xmin(), rect.ymin(), rect.xmax(), rect.ymax());
                RectHV newRect = new RectHV(rect.xmin(), rect.ymin(), h.p.x(), rect.ymax());
                h.lb = insert(h.lb, p, (dimension+1)%2, newRect);
            }
            else{
                RectHV newRect = new RectHV(h.p.x(), rect.ymin(), rect.xmax(), rect.ymax());
                h.rt = insert(h.rt, p, (dimension+1)%2, newRect);
            }
        }
        else{
            if(p.y() < h.p.y()){
                RectHV newRect = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), h.p.y());
                h.lb = insert(h.lb, p, (dimension+1)%2, newRect);
            }
            else{
                RectHV newRect = new RectHV(rect.xmin(), h.p.y(), rect.xmax(), rect.ymax());
                h.rt = insert(h.rt, p, (dimension+1)%2, newRect);
            }
        }
        return h;
    }
    public boolean contains(Point2D p){
        if(p == null){
            throw new IllegalArgumentException();
        }
        return contains(root, p, 0);
    }
    private boolean contains(Node h, Point2D p, int dimension){   
        // does the set contain point p?
        if(h == null){
            return false;
        }
        if(p.equals(h.p)){
            return true;
        }
        if(dimension == 0){
            if(p.x() < h.p.x()){
                return contains(h.lb, p, (dimension+1)%2);
            }
            else{
                return contains(h.rt, p, (dimension+1)%2);
            }
        }
        else{
            if(p.y() < h.p.y()){
                return contains(h.lb, p, (dimension+1)%2);
            }
            else{
                return contains(h.rt, p, (dimension+1)%2);
            }
        }
    }
    public void draw(){                  
        // draw all points to standard draw 
        draw(root, 0);
    }
    private void draw(Node h, int dimension){   
        // does the set contain point p?
        if(h == null){
            return;
        }
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        h.p.draw();
        System.out.println("dimension: " + dimension);
        System.out.println("rect: " + h.rect);
        if(dimension == 1){
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius();
            StdDraw.line(h.rect.xmin(), h.p.y(), h.rect.xmax(), h.p.y());
        }
        else{
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius();
            StdDraw.line(h.p.x(), h.rect.ymin(), h.p.x(), h.rect.ymax());
        }
        draw(h.lb, (dimension+1)%2);
        draw(h.rt, (dimension+1)%2);
        return;
    }
    public Iterable<Point2D> range(RectHV rect){
        if(rect == null){
            throw new IllegalArgumentException();
        }
        // all points that are inside the rectangle (or on the boundary)
        Stack<Point2D> pointStack = new Stack<Point2D>();
        range(root, pointStack, rect);
        return pointStack;
    }
    private void range(Node h, Stack<Point2D> stack, RectHV rect){   
        if(h == null){
            return;
        }
        if(h.rect.intersects(rect)){
            if(rect.contains(h.p)){
                stack.push(h.p);
            }
            range(h.lb, stack, rect);
            range(h.rt, stack, rect);
        }
        return;
    }
    public Point2D nearest(Point2D p){
        if(p == null){
            throw new IllegalArgumentException();
        }
        // a nearest neighbor in the set to point p; null if the set is empty
        if(root == null){
            throw new IllegalArgumentException("calls nearest() with a null key");
        }
        return nearest(root, p, root.p);
    }
    private Point2D nearest(Node h, Point2D p, Point2D nearestToP){      
        // a nearest neighbor in the set to point p; null if the set is empty
        if(h == null){
            return nearestToP;
        }
        if(h.p.distanceTo(p) < nearestToP.distanceTo(p)){
            nearestToP = nearest(h.rt, p, h.p);
            nearestToP = nearest(h.lb, p, h.p);
        }
        if(h.rect.distanceTo(p) < nearestToP.distanceTo(p)){
            nearestToP = nearest(h.rt, p, nearestToP);
            nearestToP = nearest(h.lb, p, nearestToP);
        }
        return nearestToP;
    }
    public static void main(String[] args){   
        // unit testing of the methods (optional) 
    }    
}
