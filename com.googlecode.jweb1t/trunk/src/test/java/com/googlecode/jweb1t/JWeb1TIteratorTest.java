/*******************************************************************************
 * Copyright 2011
 * Ubiquitous Knowledge Processing (UKP) Lab
 * Technische Universität Darmstadt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.googlecode.jweb1t;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import com.googlecode.jweb1t.util.NGramIterator;

public class JWeb1TIteratorTest
{
	private static final String DATA_DIR = "src/test/resources/";

	@Test
	public void listUnigramsTest()
		throws IOException
	{
		final JWeb1TIterator iterator = new JWeb1TIterator(DATA_DIR, 1);
		
		NGramIterator ngramIterator = iterator.getIterator();
		int i=0;
		while (ngramIterator.hasNext()) {
		    System.out.println(ngramIterator.next());
		    i++;
		}
		assertEquals(11, i);
	}

   @Test
    public void listBigramsTest()
        throws IOException
    {
        final JWeb1TIterator iterator = new JWeb1TIterator(DATA_DIR, 2);
        
        NGramIterator ngramIterator = iterator.getIterator();
        int i=0;
        while (ngramIterator.hasNext()) {
            System.out.println(ngramIterator.next());
            i++;
        }
        assertEquals(21, i);
    }

    @Test
    public void multipleHasNextTest()
        throws IOException
    {
        final JWeb1TIterator iterator = new JWeb1TIterator(DATA_DIR, 1);
        
        NGramIterator ngramIterator = iterator.getIterator();
        assertTrue(ngramIterator.hasNext());
        assertTrue(ngramIterator.hasNext());
        assertTrue(ngramIterator.hasNext());
        assertTrue(ngramIterator.hasNext());
        assertTrue(ngramIterator.hasNext());
        assertTrue(ngramIterator.hasNext());
        assertTrue(ngramIterator.hasNext());
        assertTrue(ngramIterator.hasNext());
        assertTrue(ngramIterator.hasNext());
        assertTrue(ngramIterator.hasNext());
        assertTrue(ngramIterator.hasNext());
        assertTrue(ngramIterator.hasNext());
        assertTrue(ngramIterator.hasNext());
        assertTrue(ngramIterator.hasNext());
        assertTrue(ngramIterator.hasNext());
        assertTrue(ngramIterator.hasNext());
        assertTrue(ngramIterator.hasNext());
        
        assertEquals("!", ngramIterator.next());
    }

    @Test
    public void nextWithoutHasNext()
        throws IOException
    {
        final JWeb1TIterator iterator = new JWeb1TIterator(DATA_DIR, 1);
        
        NGramIterator ngramIterator = iterator.getIterator();
        assertEquals("!", ngramIterator.next());
    }
}
