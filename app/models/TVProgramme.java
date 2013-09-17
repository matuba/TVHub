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
	public String getStopHHMM(){
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		return sdf.format(stop);
	}

	public String getStartYYMMDDHHMM(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		return sdf.format(start);
	}
	public String getProgrammeID(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(start) + channel;
	}
	public String getTitle(){
		long time = stop.getTime() - start.getTime();
		if((time/1000)/60 <= 15){
			if(title.length() < 6){
				return title;
			}
			return title.substring(0, 6) + "‥.";
		}
		if((time/1000)/60 <= 30){
			if(title.length() < 16){
				return title;
			}
			return title.substring(0, 16) + ".‥";
		}
		if(title.length() < 32){
			return title;
		}
		return title.substring(0, 32) + "‥.";
	}
	public String getDesc(){
		if(desc.length() < 32){
			return desc;
		}
		return desc.substring(0, 32) + "...";
	}

}
