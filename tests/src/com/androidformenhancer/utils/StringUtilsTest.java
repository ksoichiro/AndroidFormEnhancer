
package com.androidformenhancer.utils;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

/**
 * @author Soichiro Kashima
 */
public class StringUtilsTest extends TestCase {

    public void testSerialize() throws Exception {
        assertEquals("", StringUtils.serialize(null));
        List<String> errorMessages = new ArrayList<String>();
        assertEquals("", StringUtils.serialize(errorMessages));
        final String message1 = "Test";
        errorMessages.add(message1);
        assertEquals(message1, StringUtils.serialize(errorMessages));
        final String message2 = "TEST";
        errorMessages.add(message2);
        assertEquals(message1 + "\n" + message2, StringUtils.serialize(errorMessages));
        final String message3 = "あいうえお";
        errorMessages.add(message3);
        assertEquals(message1 + "\n" + message2 + "\n" + message3,
                StringUtils.serialize(errorMessages));
        final String message4 = "123\n456";
        errorMessages.add(message4);
        assertEquals(message1 + "\n" + message2 + "\n" + message3 + "\n" + message4,
                StringUtils.serialize(errorMessages));
    }
}
