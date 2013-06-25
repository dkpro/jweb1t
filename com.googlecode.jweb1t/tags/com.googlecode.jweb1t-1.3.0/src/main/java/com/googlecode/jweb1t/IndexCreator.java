/*
 * Copyright 2007 FBK-irst http://www.itc.it/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.googlecode.jweb1t;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Creates an index showing which ngrams (indexed by the first two characters) are to be found in which files on the disk.
 * 
 * @author Claudio Giuliano
 */
public class IndexCreator
{
	private final Log logger = LogFactory.getLog(this.getClass());

	private final Map<String, List<File>> map;

	private final File baseDir;

	public IndexCreator(final File aBaseDir, final String aNGramSize)
		throws IOException
	{
		map = new HashMap<String, List<File>>();

		baseDir = aBaseDir;

		final File ngramFile = new File(aBaseDir + "/" + aNGramSize + "gms/");
		final File indexFile = new File(aBaseDir + "/" + "index-" + aNGramSize + "gms");

		if (ngramFile.isFile()) {
			read(ngramFile);
		}
		else {
			final FolderScanner scanner = new FolderScanner(ngramFile);
			scanner.setFilter(new IndexFilter());

			int count = 0;
			while (scanner.hasNext()) {
				final File[] files = scanner.next();
				logger.info((count++) + " : " + files.length);
				for (int i = 0; i < files.length; i++) {
					final long begin = System.currentTimeMillis();
					read(files[i]);

					final long end = System.currentTimeMillis();
					logger.info(count + ", " + files[i] + " read in " + (end - begin) + " ms");
					count++;
				}
			}
		}

		write(indexFile);
	}

	private void write(final File outputFile)
		throws IOException
	{
		PrintWriter writer = null;

		try {
			writer = new PrintWriter(new FileWriter(outputFile));

			for (final String ch : map.keySet()) {
				final List<File> fileList = map.get(ch);
				writer.print(ch);
				for (final File file : fileList) {
					// store only the path relative to the index file
					final String relative = baseDir.toURI().relativize(file.toURI()).getPath();
					writer.print("\t" + relative);
				}
				writer.print("\n");
			}
			writer.flush();
		}
		finally {
			IOUtils.closeQuietly(writer);
		}
	}

	private void read(final File aFile)
		throws IOException
	{
		LineNumberReader reader = null;
		try {
			reader = new LineNumberReader(new FileReader(aFile));
			String line = null;
			String ch = null;

			// first line
			if ((line = reader.readLine()) != null) {
				ch = line.substring(0, 2).trim();
			}

			String old = ch;

			// second line
			while ((line = reader.readLine()) != null) {
				ch = line.substring(0, 2).trim();

				if (!ch.equals(old)) {
					put(old, aFile);
					old = ch;
				}
			}

			put(old, aFile);
		}
		finally {
			IOUtils.closeQuietly(reader);
		}
	}

	private void put(final String ch, final File aFile)
	{
		List<File> fileList = map.get(ch);

		if (fileList == null) {
			fileList = new ArrayList<File>();
			fileList.add(aFile);
			map.put(ch, fileList);
			return;
		}

		fileList.add(aFile);
	}
}