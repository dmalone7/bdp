package com.refactorlabs.cs378.assign6;

import com.refactorlabs.cs378.utils.Utils;
import org.apache.avro.mapred.AvroValue;
import org.apache.avro.mapreduce.AvroJob;
import org.apache.avro.mapred.AvroMultipleInputs;
import org.apache.hadoop.mapred.lib.MultipleInputs;

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

public class UserJoin extends Configured implements Tool {

	// read input from user sessions files
	public static class UserSessionsMapper extends Mapper<AvroKey<CharSequence>, AvroValue<Session>, Text, AvroValue<VinImpressionCounts>> {

		private Text word = new Text();

		@Override
		public void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			Session userSession = value.datum();

			for (Event event : userSession.getEvents()) {
				
			}
			// setUniqueUsers(Long value)
			// setClicks(Map<java.lang.CharSequence, Long> value)
			// setEditContactForm(Long value)
		}
	}

	// read vin,impression_type,count input from CSV files
	public static class VinImpressionMapperMapper extends Mapper<LongWritable, Text, Text, AvroValue<VinImpressionCounts>> {

		private Text word = new Text();

		@Override
		public void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			String line = value.toString();

			String[] splitLine = line.split(",");

			word.set(splitLine[0]);

			VinImpressionCounts.Builder builder = VinImpressionCounts.newBuilder();
			if (splitLine[1].equals("SRP"))
				builder.setMarketplaceSrps(Long.parseLong(slitLine[2]));
			else
				builder.setMarketplaceVdps(Long.parseLong(slitLine[2]));
			context.write(word, new AvroValue<VinImpressionCounts>(builder.build()));
		}
	}

	public static class ReduceClass extends <Text, AvroValue<VinImpressionCounts>, Text, AvroValue<VinImpressionCounts>> {

		@Override
		public void reduce(Text key, Iterable<AvroValue<VinImpressionCounts>> values, Context context)
				throws IOException, InterruptedException {

			long uniqueUsers = 0;
			long editContactForm = 0;
			long marketplaceSrps = 0;
			long marketplaceVdps = 0;

			for (AvroValue<VinImpressionCounts> value : values) {
				uniqueUsers += value.getUniqueUsers();
				editContactForm += value.getEditContactForm();
				marketplaceSrps += value.getMarketplaceSrps();
				marketplaceVdps += value.getMarketplaceVdps();
			}

			// implement left side join, don't output if right side is empty
			if (uniqueUsers == 0 && editContactForm == 0)
				return;

			VinImpressionCounts.Builder builder = VinImpressionCounts.newBuilder();
			builder.setUniqueUsers(uniqueUsers);
			builder.setEditContactForm(editContactForm);
			builder.setMarketplaceSrps(marketplaceSrps);
			builder.setMarketplaceVdps(marketplaceVdps);
			context.write(word, new AvroValue<VinImpressionCounts>(builder.build()));
		}
	}

	/**
	 * The run() method is called (indirectly) from main(), and contains all the job
	 * setup and configuration.
	 */
	public int run(String[] args) throws Exception {
		if (args.length != 2 && args.length != 3) {
			System.err.println("Usage: UserJoin <input path> <output path>");
			return -1;
		}

		Configuration conf = getConf();
		Job job = Job.getInstance(conf, "UserJoin");
		String[] appArgs = new GenericOptionsParser(conf, args).getRemainingArgs();

		// Identify the JAR file to replicate to all machines.
		job.setJarByClass(UserJoin.class);

		// Specify the Map
		job.setMapOutputKeyClass(Text.class);
		AvroJob.setInputKeySchema(job, Schema.create(Schema.Type.STRING));
		AvroJob.setInputValueSchema(job, Session.getClassSchema());
		AvroJob.setMapOutputValueSchema(job, VinImpressionCounts.getClassSchema());

		// Specify the Reduce
		job.setOutputFormatClass(TextOutputFormat.class);
		job.setReducerClass(ReduceClass.class);
		job.setOutputKeyClass(Text.class);
		AvroJob.setOutputValueSchema(job, WordStatisticsData.getClassSchema()); 

		// Grab the input file and output directory from the command line.
		// FileInputFormat.addInputPaths(job, appArgs[0] + "," + appArgs[1]);

		// specify AvroKeyValueInputFormat as the input format
		MultipleInputs.addInputPath(job, new Path(appArgs[0]), AvroKeyValueInputFormat.class, UserSessionsMapper.class);
		MultipleInputs.addInputPath(job, new Path(appArgs[1]), TextInputFormat.class, VinImpressionMapper.class);
		MultipleInputs.addInputPath(job, new Path(appArgs[2]), TextInputFormat.class, VinImpressionMapper.class);
		FileOutputFormat.setOutputPath(job, new Path(appArgs[3]));

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
