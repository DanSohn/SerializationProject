/*
An object that contains an array of primitives. Allow the user to set the values for the array
elements to arbitrary values.
 */
package source;

public class ObjectC{
    public int[] arr;

    public ObjectC(int size){
        arr = new int[size];
    }

    public void set(int[] vals){
        arr = vals;
    }
}