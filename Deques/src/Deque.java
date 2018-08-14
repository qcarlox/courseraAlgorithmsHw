
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
public class Deque<Item> implements Iterable<Item>{
    private class Node{
        Item item;
        Node next;
        Node previous;
    }
    private Node first = null;
    private Node last = null;
    private int size = 0;
    public Deque(){
        // construct an empty deque
    }
    public boolean isEmpty(){
        // is the deque empty?
        return this.first == null && this.last == null;
    }
    public int size(){
        // return the number of items on the deque
        return this.size;
    }
    public void addFirst(Item item){
        // add the item to the front
        if(item == null){
            throw new IllegalArgumentException();
        }
        if(this.size == 0){
            Node node = new Node();
            node.item = item;
            node.next = null;
            node.previous = null;
            this.first = node;
            this.last = node;
        }
        else{
            Node node = new Node();
            node.item = item;
            node.next = this.first;
            if(node.next != null){
                node.next.previous = node;
            }
            node.previous = null;
            this.first = node;
        }
        this.size++;
    }
    public void addLast(Item item){
        if(item == null){
            throw new IllegalArgumentException();
        }
        // add the item to the end
        if(this.size == 0){
            Node node = new Node();
            node.item = item;
            node.next = null;
            node.previous = null;
            this.first = node;
            this.last = node;
        }
        else{
            Node node = new Node();
            node.item = item;
            node.previous = this.last;
            if(node.previous != null){
                node.previous.next = node;
            }
            node.next = null;
            this.last = node;
        }
        this.size++;
    }
    public Item removeFirst(){
        if(this.isEmpty()){
            throw new NoSuchElementException();
        }
        // remove and return the item from the front
        
        Item item = this.first.item;
        if(this.first == this.last){
            this.first = null;
            this.last = null;
        }
        else{
            this.first = this.first.next;
            if(this.first != null){
                this.first.previous = null;
            }
        }
        this.size--;
        return item;
    }
    
public Item removeLast(){
        if(this.isEmpty()){
            throw new NoSuchElementException();
        }
        // remove and return the item from the end
        Item item = this.last.item;
        if(this.first == this.last){
            this.first = null;
            this.last = null;
        }
        else{
            this.last = this.last.previous;
            if(this.last != null){
                this.last.next = null;
            }
        }
        this.size--;
        return item;
    }
    public Iterator<Item> iterator(){ 
        return new ListIterator(); 
    }
    private class ListIterator implements Iterator<Item>
    {
        private Node current = first;
        public boolean hasNext(){  
            return current != null;  
        }
        public void remove(){  
            /* not supported */
            throw new UnsupportedOperationException();
        }      
        public Item next()
        {
            if(current == null){
                throw new NoSuchElementException();
            }
            Item item = current.item;
            current   = current.next; 
            return item;
        }
    }
    public static void main(String[] args){
        // unit testing (optional)
        Deque<Integer> deque = new Deque<>();
        System.out.println(deque.isEmpty());
        deque.addFirst(1);
        System.out.println(deque.isEmpty());
        deque.removeFirst();
        System.out.println(deque.isEmpty());
        
    }
}
