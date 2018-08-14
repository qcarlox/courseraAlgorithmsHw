
import edu.princeton.cs.algs4.In;
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
public class Outcast {
    // constructor takes a WordNet object
    private WordNet wordNet;
    public Outcast(WordNet wordnet){
        this.wordNet = wordnet;
    }
    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns){
        int maxDistance = 0;
        String outcastNoun = nouns[0];
        for(String nounA: nouns){
            int distance = 0;
            for(String nounB: nouns){
                distance += this.wordNet.distance(nounA, nounB);
            }
            if(distance > maxDistance){
                maxDistance = distance;
                outcastNoun = nounA;
            }
        }
        return outcastNoun;
    }   
    // see test client below
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
