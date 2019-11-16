package receiver;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.After;
import java.io.*;

public class DeserializerTest {
    @After
    public void after(){
        System.out.println("All tests completed");
    }

    @Before
    public void before(){
        System.out.println("initiating tests");
    }

    @Test
    public void testCheckType(){
        String[] values = {"1", "1.1", "false", "1.2"};
        Class[] fieldTypes = {int.class, float.class, boolean.class, double.class};
        assertEquals(1, Deserializer.checkType(values[0], fieldTypes[0]));
        assertEquals((float) 1.1, Deserializer.checkType(values[1], fieldTypes[1]));
        assertEquals(false, Deserializer.checkType(values[2], fieldTypes[2]));
        assertEquals((double) 1.2, Deserializer.checkType(values[3], fieldTypes[3]));

    }


}
