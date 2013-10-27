package models;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;

import play.Logger;

public class XmlFileXpath {
	protected Document m_doc = null;
	protected XPath m_xpath = null;

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

}
