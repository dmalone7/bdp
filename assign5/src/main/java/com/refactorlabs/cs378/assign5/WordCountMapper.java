package com.refactorlabs.cs378.assign5;

import com.refactorlabs.cs378.utils.Utils;
import org.apache.avro.mapred.AvroValue;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.StringTokenizer;

/**
 * Map class for various WordCount examples that use the AVRO generated class WordCountData.
 */
public class WordCountMapper extends Mapper<LongWritable, Text, Text, AvroValue<WordCountData>> {

	/**
	 * Local variable "word" will contain the word identified in the input.
	 */
	private Text word = new Text();
	private String[] fields = {
		"user_id","event_type","event_timestamp","city",
		"vin","vehicle_condition","year","make",
		"model","trim","body_style","cab_style",
		"price","mileage","free_carfax_report","feature"
	};

	@Override
	public void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {
		String line = value.toString();
		// StringTokenizer tokenizer = new StringTokenizer(line, "\t");
		String[] split = line.split("\\t");

		// int count = 0;
		context.getCounter(Utils.MAPPER_COUNTER_GROUP, "Input Lines").increment(1L);


		// For each word in the input line, emit a count of 1 for that word.
		// while (tokenizer.hasMoreTokens()) {
		for (int i = 0; i < split.length-1; i++) {

			if (i == 0 || i == 2 || i == 4 | i == 12 || i == 13)
				continue;

			if (i < split.length-1) {
			    word.set(fields[i] + ":" + split[i]);
				WordCountData.Builder builder = WordCountData.newBuilder();
				builder.setCount(Utils.ONE);
				context.write(word, new AvroValue<WordCountData>(builder.build()));
				context.getCounter(Utils.MAPPER_COUNTER_GROUP, "Input Words").increment(1L);
			}
		}

		String[] featureSplit = split[15].split(":");
		for (String feature : featureSplit) {
			word.set(fields[split.length-1] + ":" + feature);
			WordCountData.Builder builder = WordCountData.newBuilder();
			builder.setCount(Utils.ONE);
			context.write(word, new AvroValue<WordCountData>(builder.build()));
			context.getCounter(Utils.MAPPER_COUNTER_GROUP, "Input Words").increment(1L);

			// String token = tokenizer.nextToken();
			// contextWrite(s);
			// count++;
		}
	}
}
