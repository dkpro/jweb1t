/*
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
 */
package com.googlecode.jweb1t;

import java.io.IOException;
import java.util.Collection;

/**
 * Interface for any classes providing frequency counts.
 */
public interface Searcher
{
	long getFrequency(String... aPhrase)
		throws IOException;

    long getFrequency(Collection<String> aPhrase)
        throws IOException;

    long getNrOfNgrams(int aNGramSize);

	long getNrOfDistinctNgrams(int aNGramSize);
}