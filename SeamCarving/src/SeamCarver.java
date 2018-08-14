
import edu.princeton.cs.algs4.AcyclicSP;
import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.Picture;
import java.awt.Color;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author windows
 */
public class SeamCarver {
    private Picture picture;
    public SeamCarver(Picture picture){                // create a seam carver object based on the given picture
        this.picture = new Picture(picture);
    }
    public Picture picture(){                          // current picture
        return new Picture(this.picture);
    }
    public int width(){                            // width of current picture
        return this.picture.width();
    }
    public int height(){                           // height of current picture
        return this.picture.height();
    }
    public double energy(int x, int y){               // energy of pixel at column x and row y
        if(x < 0 || x > width()-1 || y < 0 || y > height()-1){
            throw new IllegalArgumentException();
        }
        if(x == 0 || x == width()-1 || y == 0 || y == height()-1){
            return 1000;
        }
        Color pixelXMinus = picture.get(x-1, y);
        Color pixelXPlus = picture.get(x+1, y);
        Color pixelYMinus = picture.get(x, y-1);
        Color pixelYPlus = picture.get(x, y+1);
        
        int Rx = pixelXPlus.getRed()-pixelXMinus.getRed();
        int Gx = pixelXPlus.getGreen()-pixelXMinus.getGreen();
        int Bx = pixelXPlus.getBlue()-pixelXMinus.getBlue();
        
        int Ry = pixelYPlus.getRed()-pixelYMinus.getRed();
        int Gy = pixelYPlus.getGreen()-pixelYMinus.getGreen();
        int By = pixelYPlus.getBlue()-pixelYMinus.getBlue();
        
        double energy = Math.sqrt(Rx*Rx + Gx*Gx + Bx*Bx + Ry*Ry + Gy*Gy + By*By);
        return energy;
    }
    public int[] findHorizontalSeam(){               // sequence of indices for horizontal seam
        int numberOfNodes = this.width()*this.height()+2;
        EdgeWeightedDigraph graph = constructHorizontalGraph();
        AcyclicSP shortestPath = new AcyclicSP(graph, 0);
        int[] horizontalSeam = new int[this.width()];
        int i=0;
        for(DirectedEdge edge: shortestPath.pathTo(numberOfNodes-1)){
            int w = edge.to();
            int x = (w-1)%this.width();
            int y = (w-1)/this.width();
            if(w < numberOfNodes-1){
                horizontalSeam[i] = y;
                i++;
            }
        }
        return horizontalSeam;
    }
    public int[] findVerticalSeam(){                 // sequence of indices for vertical seam
        int numberOfNodes = this.width()*this.height()+2;
        EdgeWeightedDigraph graph = constructVerticalGraph();
        AcyclicSP shortestPath = new AcyclicSP(graph, 0);
        int[] verticalSeam = new int[this.height()];
        int i=0;
        for(DirectedEdge edge: shortestPath.pathTo(numberOfNodes-1)){
            int w = edge.to();
            int x = (w-1)%this.width();
            int y = (w-1)/this.width();
            if(w < numberOfNodes-1){
                verticalSeam[i] = x;
                i++;
            }
        }
        return verticalSeam;
    }
    private EdgeWeightedDigraph constructVerticalGraph(){
        int numberOfNodes = this.width()*this.height()+2;
        EdgeWeightedDigraph graph = new EdgeWeightedDigraph(numberOfNodes);
        
        for(int j=0; j<this.width(); j++){
            DirectedEdge edge = new DirectedEdge(0, vertexFromCoordinates(j, 0), energy(j, 0));
            graph.addEdge(edge);
        }
        for(int j=0; j<this.width(); j++){
            DirectedEdge edge = new DirectedEdge(vertexFromCoordinates(j, this.height()-1), numberOfNodes-1, 0);
            graph.addEdge(edge);
        }
        for(int x=0; x<this.width(); x++){
            for(int y=0; y<this.height()-1; y++){
                if(x == 0 && this.width() == 1){
                    DirectedEdge edge = new DirectedEdge(vertexFromCoordinates(x, y), vertexFromCoordinates(x, y+1), energy(x, y+1));
                    graph.addEdge(edge);
                }
                else if(x == 0){
                    DirectedEdge edge = new DirectedEdge(vertexFromCoordinates(x, y), vertexFromCoordinates(x, y+1), energy(x, y+1));
                    graph.addEdge(edge);
                    edge = new DirectedEdge(vertexFromCoordinates(x, y), vertexFromCoordinates(x+1, y+1), energy(x+1, y+1));
                    graph.addEdge(edge);
                }
                else if(x == this.width()-1){
                    DirectedEdge edge = new DirectedEdge(vertexFromCoordinates(x, y), vertexFromCoordinates(x, y+1), energy(x, y+1));
                    graph.addEdge(edge);
                    edge = new DirectedEdge(vertexFromCoordinates(x, y), vertexFromCoordinates(x-1, y+1), energy(x-1, y+1));
                    graph.addEdge(edge);
                }
                else{
                    DirectedEdge edge = new DirectedEdge(vertexFromCoordinates(x, y), vertexFromCoordinates(x, y+1), energy(x, y+1));
                    graph.addEdge(edge);
                    edge = new DirectedEdge(vertexFromCoordinates(x, y), vertexFromCoordinates(x-1, y+1), energy(x-1, y+1));
                    graph.addEdge(edge);
                    edge = new DirectedEdge(vertexFromCoordinates(x, y), vertexFromCoordinates(x+1, y+1), energy(x+1, y+1));
                    graph.addEdge(edge);
                }
            }
        }
        return graph;
    }
    private EdgeWeightedDigraph constructHorizontalGraph(){
        int numberOfNodes = this.width()*this.height()+2;
        EdgeWeightedDigraph graph = new EdgeWeightedDigraph(numberOfNodes);
        
        for(int j=0; j<this.height(); j++){
            DirectedEdge edge = new DirectedEdge(0, vertexFromCoordinates(0, j), energy(0, j));
            graph.addEdge(edge);
        }
        for(int j=0; j<this.height(); j++){
            DirectedEdge edge = new DirectedEdge(vertexFromCoordinates(this.width()-1, j), numberOfNodes-1, 0);
            graph.addEdge(edge);
        }
        for(int x=0; x<this.width()-1; x++){
            for(int y=0; y<this.height(); y++){
                if(y == 0 && this.height()== 1){
                    DirectedEdge edge = new DirectedEdge(vertexFromCoordinates(x, y), vertexFromCoordinates(x+1, y), energy(x+1, y));
                    graph.addEdge(edge);
                }
                else if(y == 0){
                    DirectedEdge edge = new DirectedEdge(vertexFromCoordinates(x, y), vertexFromCoordinates(x+1, y), energy(x+1, y));
                    graph.addEdge(edge);
                    edge = new DirectedEdge(vertexFromCoordinates(x, y), vertexFromCoordinates(x+1, y+1), energy(x+1, y+1));
                    graph.addEdge(edge);
                }
                else if(y == this.height()-1){
                    DirectedEdge edge = new DirectedEdge(vertexFromCoordinates(x, y), vertexFromCoordinates(x+1, y), energy(x+1, y));
                    graph.addEdge(edge);
                    edge = new DirectedEdge(vertexFromCoordinates(x, y), vertexFromCoordinates(x+1, y-1), energy(x+1, y-1));
                    graph.addEdge(edge);
                }
                else{
                    DirectedEdge edge = new DirectedEdge(vertexFromCoordinates(x, y), vertexFromCoordinates(x+1, y), energy(x+1, y));
                    graph.addEdge(edge);
                    edge = new DirectedEdge(vertexFromCoordinates(x, y), vertexFromCoordinates(x+1, y-1), energy(x+1, y-1));
                    graph.addEdge(edge);
                    edge = new DirectedEdge(vertexFromCoordinates(x, y), vertexFromCoordinates(x+1, y+1), energy(x+1, y+1));
                    graph.addEdge(edge);
                }
            }
        }
        return graph;
    }
    private int vertexFromCoordinates(int x, int y){
        return y*this.width() + x + 1;
    }
    
