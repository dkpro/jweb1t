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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.io.IOUtils;

/**
 * Creates some aggregated counts that are not directly available in the data.
 */
public class JWeb1TAggregator
{
	public final static String AGGREGATED_COUNTS_FILE = "aggregated_counts.cnt";

	private final String ngramLocation;
	private final int maxNgramSize;

	public JWeb1TAggregator(final String aNGramLocation, final int aMaxNGramSize)
		throws IOException
	{
		ngramLocation = aNGramLocation;
		maxNgramSize = aMaxNGramSize;
	}

	/**
	 * Run this method to create the counts.
	 * 
	 * @throws IOException
	 */
	public void create()
		throws IOException
	{
		PrintWriter writer = null;

		try {
			writer = new PrintWriter(new FileWriter(new File(ngramLocation, AGGREGATED_COUNTS_FILE)));

			for (int i = 1; i <= maxNgramSize; i++) {
				final AggregatedCountsCreator counter = new AggregatedCountsCreator(new File(
						ngramLocation), Integer.toString(i));
				writer.print(i);
				writer.print("\t");
				writer.print(counter.getNrOfDifferentNGrams());
				writer.print("\t");
				writer.print(counter.getAggregatedNGramCount());
				writer.print("\n");
			}
			writer.flush();
		}
		finally {
			IOUtils.closeQuietly(writer);
		}
	}
}