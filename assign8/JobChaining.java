package com.refactorlabs.cs378.assign8;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.avro.Schema;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.avro.mapred.AvroKey;
import org.apache.avro.mapred.AvroValue;
import org.apache.avro.mapreduce.AvroJob;
import org.apache.avro.mapreduce.AvroKeyValueInputFormat;
import org.apache.avro.mapreduce.AvroKeyValueOutputFormat;
import org.apache.avro.mapreduce.AvroMultipleOutputs;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.MRJobConfig;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.util.GenericOptionsParser;

import org.apache.hadoop.util.ToolRunner;
import org.apache.hadoop.util.Tool;

import com.refactorlabs.cs378.sessions.Event;
import com.refactorlabs.cs378.sessions.EventSubtype;
import com.refactorlabs.cs378.sessions.EventType;
import com.refactorlabs.cs378.sessions.SessionData;
import com.refactorlabs.cs378.sessions.SessionType;

public class JobChaining extends Configured implements Tool {

	public static class MapClass extends Mapper<AvroKey<CharSequence>, AvroValue<SessionData>, AvroKey<CharSequence>, AvroValue<SessionData>> {

		//From the API Documentation - https://avro.apache.org/docs/1.8.2/api/java/org/apache/avro/mapred/AvroMultipleOutputs.html
		private AvroMultipleOutputs multiOutput;

		public void setup(Context context) {
			multiOutput = new AvroMultipleOutputs(context);
		}

		public void cleanup(Context context) throws IOException, InterruptedException {
			multiOutput.close();
		}

		public void map(AvroKey<CharSequence> key, AvroValue<SessionData> value, Context context) throws IOException, InterruptedException {
			// Get all the values from a session
			SessionData session = value.datum();

			// Get all the events for that particular session
			List<Event> eventList = session.getEvents();

			boolean writeSubmitter = false, writeClicker = false, writeShower = false, writeVisitor = false;

			if(eventList.size() <= 100) {
				/* Going thrugh the loop of events setting the uniqueUser ID's for each event
				 * and setting 1 for evnet type click and edit contact form
				 */
				for (Event singleEvent : eventList) {
					//Following 3 for Submitter
					if(singleEvent.getEventType() == EventType.CHANGE || singleEvent.getEventType() == EventType.EDIT ||
							singleEvent.getEventType() == EventType.SUBMIT) {
						if(singleEvent.getEventSubtype() == EventSubtype.CONTACT_FORM) {
							writeSubmitter = true;
						}
					}
					//Clicker
					else if(singleEvent.getEventType() == EventType.CLICK) {
						writeClicker = true;
					}
					//Following 2 for Shower
					else if(singleEvent.getEventType() == EventType.SHOW || singleEvent.getEventType() == EventType.DISPLAY) {
						writeShower = true;
					}
					//Visitor
					else if(singleEvent.getEventType() == EventType.VISIT) {
						writeVisitor = true;
					}
				}

				if(writeSubmitter) {
					multiOutput.write(SessionType.SUBMITTER.getText(), new AvroKey<CharSequence>(SessionType.SUBMITTER.getText()), value);
				}
				else if (writeClicker) {
					multiOutput.write(SessionType.CLICKER.getText(), new AvroKey<CharSequence>(SessionType.CLICKER.getText()), value);
				}
				else if(writeShower) {
					multiOutput.write(SessionType.SHOWER.getText(), new AvroKey<CharSequence>(SessionType.SHOWER.getText()), value);
				}
				else if(writeVisitor) {
					multiOutput.write(SessionType.VISITOR.getText(), new AvroKey<CharSequence>(SessionType.VISITOR.getText()), value);
				}
				else {   
					multiOutput.write(SessionType.OTHER.getText(), new AvroKey<CharSequence>(SessionType.OTHER.getText()), value);
				}
			}
			//Reject All session that are >100 events
			else {
				context.getCounter("Reject", "LargeSessionDiscarded").increment(1L);
			}

		} // map method end

	} // mapper class end

