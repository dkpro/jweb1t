/*
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
 */
package com.googlecode.jweb1t;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class JWeb1TAggregatorTest
{
	private static final String DATA_DIR = "src/test/resources/";

	@Test
	public void jweb1TIndexerTest()
		throws IOException
	{
		final JWeb1TAggregator aggregator = new JWeb1TAggregator(DATA_DIR, 2);
		aggregator.create();

		final File countFile = new File(DATA_DIR, JWeb1TAggregator.AGGREGATED_COUNTS_FILE);
		assertTrue(countFile.exists());

		final JWeb1TSearcher web1t = new JWeb1TSearcher(new File(DATA_DIR), 1, 2);
		assertEquals(13893397919l, web1t.getNrOfNgrams(1));
		assertEquals(6042, web1t.getNrOfNgrams(2));
		assertEquals(11, web1t.getNrOfDistinctNgrams(1));
		assertEquals(21, web1t.getNrOfDistinctNgrams(2));

		countFile.delete();
	}
}
