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

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class JWeb1TIndexerTest
{
	private static final String INDEX_FILE_1 = "src/test/resources/index-1gms";
	private static final String INDEX_FILE_2 = "src/test/resources/index-2gms";

	@Test
	public void jweb1TIndexerTest()
		throws IOException
	{
		final JWeb1TIndexer indexer = new JWeb1TIndexer("src/test/resources/", 2);
		indexer.create();

		assertTrue(new File(INDEX_FILE_1).exists());
		assertTrue(new File(INDEX_FILE_2).exists());

		// indexer also runs the aggregator
		final File countFile = new File("src/test/resources", JWeb1TAggregator.AGGREGATED_COUNTS_FILE);
		assertTrue(countFile.exists());

		countFile.delete();
	}
}
