package controllers;

import play.*;
import play.mvc.*;
import play.libs.Akka;
import java.util.concurrent.TimeUnit;
import scala.concurrent.duration.*;
import views.html.*;

public class Application extends Controller {
  
    public static Result index() {
    	
    	return ok(index.render("Your new application is ready."));
    }
  
}
