package org.droiddraw.property;

import org.droiddraw.AndroidEditor;

public class WidthProperty extends StringProperty
{
	public WidthProperty(String name, String attName, int defaultValue)
	{
		this(name, attName, defaultValue
				+ AndroidEditor.instance().getScreenUnit());
	}

	public WidthProperty(String name, String attName, String defaultValue)
	{
		super(name, attName, defaultValue);
	}

	public int getIntValue()
	{
		String value = getStringValue();
		if (value.endsWith("px") || value.endsWith("dp")
				|| value.endsWith("sp") || value.endsWith("in")
				|| value.endsWith("pt"))
		{
			String substr = value.substring(0, value.length() - 2);
			return Integer.parseInt(substr);
		}
		else
		{
			return Integer.parseInt(value);
		}
	}

	public Object getValue()
	{
		String val = getStringValue();
		try
		{
			// This is kind of hacky, try to parse into an int, if that works,
			// append dp.
			int value = Integer.parseInt(val);
			return value + AndroidEditor.instance().getScreenUnit();
		}
		catch (Exception ignore)
		{
		}
		return val;
	}

}
