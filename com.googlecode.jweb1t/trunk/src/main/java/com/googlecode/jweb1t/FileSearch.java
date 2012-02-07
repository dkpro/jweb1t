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
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Implements the actual (binary) search for an ngram in a Web1T-format file.
 *
 * @author 	Claudio Giuliano
 */
public class FileSearch
{
	private final RandomAccessFile raf;

	public FileSearch(final File file) throws IOException
	{
		raf = new RandomAccessFile(file, "r");

	}

	public void close() throws IOException
	{
		raf.close();
	}

	public long getFreq(final String aSymbol) throws IOException
	{
		//logger.info("searching '" + t + "' in " + file.getName());
		long s = 0;
		long m = raf.length();
		long e = raf.length();
//		int loop = 0;
		while (e > (s + 1))
		{
//			++loop;
			//logger.info("loop: " + loop);
			m = s + ((e - s) / 2);
			//logger.debug(s + ", [" + m + "], " + e);
			final String[] ngram = read(m);
			if (ngram == null)
			{
				//logger.info("loop: " + loop);
				return 0;
			}

			final int c = aSymbol.compareTo(ngram[0]);

			if (c == 0)
			{
				//logger.debug(t + " == " + n.s + " (" + c + ")");
				//logger.info("loops: " + loop);
				return Long.parseLong(ngram[1]);
			}
			else if (c > 0)
			{
				//logger.debug(t + " > " + n.s + " (" + c + ")");
				s = m;
			}
			else
			{
				//logger.debug(t + " < " + n.s + " (" + c + ")");
				e = m;
			}

		}

		//logger.info("loops: " + loop);
		return 0;
 	}

	public String[] read(final long m) throws IOException
	{
		long s = m - 50;
		if (s < 0) {
			s = 0;
		}
		long e = m + 50;
		if (e > raf.length()) {
			e = raf.length();
		}

		final int len = (int) (e - s);
		final int nm = (int) (m - s);

		//logger.debug("nm = " + nm);
		//logger.debug("len = " + len);

		raf.seek(s);
		final byte[] array = new byte[len];

		raf.read(array);

		int i = nm;

		//Go back to the beginning of the line
		while ((i >= 0) && ((char) array[i]) != '\n')
		{
			i--;
		}

		//remember line start position
		final int ns = i + 1;

		i = nm + 1;

		//go to end of line
		while ((i < array.length) && ((char) array[i]) != '\n')
		{
			i++;
		}

		//remember line end position
		final int ne = i;

		//copy the bytes for the current line to a new byte[]
		final byte[] curLine = new byte[ne-ns];
		int index = 0;
		for (int j=ns;j<ne;j++)
		{
			curLine[index++]=array[j];
		}

		//convert the curLine-byte[] to UTF-8 String
		final String lineAsString = new String(curLine, "UTF-8");

		if (lineAsString.length() == 0) {
			return null;
		}
		
		return lineAsString.split("\\t+");
	}
}