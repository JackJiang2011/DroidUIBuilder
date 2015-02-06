/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * IntProperty.java at 2015-2-6 16:12:01, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw.property;

public class IntProperty extends Property
{
	int value;
	int defValue;

	public IntProperty(String name, String attName, int defaultValue)
	{
		this(name, attName, defaultValue, true);
	}

	public IntProperty(String name, String attName, int defaultValue,
			boolean editable)
	{
		super(name, attName, editable);
		this.value = defaultValue;
		this.defValue = defaultValue;
	}

	@Override
	public Object getValue()
	{
		return new Integer(value);
	}

	@Override
	protected boolean isDefaultInternal()
	{
		return value == defValue;
	}

	@Override
	public void setValue(String value)
	{
		if (value != null)
			setIntValue(Integer.parseInt(value));
	}

	public int getIntValue()
	{
		return value;
	}

	public void setIntValue(int v)
	{
		this.value = v;
	}
}
