package models;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.codehaus.jackson.node.ObjectNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import play.Logger;
import play.libs.Json;

public class TvListings {
	Document m_doc = null;
	XPath m_xpath = null;

	public boolean LoadXML(String filename){
		File file = new File(filename);
		DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
		try{
			m_xpath = XPathFactory.newInstance().newXPath();
			DocumentBuilder builder = fact.newDocumentBuilder();
			m_doc = builder.parse(file);
		}
		catch(Exception e){
	    	Logger.info(e.getMessage());
			return false;
		}
		return true;
	}
	public String getChannelID(){
		try {
			return (String)m_xpath.evaluate( "/tv/channel/@id", m_doc, XPathConstants.STRING);
		} catch (Exception e) {
	    	Logger.info(e.getMessage());
			return "";
		}
	}
	public String getChannelName(String ch){
		try {
			return (String)m_xpath.evaluate( "/tv/channel[@id='"+ch+"']/display-name[@lang='ja_JP']", m_doc, XPathConstants.STRING);
		} catch (Exception e) {
	    	Logger.info(e.getMessage());
			return "";
		}
	}
	public int getProgrammeNum(){
		NodeList nodesProgramme = null;
		Object programmes = null;
		try {
			programmes = m_xpath.evaluate( "/tv/programme", m_doc, XPathConstants.NODESET);
			nodesProgramme = (NodeList) programmes;
		} catch (Exception e) {
	    	Logger.info(e.getMessage());
	    	return 0;
		}
		return nodesProgramme.getLength();
	}
	public TVProgramme getProgramme(int index){
		index++;
		TVProgramme programme = new TVProgramme();
		Calendar calendar = Calendar.getInstance();

		try {
			programme.title = (String)m_xpath.evaluate( "/tv/programme[" + Integer.toString(index) + "]/title[@lang='ja_JP']", m_doc, XPathConstants.STRING);
			programme.category = (String)m_xpath.evaluate( "/tv/programme[" + Integer.toString(index) + "]/category[@lang='ja_JP']", m_doc, XPathConstants.STRING);
			programme.desc = (String)m_xpath.evaluate( "/tv/programme[" + Integer.toString(index) + "]/desc[@lang='ja_JP']", m_doc, XPathConstants.STRING);
			programme.channel = (String)m_xpath.evaluate( "/tv/programme[" + Integer.toString(index) + "]/@channel", m_doc, XPathConstants.STRING);

			String startString = (String)m_xpath.evaluate( "/tv/programme[" + Integer.toString(index) + "]/@start", m_doc, XPathConstants.STRING);
			int year = Integer.parseInt(startString.substring(0, 4));
			int month = Integer.parseInt(startString.substring(4, 6));
			int day = Integer.parseInt(startString.substring(6, 8));
			int hour = Integer.parseInt(startString.substring(8, 10));
			int min = Integer.parseInt(startString.substring(10, 12));
			calendar.set(year, month, day, hour, min, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			programme.start = calendar.getTime();

			String stopString = (String)m_xpath.evaluate( "/tv/programme[" + Integer.toString(index) + "]/@stop", m_doc, XPathConstants.STRING);
			year = Integer.parseInt(stopString.substring(0, 4));
			month = Integer.parseInt(stopString.substring(4, 6));
			day = Integer.parseInt(stopString.substring(6, 8));
			hour = Integer.parseInt(stopString.substring(8, 10));
			min = Integer.parseInt(stopString.substring(10, 12));
			calendar.set(year, month, day, hour, min, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			programme.stop = calendar.getTime();
		} catch (Exception e) {
	    	Logger.info(e.getMessage());
			return null;
		}
		return programme;
	}

	public TVProgramme getProgramme(String ch, Node node){
		String channel = ((Element)node).getAttribute("channel");
		if(!channel.equals(ch)){
			return null;
		}
		String startString = ((Element)node).getAttribute("start");
		String stopString = ((Element)node).getAttribute("stop");
		TVProgramme programme = new TVProgramme();
		Calendar calendar = Calendar.getInstance();
		NodeList childNodes = node.getChildNodes();
		programme.channel = channel;


		for (int j = 0; j < childNodes.getLength(); j++) {
			Node nodeItem = childNodes.item(j);
			if(nodeItem.getNodeName().equals("title")){
				programme.title = nodeItem.getTextContent();
			}
			if(nodeItem.getNodeName().equals("category")){
				Node nodeLang = nodeItem.getAttributes().item(0);
				if(nodeLang.getNodeValue().equals("en") && nodeLang.getNodeName().equals("lang")){
					programme.category = nodeItem.getTextContent();
				}
			}
			if(nodeItem.getNodeName().equals("desc")){
				programme.desc = nodeItem.getTextContent();
			}
		}
		int year = Integer.parseInt(startString.substring(0, 4));
		int month = Integer.parseInt(startString.substring(4, 6));
		int day = Integer.parseInt(startString.substring(6, 8));
		int hour = Integer.parseInt(startString.substring(8, 10));
		int min = Integer.parseInt(startString.substring(10, 12));
		calendar.set(year, month - 1, day, hour, min, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		programme.start = calendar.getTime();

		year = Integer.parseInt(stopString.substring(0, 4));
		month = Integer.parseInt(stopString.substring(4, 6));
		day = Integer.parseInt(stopString.substring(6, 8));
		hour = Integer.parseInt(stopString.substring(8, 10));
		min = Integer.parseInt(stopString.substring(10, 12));
		calendar.set(year, month - 1, day, hour, min, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		programme.stop = calendar.getTime();

		return programme;
	}

	
	public List<TVProgramme> getTVProgrammeList(){
		List<TVProgramme> tvProgrammeList = new ArrayList<TVProgramme>();
		for(int i=0;i<getProgrammeNum();i++){
			tvProgrammeList.add(getProgramme(i));
		}
		return tvProgrammeList;
	}

	public List<TVProgramme> getTVProgrammeList(String ch, Date start, Date stop){
		List<TVProgramme> tvProgrammeList = new ArrayList<TVProgramme>();
		XPathExpression expr = null;
		NodeList nodes = null;
		try {
			expr = m_xpath.compile("/tv/programme");
			nodes = (NodeList) expr.evaluate( m_doc, XPathConstants.NODESET);
		} catch (Exception e) {
			return null;
		}
		for (int i = 0; i < nodes.getLength(); i++) {
			TVProgramme programme = getProgramme(ch, nodes.item(i));
			if(programme == null){
				continue;
			}
			if(start.compareTo(programme.start) > 0){
				continue;
			}
			if(stop.compareTo(programme.start) <= 0){
				continue;
			}
			tvProgrammeList.add(programme);
		}

		return tvProgrammeList;
	}



}
