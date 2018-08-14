
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.KosarajuSharirSCC;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Topological;
import java.util.ArrayList;
import java.util.HashMap;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author windows
 */
public class WordNet {
    private HashMap<String, ArrayList<Integer>> nounToIdMap;
    private HashMap<Integer, ArrayList<String>> IdToNounMap;
    private HashMap<Integer, String> IdToSynsetMap;
    private int numberOfSynsets;
    private Digraph wordNetDigraph;
    private SAP wordNetSap;
    // constructor takes the name of the two input files
    public WordNet(String synsetsFile, String hypernymsFile){
        if(synsetsFile == null || hypernymsFile == null){
            throw new IllegalArgumentException();
        }
        this.nounToIdMap = new HashMap<String, ArrayList<Integer>>();
        this.IdToNounMap = new HashMap<Integer, ArrayList<String>>();
        this.IdToSynsetMap = new HashMap<Integer, String>();
        this.numberOfSynsets = 0;
        processSynsetFile(synsetsFile);
        this.wordNetDigraph = new Digraph(this.numberOfSynsets);
        
        In hypernymsIn = new In(hypernymsFile);
        while(hypernymsIn.hasNextLine()){
            String line = hypernymsIn.readLine();
            String[] list = line.split(",");
            int synsetId = Integer.parseInt(list[0]);
            for(int i=1; i<list.length; i++){
                int hypernym = Integer.parseInt(list[i]);
                this.wordNetDigraph.addEdge(synsetId, hypernym);
            }
        }
        checkIfRootedDigraph();
    }
    private void processSynsetFile(String synsetsFile){
        In synsetIn = new In(synsetsFile);
        while(synsetIn.hasNextLine()){
            String line = synsetIn.readLine();
            String[] list = line.split(",");
            int synsetId = Integer.parseInt(list[0]);
            this.IdToSynsetMap.put(synsetId, list[1]);
            String[] synonymList = list[1].split(" ");
            this.numberOfSynsets++;
            for(String noun: synonymList){
                if(this.nounToIdMap.containsKey(noun) == false){
                    ArrayList<Integer> idList = new ArrayList<Integer>();
                    idList.add(synsetId);
                    this.nounToIdMap.put(noun, idList);
                }
                else{
                    if(this.nounToIdMap.get(noun).contains(synsetId) == false){
                        this.nounToIdMap.get(noun).add(synsetId);
                    }
                }
                if(this.IdToNounMap.containsKey(synsetId) == false){
                    ArrayList<String> nounList = new ArrayList<String>();
                    nounList.add(noun);
                    this.IdToNounMap.put(synsetId, nounList);
                }
                else{
                    if(this.IdToNounMap.get(synsetId).contains(noun) == false){
                        this.IdToNounMap.get(synsetId).add(noun);
                    }
                }
            }
        }
    }
    private void checkIfRootedDigraph(){
        ArrayList<Integer> nodesWithOutDegreeZero = new ArrayList<Integer>();
        for(int i=0; i<this.wordNetDigraph.V(); i++){
            if(this.wordNetDigraph.outdegree(i) == 0){
                nodesWithOutDegreeZero.add(i);
            }
        }
        if(nodesWithOutDegreeZero.size() != 1){
            throw new IllegalArgumentException();
        }
        this.wordNetSap = new SAP(this.wordNetDigraph);
        
        for(int i=0; i<this.wordNetDigraph.V(); i++){
            int ancestor = this.wordNetSap.ancestor(i, nodesWithOutDegreeZero.get(0));
            if(ancestor != nodesWithOutDegreeZero.get(0)){
                throw new IllegalArgumentException();
            }
        }
    }
    // returns all WordNet nouns
    public Iterable<String> nouns(){
        return this.nounToIdMap.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word){
        return this.nounToIdMap.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB){
        ArrayList<Integer> idA = this.nounToIdMap.get(nounA);
        ArrayList<Integer> idB = this.nounToIdMap.get(nounB);
        if(idA.isEmpty() || idB.isEmpty()){
             throw new IllegalArgumentException();
        }
        return this.wordNetSap.length(idA, idB);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB){
        ArrayList<Integer> idA = this.nounToIdMap.get(nounA);
        ArrayList<Integer> idB = this.nounToIdMap.get(nounB);
        if(idA.isEmpty() || idB.isEmpty()){
             throw new IllegalArgumentException();
        }
        int ancestor = this.wordNetSap.ancestor(idA, idB);
        return this.IdToSynsetMap.get(ancestor);
    }

    // do unit testing of this class
    public static void main(String[] args){
        WordNet wordNet = new WordNet(args[0], args[1]);
        System.out.println(wordNet.sap("c", "b"));
    }
}