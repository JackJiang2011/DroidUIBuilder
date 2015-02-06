/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * MakeArraysPane.java at 2015-2-6 16:12:00, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import org.droiddraw.gui.AbstractDataPanel;
import org.droiddraw.util.ArrayHandler;

public class MakeArraysPane extends AbstractDataPanel
{
	private static final long serialVersionUID = 1L;

	public MakeArraysPane()
	{
		super(new Class<?>[] { String.class, String.class });
	}

	@Override
	protected void addValue(String name)
	{
		AndroidEditor.instance().getArrays().put(name, new Vector<String>());
	}

	@Override
	protected void doSave()
	{
		File f = AndroidEditor.instance().getArrayFile();
		try
		{
			if (f == null)
			{
				f = Launch.doSaveBasic();
			}
			if (f != null)
			{
				FileWriter fw = new FileWriter(f);
				ArrayHandler.dump(fw, AndroidEditor.instance().getArrays());
				fw.flush();
				fw.close();
			}
		}
		catch (IOException ex)
		{
			MainPane.getTipCom().error(ex);
		}
	}

	@Override
	protected void parentDeleteRow(int row)
	{
		String key = (String) parentValueAt(row, 0);
		Hashtable<String, Vector<String>> arrays = AndroidEditor.instance()
				.getArrays();
		arrays.remove(key);
	}

	@Override
	protected int parentRowCount()
	{
		Hashtable<String, Vector<String>> arrays = AndroidEditor.instance()
				.getArrays();
		return arrays.size();
	}

	@Override
	protected void parentSetValueAt(Object value, int rowIndex, int columnIndex)
	{
		String key = (String) parentValueAt(rowIndex, 0);
		Hashtable<String, Vector<String>> arrays = AndroidEditor.instance()
				.getArrays();
		if (columnIndex == 1)
		{
			String val = (String) value;
			Vector<String> data = arrays.get(key);
			data.clear();
			StringTokenizer tok = new StringTokenizer(val, ",");
			while (tok.hasMoreTokens())
			{
				data.add(tok.nextToken());
			}
			arrays.put(key, data);
		}
		else
		{
			Vector<String> val = arrays.get(key);
			arrays.remove(key);
			arrays.put((String) value, val);
		}
	}

	@Override
	protected Object parentValueAt(int row, int col)
	{
		Hashtable<String, Vector<String>> arrays = AndroidEditor.instance()
				.getArrays();
		ArrayList<String> sorted = Collections.list(arrays.keys());
		Collections.sort(sorted);

		if (col == 0)
		{
			return sorted.get(row);
		}
		else if (col == 1)
		{
			Vector<String> data = arrays.get(sorted.get(row));
			String res = "";
			if (data.size() > 0)
			{
				res = data.get(0);
				for (int i = 1; i < data.size(); i++)
				{
					res = res + "," + data.get(i);
				}
			}
			return res;
		}
		return null;
	}

}
