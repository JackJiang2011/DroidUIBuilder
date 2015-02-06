/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * BooleanProperty.java at 2015-2-6 16:12:03, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw.property;

import java.beans.PropertyChangeEvent;

public class BooleanProperty extends Property
{
	boolean value;
	boolean defaultValue;

	public BooleanProperty(String englishName, String attName,
			boolean defaultValue)
	{
		this(englishName, attName, defaultValue, true);
		this.defaultValue = defaultValue;
	}

	public BooleanProperty(String englishName, String attName,
			boolean defaultValue, boolean editable)
	{
		super(englishName, attName, editable);
		this.value = defaultValue;
	}

	@Override
	protected boolean isDefaultInternal()
	{
		return value == defaultValue;
	}

	@Override
	public Object getValue()
	{
		if (value)
			return "true";
		else
			return "false";
	}

	public boolean getBooleanValue()
	{
		return value;
	}

	public void setBooleanValue(boolean b)
	{
		boolean old = value;
		this.value = b;
		firePropertyChangedEvent(new PropertyChangeEvent(this, this
				.getAtttributeName(), old, b));
	}

	@Override
	public void setValue(String value)
	{
		if ("true".equals(value))
		{
			setBooleanValue(true);
		}
		else if ("false".equals(value))
		{
			setBooleanValue(false);
		}
	}
}
