/*
Taken from my own Assignment 2, which inspects an object and all its information
Fields, Methods, Constructors, Superclasses and Interfaces
 */

package receiver;

import java.lang.*;
import java.lang.reflect.*;
import java.util.*;

import static java.lang.System.identityHashCode;

public class Visualizer {
    public void inspect(Object obj, boolean recursive) throws IllegalAccessException {
        // reference obj's class object
        Class c = obj.getClass();
        inspectClass(c, obj, 0);
    }


    private void inspectClass(Class c, Object obj, int depth) throws IllegalAccessException {
        inspectConstructor(c, depth);
        inspectMethod(c, depth);
        inspectField(c, obj, depth);
    }

    private void inspectSuperClass(Class c, Object obj, int depth) throws IllegalAccessException {
        //System.out.println("inspecting super class of " + c);

        // handling formatting
        String[] tabs = getTab(depth);
        String tab = tabs[0];
        String tab2 = tabs[1];
        String tab3 = tabs[2];
        String tab4 = tabs[3];

        /*
        System.out.println("Tabbing test:");
        System.out.println(tab + "here");
        System.out.println(tab2 + "here");
        System.out.println(tab3 + "here");
        System.out.println(tab4 + "here");

         */
        //print class we are currently looking at
        //check if its an array
        if(c.isArray()){
            // BRUTE FORCING THIS FOR ONE OR TWO ITERATIONS, NOT DOING IN A WHILE LOOP. COULD FIX LATER
            if(c.getComponentType().isArray()){
                if(c.getComponentType().getComponentType().isArray()) {
                    System.out.println(tab + "CLASS: " + c.getComponentType().getComponentType().getComponentType().getName() + "[[[]]]");
                }else {
                    System.out.println(tab + "CLASS: " + c.getComponentType().getComponentType().getName() + "[[]]");
                }
            }else {
                System.out.println(tab + "CLASS: " + c.getComponentType().getName() + "[]");
            }
        }else {
            System.out.println(tab + "CLASS: " + c.getName());
        }
        Class superClass = c.getSuperclass();

        // handle case when class and superclass both equal object
        if(c.equals(Object.class) && superClass == null){
            return;
        }
        //print next superclass
        System.out.println(tab2 + "SUPERCLASS: " + superClass.getName());
        inspectClass(superClass, obj, depth+2);

    }

    private void inspectInterface(Class c, Object obj, int depth) throws IllegalAccessException {
        //System.out.println("inspecting interface for " + c);
        depth += 1;
        Class[] interfaces = c.getInterfaces();

        // handling formatting
        String[] tabs = getTab(depth);
        String tab = tabs[0];

        if(interfaces.length==0){
            System.out.println(tab + "** NO SUPERINTERFACE **");
        }else{
            // class does have interfaces
            for (Class anInterface : interfaces) {
                System.out.println(tab + "INTERFACE: " + anInterface.getName());
                inspectInterface(anInterface, obj, depth);
                inspectMethod(anInterface, depth);
                inspectField(anInterface, obj, depth);
            }
        }
    }

    private void inspectConstructor(Class c, int depth){
        depth += 1;
        // handling formatting
        String[] tabs = getTab(depth);
        String tab = tabs[0];
        String tab2 = tabs[1];

        // list of each constructors for a class
        Constructor[] constructors = c.getDeclaredConstructors();
        for (Constructor constructor : constructors) {
            System.out.println(tab + "CONSTRUCTOR:");
            System.out.println(tab2 + "Name: " + constructor.getName());

            //parameters
            Class[] parameterTypes = constructor.getParameterTypes();
            System.out.print(tab2 + "Parameter Types: ");
            if (parameterTypes.length == 0) {
                System.out.println("None");
            } else {
                for (Class parameter : parameterTypes) {
                    System.out.print(parameter.getName() + " ");
                }
                System.out.println("");
            }

            //modifier
            int mod = constructor.getModifiers();
            System.out.println(tab2 + "Modifier: " + Modifier.toString(mod));
        }
    }

