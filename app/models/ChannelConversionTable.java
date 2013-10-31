package models;

import javax.xml.xpath.XPathConstants;

import play.Logger;

public class ChannelConversionTable extends XmlFileXpath{
	public String conversionChannel( String broadcast, String ch){
		try {
			StringBuffer xmlPath = new StringBuffer("");
			xmlPath.append("/channel[@broadcast='");
			xmlPath.append(broadcast);
			xmlPath.append("' and ");
			xmlPath.append("@src='");
			xmlPath.append(ch);
			xmlPath.append("']@des");
			return (String)m_xpath.evaluate( xmlPath.toString(), m_doc, XPathConstants.STRING);
		} catch (Exception e) {
	    	Logger.info(e.getMessage());
			return "";
		}
	}
}
