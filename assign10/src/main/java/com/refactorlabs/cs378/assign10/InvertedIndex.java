package com.refactorlabs.cs378.assign10;

import com.google.common.collect.Lists;
import com.refactorlabs.cs378.utils.Utils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.PairFlatMapFunction;
import scala.Tuple2;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * InvertedIndex application for Spark.
 */
public class InvertedIndex {
	public static void main(String[] args) {
		//Utils.printClassPath();

		String inputFilename = args[0];
		String outputFilename = args[1];

		// Create a Java Spark context
		SparkConf conf = new SparkConf().setAppName(InvertedIndex.class.getName()).setMaster("local");
		JavaSparkContext sc = new JavaSparkContext(conf);

		try {
            // Load the input data
            JavaRDD<String> input = sc.textFile(inputFilename);

            // Split the input into words + verse
            FlatMapFunction<String, String> splitFunction =
                    new FlatMapFunction<String, String>() {
                        @Override
                        public Iterator<String> call(String line) throws Exception {
                            StringTokenizer tokenizer = new StringTokenizer(line);
                            HashSet<String> wordList = new HashSet<>();

                            String verse = "";
                            if (tokenizer.hasMoreTokens())
                                verse = tokenizer.nextToken();

                            // For each word in the input line, emit that word.
                            while (tokenizer.hasMoreTokens()) 
                                wordList.add(tokenizer.nextToken().toLowerCase().replaceAll("[^a-z0-9]", "") + " " + verse);

                            return wordList.iterator();
                        }
                    };

            // Transform into word and verse tuple
            PairFunction<String, String, String> separateFunction =
                    new PairFunction<String, String, String>() {
                        @Override
                        public Tuple2<String, String> call(String s) throws Exception {
                            StringTokenizer tokenizer = new StringTokenizer(s);
                            return new Tuple2(tokenizer.nextToken(), tokenizer.nextToken());
                        }
                    };

            // Add all verses to one string, separated by ,
            Function2<String, String, String> addVersesFunction =
                    new Function2<String, String, String>() {
                        @Override
                        public String call(String verse1, String verse2) throws Exception {
                            return verse1 + "," + verse2;
                        }
                    };

            // Sorts all verses by lexi order
            Function<String, Iterable<String>> sortVersesFunction =
                    new Function<String, Iterable<String>>() {
                        @Override
                        public Iterable<String> call(String verses) throws Exception {
                            List<String> verseList = Arrays.asList(verses.split(","));
                            Collections.sort(verseList, new compareVerses());
                            return verseList;
                        }
                    };

            JavaPairRDD<String, String> ordered = input
                .flatMap(splitFunction)
                .mapToPair(separateFunction)
                .reduceByKey(addVersesFunction)
                .flatMapValues(sortVersesFunction)
                .reduceByKey(addVersesFunction)
                .sortByKey();

            // All in one line:
            // JavaPairRDD<String, Integer> ordered =
            //          input.flatMap(splitFunction).mapToPair(addCountFunction)
            //               .reduceByKey(sumFunction).sortByKey();

            // Save the word count to a text file (initiates evaluation)
            ordered.saveAsTextFile(outputFilename);
        } finally {
            // Shut down the contextjava.util
            sc.stop();
        }
	}

    public static class compareVerses implements Comparator<String> {
        public int compare(String s1, String s2) { 
            String[] arr1 = s1.split(":");
            String[] arr2 = s2.split(":");

            // same book
            if ( arr1[0].equals(arr2[0]) ) {  
                // same chapter
                if ( Integer.parseInt(arr1[1]) == Integer.parseInt(arr2[1]) ) {
                    if ( Integer.parseInt(arr1[2]) > Integer.parseInt(arr2[2]) ) 
                        return 1;
                    else
                        return -1;
                } 
                else { // different chapters
                    if ( Integer.parseInt(arr1[1]) > Integer.parseInt(arr2[1]) )
                        return 1;
                    else
                        return -1;
                }
            }
            else { // different books
                return arr1[0].compareTo(arr2[0]); // comparable            
            }
        }
    }

}
