package controllers;

import models.TVProgramme;
import models.TvListings;
import play.mvc.*;
import views.html.*;

public class Application extends Controller {
  
    public static Result index() {
    	TvListings tvlistingsGR27 = new TvListings();
    	tvlistingsGR27.LoadXML("public/listings/27ch.xml");
    	TVProgramme programmeGR27 = tvlistingsGR27.getProgramme(1);
    	return ok(tvlistings.render(tvlistingsGR27.getTVProgrammeList()));
    }
  
}