	//Counting from the assignment 7 mapper counts
	public static class SumMappers extends Mapper<AvroKey<CharSequence>, AvroValue<SessionData>, AvroKey<EventSubtypeStatisticsKey>, AvroValue<EventSubtypeStatisticsData>> {
		@Override
		public void map(AvroKey<CharSequence> key, AvroValue<SessionData> value, Context context) throws IOException, InterruptedException {
			// Get all the values from a session
			SessionData session = value.datum();

			// Get all the events for that particular session
			List<Event> eventList = session.getEvents();

			// HashMap to save word and its count, and later use it for (number of times in paragrah)^2
			// Also creating just a dictionary to count words in paragraphs
			Map<String, Long> countTracker = new HashMap<String, Long>();

			/* Going thrugh the loop of events setting the uniqueUser ID's for each event
			 * and setting 1 for evnet type click and edit contact form
			 */
			String type = "";
			if(eventList.size() <= 100) {
				for (Event singleEvent : eventList) {
					type = key.toString();
					if(countTracker.get(singleEvent.getEventSubtype().toString().toUpperCase()) == null) {
						countTracker.put((singleEvent.getEventSubtype().toString().toUpperCase()), 1L);
					}
					else {
						long oldCount = countTracker.get(singleEvent.getEventSubtype().toString().toUpperCase());
						countTracker.put(singleEvent.getEventSubtype().toString().toUpperCase(), ++oldCount);
					}
				}

				EventSubtypeStatisticsKey.Builder eventSubtypeKeyBuilder = EventSubtypeStatisticsKey.newBuilder();
				eventSubtypeKeyBuilder.setSessionType(type);
				for(String eventOut : countTracker.keySet()) {
					eventSubtypeKeyBuilder.setEventSubtype(eventOut);
					EventSubtypeStatisticsData.Builder eventStatisticsBuilder = EventSubtypeStatisticsData.newBuilder();
					eventStatisticsBuilder.setMean(0.0);
					eventStatisticsBuilder.setSessionCount(1L);
					eventStatisticsBuilder.setSumOfSquares(countTracker.get(eventOut) * countTracker.get(eventOut));
					eventStatisticsBuilder.setTotalCount(countTracker.get(eventOut));
					eventStatisticsBuilder.setVariance(0.0);

					context.write(new AvroKey<EventSubtypeStatisticsKey>(eventSubtypeKeyBuilder.build()), new AvroValue<EventSubtypeStatisticsData>(eventStatisticsBuilder.build()));
				}
				//				//ANY
				//				EventSubtypeStatisticsKey.Builder anyKeyBuilder = EventSubtypeStatisticsKey.newBuilder();
				//				for (Event singleEvent : eventList) {
				//					type = key.toString();
				//					if(countTracker.get(singleEvent.getEventSubtype().toString().toUpperCase()) == null) {
				//						countTracker.put((singleEvent.getEventSubtype().toString().toUpperCase()), 1L);
				//					}
				//					else {
				//						long oldCount = countTracker.get(singleEvent.getEventSubtype().toString().toUpperCase());
				//						countTracker.put(singleEvent.getEventSubtype().toString().toUpperCase(), ++oldCount);
				//					}
				//				}		
				//				eventSubtypeKeyBuilder.setSessionType(type);
				//				for(String eventOut : countTracker.keySet()) {
				//					anyKeyBuilder.setEventSubtype("any");
				//					EventSubtypeStatisticsData.Builder anyDataBuild = EventSubtypeStatisticsData.newBuilder();
				//					anyDataBuild.setMean(0.0);
				//					anyDataBuild.setSessionCount(1L);
				//					anyDataBuild.setSumOfSquares(countTracker.get(eventOut) * countTracker.get(eventOut));
				//					anyDataBuild.setTotalCount(countTracker.get(eventOut));
				//					anyDataBuild.setVariance(0.0);
				//
				//					context.write(new AvroKey<EventSubtypeStatisticsKey>(anyKeyBuilder.build()), new AvroValue<EventSubtypeStatisticsData>(anyDataBuild.build()));
				//				}

			} // If End
		}
	} //SumMapper End

	//Counting from the assignment 7 mapper counts
	public static class SumReducer extends Reducer<AvroKey<EventSubtypeStatisticsKey>, AvroValue<EventSubtypeStatisticsData>, AvroKey<EventSubtypeStatisticsKey>, AvroValue<EventSubtypeStatisticsData>> {

		public void reduce(AvroKey<EventSubtypeStatisticsKey> key, Iterable<AvroValue<EventSubtypeStatisticsData>> values, Context context) throws IOException, InterruptedException {
			long reducerSessionCount = 0, reducerTotalCount = 0, reducerSquareCount = 0;
			double reducerMean = 0.00000000, reducerVariance=0.00000000;

			EventSubtypeStatisticsData.Builder builder = EventSubtypeStatisticsData.newBuilder();

			// Sum up the counts for the current word, specified in object "key", calculate mean, variance
			for (AvroValue<EventSubtypeStatisticsData> value : values) {
				reducerTotalCount += value.datum().getTotalCount();
				reducerSessionCount += value.datum().getSessionCount();
				reducerSquareCount += value.datum().getSumOfSquares();
			}

			reducerMean = (reducerTotalCount / (double) reducerSessionCount);
			// E{x^2} - E[x]^2(Mean^2)
			reducerVariance = ((reducerSquareCount/ (double) reducerSessionCount) - (reducerMean*reducerMean));

			builder.setSessionCount(reducerSessionCount);
			builder.setTotalCount(reducerTotalCount);
			builder.setSumOfSquares(reducerSquareCount);
			builder.setMean(reducerMean);
			builder.setVariance(reducerVariance);

			context.write(key, new AvroValue<EventSubtypeStatisticsData>(builder.build()));
		}
	} //SumReducer End

