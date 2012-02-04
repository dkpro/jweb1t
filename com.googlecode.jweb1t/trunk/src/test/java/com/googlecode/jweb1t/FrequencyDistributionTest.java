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

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class FrequencyDistributionTest
{
	@Test
	public void cfdTest()
	{
		final List<String> tokens = Arrays
				.asList("This is a first test that contains a first test example".split(" "));

		final FrequencyDistribution<String> freqDist = new FrequencyDistribution<String>();
		freqDist.incAll(tokens);

		System.out.println(freqDist); // NOPMD

		assertEquals(11, freqDist.getN());
		assertEquals(8, freqDist.getB());

		assertEquals(0, freqDist.getCount("humpelgrumpf"));
		assertEquals(1, freqDist.getCount("This"));
		assertEquals(2, freqDist.getCount("test"));
	}
}