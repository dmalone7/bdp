package com.refactorlabs.cs378.assign7;

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

import com.refactorlabs.cs378.sessions.*;
import com.refactorlabs.cs378.utils.*;

public class FilterSessions  {

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
				context.getCounter(Utils.FILTER_COUNTER_GROUP, "large").increment(1L);
				return;
			}

			if (category[0]) 
					output.write(SessionType.SUBMITTER.getText(), key, value);
			else if (category[1]) {
				if (Math.random() < 0.1) // 10%
					output.write(SessionType.CLICKER.getText(), key, value);
				else 
					context.getCounter(Utils.FILTER_COUNTER_GROUP, "clicker").increment(1L);
			}
			else if (category[2]) {
				if (Math.random() < 0.02) // 2%
					output.write(SessionType.SHOWER.getText(), key, value);
				else 
					context.getCounter(Utils.FILTER_COUNTER_GROUP, "shower").increment(1L);
			}
			else if (category[3]) 
				output.write(SessionType.VISITOR.getText(), key, value);
			else 
				output.write(SessionType.OTHER.getText(), key, value);
		}
	}

	/**
	 * The main method specifies the characteristics of the map-reduce job
	 * by setting values on the Job object, and then initiates the map-reduce
	 * job and waits for it to complete.
	 */
	public static void main(String[] args) throws Exception {
		Utils.printClassPath();
				if (args.length != 2) {
			System.err.println("Usage: FilterSessions <input path(s)> <output path>");
			return;
		}

		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "FilterSessions");
		String[] appArgs = new GenericOptionsParser(conf, args).getRemainingArgs();

		// Identify the JAR file to replicate to all machines.
		job.setJarByClass(FilterSessions.class);

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
	}

}
