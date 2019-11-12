package sender;

import java.lang.*;
import java.lang.reflect.*;
import java.sql.SQLOutput;
import java.util.Scanner;

import source.*;

public class ObjectHandler {
    private Scanner input;

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

    public Object HandlerB() throws Exception {
        //initialize the objectB class and find its constructor
        Class<ObjectB> c = ObjectB.class;
        Constructor<ObjectB> con = c.getDeclaredConstructor(int.class, float.class, boolean.class, double.class);
        //even though i know it's public, set accessible to true
        con.setAccessible(true);
        //ask user for parameters
        Object[] details = userInfoB();
        //create new instance using constructor
        //Object obj = con.newInstance((int) details[0], (float) details[1], (boolean) details[2], (double) details[3]);
        //return new instance
        return (Object) con.newInstance((int) details[0], (float) details[1], (boolean) details[2], (double) details[3]);
    }

    private Object[] userInfoB(){
        System.out.println("ObjectB contains a field reference to ObjectA");
        System.out.println("Creating Object A .........");
        //Object[] details = userInfoA();
        //return details;
        return userInfoA();
    }

    public Object HandlerC() throws Exception{
        //initialize the ObjectC class and its constructor, and its method set
        Class<ObjectC> c = ObjectC.class;
        Constructor<ObjectC> con = c.getDeclaredConstructor(int.class);
        Method m = c.getDeclaredMethod("set", int[].class);
        //even though i know it's public, set accessible to true
        con.setAccessible(true);
        m.setAccessible(true);
        //ask user for parameters
        input = new Scanner(System.in);
        //size of array
        System.out.println("Size of int array:");
        int arr_size = input.nextInt();
        //instantiate the object using constructor
        Object obj = con.newInstance(arr_size);
        //call method to set values of array
        int[] vals = new int[arr_size];
        for(int i = 0; i < arr_size; i++){
            System.out.println("Provide element " + (i+1) + ":");
            vals[i] = input.nextInt();
        }
        m.invoke(obj, (Object) vals);

        return obj;
    }

    public Object HandlerD() throws Exception{
        //initialize the ObjectC class and its constructor, and its method set
        Class<ObjectD> c = ObjectD.class;
        Constructor<ObjectD> con = c.getDeclaredConstructor(int.class);
        Method m = c.getDeclaredMethod("set", ObjectA[].class);
        //even though i know it's public, set accessible to true
        con.setAccessible(true);
        m.setAccessible(true);
        //ask user for parameters
        input = new Scanner(System.in);
        //size of array
        System.out.println("Size of ObjectA array:");
        int arr_size = input.nextInt();
        //instantiate the object using constructor
        Object obj = con.newInstance(arr_size);
        //call method to set values of array
        ObjectA[] vals = new ObjectA[arr_size];
        for(int i = 0; i < arr_size; i++){
            System.out.println("Provide element " + (i+1) + ":");
            vals[i] = (ObjectA) HandlerA();
        }
        m.invoke(obj, (Object) vals);

        return obj;
    }

    public Object HandlerE() throws Exception{
        //initialize the ObjectC class and its constructor, and its method set
        Class<ObjectE> c = ObjectE.class;
        Method m = c.getDeclaredMethod("set", ObjectA.class);
        //even though i know it's public, set accessible to true
        m.setAccessible(true);
        //ask user for parameters
        input = new Scanner(System.in);
        //instantiate the object using constructor
        Object obj = c.newInstance();
        //call method to set values of array
        ObjectA val;
        //ask how many elements they want to add
        System.out.println("How many elements would you like to add to the ArrayList?");
        int arr_size = input.nextInt();
        for(int i = 0; i < arr_size; i++){
            System.out.println("Provide element " + (i+1) + ":");
            val = (ObjectA) HandlerA();
            m.invoke(obj, val);
        }

        return obj;
    }




}
