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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 *
 * Represents an index showing which ngrams (indexed by the first two characters) are to be found in which files on the disk.
 * 
 * @author Claudio Giuliano
 */
public class FileMap
{
	private static final String LF = System.getProperty("line.separator");

	private final Map<String, String[]> map;

	private final File indexFile;

	public FileMap(final File aIndexFile)
		throws IOException
	{
		indexFile = aIndexFile;
		map = new HashMap<String, String[]>();
		read(aIndexFile);
	}

    private void read(final File aFile) throws IOException
    {
        try (LineNumberReader reader = new LineNumberReader(
                new InputStreamReader(new FileInputStream(aFile), Constants.ENCODING))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (line.length() > 0) {
                    final String[] s = line.split("\t");
                    final String[] t = new String[s.length - 1];
                    System.arraycopy(s, 1, t, 0, t.length);

                    // get absolute path for stored relative path
                    for (int i = 0; i < t.length; i++) {
                        t[i] = new File(indexFile.getParent(), t[i]).getAbsolutePath();
                    }

                    map.put(s[0], t);
                }
            }
        }
    }

	public String[] get(final String ch)
	{
		return map.get(ch);
	}

	public static int main(final String args[])
		throws IOException
	{
		String logConfig = System.getProperty("log-config");
		if (logConfig == null) {
			logConfig = "log-config.txt";
		}

		if (args.length != 2) {
			System.out.println("java PACKAGE.FileMap index-file char"); // NOPMD
			return -1;
		}

		final String ch = args[1].substring(0, 1);

		final FileMap map = new FileMap(new File(args[0]));
		final String[] s = map.get(ch);

		System.out.print("\"" + ch + "\"\t"); // NOPMD

		for (int i = 0; i < s.length; i++) {
			if (i > 0) {
				System.out.print(" "); // NOPMD
			}
			System.out.print(s[i]); // NOPMD
		}

		System.out.print("\n"); // NOPMD
		return 0;
	}

	@Override
	public String toString()
	{
		final StringBuilder buffer = new StringBuilder();
		for (final String key : map.keySet()) {
			buffer.append(key);
			buffer.append(" - ");
			buffer.append(StringUtils.join(map.get(key), ","));
			buffer.append(LF);
		}
		buffer.append(LF);

		return buffer.toString();
	}
}