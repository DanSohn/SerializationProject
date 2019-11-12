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
import java.util.IdentityHashMap;
import java.lang.*;
import java.lang.reflect.*;

public class Serializer {
    public Document serialize(Object obj) throws Exception{
        Element rootEl = new Element("serialized");
        Document d = new Document(rootEl);
        rootEl.addContent(serializeObject(obj));
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

    public Element serializeObject(Object obj) throws Exception{
        Class classObj = obj.getClass();
        // object element
        Element objEle = new Element(classObj.getName());
        objEle.setAttribute("class", classObj.getName());
        objEle.setAttribute("id", String.valueOf(classObj.hashCode()));

        //get list of fields
        Field[] fields = classObj.getDeclaredFields();
        for (Field field : fields) {
            //ensure that the field is accessible if set to protected/ private
            field.setAccessible(true);

            //each field will have two attributes: name and declaring class
            Element fieldEle = new Element(field.getName());
            fieldEle.setAttribute("name", field.getName());

            //gets the class declaring class of field, then translates it into a string
            fieldEle.setAttribute("declaringclass", field.getDeclaringClass().getName());

            //retrieve value of field
            Object value = field.get(obj);
        }



        return null;
    }
}
