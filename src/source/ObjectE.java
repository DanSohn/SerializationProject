/*
An object that uses an instance of one of Java’s collection classes to refer to several other objects.
These objects, too, must be created at the same time.
 */

package source;

import java.util.*;

public class ObjectE {

    ArrayList<Object> arrayList = new ArrayList<>();

    public void set(ObjectA val){
        arrayList.add(val);
    }
}