/*
An object that contains an array of object references. The other objects must also be created at the
same time.
 */

package source;

public class ObjectD{
    public ObjectA[] arr;
    public ObjectD(){

    }
    public ObjectD(int size){
        arr = new ObjectA[size];
    }

    public void set(ObjectA[] vals){
        arr = vals;
    }
}