
import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.Queue;
import java.util.Arrays;
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
public class BurrowsWheeler {
    // apply Burrows-Wheeler transform, reading from standard input and writing to standard output
    
    public static void transform(){
        StringBuilder builder = new StringBuilder();
        while(!BinaryStdIn.isEmpty()){
            builder.append(BinaryStdIn.readChar());
        }
        String text = builder.toString();
        CircularSuffixArray circularSuffixArray = new CircularSuffixArray(text);
        builder.setLength(0);
        int first = 0;
        for(int i=0; i<text.length(); i++){
            if(circularSuffixArray.index(i) == 0){
                first = i;
            }
            int t_index = (circularSuffixArray.index(i)+text.length()-1)%text.length();
            builder.append(text.charAt(t_index));
        }
        BinaryStdOut.write(first);
        String t = builder.toString();
        for(int i=0; i<t.length(); i++){
            BinaryStdOut.write(t.charAt(i));
        }
        BinaryStdOut.flush();
    }

    // apply Burrows-Wheeler inverse transform, reading from standard input and writing to standard output
    public static void inverseTransform(){
        int first = BinaryStdIn.readInt();
        StringBuilder builder = new StringBuilder();
        while(!BinaryStdIn.isEmpty()){
            builder.append(BinaryStdIn.readChar());
        }
        String transformedText = builder.toString();
        HashMap<Character, Queue<Integer>> charToIndexMap = new HashMap<Character, Queue<Integer>>();
        for(int i=0; i<transformedText.length(); i++){
            char currentChar = transformedText.charAt(i);
            if(!charToIndexMap.containsKey(currentChar)){
                Queue<Integer> queue = new Queue<>();
                queue.enqueue(i);
                charToIndexMap.put(currentChar, queue);
            }
            else{
                charToIndexMap.get(currentChar).enqueue(i);
            }
        }
        char tempArray[] = transformedText.toCharArray();
        Arrays.sort(tempArray);
        
        int[] next = new int[transformedText.length()];
        
        for(int i=0; i<tempArray.length; i++){
            char currentChar = tempArray[i];
            next[i] = charToIndexMap.get(currentChar).dequeue();
        }
        builder.setLength(0);
        
        int index = first;
        for(int i=0; i<tempArray.length; i++){
            builder.append(tempArray[index]);
            index = next[index];
        }
        
        String originalText = builder.toString();
        for(int i=0; i<originalText.length(); i++){
            BinaryStdOut.write(originalText.charAt(i));
        }
        BinaryStdOut.flush();
    }

    // if args[0] is '-', apply Burrows-Wheeler transform
    // if args[0] is '+', apply Burrows-Wheeler inverse transform
    public static void main(String[] args){
        if(args[0].equals("-")){
            transform();
        }
        else if(args[0].equals("+")){
            inverseTransform();
        }
    }
}
