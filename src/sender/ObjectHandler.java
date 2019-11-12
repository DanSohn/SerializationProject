package sender;

import java.lang.*;
import java.lang.reflect.*;
import java.util.Scanner;

import source.*;

public class ObjectHandler {
    Scanner input;

    public Object HandlerA() throws Exception{
        // initialize the objectA class and find the constructor
        Class<ObjectA> c = ObjectA.class;
        Constructor<ObjectA> con = c.getDeclaredConstructor(int.class, float.class, boolean.class, double.class);
        //even though i know it's public, set accessible to true
        con.setAccessible(true);
        //ask user for parameters
        Object[] details = userInfoA();
        //create new instance using constructor
        Object obj = con.newInstance((int) details[0], (float) details[1], (boolean) details[2], (double) details[3]);
        //return new instance
        return obj;
    }

    private Object[] userInfoA(){
        input = new Scanner(System.in);
        System.out.println("Please provide field values for the object");
        // int
        System.out.println("Int value:");
        int myInt = input.nextInt();
        // float
        System.out.println("Float value:");
        float myFloat = input.nextFloat();
        // boolean
        System.out.println("Boolean value:");
        boolean myBool = input.nextBoolean();
        // double
        System.out.println("Double value:");
        double myDouble = input.nextDouble();

        return new Object[] {myInt, myFloat, myBool, myDouble};
    }
    /*
    public Object HandlerB(){

    }
    public Object HandlerC(){

    }
    public Object HandlerD(){

    }
    public Object HandlerE(){

    }

     */
}
