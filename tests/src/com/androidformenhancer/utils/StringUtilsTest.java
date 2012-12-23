/*
 * Copyright 2012 Soichiro Kashima
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
