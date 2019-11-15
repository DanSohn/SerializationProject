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

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.lang.*;
import java.lang.reflect.*;

public class Deserializer {
    public static void main(String[] args) throws Exception{
        // receive from the server the object
        Object object = Server.Receiver();
        // cast that object back into a document
        Document doc = (Document) object;

        Element[] elements = ElementChildren(doc.getRootElement());

        //hashmap storing instances with their unique IDs
        HashMap<Integer, Object> instanceID = new HashMap<Integer, Object>();

        // for each object element in the xml document, find the class name and create instance of the class
        for(Element element: elements){
            String className = element.getAttributeValue("class");
            //class object for the element object
            Class classObject = Class.forName(className);

            //check if non-array object, or if array object
            Object obj = null;
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



        }

    }

    public static Element[] ElementChildren(Element root){
        List<Element> elementList =  root.getChildren();
        Element[] elementArray = new Element[elementList.size()];
        elementArray = elementList.toArray(elementArray);
        return elementArray;
    }
}
