package models;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

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
		XPathExpression expr = m_xpath.compile("/tv/programme[@channel='" + channelname + "']");
		stringBuffer.append("    <display-name lang=\"ja_JP\">" + displayName + "</display-name\n");
		stringBuffer.append("  </channel>\n");
		return stringBuffer.toString();
	}
	private String writeFooter() throws XPathExpressionException{
		return new String("</tv>");
	}
	private String writeProgrammes() throws XPathExpressionException{
		StringBuffer stringBuffer = new StringBuffer();
		XPathExpression expr = m_xpath.compile("/tv/programme");
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

	
	public boolean write(String filename, String channelname){
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new BufferedWriter(new FileWriter(filename)));
			pw.write(writeHeader(channelname));
			pw.write(writeProgrammes());
			pw.write(writeFooter());
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			pw.close();
		}

		return true;
	}
}
