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

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class JWeb1TSearcherTest
{

	@Test
	public void testIndexFiles()
		throws IOException
	{
		final Searcher web1t = new JWeb1TSearcher("src/test/resources/index-1gms",
				"src/test/resources/index-2gms");

		assertEquals(2147436244l, web1t.getFrequency("!"));
		assertEquals(528, web1t.getFrequency("Nilmeier"));
		assertEquals(106, web1t.getFrequency("influx takes"));
		assertEquals(69, web1t.getFrequency("frist will"));
	}

	@Test
	public void testPath()
		throws IOException
	{
		final Searcher web1t = new JWeb1TSearcher(new File("src/test/resources/"), 1, 2);

		assertEquals(2147436244l, web1t.getFrequency("!"));
		assertEquals(528, web1t.getFrequency("Nilmeier"));
		assertEquals(106, web1t.getFrequency("influx takes"));
		assertEquals(69, web1t.getFrequency("frist will"));
	}
}
