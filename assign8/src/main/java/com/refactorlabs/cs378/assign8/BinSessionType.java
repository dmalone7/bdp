package com.refactorlabs.cs378.assign8;

import java.io.IOException;

import org.apache.avro.Schema;
import org.apache.avro.mapred.AvroKey;
import org.apache.avro.mapred.AvroValue;
import org.apache.avro.mapreduce.AvroJob;
import org.apache.avro.mapreduce.AvroKeyValueInputFormat;
import org.apache.avro.mapreduce.AvroKeyValueOutputFormat;
import org.apache.avro.mapreduce.AvroMultipleOutputs;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.MRJobConfig;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
// import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import com.refactorlabs.cs378.sessions.*;
import com.refactorlabs.cs378.utils.*;

public class BinSessionType  {

	// read input from user sessions files
	public static class MapClass extends Mapper<AvroKey<CharSequence>, AvroValue<Session>, 
						AvroKey<CharSequence>, AvroValue<Session>> {

		private AvroMultipleOutputs output;

		public void setup(Context context) {
			output = new AvroMultipleOutputs(context);
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

			if (category[0]) 
				output.write(SessionType.SUBMITTER.getText(), key, value);
			else if (category[1]) 
				output.write(SessionType.CLICKER.getText(), key, value);
			else if (category[2]) 
				output.write(SessionType.SHOWER.getText(), key, value);
			else if (category[3]) 
				output.write(SessionType.VISITOR.getText(), key, value);
			else 
				output.write(SessionType.OTHER.getText(), key, value);

			context.getCounter(Utils.MAPPER_COUNTER_GROUP, "input sessions").increment(1L);
		}
	}

	/**
	 * The run() method is called (indirectly) from main(), and contains all the job
	 * setup and configuration.
	 */
	public int run(String[] args) throws Exception {
		Utils.printClassPath();
				if (args.length != 2) {
			System.err.println("Usage: BinSessionType <input path(s)> <output path>");
			return -1;
		}

		// Configuration conf = new Configuration();
		Configuration conf = getConf();
		Job job = Job.getInstance(conf, "BinSessionType");
		String[] appArgs = new GenericOptionsParser(conf, args).getRemainingArgs();

		// Identify the JAR file to replicate to all machines.
		job.setJarByClass(BinSessionType.class);

		// Specify the Map
		job.setMapperClass(MapClass.class);
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

		AvroMultipleOutputs.addNamedOutput(job, "clicker", AvroKeyValueOutputFormat.class, Schema.create(Schema.Type.STRING), Session.getClassSchema());
		AvroMultipleOutputs.addNamedOutput(job, "shower", AvroKeyValueOutputFormat.class, Schema.create(Schema.Type.STRING), Session.getClassSchema());
		AvroMultipleOutputs.addNamedOutput(job, "submitter", AvroKeyValueOutputFormat.class, Schema.create(Schema.Type.STRING), Session.getClassSchema());
		AvroMultipleOutputs.addNamedOutput(job, "visitor", AvroKeyValueOutputFormat.class, Schema.create(Schema.Type.STRING), Session.getClassSchema());
		AvroMultipleOutputs.addNamedOutput(job, "other", AvroKeyValueOutputFormat.class, Schema.create(Schema.Type.STRING), Session.getClassSchema());

		AvroMultipleOutputs.setCountersEnabled(job, true);

		// Initiate the map-reduce job, and wait for completion.
		job.waitForCompletion(true);

		Job clickerJob = clickJob(appArgs[1]+"/clicker-r-*.avro", appArgs[1]+"-click_output", conf);
		Job showerJob = showerJob(appArgs[1]+"/shower-r-*.avro", appArgs[1]+"-shower_output", conf);
		Job submitterJob = submitterJob(appArgs[1]+"/submitter-r-*.avro", appArgs[1]+"-submitter_output", conf);
		Job visitorJob = visitorJob(appArgs[1]+"/visitor-r-*.avro", appArgs[1]+"-visitor_output", conf);
		Job otherJob = otherJob(appArgs[1]+"/other-r-*.avro", appArgs[1]+"-other_output", conf);

		// while the jobs aren't completed, sleep
		// run combined mapper
		// run run combined reducer

		return 0;
	}

	public Job clickerJob(String input, String output, Configuration conf) {
		Path inputPath = new Path(input);
		Path outputPath = new Path(output);

		Job job = new Job(conf,outputPath);
		job.setJarByClass(BinSessionType.class);
		
		job.setMapperClass(??Mapper.class);
		job.setReducerClass(??Reducer.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(LongWritable.class);
		
		job.setInputFormatClass(AvroKeyInputFormat.class);
		AvroJob.setInputKeySchema(job, Schema.create(Schema.Type.STRING));		//AvroKeyInputFormat.addInputPath(job, inputDir);
		FileInputFormat.addInputPath(job,inputDir);
		
		//output AvroKeyValueOutputFormat
		job.setOutputFormatClass(TextOutputFormat.class);
		//TextOutputFormat.setOutputPath(job,outputDir);
		FileOutputFormat.setOutputPath(job, outputDir);
		
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
		int res = ToolRunner.run(new BinSessionType(), args);
		System.exit(res);
	}

}
