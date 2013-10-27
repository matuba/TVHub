package controllers;

import java.util.Calendar;
import java.util.Date;

import models.TVProgramme;
import models.TvListings;
import play.mvc.*;
import views.html.*;
import java.util.Calendar;
import java.util.TimeZone;
 
import org.codehaus.jackson.node.ObjectNode;
 
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class Application extends Controller {
  
    public static Result index() {
    	TvListings tvlistingsGR27 = new TvListings();
    	tvlistingsGR27.LoadXML("public/listings/27ch.xml");
    	TVProgramme programmeGR27 = tvlistingsGR27.getProgramme(1);
    	Calendar calendar = Calendar.getInstance(); 
    	calendar.set(2013, 7, 27, 13, 00, 0);
    	Date start = calendar.getTime();
    	calendar.set(2013, 7, 27, 19, 00, 0);
    	Date stop = calendar.getTime();

    	return ok(tvlistings.render());
    }

    public static Result getJSON() {
		ObjectNode rootJson = Json.newObject();
		rootJson.put("localDate", Calendar.getInstance(TimeZone.getDefault())
				.getTime().toString());
		rootJson.put("utcDate",
				Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime()
						.toString());
 
		ObjectNode timeZonesJson = Json.newObject();
		for (String timeZoneId : TimeZone.getAvailableIDs()) {
			TimeZone tz = TimeZone.getTimeZone(timeZoneId);
			timeZonesJson.put(tz.getDisplayName(), tz.getID());
		}
 
		rootJson.put("timeZones", timeZonesJson);
 
		return ok(rootJson);
	}

    public static Result getProgrammesJSON(String year, String month, String day, String hour, String min, String length, String broadcast, String ch) {
    	Calendar calendar = Calendar.getInstance(); 
    	calendar.set( Integer.parseInt(year), Integer.parseInt(month),  Integer.parseInt(day),  Integer.parseInt(hour),  Integer.parseInt(min),  0);
    	calendar.set(Calendar.MILLISECOND, 0);
    	Date start = calendar.getTime();
    	calendar.add(Calendar.MINUTE, Integer.parseInt(length));
    	Date stop = calendar.getTime();

    	TvListings tvlistingsGR = new TvListings();
    	tvlistingsGR.LoadXML("public/listings/" + ch + ".xml");
		return ok(Json.toJson(tvlistingsGR.getTVProgrammeList( ch, start, stop)));
    }
    public static Result getChannelNameJSON(String ch){
    	TvListings tvlistings = new TvListings();
    	tvlistings.LoadXML("public/listings/" + ch + ".xml");
		return ok(Json.toJson(tvlistings.getChannelName(ch)));
    }   
}
