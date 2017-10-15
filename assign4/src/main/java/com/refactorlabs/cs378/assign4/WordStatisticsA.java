package com.refactorlabs.cs378.assign4;

import com.refactorlabs.cs378.utils.Utils;
import org.apache.avro.mapred.AvroValue;
import org.apache.avro.mapreduce.AvroJob;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.MRJobConfig;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.StringTokenizer;
import java.io.DataInput;
import java.io.DataOutput;
import java.util.HashMap;
import java.util.Map;
/**
 * WordCount example using AVRO defined class for the word count data,
 * to demonstrate how to use AVRO defined objects.
 */
public class WordStatisticsA extends Configured implements Tool {
/**
 * Map class for various WordCount examples that use the AVRO generated class WordStatisticsData.
 */
	public static class MapClass extends Mapper<LongWritable, Text, Text, AvroValue<WordStatisticsData>> {

		/**
		 * Local variable "word" will contain the word identified in the input.
		 */
		private Text word = new Text();
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
			// context.getCounter(Utils.MAPPER_COUNTER_GROUP, "Input Lines").increment(1L);

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
				word.set(s);
				WordStatisticsData.Builder builder = WordStatisticsData.newBuilder();
				long count = hash.get(s);

				builder.setDocumentCount(1l);
				builder.setTotalCount(count);
				builder.setSumOfSquares(count * count);
				builder.setMean(0l);
				builder.setVariance(0l);
				builder.setMin(count);
				builder.setMax(count);

				context.write(word, new AvroValue<WordStatisticsData>(builder.build()));
				// context.getCounter(Utils.MAPPER_COUNTER_GROUP, "Input Words").increment(1L);
			}
		}
	}

	/**
	 * The Reduce class for word count.  Extends class Reducer, provided by Hadoop.
	 * This class defines the reduce() function for the word count example.
	 */
	public static class ReduceClass extends Reducer<Text, AvroValue<WordStatisticsData>,
			Text, AvroValue<WordStatisticsData>> {

		@Override
		public void reduce(Text key, Iterable<AvroValue<WordStatisticsData>> values, Context context)
				throws IOException, InterruptedException {

			long doc_count = 0;
			long sum = 0;
			long sum2 = 0;
			long min = 1;
			long max = 1;

			context.getCounter(Utils.REDUCER_COUNTER_GROUP, "Words Out").increment(1L);

			for (AvroValue<WordStatisticsData> value : values) {
				doc_count += value.datum().getDocumentCount();
				sum += value.datum().getTotalCount();
				sum2 += value.datum().getSumOfSquares();
				if (min > value.datum().getMin())
					min = value.datum().getMin();
				if (max < value.datum().getMax())
					max = value.datum().getMax();
			}

			double mean = sum * 1.0 / doc_count;
			double variance = (sum2 * 1.0) / doc_count - (mean * mean);

			WordStatisticsData.Builder builder = WordStatisticsData.newBuilder();
			builder.setDocumentCount(doc_count);
			builder.setTotalCount(sum);
			builder.setSumOfSquares(sum2);
			builder.setMean(mean);
			builder.setVariance(variance);
			builder.setMin(min);
			builder.setMax(max);

			context.write(key, new AvroValue<WordStatisticsData>(builder.build()));
		}
	}

	/**
	 * The run() method is called (indirectly) from main(), and contains all the job
	 * setup and configuration.
	 */
	public int run(String[] args) throws Exception {
		if (args.length != 2 && args.length != 3) {
			System.err.println("Usage: WordStatisticsA <input path> <output path>");
			return -1;
		}

		Configuration conf = getConf();
		Job job = Job.getInstance(conf, "WordStatisticsA");
		String[] appArgs = new GenericOptionsParser(conf, args).getRemainingArgs();

		// Identify the JAR file to replicate to all machines.
		job.setJarByClass(WordStatisticsA.class);

		// Specify the Map
		job.setInputFormatClass(TextInputFormat.class);
		job.setMapperClass(MapClass.class);
		job.setMapOutputKeyClass(Text.class);
		AvroJob.setMapOutputValueSchema(job, WordStatisticsData.getClassSchema());

		// Specify the Reduce
		job.setOutputFormatClass(TextOutputFormat.class);
		job.setReducerClass(ReduceClass.class);
		job.setOutputKeyClass(Text.class);
		AvroJob.setOutputValueSchema(job, WordStatisticsData.getClassSchema());

		// Grab the input file and output directory from the command line.
		FileInputFormat.addInputPaths(job, appArgs[0] + "," + appArgs[1]);
		FileOutputFormat.setOutputPath(job, new Path(appArgs[2]));

		// Initiate the map-reduce job, and wait for completion.
		job.waitForCompletion(true);

		return 0;
	}

	/**
	 * The main method specifies the characteristics of the map-reduce job
	 * by setting values on the Job object, and then initiates the map-reduce
	 * job and waits for it to complete.
	 */
	public static void main(String[] args) throws Exception {
		Utils.printClassPath();
		int res = ToolRunner.run(new WordStatisticsA(), args);
		System.exit(res);
	}

}