    public void removeHorizontalSeam(int[] seam){   // remove horizontal seam from current picture
        if(seam == null){
            throw new IllegalArgumentException();
        }
        if(this.height() <= 1){
            throw new IllegalArgumentException();
        }
        if(seam.length != this.width()){
            throw new IllegalArgumentException();
        }
        for(int i=0; i<seam.length; i++){
            if(seam[i] < 0 || seam[i] > this.height()-1){
                throw new IllegalArgumentException();
            }
        }
        for(int i=1; i<seam.length; i++){
            if(Math.abs(seam[i-1]-seam[i]) > 1){
                throw new IllegalArgumentException();
            }
        }
        
        Picture newPicture = new Picture(this.width(), this.height()-1);
        for(int x=0; x<this.width(); x++){
            for(int y=0; y<this.height()-1; y++){
                if(y<seam[x]){
                    newPicture.setRGB(x, y, this.picture.getRGB(x, y));
                }
                else{
                    newPicture.setRGB(x, y, this.picture.getRGB(x, y+1));
                }
            }
        }
        this.picture = newPicture;
    }
    public void removeVerticalSeam(int[] seam){     // remove vertical seam from current picture
        if(seam == null){
            throw new IllegalArgumentException();
        }
        if(this.width() <= 1){
            throw new IllegalArgumentException();
        }
        if(seam.length != this.height()){
            throw new IllegalArgumentException();
        }
        for(int i=0; i<seam.length; i++){
            if(seam[i] < 0 || seam[i] > this.width()-1){
                throw new IllegalArgumentException();
            }
        }
        for(int i=1; i<seam.length; i++){
            if(Math.abs(seam[i-1]-seam[i]) > 1){
                throw new IllegalArgumentException();
            }
        }
        Picture newPicture = new Picture(this.width()-1, this.height());
        for(int y=0; y<this.height(); y++){
            for(int x=0; x<this.width()-1; x++){
                if(x<seam[y]){
                    newPicture.setRGB(x, y, this.picture.getRGB(x, y));
                }
                else{
                    newPicture.setRGB(x, y, this.picture.getRGB(x+1, y));
                }
            }
        }
        this.picture = newPicture;
    }

}