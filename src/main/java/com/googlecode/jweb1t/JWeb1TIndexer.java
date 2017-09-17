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

import java.io.File;
import java.io.IOException;

/**
 * Provides a method to create the indexes to access the web1t corpus.
 * 
 * @author zesch, Mateusz Parzonka
 * 
 */
public class JWeb1TIndexer
{
	private final String ngramLocation;
	private final int maxNgramSize;

	private final JWeb1TAggregator aggregator;

	public JWeb1TIndexer(final String aNGramLocation, final int aMaxNGramSize)
		throws IOException
	{
		ngramLocation = aNGramLocation;
		maxNgramSize = aMaxNGramSize;
		aggregator = new JWeb1TAggregator(aNGramLocation, aMaxNGramSize);
	}

	/**
	 * Run this method to create the indexes.
	 * 
	 * @throws IOException
	 */
	public void create()
		throws IOException
	{
		for (int i = 1; i <= maxNgramSize; i++) {
			new IndexCreator(new File(ngramLocation), Integer.toString(i));
		}

		// also run the aggregator
		aggregator.create();
	}
}