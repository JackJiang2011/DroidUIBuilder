/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * StringHandler.java at 2015-2-6 16:11:59, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Hashtable;
import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

public class StringHandler extends DefaultHandler
{
	Hashtable<String, String> strings;
	StringBuffer buff;
	String name;

	public StringHandler()
	{
		strings = new Hashtable<String, String>();
		buff = new StringBuffer();
	}

	@Override
	public void characters(char arg0[], int arg1, int arg2)
	{
		buff.append(arg0, arg1, arg2);
	}

	@Override
	public void startDocument()
	{
		strings.clear();
	}

	@Override
	public void startElement(String ns, String lName, String qName,
			Attributes atts)
	{
		buff.setLength(0);
		if (qName.equals("string"))
			name = atts.getValue("name");
	}

	@Override
	public void endElement(String uri, String localName, String qName)
	{
		if (qName.equals("string"))
		{
			String str = buff.toString();
			while (str.indexOf("\\u") != -1)
			{
				int ix = str.indexOf("\\u");
				int code = Integer.parseInt(str.substring(ix + 2, ix + 6), 16);
				str = str.substring(0, ix) + (char) code
						+ str.substring(ix + 6);
			}
			while (str.indexOf("\\n") != -1)
			{
				str = str.replace("\\n", "\n");
			}
			strings.put(name, str);
		}
	}

	public Hashtable<String, String> getStrings()
	{
		return strings;
	}

	public static Hashtable<String, String> load(InputStream is)
		throws SAXException, ParserConfigurationException, IOException
	{
		return load(new InputSource(is));
	}

	public static Hashtable<String, String> load(InputSource in)
		throws SAXException, ParserConfigurationException, IOException
	{
		StringHandler sh = new StringHandler();
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		parser.parse(in, sh);
		return sh.getStrings();
	}

	public static void dump(Writer w, Hashtable<String, String> strings)
		throws IOException
	{
		PrintWriter pw = new PrintWriter(w);
		pw.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		pw.println("<resources>");
		for (String key : strings.keySet())
		{
			String value = strings.get(key);
			pw.println("<string name=\"" + key + "\">" + value + "</string>");
		}
		pw.println("</resources>");
		pw.flush();
		w.flush();
	}

}
