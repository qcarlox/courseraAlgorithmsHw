
import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author windows
 */

public class BoggleSolver
{
    private TrieSET dictionary;
    private HashSet<String> wordsInBoard;
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary){
        this.dictionary = new TrieSET();
        //Collections.shuffle(Arrays.asList(dictionary));
        for(int i=0; i<dictionary.length; i++){
            String word = dictionary[i];
            this.dictionary.add(word);
            
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board){
        
        TrieSET localDictionary = this.dictionary;
        this.wordsInBoard = new HashSet<>();
        
        for(int startRow=0; startRow<board.rows(); startRow++){
            for(int startCol=0; startCol<board.cols(); startCol++){
                for(int endRow=0; endRow<board.rows(); endRow++){
                    for(int endCol=0; endCol<board.cols(); endCol++){
                        new DepthFirstSearch(startRow, startCol, endRow, endCol, board, localDictionary, this.wordsInBoard);
                    }
                }
            }
        }
        return this.wordsInBoard;
    }
    
    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word){
        if(!this.dictionary.contains(word)){
            return 0;
        }
        if(word.length() <= 2){
            return 0;
        }
        else if(word.length() <= 4){
            return 1;
        }
        else if(word.length() == 5){
            return 2;
        }
        else if(word.length() == 6){
            return 3;
        }
        else if(word.length() == 7){
            return 5;
        }
        else{
            return 11;
        }
    }
    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        for(int i=0; i<1000; i++){
            //BoggleBoard board = new BoggleBoard(args[1]);
            BoggleBoard board = new BoggleBoard();
            solver.getAllValidWords(board);
            if(false){
                int score = 0;
                for (String word : solver.getAllValidWords(board)) {
                    //StdOut.println(word);
                    score += solver.scoreOf(word);
                }
                StdOut.println("Score = " + score);
            }
            
        }
    }
    private class DepthFirstSearch {
        private boolean[][] marked;    // marked[v] = is there an s-v path?
        private BoggleBoard board;
        private TrieSET dictionary;
        private HashSet<String> wordsInBoard;
        private String word;
        private DepthFirstSearch(int startRow, int startCol, int endRow, int endCol, BoggleBoard board, TrieSET dictionary, HashSet<String> wordsInBoard) {
            this.marked = new boolean[board.rows()][board.cols()];
            this.board = board;
            this.dictionary = dictionary;
            this.wordsInBoard = wordsInBoard;
            this.word = "";
            if(this.board.getLetter(startRow, startCol) == 'Q'){
                this.word += "QU";
            }
            else{
                this.word += this.board.getLetter(startRow, startCol);
            }
            dfs(startRow, startCol, endRow, endCol); 
        }

        // depth first search from v
        private void dfs(int currentRow, int currentCol, int endRow, int endCol) {
            this.marked[currentRow][currentCol] = true;
            if(currentRow == endRow && currentCol == endCol){
                //StdOut.println(this.word);
                if(this.dictionary.contains(word) && word.length() > 2){
                    this.wordsInBoard.add(word);
                }
            }
                
                //StdOut.println(this.word);
                //StdOut.println("row: " + row + " col: " + col);
            for(int c=currentCol-1; c<=currentCol+1; c++){
                for(int r=currentRow-1; r<=currentRow+1; r++){
                    if(r<0 || c<0 || c>=this.board.cols() || r>=this.board.rows()){
                        continue;
                    }
                    
                    if(!this.marked[r][c]) {
                        //StdOut.println("row: " + r + " col: " + c);
                        if(this.board.getLetter(r, c) == 'Q'){
                            this.word += "QU";
                        }
                        else{
                            this.word += this.board.getLetter(r, c);
                        }
                        
                        if(this.dictionary.containsPrefix(word)){
                            dfs(r, c, endRow, endCol);
                        }
                        if(this.word.charAt(this.word.length()-2) == 'Q'){
                            this.word = this.word.substring(0, this.word.length() - 2);
                        }
                        else{
                            this.word = this.word.substring(0, this.word.length() - 1);
                        }
                    }
                }
            }
            this.marked[currentRow][currentCol] = false;
        }
    }
    
    private class TrieSET{
        private static final int R = 26;        // extended ASCII

        private Node root;      // root of trie
        private int n;          // number of keys in trie

        // R-way trie node
        private class Node {
            private Node[] next = new Node[R];
            private boolean isString;
        }

        /**
         * Initializes an empty set of strings.
         */
        public TrieSET() {
        }

        /**
         * Does the set contain the given key?
         * @param key the key
         * @return {@code true} if the set contains {@code key} and
         *     {@code false} otherwise
         * @throws IllegalArgumentException if {@code key} is {@code null}
         */
        public boolean contains(String key) {
            if (key == null) throw new IllegalArgumentException("argument to contains() is null");
            Node x = get(root, key, 0);
            if (x == null) return false;
            return x.isString;
        }
        
        public boolean containsPrefix(String prefix) {
            if (prefix == null) throw new IllegalArgumentException("argument to contains() is null");
            Node x = get(root, prefix, 0);
            if (x == null) return false;
            return true;
        }

        private Node get(Node x, String key, int d) {
            if (x == null) return null;
            if (d == key.length()) return x;
            char c = key.charAt(d);
            return get(x.next[c-'A'], key, d+1);
        }

        /**
         * Adds the key to the set if it is not already present.
         * @param key the key to add
         * @throws IllegalArgumentException if {@code key} is {@code null}
         */
        public void add(String key) {
            if (key == null) throw new IllegalArgumentException("argument to add() is null");
            root = add(root, key, 0);
        }

        private Node add(Node x, String key, int d) {
            if (x == null) x = new Node();
            if (d == key.length()) {
                if (!x.isString) n++;
                x.isString = true;
            }
            else {
                char c = key.charAt(d);
                x.next[c-'A'] = add(x.next[c-'A'], key, d+1);
            }
            return x;
        }

        /**
         * Returns the number of strings in the set.
         * @return the number of strings in the set
         */
        public int size() {
            return n;
        }

        /**
         * Is the set empty?
         * @return {@code true} if the set is empty, and {@code false} otherwise
         */
        public boolean isEmpty() {
            return size() == 0;
        }
        
        /**
         * Returns all of the keys in the set, as an iterator.
         * To iterate over all of the keys in a set named {@code set}, use the
         * foreach notation: {@code for (Key key : set)}.
         * @return an iterator to all of the keys in the set
         */
        /*
        public Iterator<String> iterator() {
            Queue<String> results = new Queue<String>();
            collect(root, new StringBuilder(""), results);
            return results.iterator();
        }
        */
        /**
         * Removes the key from the set if the key is present.
         * @param key the key
         * @throws IllegalArgumentException if {@code key} is {@code null}
         */
        public void delete(String key) {
            if (key == null) throw new IllegalArgumentException("argument to delete() is null");
            root = delete(root, key, 0);
        }

        private Node delete(Node x, String key, int d) {
            if (x == null) return null;
            if (d == key.length()) {
                if (x.isString) n--;
                x.isString = false;
            }
            else {
                char c = key.charAt(d);
                x.next[c-'A'] = delete(x.next[c-'A'], key, d+1);
            }

            // remove subtrie rooted at x if it is completely empty
            if (x.isString) return x;
            for (int c = 0; c < R; c++)
                if (x.next[c] != null)
                    return x;
            return null;
        }
    }
}