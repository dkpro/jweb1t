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

/**
 * Class that sequentially scans all Web1T-format files in order to compute the aggregated counts.
 */
public class AggregatedCountsCreator
{
    private long aggregatedNGramCount;
    private long nrOfDifferentNGrams;
    
	public AggregatedCountsCreator(final File aBaseDir, final String aNGramSize)
	    throws IOException
	{
	    aggregatedNGramCount = 0;
	    nrOfDifferentNGrams = 0;

        final File ngramFile = new File(aBaseDir + "/" + aNGramSize + "gms/");
        
        if (ngramFile.isFile()) {
			read(ngramFile);
		}
		else {
			final FolderScanner scanner = new FolderScanner(ngramFile);
			scanner.setFilter(new IndexFilter());
			
			while (scanner.hasNext()) {
				for (final File file : scanner.next()) {
					read(file);
				}
			}
		}
	}
	
    private void read(final File aFile) throws IOException
    {
        try (LineNumberReader reader = new LineNumberReader(
                new InputStreamReader(new FileInputStream(aFile), Constants.ENCODING))) {
            String line;
            while ((line = reader.readLine()) != null) {
                final String[] parts = line.split("\t");

                if (parts.length != 2) {
                    continue;
                }

                nrOfDifferentNGrams++;

                aggregatedNGramCount += Long.valueOf(parts[1]);
            }
        }
    }

    public long getAggregatedNGramCount()
    {
        return aggregatedNGramCount;
    }

    public long getNrOfDifferentNGrams()
    {
        return nrOfDifferentNGrams;
    }
}