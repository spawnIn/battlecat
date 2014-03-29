/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.spawnin.battlecat.core.selectors;

import org.junit.Before;
import org.junit.Test;
import reactor.event.selector.Selector;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 * The test
 *
 * @author Patrick Sy (patrick.sy@get-it.us)
 */
public class ByTypeSelectorTest {
    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testMatches_ObjectTypeObject_shouldMatch() throws Exception {

        Selector s = new ByTypeSelector(Object.class);

        boolean result = s.matches(new Object());

        assertThat(result, is(true));

    }


    @Test
    public void testMatches_ObjectTypeString_shouldMatch() throws Exception {

        Selector s = new ByTypeSelector(Object.class);

        boolean result = s.matches("someString");

        assertThat(result, is(true));

    }


    @Test
    public void testMatches_StringTypeString_shouldMatch() throws Exception {

        Selector s = new ByTypeSelector(String.class);

        boolean result = s.matches("someString");

        assertThat(result, is(true));

    }


    @Test
    public void testMatches_StringTypeObject_shouldNotMatch() throws Exception {

        Selector s = new ByTypeSelector(String.class);

        boolean result = s.matches(new Object());

        assertThat(result, is(false));

    }


    @Test
    public void testMatches_null_shouldNotMatch() throws Exception {

        Selector s = new ByTypeSelector(Object.class);

        boolean result = s.matches(null);

        assertThat(result, is(false));

    }
}
