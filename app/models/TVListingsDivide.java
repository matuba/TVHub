package models;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;


import scala.collection.script.Start;

public class TVListingsDivide extends XmlFileXpath {
	public TVListingsDivide(){
	}
	public TVListingsDivide(String filename){
		LoadXML(filename);
	}
	private String writeHeader( String channelname) throws XPathExpressionException{
		StringBuffer stringBuffer = new StringBuffer();
		
		stringBuffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		stringBuffer.append("<!DOCTYPE tv SYSTEM \"xmltv.dtd\">\n");
		stringBuffer.append("\n");
		stringBuffer.append("<tv generator-info-name=\"tsEPG2xml\" generator-info-url=\"http://localhost/\">\n");
		stringBuffer.append("  <channel id=\"" + channelname + "\">\n");

		String displayName = m_xpath.evaluate("/tv/channel[@id='" + channelname + "']/display-name[@lang='ja_JP']/text()", m_doc);
		stringBuffer.append("    <display-name lang=\"ja_JP\">" + displayName + "</display-name\n");
		stringBuffer.append("  </channel>\n");
		return stringBuffer.toString();
	}
	private String writeFooter() throws XPathExpressionException{
		return new String("</tv>");
	}
	private String writeProgrammes(String channelname) throws XPathExpressionException{
		StringBuffer stringBuffer = new StringBuffer();
		XPathExpression expr = m_xpath.compile("/tv/programme[@channel='" + channelname + "']");
		NodeList nodes = (NodeList) expr.evaluate( m_doc, XPathConstants.NODESET);
		for (int i = 0; i < nodes.getLength(); i++) {
			stringBuffer.append(writeProgramme(nodes.item(i)));
		}
		return stringBuffer.toString();
	}
	private String writeProgramme( Node node) throws XPathExpressionException{
		NodeList childNodes = node.getChildNodes();

		StringBuffer stringBuffer = new StringBuffer();
		String channel = ((Element)node).getAttribute("channel");
		String start = ((Element)node).getAttribute("start");
		String stop = ((Element)node).getAttribute("stop");
		
		stringBuffer.append("  <programme ");
		stringBuffer.append("start=\"").append(start).append("\" ");
		stringBuffer.append("stop=\"").append(stop).append("\" ");
		stringBuffer.append("channel=\"").append(channel).append("\" ");
		stringBuffer.append(">\n");
		
		for (int j = 0; j < childNodes.getLength(); j++) {
			Node nodeItem = childNodes.item(j);
			String lang  = new String("ja_JP");
			NamedNodeMap attribute = nodeItem.getAttributes();
			//langがあれば情報取得
			if(attribute != null && attribute.getNamedItem("lang") != null){
				lang = attribute.getNamedItem("lang").getNodeValue();
			}
			if(nodeItem.getNodeName().equals("title")){
				stringBuffer.append("    <title lang=\"" + lang + "\">").append(nodeItem.getTextContent()).append("</title>");
				stringBuffer.append("\n");
			}
			if(nodeItem.getNodeName().equals("desc")){
				stringBuffer.append("    <desc lang=\"" + lang + "\">").append(nodeItem.getTextContent()).append("</desc>");
				stringBuffer.append("\n");
			}
			if(nodeItem.getNodeName().equals("category")){
				stringBuffer.append("    <category lang=\"" + lang + "\">").append(nodeItem.getTextContent()).append("</category>");
				stringBuffer.append("\n");
			}
		}		
		stringBuffer.append("  </programme>\n");
		return stringBuffer.toString();
	}
	
	private ArrayList<String> getChannelList() throws XPathExpressionException{
		ArrayList<String> channelList = new ArrayList<String>();
		XPathExpression expr = m_xpath.compile("/tv/channel");
		NodeList nodes = (NodeList) expr.evaluate( m_doc, XPathConstants.NODESET);
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			NamedNodeMap attribute = node.getAttributes();
			channelList.add(attribute.getNamedItem("id").getNodeValue());
		}
		return channelList;
	}

	public boolean write(){
		try {
			for(String channelName : getChannelList()){
				StringBuffer stringBuffer = new StringBuffer();
				stringBuffer.append("public/listings/");
				stringBuffer.append(channelName);
				stringBuffer.append(".xml");
				if(!write( stringBuffer.toString(), channelName)){
					System.out.println("error:"+channelName);
				}
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public boolean write(String filename, String channelname){
		StringBuffer stringBuffer = new StringBuffer();
		String header = new String();
		String programmes = new String();
		String footer = new String();
		try {
			header = writeHeader(channelname);
			programmes = writeProgrammes(channelname);
			footer = writeFooter();
			if(programmes.equals("")){
				return false;
			}
		} catch (XPathExpressionException e1) {
			e1.printStackTrace();
			return false;
		}
		stringBuffer.append(header);
		stringBuffer.append(programmes);
		stringBuffer.append(footer);
		
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new BufferedWriter(new FileWriter(filename)));
			pw.write(stringBuffer.toString());
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		finally{
			pw.close();
		}
		return true;
	}
}
