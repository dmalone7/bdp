package com.refactorlabs.cs378.assign2;

import com.refactorlabs.cs378.utils.Utils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;
import java.io.DataInput;
import java.io.DataOutput;
import java.util.StringTokenizer;
import java.util.HashMap;
import java.util.Map;

public class WordStatistics extends Configured implements Tool {

	// contains paragraph count, word count, and word count squared
	public static class WordStatisticsWritable implements Writable {
		private long paraCount; 
		private long wordCount; 
		private long wordCount2; // squared

		public long getParaCount() {
			return paraCount;
		}

		public void setParaCount(long paraCount) {
			this.paraCount = paraCount;
		}

		public long getWordCount() {
			return wordCount;
		}

		public void setWordCount(long wordCount) {
			this.wordCount = wordCount;
		}

		public long getWordCount2() {
			return wordCount2;
		}

		public void setWordCount2(long wordCount2) {
			this.wordCount2 = wordCount2;
		}

		public void readFields(DataInput in) throws IOException {
			paraCount = in.readLong();
			wordCount = in.readLong();
			wordCount2 = in.readLong();
		}

		public void write(DataOutput out) throws IOException {
			out.writeLong(paraCount);
			out.writeLong(wordCount);
			out.writeLong(wordCount2);
		}

		//  print: paragraph count, mean, variance
		public String toString() {
			// calculate mean and variance when needing to print
			double mean = (wordCount * 1.0) / paraCount;
			double variance = (wordCount2 * 1.0) / paraCount - (mean * mean);
			return paraCount + "," + mean + "," + variance;
		}

		// needed to build jar, unit tests error when outputs of tests are exactly the same
		@Override
		public boolean equals(Object obj) {
			WordStatisticsWritable statObj = (WordStatisticsWritable) obj;
			return paraCount == statObj.getParaCount()
				&& wordCount == statObj.getWordCount()
				&& wordCount2 == statObj.getWordCount2();
		}
	}

	public static class MapClass extends Mapper<LongWritable, Text, Text, WordStatisticsWritable> {
		private Text word = new Text();
		private WordStatisticsWritable stats = new WordStatisticsWritable();
		private HashMap<String, Long> hash;

		@Override
		public void map(LongWritable key, Text value, Context context) 
			throws IOException, InterruptedException {
			hash = new HashMap<String, Long>();

			String line = value.toString();
			line = line.toLowerCase();
			line = line.replace("--", " "); // special case
			line = line.replace("[", " ["); // special case
			line = line.replaceAll("[^a-z0-9\\[\\] ]", ""); // keep lowercase, numbers, spaces, and []

			StringTokenizer tokenizer = new StringTokenizer(line);

			// add words to hashmap, incrementing words already found
			while (tokenizer.hasMoreTokens()) {
				String token = tokenizer.nextToken();
				if (hash.containsKey(token)) {
					long count = hash.get(token);
					hash.put(token, count + 1L);
				} 
				else {
					hash.put(token, 1L);
				}
			}

			// write out word and data per one paragraph
			for (String s : hash.keySet()) {
				long count = hash.get(s);
				word.set(s);
				stats.setParaCount(1L);
				stats.setWordCount(count);
				stats.setWordCount2(count * count);
				context.write(word, stats);
			}
		}
	}

	public static class ReduceClass extends Reducer<Text, WordStatisticsWritable, Text, WordStatisticsWritable> {
		@Override
		public void reduce(Text key, Iterable<WordStatisticsWritable> values, Context context) 
			throws IOException, InterruptedException {
			long count = 0;
			long sum = 0;
			long sum2 = 0;

			for (WordStatisticsWritable value : values) {
				count += value.getParaCount();
				sum += value.getWordCount();
				sum2 += value.getWordCount2();
			}

			WordStatisticsWritable stats = new WordStatisticsWritable();
			stats.setParaCount(count);
			stats.setWordCount(sum);
			stats.setWordCount2(sum2);
			context.write(key, stats);
		}
	}

	public int run(String[] args) throws Exception {
		Configuration conf = getConf();
		String[] appArgs = new GenericOptionsParser(conf, args).getRemainingArgs();

		Job job = Job.getInstance(conf, "WordStatistics");

		// Identify the JAR file to replicate to all machines.
		job.setJarByClass(WordStatistics.class);

		// Set the output key and value types (for map and reduce).
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(WordStatisticsWritable.class);

		// Set the map and reduce classes.
		job.setMapperClass(MapClass.class);
		job.setReducerClass(ReduceClass.class);
		job.setCombinerClass(ReduceClass.class);

		// Set the input and output file formats.
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		// Grab the input file and output directory from the command line.
		FileInputFormat.addInputPaths(job, appArgs[0]);
		FileOutputFormat.setOutputPath(job, new Path(appArgs[1]));

		// Initiate the map-reduce job, and wait for completion.
		job.waitForCompletion(true);

		return 0;
	}

	public static void main(String[] args) throws Exception {
		Utils.printClassPath();
		int res = ToolRunner.run(new WordStatistics(), args);
		System.exit(res);
	}

}
