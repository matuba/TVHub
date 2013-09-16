package models;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TVProgramme {
	public Date start;
	public Date stop;
	public String title;
	public String category;
	public String desc;
	public String channel;
	
	public String getStartHHMM(){
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		return sdf.format(start);
	}
	public String getStartYYMMDDHHMM(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(start);
	}

}
