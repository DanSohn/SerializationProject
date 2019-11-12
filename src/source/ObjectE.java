/*
An object that uses an instance of one of Java’s collection classes to refer to several other objects.
These objects, too, must be created at the same time.
 */

package source;

import java.util.*;

public class ObjectE {

    Stack<Object> stack = new Stack<Object>();

    public void push(Object obj){
        stack.push(obj);
    }

    public void pop(){
        Object obj = stack.pop();
    }
}