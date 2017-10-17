package com.refactorlabs.cs378.utils;

import org.apache.hadoop.io.LongWritable;

import com.refactorlabs.cs378.sessions.*;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.ArrayList;

public class SessionsUtils {
	private SessionsUtils() {}

	public static Session.Builder createSession(String line) {
		SessionsUtils util = new SessionsUtils();

		String[] split = line.split("\t");
		Event.Builder eventBuilder = util.createEvent(split);

		List<Event> eventList = new ArrayList<>();
		eventList.add(eventBuilder.build());

		Session.Builder builder = Session.newBuilder();
		builder.setUserId(split[0]);
		builder.setEvents(eventList);

		return builder;
	}

	private Event.Builder createEvent(String[] splitLine) {
		// Session.Builder builder = Session.newBuilder();
		Event.Builder event = Event.newBuilder();
		eventTypeHandler(event, splitLine[1]);
		eventSubtypeHandler(event, splitLine[1]);
		eventTimeHandler(event, splitLine[2]);
		cityHandler(event, splitLine[3]);
		vinHandler(event, splitLine[4]);
		conditionHandler(event, splitLine[5]);
		yearHandler(event, splitLine[6]);
		makeHandler(event, splitLine[7]);
		modelHandler(event, splitLine[8]);
		trimHandler(event, splitLine[9]);
		bodyStyleHandler(event, splitLine[10]);
		cabStyleHandler(event, splitLine[11]);
		priceHandler(event, splitLine[12]);
		mileageHandler(event, splitLine[13]);
		freeCarfaxReportHandler(event, splitLine[14]);
		featuresHandler(event, splitLine[15]);

		return event;
	}

	private void eventTypeHandler(Event.Builder event, String eventType) {
		String[] eventSplit = eventType.split(" ");
		switch(eventSplit[0].toUpperCase()) {
			case "CHANGE":
				event.setEventType(EventType.CHANGE);
				break;
			case "CLICK":
				event.setEventType(EventType.CLICK);
				break;
			case "DISPLAY":
				event.setEventType(EventType.DISPLAY);
				break;
			case "EDIT": 
				event.setEventType(EventType.EDIT);
				break;
			case "SHOW":
				event.setEventType(EventType.SHOW);
				break;
			case "SUBMIT":
				event.setEventType(EventType.SUBMIT);
				break;
			case "VISIT":
				event.setEventType(EventType.VISIT);
				break;
			default: 
				event.setEventType(EventType.DISPLAY);
		}
	}
	
	private void eventSubtypeHandler(Event.Builder event, String eventSubtype) {
		String[] eventSplit = eventSubtype.split(" ");

		if (eventSplit.length > 2)
			eventSubtype = eventSplit[1] + " " + eventSplit[2];
		else
			eventSubtype = eventSplit[1];

		switch(eventSubtype.toUpperCase()) {
			case "CONTACT FORM":
				event.setEventSubtype(EventSubtype.CONTACT_FORM);
				break;
			case "CONTACT BANNER":
				event.setEventSubtype(EventSubtype.CONTACT_BANNER);
				break;
			case "CONTACT BUTTON":
				event.setEventSubtype(EventSubtype.CONTACT_BUTTON);
				break;
			case "DEALER PHONE":
				event.setEventSubtype(EventSubtype.DEALER_PHONE);
				break;
			case "FEATURES":
				event.setEventSubtype(EventSubtype.FEATURES);
				break;
			case "GET DIRECTIONS":
				event.setEventSubtype(EventSubtype.GET_DIRECTIONS);
				break;
			case "VEHICLE HISTORY":
				event.setEventSubtype(EventSubtype.VEHICLE_HISTORY);
				break;
			case "ALTERNATIVE":
				event.setEventSubtype(EventSubtype.ALTERNATIVE);
				break;
			case "BADGE DETAIL": 
				event.setEventSubtype(EventSubtype.BADGE_DETAIL);
				break;
			case "PHOTO MODAL":
				event.setEventSubtype(EventSubtype.PHOTO_MODAL);
				break;
			case "BADGES":
				event.setEventSubtype(EventSubtype.BADGES);
				break;
			case "MARKET REPORT":
				event.setEventSubtype(EventSubtype.MARKET_REPORT);
				break;
			default:
				event.setEventSubtype(EventSubtype.FEATURES);
		}
	}

