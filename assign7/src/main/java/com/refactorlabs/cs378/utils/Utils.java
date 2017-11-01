package com.refactorlabs.cs378.utils;

import org.apache.hadoop.io.LongWritable;

import com.refactorlabs.cs378.sessions.*;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.ArrayList;

public class Utils {
	// Not instantiable
	private Utils() {}

	/**
	 * Counter groups.  Individual counters are organized into these groups.
	 */
	public static final String MAPPER_COUNTER_GROUP = "Mapper Counts";
	public static final String FILTER_COUNTER_GROUP = "Filter Counts";

	public static final long ONE = 1L;
	public final static LongWritable WRITABLE_ONE = new LongWritable(ONE);

    /**
	 * Writes the classpath to standard out, for inspection.
	 */
	public static void printClassPath() {
		ClassLoader cl = ClassLoader.getSystemClassLoader();
		URL[] urls = ((URLClassLoader) cl).getURLs();
		System.out.println("classpath BEGIN");
		for (URL url : urls) {
			System.out.println(url.getFile());
		}
		System.out.println("classpath END");
		System.out.flush();
	}
}