    private void inspectMethod(Class c, int depth){
        depth += 1;
        // handling formatting
        String[] tabs = getTab(depth);
        String tab = tabs[0];
        String tab2 = tabs[1];


        //get methods
        Method[] methods = c.getDeclaredMethods();

        //if no methods
        if(methods.length == 0){
            System.out.println(tab + "** NO METHODS **");
            return;
        }
        //iterate through all methods
        for (Method method : methods) {
            System.out.println(tab + "METHOD:");

            //name
            System.out.println(tab2 + "Name: " + method.getName());

            //exceptions
            Class[] exceptionTypes = method.getExceptionTypes();
            System.out.print(tab2 + "Exception Types: ");
            if (exceptionTypes.length == 0) {
                System.out.println("None");
            } else {
                for (Class exception : exceptionTypes) {
                    System.out.print(exception.getName() + " ");
                }
                System.out.println("");
            }

            //parameters
            Class[] parameterTypes = method.getParameterTypes();
            System.out.print(tab2 + "Parameter Types: ");
            if (parameterTypes.length == 0) {
                System.out.println("None");
            } else {
                for (Class parameter : parameterTypes) {
                    System.out.print(parameter.getName() + " ");
                }
                System.out.println("");
            }

            //return type
            System.out.println(tab2 + "Return Type: " + method.getReturnType().getName());

            //modifier
            int mod = method.getModifiers();
            System.out.println(tab2 + "Modifier: " + Modifier.toString(mod));
        }

    }

    private void inspectField(Class c, Object obj, int depth) throws IllegalAccessException {
        depth += 1;
        // handling formatting
        String[] tabs = getTab(depth);
        String tab = tabs[0];
        String tab2 = tabs[1];
        String tab3 = tabs[2];
        String tab4 = tabs[3];

        //get all fields of class c
        Field[] fields = c.getDeclaredFields();

        //check if any fields:
        if(fields.length == 0){
            System.out.println(tab + "** NO FIELDS **");
            return;
        }
        for (Field field : fields) {
            System.out.println(tab + "FIELD: ");

            //name
            System.out.println(tab2 + "Name: " + field.getName());

            //type
            if (field.getType().isPrimitive()) {
                System.out.println(tab2 + "Type: " + field.getType());
            } else if (field.getType().isArray()) {
                System.out.println(tab2 + "Type: " + field.getType().getComponentType() + "[]");
            }
            //modifiers
            int mod = field.getModifiers();
            System.out.println(tab2 + "Modifier: " + Modifier.toString(mod));

            //value
            //If the field is an object reference, and recursive is set to false, then simply print out the
            //“reference value” directly (this will be the name of the object’s class plus the object’s
            //“identity hash code” ex. java.lang.Object@7d4991ad).
            field.setAccessible(true);
            Object value = field.get(obj);

            // if value is null / not init
            if (value == null) {
                System.out.println(tab2 + "Value: null");
                continue;
            }

            //if primitive wrapper
            if (isWrapperType(value.getClass())) {
                System.out.println(tab2 + "Value: " + value.toString());
                //if array
            } else if (value.getClass().isArray()) {
                //print out name, component type, length, and contents
                System.out.println(tab2 + "Value: ");
                //component type:
                System.out.println(tab3 + "Type: " + field.getType().getComponentType());
                //length:
                int len = Array.getLength(value);
                System.out.println(tab3 + "Length: " + len);
                //contents:
                System.out.println(tab3 + "Contents: [");
                //contents of the array
                for (int j = 0; j < len; j++) {
                    Object arrObj = Array.get(value, j);
                    System.out.println(tab4 + arrObj);
                }
                System.out.println(tab3 + "]");
            } else {
                //if object reference
                inspectClass(value.getClass(), value, depth + 1);

            }
        }
    }

    private static String[] getTab(int depth){
        String[] tab = {"","","",""};
        for(int i=0; i<depth; i++){
            tab[0] += "\t";
            tab[1] += "\t";
            tab[2] += "\t";
            tab[3] += "\t";
        }
        tab[1] += "\t";
        tab[2] += "\t\t";
        tab[3] += "\t\t\t";
        /*
        System.out.println("Tabbing inside method test:");
        System.out.println(tab[0] + "here");
        System.out.println(tab[1] + "here");
        System.out.println(tab[2] + "here");
        System.out.println(tab[3] + "here");
         */
        return tab;
    }

    // past this point is code gotten from https://stackoverflow.com/questions/709961/determining-if-an-object-is-of-primitive-type
    // that checks if an object is a wrapper for a primitive
    private static final Set<Class<?>> WRAPPER_TYPES = getWrapperTypes();

    public static boolean isWrapperType(Class<?> clazz)
    {
        return WRAPPER_TYPES.contains(clazz);
    }

    private static Set<Class<?>> getWrapperTypes()
    {
        Set<Class<?>> ret = new HashSet<Class<?>>();
        ret.add(Boolean.class);
        ret.add(Character.class);
        ret.add(Byte.class);
        ret.add(Short.class);
        ret.add(Integer.class);
        ret.add(Long.class);
        ret.add(Float.class);
        ret.add(Double.class);
        ret.add(Void.class);
        return ret;
    }



}
