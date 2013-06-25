/*******************************************************************************
 * Copyright 2012
 * Ubiquitous Knowledge Processing (UKP) Lab
 * Technische Universität Darmstadt
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
package com.googlecode.jweb1t.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Iterator;
import java.util.Queue;

public class NGramIterator
    implements Iterator<String>
{
    private Queue<File> ngramFiles;
    private LineNumberReader reader;
    private String storedNextString;
    
    public NGramIterator(Queue<File> ngramFiles) throws IOException {
        this.ngramFiles = ngramFiles;
        
        if (ngramFiles.size() > 0) {
            this.reader = new LineNumberReader(new FileReader(ngramFiles.poll()));
        }
        else {
            throw new IOException("Filelist is empty.");
        }        
    }
    
    public boolean hasNext()
    {
        // prefatch the next string
        storedNextString = getNextString();
        
        if (storedNextString == null) {
            return false;
        }
        else {
            return true;
        }
    }

    public String next()
    {
        if (storedNextString == null) {
            return getNextString();
        }
        else {
            String returnValue = storedNextString;
            storedNextString = null;
            return returnValue;
        }
    }

    public void remove()
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the next string or null if there is no next string.
     *  
     * @throws IOException
     */
    private String getNextString() {
        String nextString = null;

        if (storedNextString != null) {
            return storedNextString;
        }
        
        try {
            String line = reader.readLine();
            if (line != null) {
                final String[] parts = line.split("\t");
                
                if (parts.length > 0) {
                    nextString = parts[0];
                }
            }
            else {
                reader.close();
                if (!ngramFiles.isEmpty()) {
                    reader = new LineNumberReader(new FileReader(ngramFiles.poll()));
                    nextString = getNextString();
                }
            }
        }
        catch (IOException e) {
            // fail gracefully as we cannot throw exception in hasNext() or next() anyway
            e.printStackTrace();
        }
        
        return nextString;
    }
}