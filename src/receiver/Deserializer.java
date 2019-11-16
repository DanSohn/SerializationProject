/*
Reflective Deserializer
Basic Design:
1) Get list of objects stored in XML document
    Use getRootElement() from Document class and getChildren() from Element class
    Doing so will get me Element objects.
2) For each object, create an uninitialized instance:
    1. Dynamically load its class using forName()
        • The class name is an attribute of the object element
    2. Create an instance of the class
        • If a non-array object, get the declared no-arg constructor,
          then use newInstance()
            • May need to setAccessible(true)
        • If an array object, use Array.newInstance(. . .)
            • Use getComponentType() to find element type
            • The length is an attribute of the object element
    3. Associate the new instance with the object’s unique identifier number using a table
        • java.util.HashMap is ideal
            • The id is the key
            • The object reference is the value
        • The id is an attribute of the object element
3) Assign values to all instance variables in each nonarray object:
    1. Get a list of the child elements
        • Use getChildren() from Element class
        • Each child is a field of the object
    2. Iterate through each field in the list
        1. Find the name of its declaring class
            • Is an attribute of field element
        2. Load the class dynamically
        3. Find the field name
            • Is an attribute of field element
        4. Use getDeclaredField() to find Field metaobject
        5. Initialize the value of the field using set()
            • If a primitive type, use the stored value (use getText()
            and create appropriate wrapper object)
            • If a reference, use the unique identifier to find the
            corresponding instance in the table
            • May need to setAccessible(true)
 */


package receiver;


import org.jdom2.*;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import sender.Client;

import java.io.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.lang.*;
import java.lang.reflect.*;

public class Deserializer {
    public static void main(String[] args) throws Exception {
        // receive from the server the object
        Object object = Server.Receiver();
        // cast that object back into a document
        Document doc = (Document) object;
        Object docObject = deserialize(doc);
        boolean recursive = true;

        outputObject(docObject, recursive);
    }

    public static void outputObject(Object testObj, boolean recursive) throws Exception{
        String filename = "visualizer.txt";
        try {
            PrintStream old = System.out;
            File file = new File(filename);
            FileOutputStream fos = new FileOutputStream(file);
            PrintStream ps = new PrintStream(fos);
            System.setOut(ps);
            System.out.println("======================================================");
            System.out.println("Filename: " + filename);
            System.out.println("Running Test: " + testObj);
            System.out.println("Recursive: " + recursive);
            new Visualizer().inspect(testObj, recursive);
            System.out.println("======================================================");
            ps.flush();
            fos.flush();
            ps.close();
            fos.close();
            System.setOut(old);
        } catch (IOException ioe) {
            System.err.println("Unable to open file: " + filename);
        } catch (Exception e) {
            System.err.println("Unable to completely run test: " + testObj);
            e.printStackTrace();
        }

    }
    public static Object deserialize(org.jdom2.Document doc) throws Exception {


        Element[] elements = ElementChildren(doc.getRootElement());

        //hashmap storing instances with their unique IDs
        HashMap<Integer, Object> instanceID = new HashMap<Integer, Object>();

        Object obj = null;
        // for each object element in the xml document, find the class name and create instance of the class
        for(Element element: elements){
            String className = element.getAttributeValue("class");
            System.out.println("Class name: " + className);
            //class object for the element object
            Class classObject = Class.forName(className);

            //check if non-array object, or if array object
            obj = null;
            if(classObject.isArray()){
                //array object
                //find element type
                Class componentType = classObject.getComponentType();
                // size of array is an attribute of the object element
                int length = Integer.parseInt(element.getAttributeValue("length"));
                obj = Array.newInstance(componentType, length);
            }else{
                //non-array object
                // utilizing the no args constructor by explicitly mentioning the no argument constructor
                // in the case that is private and I need to set it accessible
                Constructor noArgCons = classObject.getDeclaredConstructor(null);
                noArgCons.setAccessible(true);
                obj = noArgCons.newInstance();
            }
            // at this point, obj is an instance of the element's class
            // stores each instance with its unique ID (hashcode?)
            Integer ID = Integer.parseInt(element.getAttributeValue("id"));
            instanceID.put(ID, obj);

            //step 3 of the basic design. Assigning values to all instance variables
            //get list of all child elements. each child is a field of the object
            Element[] children = ElementChildren(element);

            //iterate through each FIELD in the OBJECT LIST
            for(Element child:children){
                System.out.println("Child: " + child.getName());

                // under assumption that we don't do arrays, I skip array children
                if(!child.getName().equals("field")){
                    break;
                }
                //find name of its declaring class
                String declaringClass = child.getAttributeValue("declaringclass");
                //load class dynamically
                Class classObj = Class.forName(declaringClass);
                //find the field name
                String fieldName = child.getAttributeValue("name");
                //get the field metaobject
                Field field = classObj.getDeclaredField(fieldName);
                //set accessible in case private
                field.setAccessible(true);
                //store field's type
                Class fieldType = field.getType();


                //initialize value of field using set()
                //if(fieldType.isPrimitive() || isWrapperType(fieldType)){
                if(child.getChild("value") != null){
                    String value = child.getChild("value").getText();
                    System.out.println(value);
                    System.out.println(fieldName);
                    System.out.println(fieldType.toString());
                    // since i don't know the actual field type, I cast the value (received as a string) into its actual class
                    //Object val = fieldType.cast(value);
                    Object val = checkType(value, fieldType);


                    field.set(obj, val);
                }else{
                    Integer refID = Integer.parseInt(child.getChild("reference").getText());
                    Object refObj = instanceID.get(refID);
                    field.set(obj, refObj);
                }
            }

        } // end of for loop


        return obj;
    }


    public static Object checkType(String value, Class fieldType){
        Object val = null;
        if (fieldType.equals(int.class)){
            val = Integer.parseInt(value);
        }else if(fieldType.equals(float.class)){
            val = Float.parseFloat(value);
        }else if(fieldType.equals(boolean.class)){
            val = Boolean.parseBoolean(value);
        }else if(fieldType.equals(double.class)){
            val = Double.parseDouble(value);
        }
        return val;
    }

    public static Element[] ElementChildren(Element root){
        List<Element> elementList =  root.getChildren();
        Element[] elementArray = new Element[elementList.size()];
        elementArray = elementList.toArray(elementArray);
        return elementArray;
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
