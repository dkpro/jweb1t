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
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Search-on-disk based implementation of the {@link Searcher} interface for accessing the data in Web1T-format.
 * Requires very little memory, but is rather slow.
 */
public class JWeb1TSearcher
	implements Searcher
{
	private final Log logger = LogFactory.getLog(JWeb1TSearcher.class);

	// map with index files
	private Map<Integer, FileMap> indexMap;

	private Map<Integer, Long> ngramCountMap;
	private Map<Integer, Long> ngramDistinctCountMap;

	public JWeb1TSearcher(final String... indexFiles)
		throws IOException
	{
		if (indexFiles.length > 0) {
			initialize(new File(indexFiles[0]).getParentFile());
		}
		fillIndexMap(indexFiles);
	}

	/**
	 * Try to deduce the index files from the given path.
	 * 
	 * @param indexPath
	 *            The path in which the ngram index files are located.
	 * @param minN
	 *            The minimum ngram length.
	 * @param maxN
	 *            The maximum ngram length.
	 * @throws IOException
	 */
	public JWeb1TSearcher(final File indexPath, final int minN, final int maxN)
		throws IOException
	{
		initialize(indexPath);

		if (minN < 0 || maxN < 0 || minN > maxN) {
			throw new IOException("Wrong parameters.");
		}

		final int size = maxN - minN + 1;
		final String[] indexFiles = new String[size];
		for (int i = 0; i < size; i++) {
			final int ngramLevel = minN + i;
			indexFiles[i] = new File(indexPath, "index-" + ngramLevel + "gms").getAbsolutePath();
		}

		fillIndexMap(indexFiles);
	}

	private void initialize(final File baseDir)
		throws NumberFormatException, IOException
	{
		ngramCountMap = new HashMap<Integer, Long>();
		ngramDistinctCountMap = new HashMap<Integer, Long>();

		final File countFile = new File(baseDir, JWeb1TAggregator.AGGREGATED_COUNTS_FILE);
		if (countFile.exists()) {
			final LineNumberReader lineReader = new LineNumberReader(new FileReader(countFile));
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

	private void fillIndexMap(final String[] indexFiles)
		throws IOException
	{
		indexMap = new HashMap<Integer, FileMap>();
		for (int i = 1; i <= indexFiles.length; i++) {
			final File file = new File(indexFiles[i - 1]);
			if (!(file.exists())) {
				throw new IOException("Index file " + file.getPath() + " was not found");
			}
			final FileMap fileMap = new FileMap(file);
			indexMap.put(i, fileMap);
		}
	}

    public long getFrequency(Collection<String> aPhrase)
        throws IOException
    {
        return getFrequency(StringUtils.join(aPhrase, " "));
    }
    
    public long getFrequency(String[] aPhrase)
        throws IOException
    {
        return getFrequency(StringUtils.join(aPhrase, " "));
    }
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.googlecode.jweb1t.Searcher#getFrequency(java.lang.String)
	 */
	public long getFrequency(final String aPhrase)
		throws IOException
	{
		final String phrase = aPhrase.trim();

		if (phrase.length() == 0) {
			return 0;
		}

		logger.debug("search for : \"" + phrase + "\"");

		final String[] tokens = phrase.split("\\s+");
		logger.debug("length: " + tokens.length);

		final FileMap map = indexMap.get(Integer.valueOf(tokens.length));
		if (map == null) {
			logger.fatal(tokens.length + "-gram index not found");
			return 0;
		}

		String symbol = null;

		if (tokens[0].length() < 2) {
			symbol = tokens[0].substring(0, 1);
		}
		else {
			symbol = tokens[0].substring(0, 2);
		}

		final String[] file = map.get(symbol);

		if (file == null) {
			logger.warn("Could not find nGram-File for the symbol: " + symbol);
			return -1;
		}

		// FIXME really faster than join()?
		final StringBuilder ngram = new StringBuilder();
		for (int i = 0; i < tokens.length; i++) {
			if (i > 0) {
				ngram.append(' ');
			}
			ngram.append(tokens[i]);

		}

		for (int i = 0; i < file.length; i++) {
			logger.debug(i + ":" + file[i]);

			final FileSearch fileSearch = new FileSearch(new File(file[i]));

			final long frequency = fileSearch.getFreq(ngram.toString());

			if (frequency != 0) {
				fileSearch.close();
				logger.debug("Frequency: " + frequency);
				return frequency;
			}
			fileSearch.close();
		}
		logger.debug("Frequency: 0");
		return 0;
	}

	public long getNrOfNgrams(final int aNGramSize)
	{
		final Long count = ngramCountMap.get(aNGramSize);
		return count == null ? -1 : count;
	}

	public long getNrOfDistinctNgrams(final int aNGramSize)
	{
		final Long count = ngramDistinctCountMap.get(aNGramSize);
		return count == null ? -1 : count;
	}
}