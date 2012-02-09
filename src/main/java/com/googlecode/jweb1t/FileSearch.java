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
 * @author 	Claudio Giuliano, Torsten Zesch
 */
public class FileSearch
{
	private int windowSize = 200;
	private final RandomAccessFile raf;
	private final String encoding = "UTF-8";

	public FileSearch(final File file) throws IOException
	{
		raf = new RandomAccessFile(file, "r");
	}

	public void setWindowSize(final int aWindowSize)
	{
		windowSize = aWindowSize;
	}

	public int getWindowSize()
	{
		return windowSize;
	}

	public void close() throws IOException
	{
		raf.close();
	}

	public long getFreq(final String aSymbol) throws IOException
	{

		long start = 0;
		long pos = raf.length();
		long end = raf.length();

		while (end > (start + 1))
		{
			pos = start + ((end - start) / 2);

			final String[] ngram = read(pos);

            // FIXME this seems to shortcut a bit too early if we haven't found the right position yet
			if (ngram == null) {
				return 0;
			}

			final int c = aSymbol.compareTo(ngram[0]);

			if (c == 0) {
				return Long.parseLong(ngram[1]);
			}
			else if (c > 0) {
				start = pos;
			}
			else {
				end = pos;
			}

		}

		return 0;
 	}

	public String[] read(final long pos) throws IOException //NOPMD
	{
	    // span a window around the approximated position
		long start = pos - windowSize;
		if (start < 0) {
			start = 0;
		}
		long end = pos + windowSize;
		if (end > raf.length()) {
			end = raf.length();
		}

		final int len = (int) (end - start);
		final int newPos = (int) (pos - start);

		raf.seek(start);
		final byte[] window = new byte[len];

		raf.read(window);

		int i = newPos;

		// search for the beginning and end of the file

		// Go back to the beginning of the line
		while ((i >= 0) && ((char) window[i]) != '\n') {
			i--;
		}

		// remember line start position
		final int newStart = i + 1;

		i = newPos + 1;

		// go to end of line
		while ((i < window.length) && ((char) window[i]) != '\n') {
			i++;
		}

		// remember line end position
		final int newEnd = i;

		// copy the bytes for the current line to a new byte[]
		final byte[] curLine = new byte[newEnd - newStart];
		int index = 0;
		for (int j = newStart; j < newEnd; j++) {
			curLine[index++] = window[j];
		}

		// convert the curLine-byte[] to UTF-8 String
		final String lineAsString = new String(curLine, encoding);

		if (lineAsString.length() == 0) {
			return null;
		}

		String[] ngram = new String[2];
		final int tabOffset = lineAsString.indexOf('\t');
		if (tabOffset != -1) {
			ngram[0] = lineAsString.substring(0, tabOffset);
			ngram[1] = lineAsString.substring(tabOffset + 1, lineAsString.length());
			return ngram;
		}

		return null;
	}
}