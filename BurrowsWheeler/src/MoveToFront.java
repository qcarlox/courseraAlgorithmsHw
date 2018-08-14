
import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author windows
 */
public class MoveToFront {
    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode(){
        char[] positionFromChar = new char[256];
        for(char i=0; i<positionFromChar.length; i++){
            positionFromChar[i] = i;
        }
        while(!BinaryStdIn.isEmpty()){
            char currentChar = BinaryStdIn.readChar();
            char position = positionFromChar[currentChar];
            BinaryStdOut.write(position);
            for(int j=0; j<positionFromChar.length; j++){
                if(positionFromChar[j] < positionFromChar[currentChar]){
                    positionFromChar[j]++;
                }
            }
            positionFromChar[currentChar] = 0;
        }
        BinaryStdOut.flush();
    }
    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode(){
        char[] positionFromChar = new char[256];
        for(char i=0; i<positionFromChar.length; i++){
            positionFromChar[i] = i;
        }
        while(!BinaryStdIn.isEmpty()){
            char currentPosition = BinaryStdIn.readChar();
            char charOut = 0;
            for(char i=0; i<positionFromChar.length; i++){
                if(positionFromChar[i] == currentPosition){
                    charOut = i;
                    break;
                }
            }
            BinaryStdOut.write(charOut);
            for(int j=0; j<positionFromChar.length; j++){
                if(positionFromChar[j] < currentPosition){
                    positionFromChar[j]++;
                }
            }
            positionFromChar[charOut] = 0;
        }
        BinaryStdOut.flush();
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args){
        if(args[0].equals("-")){
            encode();
        }
        else if(args[0].equals("+")){
            decode();
        }
    }
}