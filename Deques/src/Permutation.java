
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
public class Permutation {
    public static void main(String[] args){
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> randomizedQueue = new RandomizedQueue<>();
        while(!StdIn.isEmpty()){
            String line = StdIn.readString();
            randomizedQueue.enqueue(line);
        }
        for(int i=0; i<k; i++){
            String line = randomizedQueue.dequeue();
            StdOut.println(line);
        }
    }
}
