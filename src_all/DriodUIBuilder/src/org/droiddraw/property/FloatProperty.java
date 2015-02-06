/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * FloatProperty.java at 2015-2-6 16:12:03, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw.property;

public class FloatProperty extends Property
{
	float def;
	float value;

	public FloatProperty(String englishName, String attName, float def)
	{
		super(englishName, attName, true, true);
		this.def = def;
		this.value = def;
	}

	@Override
	public Object getValue()
	{
		return new Float(value);
	}

	@Override
	protected boolean isDefaultInternal()
	{
		return value == def;
	}

	@Override
	public void setValue(String value)
	{
		try
		{
			this.value = Float.parseFloat(value);
		}
		catch (NumberFormatException ex)
		{
			ex.printStackTrace();
			this.value = -1f;
		}
	}

	public void setFloatValue(float value)
	{
		this.value = value;
	}
}