	private void eventTimeHandler(Event.Builder event, String time) {
		event.setEventTime(time);
	}

	private void cityHandler(Event.Builder event, String city) {
		event.setCity(city);
	}

	private void vinHandler(Event.Builder event, String vin) {
		event.setVin(vin);
	}

	private void conditionHandler(Event.Builder event, String vehicleCondition) {
		Condition condition = Condition.USED;
		if (vehicleCondition.toUpperCase().equals("NEW"))
			condition = Condition.NEW;
		event.setCondition(condition);
	}

	private void yearHandler(Event.Builder event, String year) {
		event.setYear(Integer.parseInt(year));
	}

	private void makeHandler(Event.Builder event, String make) {
		event.setMake(make);
	}

	private void modelHandler(Event.Builder event, String model) {
		event.setModel(model);
	}

	private void trimHandler(Event.Builder event, String trim) {
		if (trim.equals(""))
			event.setTrim(null);
		else
			event.setTrim(trim);
	}

	private void bodyStyleHandler(Event.Builder event, String bodyStyle) {
		switch(bodyStyle.toUpperCase()) {
			case "CASSIS":
				event.setBodyStyle(BodyStyle.CASSIS);
				break;
			case "CONVERTIBLE":
				event.setBodyStyle(BodyStyle.CONVERTIBLE);
				break;
			case "COUPE":
				event.setBodyStyle(BodyStyle.COUPE);
				break;
			case "HATCHBACK":
				event.setBodyStyle(BodyStyle.HATCHBACK);
				break;
			case "MINIVAN":
				event.setBodyStyle(BodyStyle.MINIVAN);
				break;
			case "PICKUP":
				event.setBodyStyle(BodyStyle.PICKUP);
				break;
			case "SUV":
				event.setBodyStyle(BodyStyle.SUV);
				break;
			case "SEDAN":
				event.setBodyStyle(BodyStyle.SEDAN);
				break;
			case "VAN":
				event.setBodyStyle(BodyStyle.VAN);
				break;
			case "WAGON":
				event.setBodyStyle(BodyStyle.WAGON);
				break;
			default:
				event.setBodyStyle(BodyStyle.COUPE);
		}
	}

	private void cabStyleHandler(Event.Builder event, String cabStyle) {
		switch(cabStyle.toUpperCase()) {
			case "CREW CAB": 
				event.setCabStyle(CabStyle.CREW);
				break;
			case "EXTENDED CAB": 
				event.setCabStyle(CabStyle.EXTENDED);
				break;
			case "REGULAR CAB": 
				event.setCabStyle(CabStyle.REGULAR);
				break;
			default:
				event.setCabStyle(null);
		}
	}

	private void priceHandler(Event.Builder event, String price) {
		event.setPrice(Double.parseDouble(price));
	}

	private void mileageHandler(Event.Builder event, String mileage) {
		event.setMileage(Long.parseLong(mileage));
	}

	private void freeCarfaxReportHandler(Event.Builder event, String freeCarfaxReport) {
		boolean free = false;
		if (freeCarfaxReport.equals("t"))
			free = true;
		event.setFreeCarfaxReport(free);
	}

	private void featuresHandler(Event.Builder event, String features) {
		List<CharSequence> featureList = new ArrayList<>();
		String[] splitFeatures = features.split(":");
		for (String feature : splitFeatures) {
			if (feature.equals("null"))
				continue;
			featureList.add(feature);
		}
		event.setFeatures(featureList);
	}
}
