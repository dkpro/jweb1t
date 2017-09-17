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
import java.util.LinkedList;
import java.util.Queue;

import com.googlecode.jweb1t.util.NGramIterator;

/**
 * Iterates over all n-grams of a given size in the data.
 */
public class JWeb1TIterator
{
	private final String ngramLocation;
	private final int ngramSize;

	public JWeb1TIterator(final String aNGramLocation, final int aNGramSize)
		throws IOException
	{
		ngramLocation = aNGramLocation;
		ngramSize = aNGramSize;
	}

	/**
	 * Returns an iterator over the ngrams.
	 * Note that a certain order is not guaranteed.
	 * 
	 * @throws IOException
	 */
	public NGramIterator getIterator()
	        throws IOException
	{

        final File ngramFile = new File(ngramLocation + "/" + ngramSize + "gms/");
        
        Queue<File> ngramFiles = new LinkedList<File>();
        
        if (ngramFile.isFile()) {
            ngramFiles.add(ngramFile);
        }
        else {
            final FolderScanner scanner = new FolderScanner(ngramFile);
            scanner.setFilter(new IndexFilter());
            
            while (scanner.hasNext()) {
                for (final File file : scanner.next()) {
                    ngramFiles.add(file);
                }
            }
        }
                
        return new NGramIterator(ngramFiles);
	}
}