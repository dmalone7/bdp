package com.refactorlabs.cs378.assign8;

import org.apache.avro.Schema;
import org.apache.avro.mapred.AvroKey;
import org.apache.avro.mapred.AvroValue;
import org.apache.avro.mapreduce.AvroJob;
import org.apache.avro.mapreduce.AvroKeyValueInputFormat;
import org.apache.avro.mapreduce.AvroKeyValueOutputFormat;
import org.apache.avro.mapreduce.AvroMultipleOutputs;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.MRJobConfig;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.ToolRunner;
import org.apache.hadoop.util.Tool;

import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.IOException;
import java.lang.ClassNotFoundException;
import java.util.HashMap;

import com.refactorlabs.cs378.sessions.*;
import com.refactorlabs.cs378.utils.*;

public class EventStatistics extends Configured implements Tool {
	/********************************
	 ********* MAP CLASSES **********
	 ********************************/

	// bins user sessions by event type
	public static class BinMapClass extends Mapper<AvroKey<CharSequence>, AvroValue<Session>, 
						AvroKey<CharSequence>, AvroValue<Session>> {

		private AvroMultipleOutputs output;

		public void setup(Context context) {
			output = new AvroMultipleOutputs(context);
		}

		public void cleanup(Context context) throws IOException, InterruptedException {
			output.close();
		}

		@Override
		public void map(AvroKey<CharSequence> key, AvroValue<Session> value, Context context)
				throws IOException, InterruptedException {

			Session userSession = value.datum();
			int length = 0;

			boolean[] category = new boolean[4];

			for (Event event : userSession.getEvents()) {
				EventType thisType = event.getEventType();
				EventSubtype thisSubtype = event.getEventSubtype();

				if (( thisType == EventType.CHANGE   || thisType == EventType.EDIT || 
					  thisType == EventType.SUBMIT ) && thisSubtype == EventSubtype.CONTACT_FORM ) 
					category[0] = true;
				else if ( thisType == EventType.CLICK ) 
					category[1] = true;
				else if ( thisType == EventType.SHOW || thisType == EventType.DISPLAY ) 
					category[2] = true;
				else if (thisType == EventType.VISIT) 
					category[3] = true;

				length++;
			}

			if (length > 100) {
				context.getCounter(Utils.MAPPER_COUNTER_GROUP, "large sessions").increment(1L);
				return;
			}

			// output set on what type of event type was found, preference given to submitter, etc.
			if (category[0]) 
				output.write(SessionType.SUBMITTER.getText(), new AvroKey<CharSequence>(SessionType.SUBMITTER.getText()), value);
			else if (category[1]) 
				output.write(SessionType.CLICKER.getText(), new AvroKey<CharSequence>(SessionType.CLICKER.getText()), value);
			else if (category[2]) 
				output.write(SessionType.SHOWER.getText(), new AvroKey<CharSequence>(SessionType.SHOWER.getText()), value);
			else if (category[3]) 
				output.write(SessionType.VISITOR.getText(), new AvroKey<CharSequence>(SessionType.VISITOR.getText()), value);
			else 
				output.write(SessionType.OTHER.getText(), new AvroKey<CharSequence>(SessionType.OTHER.getText()), value);

			context.getCounter(Utils.MAPPER_COUNTER_GROUP, "input sessions").increment(1L);
		}
	}

	public static class ParallelMapClass extends Mapper<AvroKey<CharSequence>, AvroValue<Session>, 
						AvroKey<EventSubtypeStatisticsKey>, AvroValue<EventSubtypeStatisticsData>> {
		@Override
		public void map(AvroKey<CharSequence> key, AvroValue<Session> value, Context context)
				throws IOException, InterruptedException {

			HashMap<String, Long> countMap = new HashMap<>();
			Session userSession = value.datum();

			// initiates 0 count for all event subtypes
			for(EventSubtype subType : EventSubtype.values()) {
				countMap.put(subType.toString().toUpperCase(), 0L);
			}

			// increments for existing event subtypes
			for (Event event : userSession.getEvents()) {
				String thisSubtype = event.getEventSubtype().toString().toUpperCase();
				countMap.put(thisSubtype, countMap.get(thisSubtype) + 1L);
			}

			// output statistics for event type/subtype key
			for(String event : countMap.keySet()) {
				EventSubtypeStatisticsKey.Builder keyBuilder = EventSubtypeStatisticsKey.newBuilder();
				keyBuilder.setSessionType(key.toString().toUpperCase()); 
				keyBuilder.setEventSubtype(event);

				long count = countMap.get(event);

				EventSubtypeStatisticsData.Builder dataBuilder = EventSubtypeStatisticsData.newBuilder();
				dataBuilder.setSessionCount(1L);
				dataBuilder.setTotalCount(count);
				dataBuilder.setSumOfSquares(count * count);
				dataBuilder.setMean(0.0);
				dataBuilder.setVariance(0.0);

				//AvroKeyValueOutputFormat
				context.write(new AvroKey<EventSubtypeStatisticsKey>(keyBuilder.build()), 
						new AvroValue<EventSubtypeStatisticsData>(dataBuilder.build()));
			}
		}
	}

