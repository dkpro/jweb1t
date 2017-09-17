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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Memory-based implementation of the {@link Searcher} interface for accessing the data in Web1T-format.
 * Requires a <b>huge</b> amount of memory for all but the tiniest ngram models.
 */
public class JWeb1TSearcherInMemory
	implements Searcher
{
	private final static Log LOG = LogFactory.getLog(JWeb1TSearcherInMemory.class);

	private final Map<Integer, FrequencyDistribution<String>> ngramLevelMap;

	private Map<Integer, Long> ngramCountMap;
	private Map<Integer, Long> ngramDistinctCountMap;

	public JWeb1TSearcherInMemory(final String ngramLocation, final int maxNgramSize)
		throws IOException
	{
		ngramLevelMap = new HashMap<Integer, FrequencyDistribution<String>>();

		final File ngramBaseDir = new File(ngramLocation);
		initialize(ngramBaseDir);

		for (int level = 1; level <= maxNgramSize; level++) {
			ngramLevelMap.put(level, new FrequencyDistribution<String>());

			final File ngramFile = new File(ngramBaseDir, level + "gms/");

			if (ngramFile.isFile()) {
				fillMap(ngramFile, level);
			}
			else {
				if (!ngramFile.isDirectory()) {
					throw new IOException(ngramFile + " is not a directory.");
				}
				final FolderScanner scanner = new FolderScanner(ngramFile);
				scanner.setFilter(new IndexFilter());

				while (scanner.hasNext()) {
					final File[] files = scanner.next();
					for (int i = 0; i < files.length; i++) {
						fillMap(files[i], level);
					}
				}
			}
		}
	}

    private void fillMap(final File aFile, final int aLevel) throws IOException
    {
        try (LineNumberReader reader = new LineNumberReader(
                new InputStreamReader(new FileInputStream(aFile), Constants.ENCODING))) {
            String line;
            while ((line = reader.readLine()) != null) {
                final String[] parts = line.split("\t");

                if (parts.length != 2) {
                    continue;
                }

                ngramLevelMap.get(aLevel).addSample(parts[0], Long.parseLong(parts[1]));
            }
        }
    }

	private void initialize(final File baseDir)
		throws NumberFormatException, IOException
	{
		ngramCountMap = new HashMap<Integer, Long>();
		ngramDistinctCountMap = new HashMap<Integer, Long>();

		final File countFile = new File(baseDir, JWeb1TAggregator.AGGREGATED_COUNTS_FILE);
		if (countFile.exists()) {
            try (LineNumberReader lineReader = new LineNumberReader(
                    new InputStreamReader(new FileInputStream(countFile), Constants.ENCODING))) {
        			String line;
        			while ((line = lineReader.readLine()) != null) {
        				final String[] parts = line.split("\t");
        
        				if (parts.length != 3) {
        					continue;
        				}
        
        				final int ngramSize = Integer.valueOf(parts[0]);
        				final long ngramDistinctCount = Long.valueOf(parts[1]);
        				final long ngramCount = Long.valueOf(parts[2]);
        
        				ngramCountMap.put(ngramSize, ngramCount);
        				ngramDistinctCountMap.put(ngramSize, ngramDistinctCount);
        			}
        			lineReader.close();
		    }
		}

	}

    @Override
    public long getFrequency(final Collection<String> aPhrase)
        throws IOException
    {
        return getFrequency(StringUtils.join(aPhrase, " "));
    }
    
    @Override
    public long getFrequency(final String... aPhrase)
        throws IOException
    {
    	if (aPhrase == null || aPhrase.length == 0) {
    		return 0;
    	}
    	
        final String phrase = StringUtils.join(aPhrase, " ").trim();

		if (phrase.length() == 0) {
			return 0;
		}

		final int ngramLevel = phrase.split("\\s+").length;

		if (LOG.isDebugEnabled()) {
			LOG.debug("search for : \"" + phrase + "\"");
			LOG.debug("length: " + ngramLevel);
		}

		if (!ngramLevelMap.containsKey(ngramLevel)) {
			return 0;
		}

		if (!ngramLevelMap.get(ngramLevel).contains(phrase)) {
			return 0;
		}

		return ngramLevelMap.get(ngramLevel).getCount(phrase);
    }

	@Override
    public long getNrOfNgrams(final int aNGramSize)
	{
		final Long count = ngramCountMap.get(aNGramSize);
		return count == null ? -1 : count;
	}

	@Override
    public long getNrOfDistinctNgrams(final int aNGramSize)
	{
		final Long count = ngramDistinctCountMap.get(aNGramSize);
		return count == null ? -1 : count;
	}
}