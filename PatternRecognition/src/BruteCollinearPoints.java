/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author windows
 */
import java.util.Arrays;
public class BruteCollinearPoints {
    private int capacity;
    private int lineSegmentCount;
    private LineSegment[] lineSegments;
    
    public BruteCollinearPoints(Point[] points){
        // finds all line segments containing 4 points
        if(points == null){
            throw new IllegalArgumentException();
        }
        for(int i=0; i<points.length; i++){
            for(int j=0; j<points.length; j++){
                if(points[j] == null){
                    throw new IllegalArgumentException();
                }
                if(j != i && points[j].compareTo(points[i]) == 0){
                    throw new IllegalArgumentException();
                }
            }
        }
        this.capacity = 1;
        this.lineSegmentCount = 0;
        this.lineSegments = new LineSegment[this.capacity];
        for(int i=0; i<points.length; i++){
            for(int j=0; j<points.length; j++){
                for(int k=0; k<points.length; k++){
                    for(int l=0; l<points.length; l++){
                        if(points[i].compareTo(points[j]) < 0 && points[j].compareTo(points[k]) < 0 && points[k].compareTo(points[l]) < 0){
                            
                            double slope01 = points[i].slopeTo(points[j]);
                            double slope12 = points[j].slopeTo(points[k]);
                            double slope23 = points[k].slopeTo(points[l]);
                            if(slope01 == slope12 && slope12 == slope23 && slope01 != -1.0/0.0){
                                this.addLineSegment(points[i], points[l]);
                            }
                        }
                    }
                }
            }
        }
        this.lineSegments = Arrays.copyOf(this.lineSegments, this.lineSegmentCount);
        this.capacity = this.lineSegmentCount;
    }
    private void addLineSegment(Point q, Point p){
        if(this.lineSegmentCount+1 >= this.capacity){
            this.capacity = this.capacity*2;
            this.lineSegments = Arrays.copyOf(this.lineSegments, this.capacity);
        }
        LineSegment currentLineSegment = new LineSegment(q, p);
        this.lineSegments[this.lineSegmentCount] = currentLineSegment;
        this.lineSegmentCount++;
    }
    public int numberOfSegments(){
        // the number of line segments
        return this.lineSegmentCount;
    }
    public LineSegment[] segments(){
        // the line segments
        LineSegment[] lineSegmentsCopy = Arrays.copyOf(this.lineSegments, this.lineSegmentCount);
        return lineSegmentsCopy;
    }
}
