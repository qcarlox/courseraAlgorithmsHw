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
import java.util.Comparator;
public class FastCollinearPoints {
    private int capacity;
    private int lineSegmentCount;
    private LineSegment[] lineSegments;
    
    public FastCollinearPoints(Point[] points){
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
        Point[] localPoints;
        localPoints = Arrays.copyOf(points, points.length);
        Arrays.sort(localPoints);
        this.capacity = 1;
        this.lineSegmentCount = 0;
        this.lineSegments = new LineSegment[this.capacity];
        for(int i=0; i<localPoints.length; i++){
            Point[] pointsBySlope = new Point[localPoints.length-1-i];
            for(int j=i+1; j<localPoints.length; j++){
                pointsBySlope[j-(i+1)] = localPoints[j];
            }
            Arrays.sort(pointsBySlope, localPoints[i].slopeOrder());            
            for(int j=0; j<pointsBySlope.length; j++){
                int consecutiveCount = 0;
                for(int k=j+2; k<pointsBySlope.length; k++){
                    if(pointsBySlope[j].slopeTo(localPoints[i]) == pointsBySlope[k].slopeTo(localPoints[i])){
                        consecutiveCount++;
                    }
                    else{
                        break;
                    }
                }
                if(consecutiveCount > 0){
                    consecutiveCount += 2;
                    Point[] pointsOnLine = new Point[consecutiveCount+1];
                    pointsOnLine[0] = localPoints[i];
                    for(int k=1; k<pointsOnLine.length; k++){
                        pointsOnLine[k] = pointsBySlope[j+k-1];
                    }
                    Arrays.sort(pointsOnLine);
                    this.addLineSegment(pointsOnLine[0], pointsOnLine[pointsOnLine.length-1]);
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
