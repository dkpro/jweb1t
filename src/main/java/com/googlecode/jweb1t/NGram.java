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


/**
 * Represents an ngram as a combination of the ngram string and its frequency.
 * 
 * @author Claudio Giuliano
 */
public class NGram
{
	public final String symbol;
	public final long frequency;

	public NGram(final String aLine) throws NumberFormatException
	{
		final String[] t = aLine.split("\t");
		symbol = t[0];
		frequency = Long.parseLong(t[1]);
	}

	public NGram(final String aSymbol, final long aFrequency)
	{
		symbol = aSymbol;
		frequency = aFrequency;
	}
}