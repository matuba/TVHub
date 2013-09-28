package models;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.node.ObjectNode;

import play.libs.Json;

import scala.collection.mutable.Publisher;

public class TVProgramme {
	public Date start;
	public Date stop;
	public String title;
	public String category;
	public String desc;
	public String channel;

	public String getProgrammeID(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(start) + channel;
	}
	public String getCategory(){
		if(category.equals("ニュース・報道")){
			return "news";
		}
		if(category.equals("情報")){
			return "ifno";
		}
		if(category.equals("バラエティ")){
			return "vari";
		}
		if(category.equals("ドラマ")){
			return "drma";
		}
		return "etc";
	}
}
