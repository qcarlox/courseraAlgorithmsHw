
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;
import java.util.NoSuchElementException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author windows
 */
public class RandomizedQueue<Item> implements Iterable<Item>{
    private int first = 0;
    private int last = 0; //last points to empty space after last item
    private int size = 0;
    private int capacity = 1;
    private Item[] queue = (Item[]) new Object[capacity];
    
    public RandomizedQueue(){
    }
    public boolean isEmpty(){
        return this.size == 0;
    }
    public int size(){
        return this.size;
    }
    private void checkConsistensy(String state){
        int j = first;
        for(int i=0; i<size; i++){
            if(this.queue[j] == null){
                break;
            }
            j = (j+1)%capacity;
        }
    }
    public void enqueue(Item item){
        if(item == null){
            throw new IllegalArgumentException();
        }
        this.queue[this.last] = item;
        this.size++;
        if(this.size >= this.capacity/2){
            Item[] biggerQueue = (Item[]) new Object[this.capacity*2];
            int j = first;
            for(int i=0; i<this.size; i++){
                biggerQueue[i] = this.queue[j];
                j = (j+1)%this.capacity;
            }
            first = 0;
            last = this.size-1;
            this.queue = biggerQueue;
            this.capacity = this.capacity*2;
        }
        this.last = (this.last+1)%this.capacity;
        checkConsistensy("enque");
    }
    public Item dequeue(){
        if(isEmpty()){
            throw new NoSuchElementException();
        }
        
        int randomIndex = (StdRandom.uniform(this.size)+this.first)%this.capacity;
        Item item = this.queue[randomIndex];
        this.queue[randomIndex] = this.queue[this.first];
        this.queue[this.first] = null;
        this.first = (this.first+1)%this.capacity;
        this.size--;
        if(this.size < 0){
            this.size = 0;
        }
        
        if(this.size <= this.capacity/4){
            Item[] smallerQueue = (Item[]) new Object[this.capacity/2];
            int j=first;
            for(int i=0; i<this.size; i++){
                smallerQueue[i] = this.queue[j];
                j = (j+1)%this.capacity;
            }
            this.queue = smallerQueue;
            this.capacity = this.capacity/2;
            this.first = 0;
            this.last = this.size;
            if(this.last < 0){
                this.last = 0;
            }
        }
        checkConsistensy("dequeue");
        return item;
        
    }
    public Item sample(){
        if(isEmpty()){
            throw new NoSuchElementException();
        }
        int randomIndex = StdRandom.uniform(this.size)+this.first;
        Item item = this.queue[randomIndex];
        return item;
        
    }
    public Iterator<Item> iterator(){ 
        return new ListIterator(); 
    }
    private class ListIterator implements Iterator<Item>
    {
        private int current = first;
        public boolean hasNext(){  
            return queue[current] != null;  
        }
        public void remove(){  
            /* not supported */
            throw new UnsupportedOperationException();
        }      
        public Item next()
        {
            if(queue[current] == null){
                throw new NoSuchElementException();
            }
            Item item = queue[current];
            current = (current+1)%capacity;
            return item;
        }
    }
    
    public static void main(String[] args){
        // unit testing (optional)
        RandomizedQueue<Integer> randomizedQueue = new RandomizedQueue<>();
        for(int i=0; i<1000; i++){
            if(StdRandom.uniform(100) < 60){
                randomizedQueue.enqueue(i);
            }
            else{
                randomizedQueue.dequeue();
                
            }
        }
       
    }
}
