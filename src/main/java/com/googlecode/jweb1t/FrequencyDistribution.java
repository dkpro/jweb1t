/*******************************************************************************
 * Copyright 2011
 * Ubiquitous Knowledge Processing (UKP) Lab
 * Technische Universit√§t Darmstadt
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

import java.util.Set;

import bak.pcj.map.ObjectKeyLongMap;
import bak.pcj.map.ObjectKeyLongOpenHashMap;

/**
 * Inspired by nltk.probability.FreqDist
 * 
 * @author zesch
 */
public class FrequencyDistribution<T>
{
	private ObjectKeyLongMap freqDist;

	private long total;

	public FrequencyDistribution()
	{
		freqDist = new ObjectKeyLongOpenHashMap();
		total = 0;
	}

	public FrequencyDistribution(final Iterable<T> aIterable)
	{
		this();
		incAll(aIterable); // NOPMD
	}

	public boolean contains(final T aSample)
	{
		return freqDist.containsKey(aSample);
	}

	public void inc(final T aSample)
	{
		addSample(aSample, 1);
	}

	public void incAll(final Iterable<T> aIterable)
	{
		for (T sample : aIterable) {
			addSample(sample, 1);
		}
	}

	/**
	 * @return The total number of sample outcomes that have been recorded by this FreqDist.
	 */
	public long getN()
	{
		return total;
	}

	/**
	 * @return The total number of sample values (or bins) that have counts greater than zero.
	 */
	public long getB()
	{
		return freqDist.size();
	}

	public long getCount(final T aSample)
	{
		if (freqDist.containsKey(aSample)) {
			return freqDist.get(aSample);
		}
		else {
			return 0;
		}
	}

	@SuppressWarnings("unchecked")
	public Set<T> getKeys()
	{
		return freqDist.keySet();
	}

	public void addSample(final T aSample, final long aNumber)
	{
		total = total + aNumber;
		if (freqDist.containsKey(aSample)) {
			freqDist.put(aSample, freqDist.get(aSample) + aNumber);
		}
		else {
			freqDist.put(aSample, aNumber);
		}
	}

	@Override
	public String toString()
	{
		final StringBuilder buffer = new StringBuilder();
		for (Object o : freqDist.keySet()) {
			buffer.append(String.valueOf(o));
			buffer.append(" - ");
			buffer.append(freqDist.get(o));
			buffer.append(System.getProperty("line.separator"));
		}

		return buffer.toString();
	}
}