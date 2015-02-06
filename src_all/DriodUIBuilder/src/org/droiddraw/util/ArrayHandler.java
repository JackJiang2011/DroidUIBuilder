/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * ArrayHandler.java at 2015-2-6 16:12:02, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Hashtable;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ArrayHandler extends DefaultHandler
{
	Hashtable<String, Vector<String>> arrays;
	Vector<String> items;

	StringBuffer buff;
	String name;

	public ArrayHandler()
	{
		arrays = new Hashtable<String, Vector<String>>();
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
		arrays.clear();
	}

	@Override
	public void startElement(String ns, String lName, String qName,
			Attributes atts)
	{
		buff.setLength(0);
		if (qName.equals("array"))
		{
			name = atts.getValue("name");
			items = new Vector<String>();
		}

	}

	@Override
	public void endElement(String uri, String localName, String qName)
	{
		if (qName.equals("item"))
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
			items.add(str);
		}
		else if (qName.equals("array"))
		{
			arrays.put(name, items);
			items = null;
		}
	}

	public Hashtable<String, Vector<String>> getArrays()
	{
		return arrays;
	}

	public static Hashtable<String, Vector<String>> load(InputStream is)
		throws SAXException, ParserConfigurationException, IOException
	{
		return load(new InputSource(is));
	}

	public static Hashtable<String, Vector<String>> load(InputSource in)
		throws SAXException, ParserConfigurationException, IOException
	{
		ArrayHandler sh = new ArrayHandler();
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		parser.parse(in, sh);
		return sh.getArrays();
	}

	public static void dump(Writer w, Hashtable<String, Vector<String>> arrays)
	{
		PrintWriter pw = new PrintWriter(w);
		pw.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		pw.println("<resources>");
		if (arrays != null)
		{
			for (String key : arrays.keySet())
			{
				pw.println("<array name=\"" + key + "\">");
				Vector<String> array = arrays.get(key);
				for (String item : array)
				{
					pw.println("<item>" + item + "</item>");
				}
				pw.println("</array>");
			}
		}
		pw.println("</resources>");
		pw.flush();
	}

}
