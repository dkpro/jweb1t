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
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Scans recursively a directory. Successive calls to the {@link #next()} method return successive
 * arrays of objects {@link File}.
 * <p>
 * The following code fragment, in which {@code root} is the starting directory, illustrates how to
 * use a folder scanner.
 * <p>
 * 
 * <pre>
 * FolderScanner fs = new FolderScanner(root);
 * while (fs.hasNext()) {
 * 	Object[] files = fs.next();
 * 
 * 	for (int i = 0; i &lt; files.length; i++)
 * 		System.out.println((File) files[i]);
 * }
 * </pre>
 * 
 * @author Claudio Giuliano
 */
final class FolderScanner
{
	private final Log logger = LogFactory.getLog(getClass());

	private final Stack<File> stack;

	private FileFilter filter;

	/**
	 * Create a folder scanner.
	 * 
	 * @param root
	 *            the root directory.
	 */
	public FolderScanner(final File aRoot)
	{
		stack = new Stack<File>();
		stack.push(aRoot);

	}

	/**
	 * Sets a file filter for this scanner
	 * 
	 * @param filter
	 *            a file filter.
	 */
	public void setFilter(final FileFilter aFilter)
	{
		filter = aFilter;
	}

	/**
	 * Returns <code>true</code> if the scanner has more directories. (In other words, returns
	 * <code>true</code> if <code>next</code> would return an array of files rather than return
	 * <code>null</code>.)
	 * 
	 * @return <code>true</code> if the scanner has more elements.
	 */
	public boolean hasNext()
	{
		return !stack.empty();
	}

	/**
	 * Returns the next array of files in the iteration.
	 * 
	 * @return the next array of files in the iteration.
	 */
	public File[] next()
	{
		File dir = null;

		File[] result = null;
		
		if (!stack.empty()) {
			final List<File> res = new ArrayList<File>();
			dir = stack.pop();

			if (!dir.isDirectory()) {
				logger.error(dir + " is not a directory.");
			}

			// listFiles is null-safe. A null filter simply accepts anything
			final File[] files = dir.listFiles(filter);

			if (files != null) {
				for (int i = 0; i < files.length; i++) {
					if (files[i].isFile()) {
						res.add(files[i]);
					}
					else if (files[i].isDirectory()) {
						stack.push(files[i]);
					}
				}
			}
			result = res.toArray(new File[res.size()]);
		}
		return result;
	}
}