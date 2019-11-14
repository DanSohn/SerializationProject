/*
Reflective Serializer, serializing any object passed as a parameter
Basic Design
1) Give object a unique identifier number
2) Get a list of all the object's fields, of all visibilities
3) Uniquely identify each field with its declaring class and field name
4) Get value for each field
    1) If primitive, simple store it so it can be easily retrieved
    2) IF non-array object, recursively serialize the object
        • Use the new object’s unique id number as a reference
        • Store the reference as the field value in the originating object
        • Don’t serialize an object more than once
        • Occurs when you have several references to the same object
    3) If array object, serialize it
        Serialize each element of the array, and use recursion of element is an object
 */

package sender;

import org.jdom2.*;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.lang.*;
import java.lang.reflect.*;

public class Serializer {

    public static void main(String[] args) throws Exception{
        ObjectCreator myObj = new ObjectCreator();
        Object obj = myObj.objectCreator();
        Document d = serialize(obj);
        Object sendObject = d;
    }
    public static Object serializetoSocket() throws Exception{
        ObjectCreator myObj = new ObjectCreator();
        Object obj = myObj.objectCreator();
        Document d = serialize(obj);
        Object sendObject = d;
        return sendObject;
    }

    public static Document serialize(Object obj) throws Exception{
        Element rootEl = new Element("serialized");
        Document d = new Document(rootEl);
        //add all elements ( just a single if primitives, no references, multiple if references)
        for(Element element: serializeObject(obj)){
            rootEl.addContent(element);
        }
        try{
            new XMLOutputter().output(d, System.out);
            XMLOutputter xmlOut = new XMLOutputter();
            xmlOut.setFormat(Format.getPrettyFormat());
            xmlOut.output(d, new FileWriter("serialized.xml"));

        }catch(IOException e){
            e.printStackTrace();
        }

        return d;
    }

    public static Element[] serializeObject(Object obj) throws Exception{
        ArrayList<Element> myElements = new ArrayList<Element>();
        Class classObj = obj.getClass();
        // object element
        Element objEle = new Element("object");
        objEle.setAttribute("class", classObj.getName());
        objEle.setAttribute("id", String.valueOf(obj.hashCode()));

        //get list of fields
        Field[] fields = classObj.getDeclaredFields();
        for (Field field : fields) {
            //ensure that the field is accessible if set to protected/ private
            field.setAccessible(true);

            //each field will have two attributes: name and declaring class
            Element fieldEle = new Element("field");

            fieldEle.setAttribute("name", field.getName());

            //gets the class declaring class of field, then translates it into a string
            fieldEle.setAttribute("declaringclass", field.getDeclaringClass().getName());

            //retrieve value of field
            Object value = field.get(obj);

            //check if value is primitive, array, or non-array
            // remember, field is type Field
            if(field.getType().isPrimitive() || isWrapperType(field.getType())){
                Element fieldVal = new Element("value");
                fieldVal.addContent(String.valueOf(value));

                fieldEle.addContent(fieldVal);
                // add field element to object element
                objEle.addContent(fieldEle);
            // maybe field.getClass().isArray()
                //field.getType() is a Class type
            }else if(field.getType().isArray() || field.getType().equals(ArrayList.class)){

                // if the field type is an arraylist, i need to convert to array first to do the array functions
                if (field.getType().equals(ArrayList.class)){
                    //value is the arraylist OBJECT
                    value = ((ArrayList) value).toArray();
                }

                //array
                Element arrayEle = new Element("object");
                Class componentType = field.getType().getComponentType();

                arrayEle.setAttribute("class", String.valueOf(field.getType()));

                int length = Array.getLength(value);
                arrayEle.setAttribute("length", String.valueOf(length));

                Element fieldVal = null;
                // add the length of array at the end
                //check if primitive type or reference
                System.out.println(value.getClass().getComponentType());
                if(value.getClass().getComponentType().isPrimitive() || isWrapperType(value.getClass())){
                    for(int i = 0; i < length; i++){
                        fieldVal = new Element("value");
                        fieldVal.addContent(String.valueOf(Array.get(value, i)));
                        arrayEle.addContent(fieldVal);

                    }
                }else{
                    //reference - gets the object from array, finds the object's hash code and prints it out
                    for(int i = 0; i < length; i++) {
                        fieldVal = new Element("reference");
                        // reference the object's identity hash code
                        Object refObj = Array.get(value, i);
                        int objHashCode = refObj.hashCode();
                        fieldVal.addContent(String.valueOf(objHashCode));
                        arrayEle.addContent(fieldVal);

                        //reference object recursively caling serializeObject, and then passing it back to serialize
                        Element[] els = serializeObject(refObj);
                        Element refElement = els[0];
                        myElements.add(refElement);

                    }
                }


                // add field element to object element
                objEle.addContent(arrayEle);
            }else{
                //non array object
                Element fieldVal = new Element("reference");
                Object refObjCode = value.hashCode();
                fieldVal.addContent(String.valueOf(refObjCode));
                fieldEle.addContent(fieldVal);
                // add field element to object element
                objEle.addContent(fieldEle);

                //reference object recursively caling serializeObject, and then passing it back to serialize
                Element[] els = serializeObject(value);
                Element refElement = els[0];
                myElements.add(refElement);
            }

        }

        myElements.add(objEle);
        Collections.reverse(myElements);
        Element[] arr = new Element[myElements.size()];
        arr = myElements.toArray(arr);
        return arr;
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
