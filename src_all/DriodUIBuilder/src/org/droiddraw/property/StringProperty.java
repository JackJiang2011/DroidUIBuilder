/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * StringProperty.java at 2015-2-6 16:12:03, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw.property;

import java.beans.PropertyChangeEvent;

import org.droiddraw.AndroidEditor;

public class StringProperty extends Property
{
	String value;
	String defaultValue;

	public StringProperty(String englishName, String attName,
			String defaultValue)
	{
		this(englishName, attName, defaultValue, true);
		this.defaultValue = defaultValue;
	}

	public StringProperty(String englishName, String attName,
			String defaultValue, boolean editable)
	{
		super(englishName, attName, editable);
		this.value = defaultValue;
	}

	@Override
	protected boolean isDefaultInternal()
	{
		return value.equals(defaultValue);
	}

	@Override
	public Object getValue()
	{
		return getStringValue();
	}

	public String getRawStringValue()
	{
		return value;
	}

	public String getStringValue()
	{
		if (value != null && value.startsWith("@string")
				&& AndroidEditor.instance().getStrings() != null)
		{
			String key = value.substring(value.indexOf("/") + 1);
			String str = AndroidEditor.instance().getStrings().get(key);
			if (str == null)
				str = value;
			return str;
		}
		else
		{
			return value;
		}
	}

	public void setStringValue(String value)
	{
		this.setStringValue(value, false);
	}

	public void setStringValue(String value, boolean setDefault)
	{
		String old = this.value;
		this.value = value;
		if (setDefault)
		{
			this.defaultValue = value;
		}
		firePropertyChangedEvent(new PropertyChangeEvent(this, this
				.getAtttributeName(), old, value));
	}

	@Override
	public void setValue(String value)
	{
		setStringValue(value);
	}
}