	public static class MapClass extends Mapper<AvroKey<EventSubtypeStatisticsKey>, AvroValue<EventSubtypeStatisticsData>, 
						AvroKey<EventSubtypeStatisticsKey>, AvroValue<EventSubtypeStatisticsData>> {
		@Override
		public void map(AvroKey<EventSubtypeStatisticsKey> key, AvroValue<EventSubtypeStatisticsData> value, Context context)
				throws IOException, InterruptedException {

			context.write(key, value);
		}
	}

	/********************************
	 ******** REDUCE CLASSES ********
	 ********************************/

	// calculate statistics for each Type/Subtype key
	public static class SumReducer extends Reducer<AvroKey<EventSubtypeStatisticsKey>, AvroValue<EventSubtypeStatisticsData>, 
						AvroKey<EventSubtypeStatisticsKey>, AvroValue<EventSubtypeStatisticsData>> {

		public void reduce(AvroKey<EventSubtypeStatisticsKey> key, Iterable<AvroValue<EventSubtypeStatisticsData>> values, Context context) 
				throws IOException, InterruptedException {
			long session_count = 0;
			long sum = 0;
			long sum2 = 0;

			context.getCounter(Utils.REDUCER_COUNTER_GROUP, "Sessions Out").increment(1L);

			for (AvroValue<EventSubtypeStatisticsData> value : values) {
				session_count += value.datum().getSessionCount();
				sum += value.datum().getTotalCount();
				sum2 += value.datum().getSumOfSquares();
			}

			double mean = sum * 1.0 / session_count;
			double variance = (sum2 * 1.0) / session_count - (mean * mean);

			EventSubtypeStatisticsData.Builder builder = EventSubtypeStatisticsData.newBuilder();
			builder.setSessionCount(session_count);
			builder.setTotalCount(sum);
			builder.setSumOfSquares(sum2);
			builder.setMean(mean);
			builder.setVariance(variance);

			context.write(key, new AvroValue<EventSubtypeStatisticsData>(builder.build()));
		}
	}

	public static class ReducerClass extends Reducer<AvroKey<EventSubtypeStatisticsKey>, AvroValue<EventSubtypeStatisticsData>, 
						AvroKey<EventSubtypeStatisticsKey>, AvroValue<EventSubtypeStatisticsData>> {

		public void reduce(AvroKey<EventSubtypeStatisticsKey> key, AvroValue<EventSubtypeStatisticsData> value, Context context) 
				throws IOException, InterruptedException {

			// for (AvroValue<EventSubtypeStatisticsData> value : values) {
			context.write(key, value);
			// }
		}
	}

