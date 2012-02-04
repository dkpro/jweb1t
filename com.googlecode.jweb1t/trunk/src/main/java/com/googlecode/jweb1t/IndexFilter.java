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

/**
 * Filter used when reading in the Web1T-format files.
 * Automatically filters out some file types that are present in the Google Web1T release, but not needed to retrieve the frequencies.
 * 
 * @author Claudio Giuliano
 * @version %I%, %G%
 * @since 1.0
 */
class IndexFilter
	implements FileFilter
{

	// Accept all directories and all .test files.
	public boolean accept(final File aFile)
	{
		if (aFile.isDirectory()) {
			return !aFile.getName().startsWith(".");
		}

		final String extension = getExtension(aFile.getName());
		if (extension != null && extension.equalsIgnoreCase("idx")) {
			return false;
		}

		return true;
	}

	// The description of this filter
	public String getDescription()
	{
		return "Filters out .idx files.";
	}

	// The description of this filter
	private String getExtension(final String aName)
	{
		final int separatorIndex = aName.lastIndexOf('.') + 1;

		if ((separatorIndex == -1) && (separatorIndex == aName.length())) {
			return "";
		}
		else {
			return aName.substring(separatorIndex);
		}
	}
}