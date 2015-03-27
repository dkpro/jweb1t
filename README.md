# jweb1t

jWeb1T is an open source Java tool for efficiently searching n-gram data in the Web 1T 5-gram corpus format.
It is based on a binary search algorithm that finds the n-grams and returns their frequency counts in logarithmic time.
As the corpus is stored in many files a simple index is used to retrieve the files containing the n-grams.

jWeb1T has been developed by Claudio Giuliano at FBK for the English Lexical Substitution Task at SemEval 2007:

> Claudio Giuliano, Alfio Gliozzo and Carlo Strapparava. FBK-irst: Lexical Substitution Task Exploiting Domain and Syntagmatic Coherence. In Proceedings of the 4th Interational Workshop on Semantic Evaluations (SemEval-2007), Prague, 23-24 June 2007.

jWeb1T has been funded by X-Media Project.

The [UKP Lab at Technische Universität Darmstadt](https://www.ukp.tu-darmstadt.de/ukp-home/) has contributed several bug fixes and updates.
A UIMA wrapper for jWeb1T is available as part of DKPro.


## Getting it
The latest version of jWeb1T is now available via Maven Central.
If you use Maven as your build tool, then you can add jWeb1T as a dependency in your pom.xml file:
```
<dependency>
  <groupId>com.googlecode.jweb1t</groupId>
  <artifactId>com.googlecode.jweb1t</artifactId>
  <version>1.3.0</version>
</dependency>
```

## Usage
### Prerequisites
- Obtain or create data in Web1T format.
- Unzip
- Delete zipped files (if still present)
### Creating necessary indexes
```Java
JWeb1TIndexer indexer = new JWeb1TIndexer(PATH_TO_DATA, MAX_NGRAM_LEVEL);
indexer.create();
```

### Getting n-gram counts
```Java
JWeb1TSearcher web1t = new JWeb1TSearcher (
    INDEX_FILE_1
    INDEX_FILE_2
    ...
    INDEX_FILE_N
);

web1t.getFrequency("test phrase")
```
