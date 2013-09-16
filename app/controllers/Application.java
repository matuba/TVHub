package controllers;

import java.util.Calendar;
import java.util.Date;

import models.TVProgramme;
import models.TvListings;
import play.mvc.*;
import views.html.*;

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

    	return ok(tvlistings.render(tvlistingsGR27.getTVProgrammeList(start, stop)));
    }
  
}