	//Final Mapper
	public static class FinalMap extends Mapper<AvroKey<EventSubtypeStatisticsKey>, AvroValue<EventSubtypeStatisticsData>, AvroKey<EventSubtypeStatisticsKey>, AvroValue<EventSubtypeStatisticsData>> {

		public void map(AvroKey<EventSubtypeStatisticsKey> key, AvroValue<EventSubtypeStatisticsData> value, Context context) throws IOException, InterruptedException {
			context.write(key, value);
		}
	} // FinalMap End

	//Final Reducer
	public static class FinalReducer extends Reducer<AvroKey<EventSubtypeStatisticsKey>, AvroValue<EventSubtypeStatisticsData>, AvroKey<EventSubtypeStatisticsKey>, AvroValue<EventSubtypeStatisticsData>> {

		public void reduce(AvroKey<EventSubtypeStatisticsKey> key, AvroValue<EventSubtypeStatisticsData> value, Context context) throws IOException, InterruptedException {
			//context.write(key, value);
			context.write(key, value);
		}
	} // FinalMap End

	/**
	 * The run() method is called (indirectly) from main(), and contains all the job
	 * setup and configuration.
	 */
	public int run(String[] args) throws Exception {
		// Configuration conf = new Configuration();
		Configuration conf = getConf();
		Job job = Job.getInstance(conf, "EventStatistics");
		String[] appArgs = new GenericOptionsParser(conf, args).getRemainingArgs();

		// Identify the JAR file to replicate to all machines.
		job.setJarByClass(JobChaining.class);

		// Specify the Map
		job.setMapperClass(MapClass.class);
		job.setInputFormatClass(AvroKeyValueInputFormat.class);
		AvroJob.setInputKeySchema(job, Schema.create(Schema.Type.STRING));
		AvroJob.setInputValueSchema(job, SessionData.getClassSchema());
		AvroJob.setMapOutputKeySchema(job, Schema.create(Schema.Type.STRING));
		AvroJob.setMapOutputValueSchema(job, SessionData.getClassSchema());

		// Specify no Reduce
		job.setNumReduceTasks(0);
		job.setOutputFormatClass(AvroKeyValueOutputFormat.class);
		AvroJob.setOutputKeySchema(job, Schema.create(Schema.Type.STRING));
		AvroJob.setOutputValueSchema(job, SessionData.getClassSchema());

		// Grab the input file and output directory from the command line.
		String[] inputPaths = appArgs[0].split(",");
		for ( String inputPath : inputPaths ) {
			FileInputFormat.addInputPath(job, new Path(inputPath));
		}
		FileOutputFormat.setOutputPath(job, new Path(appArgs[1]));

		AvroMultipleOutputs.addNamedOutput(job, SessionType.SUBMITTER.getText(), AvroKeyValueOutputFormat.class, Schema.create(Schema.Type.STRING), SessionData.getClassSchema());
		AvroMultipleOutputs.addNamedOutput(job, SessionType.CLICKER.getText(), AvroKeyValueOutputFormat.class, Schema.create(Schema.Type.STRING), SessionData.getClassSchema());
		AvroMultipleOutputs.addNamedOutput(job, SessionType.SHOWER.getText(), AvroKeyValueOutputFormat.class, Schema.create(Schema.Type.STRING), SessionData.getClassSchema());
		AvroMultipleOutputs.addNamedOutput(job, SessionType.VISITOR.getText(), AvroKeyValueOutputFormat.class, Schema.create(Schema.Type.STRING), SessionData.getClassSchema());

		AvroMultipleOutputs.setCountersEnabled(job, true);

		// Initiate the map-reduce job, and wait for completion.
		job.waitForCompletion(true);

		Job clickerJob = parallelJob(appArgs[1]+"/clicker-m-*.avro", appArgs[1]+"/click-output", conf);
		Job showerJob = parallelJob(appArgs[1]+"/shower-m-*.avro", appArgs[1]+"/shower-output", conf);
		Job submitterJob = parallelJob(appArgs[1]+"/submitter-m-*.avro", appArgs[1]+"/submitter-output", conf);
		Job visitorJob = parallelJob(appArgs[1]+"/visitor-m-*.avro", appArgs[1]+"/visitor-output", conf);

		// maybe change to while loop
		while (( submitterJob.isComplete() && 
				clickerJob.isComplete() &&
				showerJob.isComplete() &&
				visitorJob.isComplete()) == false) {
			Thread.sleep(500);
		}

		/************************************
		 ********* IGNORE PAST THIS **********
		 ************************************/

		// Path inputPath = new Path(input);
		// Path outputPath = new Path(output);
		// Job job = Job.getInstance(conf, output);

		// // Identify the JAR file to replicate to all machines.
		// job.setJarByClass(EventStatistics.class);

		// // Specify the Map
		// job.setInputFormatClass(AvroKeyValueInputFormat.class);
		// job.setMapperClass(MapClass.class);
		// AvroJob.setMapOutputKeySchema(job, EventSubtypeStatisticsKey.getClassSchema());
		// AvroJob.setMapOutputValueSchema(job, EventSubtypeStatisticsData.getClassSchema());
		// AvroJob.setInputKeySchema(job, Schema.create(Schema.Type.STRING));
		// AvroJob.setInputValueSchema(job, Session.getClassSchema());

		// // Specify the Reduce
		// job.setOutputFormatClass(TextOutputFormat.class);
		// job.setReducerClass(ReduceClass.class);
		// job.setOutputKeyClass(Text.class);
		// // AvroJob.setOutputKeySchema(job, EventSubtypeStatisticsKey.getClassSchema());
		// AvroJob.setOutputValueSchema(job, EventSubtypeStatisticsData.getClassSchema());

		// // Grab the input file and output directory from the command line.
		// FileInputFormat.addInputPaths(job, appArgs[0] + "," + appArgs[1]);
		// FileOutputFormat.setOutputPath(job, new Path(appArgs[2]));
		// aggregate job
		Job aggregateClickStatsJob = Job.getInstance(conf, "AggregateSubeventStats");
		aggregateClickStatsJob.setJarByClass(JobChaining.class);
		aggregateClickStatsJob.setInputFormatClass(AvroKeyValueInputFormat.class);
		aggregateClickStatsJob.setMapperClass(FinalMap.class);
		AvroJob.setInputKeySchema(aggregateClickStatsJob, EventSubtypeStatisticsKey.getClassSchema());
		AvroJob.setInputValueSchema(aggregateClickStatsJob, EventSubtypeStatisticsData.getClassSchema());
		AvroJob.setMapOutputKeySchema(aggregateClickStatsJob, EventSubtypeStatisticsKey.getClassSchema());
		AvroJob.setMapOutputValueSchema(aggregateClickStatsJob, EventSubtypeStatisticsData.getClassSchema());

		aggregateClickStatsJob.setReducerClass(FinalReducer.class);
		AvroJob.setOutputKeySchema(aggregateClickStatsJob, EventSubtypeStatisticsKey.getClassSchema());
		AvroJob.setOutputValueSchema(aggregateClickStatsJob, EventSubtypeStatisticsData.getClassSchema());
		aggregateClickStatsJob.setOutputFormatClass(TextOutputFormat.class);
		aggregateClickStatsJob.setNumReduceTasks(1);

		FileInputFormat.addInputPaths(aggregateClickStatsJob, appArgs[1]+"/visitor-output/part-r-*.avro");
		FileInputFormat.addInputPaths(aggregateClickStatsJob, appArgs[1]+"/click-output/part-r-*.avro");
		FileInputFormat.addInputPaths(aggregateClickStatsJob, appArgs[1]+"/submitter-output/part-r-*.avro");
		FileInputFormat.addInputPaths(aggregateClickStatsJob, appArgs[1]+"/shower-output/part-r-*.avro");
		FileOutputFormat.setOutputPath(aggregateClickStatsJob, new Path(appArgs[1]+"/final-output"));

		aggregateClickStatsJob.waitForCompletion(true);
		
		return 0;
	}

	public Job parallelJob(String input, String output, Configuration conf) 
			throws IOException, InterruptedException, ClassNotFoundException {
		Path inputPath = new Path(input);
		Path outputPath = new Path(output);

		Job job = Job.getInstance(conf, output);
		// Identify the JAR file to replicate to all machines.
		job.setJarByClass(JobChaining.class);
		// Specify the Map through input
		job.setInputFormatClass(AvroKeyValueInputFormat.class);
		job.setOutputFormatClass(AvroKeyValueOutputFormat.class);

		job.setMapperClass(SumMappers.class);
		job.setReducerClass(SumReducer.class);

		AvroJob.setInputKeySchema(job, Schema.create(Schema.Type.STRING));
		AvroJob.setInputValueSchema(job, SessionData.getClassSchema());
		AvroJob.setMapOutputKeySchema(job, EventSubtypeStatisticsKey.getClassSchema());
		AvroJob.setMapOutputValueSchema(job, EventSubtypeStatisticsData.getClassSchema());
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
		int res = ToolRunner.run(new JobChaining(), args);
		System.exit(res);
	}
}

