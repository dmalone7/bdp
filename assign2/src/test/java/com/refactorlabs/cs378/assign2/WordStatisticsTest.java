package com.refactorlabs.cs378.assign2;

import com.google.common.collect.Lists;
import com.refactorlabs.cs378.utils.Utils;
import junit.framework.Assert;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import com.refactorlabs.cs378.assign2.WordStatistics.*;

/**
 * Unit test for the WordCount map-reduce program.
 *
 * @author David Franke (dfranke@cs.utexas.edu)
 */
public class WordStatisticsTest {
	MapDriver<LongWritable, Text, Text, WordStatisticsWritable> mapDriver;
	ReduceDriver<Text, WordStatisticsWritable, Text, WordStatisticsWritable> reduceDriver;

	@Before
	public void setup() {
		MapClass mapper = new MapClass();
		ReduceClass reducer = new ReduceClass();

		mapDriver = MapDriver.newMapDriver(mapper);
		reduceDriver = ReduceDriver.newReduceDriver(reducer);
	}

	private static final String TEST_WORD = "yadayada";

	@Test
	public void testMapClass() {
		WordStatisticsWritable output = new WordStatisticsWritable();
		output.setParaCount(1L);
		output.setWordCount(1L);
		output.setWordCount2(1L);

		mapDriver.withInput(new LongWritable(0L), new Text("Yadayada\n"));
		mapDriver.withOutput(new Text(TEST_WORD), output);
		try {
			mapDriver.runTest();
		} catch (IOException ioe) {
			Assert.fail("IOException from mapper: " + ioe.getMessage());
		}
	}

	@Test
	public void testReduceClass() {
		WordStatisticsWritable value = new WordStatisticsWritable();
		value.setParaCount(1L);
		value.setWordCount(1L);
		value.setWordCount2(1L);

		WordStatisticsWritable output = new WordStatisticsWritable();
		output.setParaCount(3L);
		output.setWordCount(3L);
		output.setWordCount2(3L);

		List<WordStatisticsWritable> valueList = Lists.newArrayList(value, value, value);
		reduceDriver.withInput(new Text(TEST_WORD), valueList);
		reduceDriver.withOutput(new Text(TEST_WORD), output);
		try {
			reduceDriver.runTest();
		} catch (IOException ioe) {
			Assert.fail("IOException from mapper: " + ioe.getMessage());
		}
	}
}