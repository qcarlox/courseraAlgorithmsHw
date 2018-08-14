
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author windows
 */
public class CircularSuffixArray {
    private class CompareCircularSuffix implements Comparator<Integer>{
        private StringBuilder textBuilder;
        public CompareCircularSuffix(StringBuilder textBuilder){
            this.textBuilder = textBuilder;
        }
        public int compare(Integer index1, Integer index2){
            for(int i=0; i<this.textBuilder.length(); i++){
                if(this.textBuilder.charAt(index1) < this.textBuilder.charAt(index2)){
                    return -1;
                }
                else if(this.textBuilder.charAt(index1) > this.textBuilder.charAt(index2)){
                    return 1;
                }
                else{
                    index1 = (index1+1)%this.textBuilder.length();
                    index2 = (index2+1)%this.textBuilder.length();
                }
            }
            return 0;
        }
    }
    private StringBuilder textBuilder;
    private Integer[] indexList;
    public CircularSuffixArray(String s){
        if(s == null){
            throw new IllegalArgumentException();
        }
        // circular suffix array of s
        this.textBuilder = new StringBuilder(s);
        this.indexList = new Integer[this.textBuilder.length()];
        for(int i=0; i<this.textBuilder.length(); i++){
            this.indexList[i] = i;
        }
        Comparator<Integer> compareIndeces = new CompareCircularSuffix(this.textBuilder);
        Arrays.sort(this.indexList, compareIndeces);
        
    }
    public int length(){                   
        // length of s
        return this.textBuilder.length();
    }
    public int index(int i){ 
        // returns index of ith sorted suffix
        if( i < 0 || i >= this.textBuilder.length()){
            throw new IllegalArgumentException();
        }
        return this.indexList[i];
    }
    public static void main(String[] args){ 
        // unit testing (required)
        String text = "ABRACADABRA!";
        CircularSuffixArray suffixArray = new CircularSuffixArray(text);
        System.out.println("length:" + suffixArray.length());
        for(int i=0; i<text.length(); i++){
            System.out.println("index[" + i + "]: " + suffixArray.index(i));
        }
    }
}