	/**
	 * The run() method is called (indirectly) from main(), and contains all the job
	 * setup and configuration.
	 */
	public int run(String[] args) throws Exception {
		Utils.printClassPath();
		if (args.length != 2) {
			System.err.println("Usage: EventStatistics <input path(s)> <output path>");
			return -1;
		}

		// Configuration conf = new Configuration();
		Configuration conf = getConf();
		Job job = Job.getInstance(conf, "EventStatistics");
		String[] appArgs = new GenericOptionsParser(conf, args).getRemainingArgs();

		// Identify the JAR file to replicate to all machines.
		job.setJarByClass(EventStatistics.class);

		// Specify the Map
		job.setMapperClass(BinMapClass.class);
		job.setInputFormatClass(AvroKeyValueInputFormat.class);
		AvroJob.setInputKeySchema(job, Schema.create(Schema.Type.STRING));
		AvroJob.setInputValueSchema(job, Session.getClassSchema());
		AvroJob.setMapOutputKeySchema(job, Schema.create(Schema.Type.STRING));
		AvroJob.setMapOutputValueSchema(job, Session.getClassSchema());

		// Specify no Reduce
		job.setNumReduceTasks(0);
		job.setOutputFormatClass(AvroKeyValueOutputFormat.class);
		AvroJob.setOutputKeySchema(job, Schema.create(Schema.Type.STRING));
		AvroJob.setOutputValueSchema(job, Session.getClassSchema());

		// Grab the input file and output directory from the command line.
		String[] inputPaths = appArgs[0].split(",");
		for ( String inputPath : inputPaths ) {
			FileInputFormat.addInputPath(job, new Path(inputPath));
		}
		FileOutputFormat.setOutputPath(job, new Path(appArgs[1]));

		AvroMultipleOutputs.addNamedOutput(job, SessionType.SUBMITTER.getText(), AvroKeyValueOutputFormat.class, Schema.create(Schema.Type.STRING), Session.getClassSchema());
		AvroMultipleOutputs.addNamedOutput(job, SessionType.CLICKER.getText(), AvroKeyValueOutputFormat.class, Schema.create(Schema.Type.STRING), Session.getClassSchema());
		AvroMultipleOutputs.addNamedOutput(job, SessionType.SHOWER.getText(), AvroKeyValueOutputFormat.class, Schema.create(Schema.Type.STRING), Session.getClassSchema());
		AvroMultipleOutputs.addNamedOutput(job, SessionType.VISITOR.getText(), AvroKeyValueOutputFormat.class, Schema.create(Schema.Type.STRING), Session.getClassSchema());

		AvroMultipleOutputs.setCountersEnabled(job, true);

		// Initiate the map-reduce job, and wait for completion.
		job.waitForCompletion(true);

		// create parallel jobs with input from binning mapper output files.  output to own directories
		Job clickerJob = parallelJob(appArgs[1]+"/clicker-m-*.avro", appArgs[1]+"/visitor-output", conf);
		Job showerJob = parallelJob(appArgs[1]+"/shower-m-*.avro", appArgs[1]+"/click-output", conf);
		Job submitterJob = parallelJob(appArgs[1]+"/submitter-m-*.avro", appArgs[1]+"/submitter-output", conf);
		Job visitorJob = parallelJob(appArgs[1]+"/visitor-m-*.avro", appArgs[1]+"/shower-output", conf);

		// wait for all jobs to complete, polling ever .1 seconds
		while (( submitterJob.isComplete() && 
				 clickerJob.isComplete() &&
				 showerJob.isComplete() &&
				 visitorJob.isComplete()) == false) {
			Thread.sleep(100);
		}

		// final aggregate mapper/reducer
		Job finalJob = Job.getInstance(conf, "AggregateStatistics");

		finalJob.setJarByClass(EventStatistics.class);

		finalJob.setInputFormatClass(AvroKeyValueInputFormat.class);
		finalJob.setMapperClass(MapClass.class);
		AvroJob.setInputKeySchema(finalJob, EventSubtypeStatisticsKey.getClassSchema());
		AvroJob.setInputValueSchema(finalJob, EventSubtypeStatisticsData.getClassSchema());
		AvroJob.setMapOutputKeySchema(finalJob, EventSubtypeStatisticsKey.getClassSchema());
		AvroJob.setMapOutputValueSchema(finalJob, EventSubtypeStatisticsData.getClassSchema());

		finalJob.setReducerClass(ReducerClass.class);
		AvroJob.setOutputKeySchema(finalJob, EventSubtypeStatisticsKey.getClassSchema());
		AvroJob.setOutputValueSchema(finalJob, EventSubtypeStatisticsData.getClassSchema());
		finalJob.setOutputFormatClass(TextOutputFormat.class);
		finalJob.setNumReduceTasks(1);

		FileInputFormat.addInputPaths(finalJob, appArgs[1]+"/visitor-output/part-r-*.avro");
		FileInputFormat.addInputPaths(finalJob, appArgs[1]+"/click-output/part-r-*.avro");
		FileInputFormat.addInputPaths(finalJob, appArgs[1]+"/submitter-output/part-r-*.avro");
		FileInputFormat.addInputPaths(finalJob, appArgs[1]+"/shower-output/part-r-*.avro");

		FileOutputFormat.setOutputPath(finalJob, new Path(appArgs[1] + "/final-output"));

		finalJob.waitForCompletion(true);

		return 0;
	}

	// refactored parallel jobs
	public Job parallelJob(String input, String output, Configuration conf) 
					throws IOException, InterruptedException, ClassNotFoundException {
		Path inputPath = new Path(input);
		Path outputPath = new Path(output);

		Job job = Job.getInstance(conf, output);

		// Identify the JAR file to replicate to all machines.
		job.setJarByClass(EventStatistics.class);

		// Specify the Map through input
		job.setInputFormatClass(AvroKeyValueInputFormat.class);
		job.setMapperClass(ParallelMapClass.class);
		AvroJob.setInputKeySchema(job, Schema.create(Schema.Type.STRING));
		AvroJob.setInputValueSchema(job, Session.getClassSchema());
		AvroJob.setMapOutputKeySchema(job, EventSubtypeStatisticsKey.getClassSchema());
		AvroJob.setMapOutputValueSchema(job, EventSubtypeStatisticsData.getClassSchema());

		// Specify the Reducer
		job.setOutputFormatClass(AvroKeyValueOutputFormat.class);
		job.setReducerClass(SumReducer.class);
		AvroJob.setOutputKeySchema(job, EventSubtypeStatisticsKey.getClassSchema());
		AvroJob.setOutputValueSchema(job, EventSubtypeStatisticsData.getClassSchema());

		// Grab the input file and output directory from the command line.
		FileInputFormat.addInputPath(job, inputPath);
		FileOutputFormat.setOutputPath(job, outputPath);

		job.submit();

		return job;
	}

	/**
	 * The main method specifies the characteristics of the map-reduce job
	 * by setting values on the Job object, and then initiates the map-reduce
	 * job and waits for it to complete.
	 */
	public static void main(String[] args) throws Exception {
		Utils.printClassPath();
		int res = ToolRunner.run(new EventStatistics(), args);
		System.exit(res);
	}
}
